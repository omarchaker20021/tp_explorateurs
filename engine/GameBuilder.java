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

        environment = generateRandomTreasures(10, environment);
//        environment.addElements(initForestsByTreasures(environment.getElements()));
//        ArrayList<EnvironmentElement> obstacles = initObstacles(10, environment);

        environment.addElements(initObstacles(10, environment));

//        ArrayList<EnvironmentElement> animals = initAnimals(20, environment);

        environment.addElements(initAnimals(20, environment));


//        Treasure treasure = new Treasure(new Block(1,5));
//        environment.addElement(treasure);
//
//        Treasure treasure2 = new Treasure(new Block(5,1));
//        environment.addElement(treasure2);
//
//        Treasure treasure3 = new Treasure(new Block(5,5));
//        environment.addElement(treasure3);
//
//
//        Animal animal = new Animal(new Block(1,3), 100);
//        environment.addElement(animal);
//
//
//        Obstacle obstacle = new Obstacle(new Block(6,3));
//        environment.addElement(obstacle);


        return environment;

    }
    public static Block generateExplorerPosition(){
        // Générer une ligne aléatoire (0 à 3)
        int line = Utility.getRandomNumber(0, 4);

        // Générer une colonne en fonction de la ligne
        int column;
        if (line % 2 == 0) {
            // Lignes paires (0 et 2)
            column = (Utility.getRandomNumber(0,1) == 1) ? 1 : 3;
        } else {
            // Lignes impaires (1 et 3)
            column = (Utility.getRandomNumber(0,1) == 1) ? 0 : 2;
        }
        return new Block(line, column);
    }
    public static ArrayList<ExplorerManager> buildInitMobile(Environment environment, EnvironmentManager environmentManager) {

//        intializeExplorer(environment, manager);

        ArrayList<ExplorerManager> managers = new ArrayList<>();
        managers.addAll(generateExplorerManagers(environment, environmentManager, GameConfig.NB_REACTIFS, Explorer.REACTIVE_EXPLORER));
        managers.addAll(generateExplorerManagers(environment, environmentManager, GameConfig.NB_COGNITIFS, Explorer.COGNITIVE_EXPLORER));
        managers.addAll(generateExplorerManagers(environment, environmentManager, GameConfig.NB_COMMUNICANTS, Explorer.COMMUNICATIVE_EXPLORER));

        return managers;
    }

//    private static void intializeExplorer(Environment environment, ExplorerManager manager) {
//        Block block = environment.getBlock(GameConfig.LINE_COUNT - 1, (GameConfig.COLUMN_COUNT - 1) / 2);
//        Explorer aircraft = new Explorer(block);
//        manager.set(aircraft);
//    }

    public static ArrayList<ExplorerManager> generateExplorerManagers(Environment environment, EnvironmentManager environmentManager, int nbExplorerManagers, int type){

        ArrayList<ExplorerManager> managers = new ArrayList<ExplorerManager>();

        for (int i = 0; i < nbExplorerManagers; i++) {
            Explorer explorer = ExplorerFactory.constructExplorer(type);
            explorer.setBlock(generateExplorerPosition());

            managers.add(new ExplorerManager(explorer, environment, environmentManager));
        }
        return managers;
    }

    /**
     * @brief This function generates random treasures on the environment.
     * @param nbTreasures The number of treasures to generate.
     * @param environment The environment on which to generate the treasures.
     * @return Map The environment with the generated treasures. 
     */
    public static Environment generateRandomTreasures(int nbTreasures, Environment environment) {
        int line, column;
        for(int i = 0; i < nbTreasures; i++) {
            do {
                line = Utility.getRandomNumber(1, Environment.NUM_ZONES * Environment.NUM_ZONES);
                column = Utility.getRandomNumber(1, Environment.NUM_ZONES * Environment.NUM_ZONES);

            } while(Utility.isElementNBlockNearElement(environment, new Block(line, column), 2)
                    && ((column / 4 == 0) && (line / 4 == 0)));

            Block position = new Block(line, column);
            environment.addElement(new Treasure(position));
//                environment.addElement((Treasure)StaticElementFactory.createStaticElement(
//                        StaticElementFactory.TREASURE, position));
        }
        return environment;
    }

    public static ArrayList<EnvironmentElement> initObstacles(int nbSlowingDownObstacles, Environment map){
        ArrayList<EnvironmentElement> obstacles = new ArrayList<EnvironmentElement>();
        int column, line, i, randInstance;
        Block obstaclePosition;
        for(i = 0; i<nbSlowingDownObstacles; i++) {
            do {
                column = Utility.getRandomNumber(1, map.getColumnCount());
                line = Utility.getRandomNumber(1, map.getLineCount());
                obstaclePosition = new Block(line, column);
            }while((Utility.getEnvironmentElementFromPosition(map, obstaclePosition) != null
                    || ((column / 4 == 0) && (line / 4 == 0)))
                    || Utility.isLineTreasure(line, map.getElements())
                    || Utility.isColumnTreasure(column, map.getElements()));

            obstacles.add(new Obstacle(new Block(line, column)));
        }

        return obstacles;

    }

    public static ArrayList<EnvironmentElement> initAnimals(int nbSlowingDownAnimals, Environment map){
        ArrayList<EnvironmentElement> animals = new ArrayList<EnvironmentElement>();
        int column, line, i;
        Block animalPosition;
        for(i = 0; i<nbSlowingDownAnimals; i++) {
            do {
                column = Utility.getRandomNumber(1, map.getColumnCount());
                line = Utility.getRandomNumber(1, map.getLineCount());
                animalPosition = new Block(line, column);
            }while(!(Utility.getEnvironmentElementFromPosition(map, animalPosition) instanceof Treasure)
            		&& Utility.getEnvironmentElementFromPosition(map, animalPosition) != null
                    || ((column / 4 == 0) && (line / 4 == 0)));

            animals.add(new Animal(new Block(line, column)));
        }

        return animals;

    }

}
