package engine;

import config.GameConfig;
import data.Block;
import data.Environment;
import data.EnvironmentElement;
import data.Obstacle;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.text.Position;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    /**
     * This method returns a GraphicElement from a position
     *
     * @param searchedBlock: a {@link Block} .
     * @param environment: a {@link EnvironmentElement} instance.
     *
     * @return a {@link EnvironmentElement} since the position in parameter.
     * */

    public static EnvironmentElement getElementFromBlock(Environment environment, Block searchedBlock) {

        for(EnvironmentElement environmentElement : environment.getElements()){
            int xSearchedBlock = searchedBlock.getColumn();
            int ySearchedBlock = searchedBlock.getLine();

            int columnElement = environmentElement.getBlock().getColumn();
            int lineElement = environmentElement.getBlock().getLine();


            if(searchedBlock.equals(environmentElement.getBlock())) {
                return environmentElement;
            }
            else if(xSearchedBlock < columnElement + GameConfig.BLOCK_SIZE
                    && xSearchedBlock > columnElement
                    && ySearchedBlock < lineElement + GameConfig.BLOCK_SIZE
                    && ySearchedBlock > lineElement) {
                return environmentElement;
            }

        }

        return null;
    }

    public static ArrayList<Integer> getGraphicPosition(EnvironmentElement element){
        ArrayList<Integer> position = new ArrayList<Integer>(2);
        position.add(element.getColumn() * GameConfig.BLOCK_SIZE);
        position.add(element.getLine() * GameConfig.BLOCK_SIZE);
        return position;
    }


    public static boolean isObstacleByBlock(Block block, Environment environment){
        EnvironmentElement element = getElementFromBlock(environment, block);
        if (element instanceof Obstacle){
            return true;
        }
        return false;

    }
}