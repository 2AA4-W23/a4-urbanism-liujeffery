import java.util.Set;

import island.Island.Tile;

public abstract class GeneratorTest {
    protected Set<Tile> tiles;

    public GeneratorTest(int size){
        for (int i = 0; i < size; i++){
            tiles.add(new Tile(i, 1.0*i/size, 1.0*i/size));
        }
    }

    public abstract void testNumOfElements();
}