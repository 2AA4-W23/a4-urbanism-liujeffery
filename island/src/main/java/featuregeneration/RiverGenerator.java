package featuregeneration;

import java.util.List;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.locationtech.jts.util.Stopwatch;

import attributes.Attribute;
import attributes.LandAttribute;
import attributes.RiverAttribute;
import attributes.BiomeAttribute;
import attributes.ElevationAttribute;
import attributes.LakeAttribute;
import island.Edge;
import island.Tile;

public class RiverGenerator extends Generator {
    class Vertex{
        int id;
        double height;
        Set<Tile> tilesWithin; //tiles that contain this vertex
        Set<Vertex> adjTo;
        public Vertex(int id){
            this.id = id;
            this.tilesWithin = new HashSet<>();
            this.adjTo = new HashSet<>();
        }
        int getId(){
            return this.id;
        }
        void addNeighbor(Vertex v){
            this.adjTo.add(v);
        }

        void addTile(Tile t){
            this.tilesWithin.add(t);
        }

        double getVertexHeight(){
            int count = 0;
            double height = 0;
            for(Tile t : this.tilesWithin){
                count++;
                height += t.getAttribute(ElevationAttribute.class).elevation;
            }
            height /= (double) count;
            return height;
        }

        Vertex nextLowestVertex(){
            double minHeight = getVertexHeight();
            Vertex minHeightV = this;
            //search through the vertices it is connected to
            for(Vertex nextV : adjTo){
                double nextVHeight = nextV.getVertexHeight();
                if(nextVHeight < minHeight){
                    minHeight = nextVHeight;
                    minHeightV = nextV;
                }
            }
            return minHeightV;
        }
    }
    int numLakes;
    Map<Integer, Vertex> vertices;
    public RiverGenerator(Integer numLakes){
        this.numLakes = numLakes;
        this.vertices = new HashMap<>();
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        set.add(ElevationAttribute.class);
        set.add(LakeAttribute.class);
        return set;
    }

