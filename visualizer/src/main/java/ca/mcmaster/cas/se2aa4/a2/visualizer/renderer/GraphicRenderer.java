package ca.mcmaster.cas.se2aa4.a2.visualizer.renderer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.visualizer.renderer.properties.ColorProperty;
import ca.mcmaster.cas.se2aa4.a2.visualizer.renderer.properties.ThicknessProperty;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.Optional;

public class GraphicRenderer implements Renderer {

    private static final float THICKNESS = 0.2f;
    public void render(Mesh aMesh, Graphics2D canvas) {
        canvas.setColor(Color.WHITE);
        Stroke stroke = new BasicStroke(THICKNESS);
        canvas.setStroke(stroke);
        drawPolygons(aMesh,canvas);
        drawSegments(aMesh, canvas);
    }

    private void drawPolygons(Mesh aMesh, Graphics2D canvas) {
        for(Structs.Polygon p: aMesh.getPolygonsList()){
            drawAPolygon(p, aMesh, canvas);
        }
    }

    private void drawAPolygon(Structs.Polygon p, Mesh aMesh, Graphics2D canvas) {
        Hull hull = new Hull();
        for(Integer segmentIdx: p.getSegmentIdxsList()) {
            hull.add(aMesh.getSegments(segmentIdx), aMesh);
        }
        Path2D path = new Path2D.Float();
        Iterator<Vertex> vertices = hull.iterator();
        Vertex current = vertices.next();
        path.moveTo(current.getX(), current.getY());
        while (vertices.hasNext()) {
            current = vertices.next();
            path.lineTo(current.getX(), current.getY());
        }
        path.closePath();
        canvas.draw(path);
        Optional<Color> fill = new ColorProperty().extract(p.getPropertiesList());
        if(fill.isPresent()) {
            Color old = canvas.getColor();
            canvas.setColor(fill.get());
            canvas.fill(path);
            canvas.setColor(old);
        }
    }
    private void drawSegments(Mesh aMesh, Graphics2D canvas){
        for(Structs.Segment s: aMesh.getSegmentsList()){
            drawASegment(s, aMesh, canvas);
        }

    }
    private void drawASegment(Structs.Segment s, Mesh aMesh, Graphics2D canvas){
        //only draw a segment if it was specifically given a colour
        Optional<Color> color = new ColorProperty().extract(s.getPropertiesList());
        if(!color.isPresent()){
            return;
        }

        //create the line to draw
        Structs.Vertex v1 = aMesh.getVertices(s.getV1Idx());
        Structs.Vertex v2 = aMesh.getVertices(s.getV2Idx());
        Line2D line = new Line2D.Double(v1.getX(), v1.getY(), v2.getX(), v2.getY());

        

        Color oldColor = canvas.getColor();
        Stroke oldStroke = canvas.getStroke();
        //apply the segments specified width

        Optional<Double> thickness = new ThicknessProperty().extract(s.getPropertiesList());
        if(thickness.isPresent()){
            canvas.setStroke(new BasicStroke(thickness.get().floatValue())); //update the stroke width to match desired thickness
        }
        
        canvas.draw(line);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }
}
