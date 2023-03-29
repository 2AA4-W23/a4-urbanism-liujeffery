package ca.mcmaster.cas.se2aa4.a2.visualizer.renderer.properties;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ThicknessProperty implements PropertyAccess<Double> {
    @Override
    public Optional<Double> extract(List<Property> props) {
        String value = new Reader(props).get("thickness");
        if (value == null)
            return Optional.empty();

        return Optional.of(Double.parseDouble(value));
    }
}