    @Override
    public RiverAttribute generate(Set<Tile> tiles) {
        HashMap<Tile, RiverAttribute> attributeLayer = new HashMap<>();
        //first create vertices
        initializeVerticesFromTiles(tiles);

        // compute valid vertex to start a lake from
        // valid vertices are defined by a vertex shared by a land and water tile
        Set<Vertex> validStartVertices = new HashSet<>();
        for(Tile t : tiles){
            if(!isWaterTile(t)) continue;
            for(Tile neighbor : t.getNeighbours()){
                if(isWaterTile(neighbor)) continue;

                Set<Vertex> waterTileVertices = getVerticesFromEdges(t.getEdges());
                Set<Vertex> landTileVertices = getVerticesFromEdges(neighbor.getEdges());

                //find shared vertices
                for(Vertex v1 : waterTileVertices){
                    for(Vertex v2 : landTileVertices){
                        if(v1.equals(v2)){
                            validStartVertices.add(v1);
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("number of valid start vertices: " + validStartVertices.size());

        //TODO: get this away from here
        Random bag = new Random();
        //while the start point doesn't work, pick a new start point
        int count = 0;
        while(count < numLakes){
            //pick random start vertex
            List<Vertex> choices = new ArrayList<>(validStartVertices);
            Vertex startV = choices.get(bag.nextInt(choices.size()));

            List<Vertex> path = generateRiver(startV);
            if(path.size()<3){ //make sure we actually generated a river
                continue;
            }

            System.out.print("River: ");
            for(Vertex v : path){
                System.out.print(v.getId() + " ");
            }
            System.out.println();

            //now apply the attributes to tile the rivers are connected to
            applyRiverAttributes(path);

            count++;
        }


        return new RiverAttribute();
    }

    void initializeVerticesFromTiles(Set<Tile> tiles){
        for(Tile t : tiles){
            for(Edge edge : t.getEdges()){
                if(!vertices.containsKey(edge.v1)){
                    vertices.put(edge.v1, new Vertex(edge.v1));
                }
                if(!vertices.containsKey(edge.v2)){
                    vertices.put(edge.v2, new Vertex(edge.v2));
                }
                
                Vertex v1 = vertices.get(edge.v1);
                Vertex v2 = vertices.get(edge.v2);

                //add tile
                v1.addTile(t);
                v2.addTile(t);

                //add adj
                v1.addNeighbor(v2);
                v2.addNeighbor(v1);
            }
        }
    }

    Set<Vertex> getVerticesFromEdges(Set<Edge> edges){
        Set<Vertex> vertices = new HashSet<>();
        for(Edge e : edges){
            vertices.add(this.vertices.get(e.v1));
            vertices.add(this.vertices.get(e.v2));
        }

        return vertices;
    }

    List<Vertex> generateRiver(Vertex startV){
        List<Vertex> path = new ArrayList<>();
        path.add(startV);
        
        //now recursively generate the rest of the river
        // note we do not need to check if a vertex is already in the path, as we guarantee the heights of subsequent vertices are lower
        boolean genRiver = true;
        while(genRiver){
            Vertex currV = path.get(path.size()-1); //the last vertex we added to path
            Vertex nextV = currV.nextLowestVertex(); //find the next vertex the river moves to

            //currV was already the lowest, river generation ended
            if(nextV.equals(currV)){
                System.out.println("river ended due to elevation");
                genRiver = false;
                return path;
            }

            path.add(nextV);

            //river touches another water tile, end river generation
            for(Tile t : nextV.tilesWithin){
                if(isWaterTile(t)){
                    System.out.println("river ended due to water");
                    genRiver = false;
                    return path;
                }
            }
        }
        return path;
    }

    boolean isWaterTile(Tile t){
        return !t.getAttribute(LandAttribute.class).isLand || t.getAttribute(LakeAttribute.class).isLake;
    }

    void applyRiverAttributes(List<Vertex> path){
        for(int i = 1;i < path.size(); i++){
            //find the common tiles between 2 sequential vertices, there should only be 2
            Vertex prevV = path.get(i-1);
            Vertex currV = path.get(i);
            List<Tile> common = new ArrayList<>();
            for(Tile t1 : prevV.tilesWithin){
                for(Tile t2 : currV.tilesWithin){
                    if(t1.equals(t2)){
                        common.add(t1);
                    }
                }
            }
            System.out.print(prevV.getId() + " tiles: ");
            for(Tile t1: prevV.tilesWithin){
                System.out.print(t1.getId() + " ");
            }
            System.out.println();
            System.out.print(currV.getId() + " tiles: ");
            for(Tile t2: currV.tilesWithin){
                System.out.print(t2.getId() + " ");
            }
            System.out.println();

            //assert the size is 2
            if(common.size() != 2){
                throw new Error("Sequential vertices in path don't share 2 common tiles, number of shared tiles: " + common.size());
            }

            Tile t1 = common.get(0);
            Tile t2 = common.get(1);

            //now with these 2 tiles, find their shared edges
            Edge sharedEdge = null;
            for(Edge e1 : t1.getEdges()){
                for(Edge e2: t2.getEdges()){
                    if(e1.equals(e2)){
                        sharedEdge = e1;
                    }
                }
            }

            //this shouldn't be possible
            if(sharedEdge == null){
                throw new Error("No shared edge between the tiles");
            }

            //apply the attributes to the tiles
            if(t1.getAttribute(RiverAttribute.class) == null){
                t1.addAttribute(new RiverAttribute());
            }
            if(t2.getAttribute(RiverAttribute.class) == null){
                t2.addAttribute(new RiverAttribute());
            }
            t1.getAttribute(RiverAttribute.class).addRiver(sharedEdge);
            t2.getAttribute(RiverAttribute.class).addRiver(sharedEdge);
        }
    }
}