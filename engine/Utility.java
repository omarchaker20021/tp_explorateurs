package engine;

import java.awt.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * This class contains static methods for different utilities
 *
 * @author Omar CHAKER
 * @version 1.0
 * */

public class Utility {

    /**
     * This method reads image from their location.
     *
     * @param filePath the image path.
     * @return an {@link Image}.
     */
    public static Image readImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("-- Can not read the image file ! --");
            return null;
        }
    }

    /**
     * This method generates random (integer) number between "min" and "max".
     *
     * @param min a minimum.
     * @param max a maximum.
     * @return a random number.
     */
    public static int getRandomNumber(int min, int max) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int randomNumber = random.nextInt(max - min) + min;


        return (int) (Math.random() * (max + 1 - min)) + min;

//        return randomNumber;
    }
}