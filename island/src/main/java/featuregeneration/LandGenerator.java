package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import attributes.Attribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class LandGenerator extends Generator {
    private final GeometryFactory gf;
    private final Geometry landGeometry;
    public LandGenerator(Geometry landGeometry){
        this.gf = new GeometryFactory();
        this.landGeometry = landGeometry;
    }

    @Override
    public Set<Class<? extends Attribute>> preRequisiteAttributes() {
        Set<Class<? extends Attribute>> set = new HashSet<>();
        return set;
    }

    @Override
    public Map<Tile, LandAttribute> generate(Set<Tile> tiles) {
        HashMap<Tile, LandAttribute> attributeLayer = new HashMap<>();
        for(Tile tile : tiles){
            //turn the tile centroid into a jts Point object
            Point tileCentroid = this.gf.createPoint(new Coordinate(tile.getX(), tile.getY()));
            
            //mark whether or not it is within the land region
            boolean isLand = this.landGeometry.intersects(tileCentroid);
            attributeLayer.put(tile, new LandAttribute(isLand));
        }
        return attributeLayer;
    }
}
