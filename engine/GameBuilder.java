package engine;

import config.GameConfig;
import data.*;

import java.util.ArrayList;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */

public class GameBuilder {

    public static Environment buildMap() {
        return initMapElements();
    }

    /**
     * @brief This function generates and initializes the elements of the game environment.
     * @return Map The environment with its elements.
     */

    public static Environment initMapElements(){
        Environment environment = new Environment(GameConfig.LINE_COUNT, GameConfig.COLUMN_COUNT);
//        environment = generateRandomTreasures(5, environment);
//        environment.addElements(initForestsByTreasures(environment.getElements()));
//        environment.addElements(initObstacles(10, environment));

        ArrayList<EnvironmentElement> elements = environment.getElements();

        Treasure treasure = new Treasure(new Block(1,5));
        environment.addElement(treasure);


        Animal animal = new Animal(new Block(1,3), 100);
        environment.addElement(animal);


        Obstacle obstacle = new Obstacle(new Block(6,3));
        environment.addElement(obstacle);


        return environment;

    }

    public static ArrayList<ExplorerManager> buildInitMobile(Environment environment, EnvironmentManager environmentManager) {

//        intializeExplorer(environment, manager);

        ArrayList<ExplorerManager> managers = new ArrayList<>();


        // Generation d'un explorateur dans le qg
        int line = Utility.getRandomNumber(0, 3);
        int column = Utility.getRandomNumber(0, 3);

        Explorer explorer = ExplorerFactory.constructExplorer(Explorer.COMMUNICATIVE_EXPLORER);
        explorer.setBlock(new Block(column, line));

        ExplorerManager manager = new ExplorerManager(explorer, environment, environmentManager);

        managers.add(manager);


        return managers;
    }

//    private static void intializeExplorer(Environment environment, ExplorerManager manager) {
//        Block block = environment.getBlock(GameConfig.LINE_COUNT - 1, (GameConfig.COLUMN_COUNT - 1) / 2);
//        Explorer aircraft = new Explorer(block);
//        manager.set(aircraft);
//    }

    /**
     * @brief This function generates random treasures on the environment.
     * @param nbTreasures The number of treasures to generate.
     * @param environment The environment on which to generate the treasures.
     * @return Map The environment with the generated treasures. 
     */
//    public static Environment generateRandomTreasures(int nbTreasures, Environment environment) {
//        int x, y;
//        for(int i = 0; i < nbTreasures; i++) {
//            do {
//                x = Utility.getRandomNumber(1, GameConfig.WINDOW_WIDTH-3) * GameConfig.BLOCK_SIZE;
//                y = Utility.getRandomNumber(GameConfig.WINDOW_WIDTH/2, GameConfig.WINDOW_HEIGHT-3) * GameConfig.BLOCK_SIZE;
//
//            }while(Utility.isElementNBlockNearElement(environment, new Block(x, y), 3));
//
//            Position position = new Position(x, y);
//            try {
//                environment.addElement((Treasure)StaticElementFactory.createStaticElement(
//                        StaticElementFactory.TREASURE, position));
//            } catch (ValueException e) {
//                e.printStackTrace();
//            }
//        }
//        return environment;
//    }

}
