package utilities;

import java.util.Random;

public final class RandomSingleton {
    private static Random bag;

    public RandomSingleton(int seed){
        if (bag != null){
            bag = new Random();
            if (seed != -1){
                bag = new Random(seed);
            }
        }
        
    }

    public static Random getInstance(){
        return bag;
    }
}
