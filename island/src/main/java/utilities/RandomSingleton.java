package utilities;

import java.util.Random;

import configuration.Configuration;

public final class RandomSingleton {
    private static Random bag;

    private RandomSingleton(int seed){
    }

    public static Random getInstance(){
        if (bag == null){
            bag = new Random();
            if (Configuration.seed != -1){
                bag = new Random(Configuration.seed);
            }
            else{
                int generateSeed = bag.nextInt(Integer.MAX_VALUE);
                bag = new Random(generateSeed);
                System.out.println("Seed used to generate this island: " + generateSeed);
            }
        }
        return bag;
    }
}
