package island;

import java.util.ArrayList;
import java.util.List;

import attributes.Attribute;

import featuregeneration.Generator;

public class IslandBuilder {
    private List<Generator> generators;

    public IslandBuilder(){
        generators = new ArrayList<>();
    }

    public IslandBuilder addGenerator(Generator g){
        if(!generators.contains(g)) generators.add(g);
        return this;
    }

    public Island build(Island island) throws MissingAttributeError{{
        for(Generator g : generators){
            // Prerequisite verification
            for(Class<? extends Attribute> attributeType : g.preRequisiteAttributes())
                if(!island.getAttributes().contains(attributeType)) throw new MissingAttributeError();

            island.addAttributeLayer(g.generate(island.getTiles()));
        }
        return island;
    }}

    public class MissingAttributeError extends Exception{
    }
}
