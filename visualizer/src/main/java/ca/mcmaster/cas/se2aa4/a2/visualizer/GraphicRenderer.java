package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class GraphicRenderer {

    public void render(Mesh aMesh, Graphics2D canvas, boolean debug) {
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);
        for (Vertex v: aMesh.getVerticesList()) {
            drawPoint(v, canvas, debug, new Color(0, 0, 0));
        }

        //draw segments
        System.out.println("Drawing segments: " + aMesh.getSegmentsCount());
        for(Segment s : aMesh.getSegmentsList()){
            Vertex v1 = aMesh.getVertices(s.getV1Idx());
            Vertex v2 = aMesh.getVertices(s.getV2Idx());
            final int THICKNESS = extractThickness(s.getPropertiesList());
            final int TRANSPARENCY = extractTransparency(s.getPropertiesList());
            drawSegment(aMesh, v1, v2, debug, canvas, new Color(0, 0, 0), THICKNESS, TRANSPARENCY);
        }

        System.out.println("Drawing neighbour relations and centroids: ");
        List<Integer> checkDuplucPairs = new ArrayList<>();
        for (Polygon p : aMesh.getPolygonsList()){
            Vertex v1 = aMesh.getVertices(p.getCentroidIdx());
            drawPoint(v1, canvas, debug, new Color(255, 0, 0));

            for (int i : p.getNeighborIdxsList()){
                Polygon temp = aMesh.getPolygons(i);
                //to check for duplicates
                boolean isDup = false;
                for (int j : checkDuplucPairs){
                    if (j == temp.getCentroidIdx()){
                        isDup = true;
                        break;
                    }
                }
                //drawing segment line
                if (!isDup){
                    Vertex v2 = aMesh.getVertices(temp.getCentroidIdx());
                    //light grey colour for debugging, hardcoded thickness and transparency
                    drawSegment(aMesh, v1, v2, debug, canvas, new Color(178, 178, 178), 1, 255);
                }
                
            }
            checkDuplucPairs.add(p.getCentroidIdx());
        }
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                System.out.println(p.getValue());
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

    public int extractThickness(List<Property> properties){
        String val = "";
        for (Property p: properties){
            if (p.getKey().equals("thickness")){
                val = p.getValue();
            }
        }
        return Integer.parseInt(val);
    }

    public int extractTransparency(List<Property> properties){
        String val = "";
        for (Property p: properties){
            if (p.getKey().equals("transparency")){
                val = p.getValue();
            }
        }
        return Integer.parseInt(val);
    }

    public void drawPoint(Vertex v, Graphics2D canvas, boolean debug, Color debugColor){
        final int THICKNESS = extractThickness(v.getPropertiesList());
        double centre_x = v.getX() - (THICKNESS/2.0d);
        double centre_y = v.getY() - (THICKNESS/2.0d);
        Color old = canvas.getColor();
        if (debug){
            canvas.setColor(debugColor);
        }
        else{
            Color temp = extractColor(v.getPropertiesList());
            canvas.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), extractTransparency(v.getPropertiesList())));
        }
        Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
        canvas.fill(point);
        canvas.setColor(old);
    }

    public void drawSegment(Mesh aMesh, Vertex v1, Vertex v2, boolean debug, Graphics2D canvas, Color debugColor, final int THICKNESS, final int TRANSPARENCY){
        Color v1Color = extractColor(v1.getPropertiesList());
        Color v2Color = extractColor(v2.getPropertiesList());
        Color segmentColor;
        if (debug){
            segmentColor = debugColor;
        }
        else{
            segmentColor = new Color(
                (v1Color.getRed()+v2Color.getRed()) / 2, 
                (v1Color.getGreen()+v2Color.getGreen()) / 2, 
                (v1Color.getBlue()+v2Color.getBlue()) / 2,
                TRANSPARENCY
            );
        }
        
        System.out.format("Creating line at: %f %f %f %f\n", v1.getX(), v1.getY(), v2.getX(), v2.getY());
        Line2D line = new Line2D.Double(v1.getX(), v1.getY(), v2.getX(), v2.getY());
        
        Color old = canvas.getColor();
        canvas.setColor(segmentColor);
        canvas.setStroke(new BasicStroke(THICKNESS));
        canvas.draw(line);
        canvas.setColor(old);
    }

}