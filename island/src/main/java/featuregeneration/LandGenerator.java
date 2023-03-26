package featuregeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

import attributes.Attribute;
import attributes.LandAttribute;
import island.Island.Tile;

public class LandGenerator extends Generator {
    public enum Shapes{
        CIRCLE, SQUARE, LAGOON
    }
    private final GeometryFactory gf;
    private final Geometry landGeometry;
    public LandGenerator(Shapes shape){
        this.gf = new GeometryFactory();

        GeometricShapeFactory gsf = new GeometricShapeFactory(this.gf);
        gsf.setCentre(new Coordinate(0.5, 0.5));
        Geometry geom = null;
        switch(shape){
            case CIRCLE: geom = shapeCircle(gsf);
                break;
            case SQUARE: geom = shapeSquare(gsf);
                break;
            case LAGOON:
            default:  geom = shapeLagoon(gsf);
        }
        this.landGeometry = geom;
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

    private Geometry shapeLagoon(GeometricShapeFactory gsf){
        gsf.setCentre(new Coordinate(0.5, 0.5));
        gsf.setSize(0.8);
        Polygon outerCircle = gsf.createCircle();

        gsf.setCentre(new Coordinate(0.5, 0.5));
        gsf.setSize(0.5);
        Polygon innerCircle = gsf.createCircle();

        return outerCircle.difference(innerCircle);
    }
    
    private Geometry shapeSquare(GeometricShapeFactory gsf){
        gsf.setSize(0.8);
        return gsf.createRectangle();
    }

    private Geometry shapeCircle(GeometricShapeFactory gsf){
        gsf.setSize(0.8);
        return gsf.createCircle();
    }
}
