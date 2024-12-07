package engine;

import data.*;

import java.util.ArrayList;
import java.util.List;

public class ExplorerManager extends Thread {
    private Explorer explorer;

    private Environment environment;

    private EnvironmentManager environmentManager;

    /**
     * The trains has arrived at the terminus.
     */
    private boolean atTerminus = false;

    /**
     * The train departs from the start point.
     */
    private boolean running = true;

    private ArrayList<Integer> visitedZones = new ArrayList<Integer>();

    public ExplorerManager(Explorer explorer, Environment environment, EnvironmentManager environmentManager) {
        this.explorer = explorer;
        this.environment = environment;
        this.environmentManager = environmentManager;
    }

    @Override
    public void run() {
        while (running) {
            Utility.unitTime();

            // Déplacer l'explorateur
            randomMove();

            // Vérifiez si l'explorateur est mort après le mouvement
            if (explorer.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
                running = false; // Arrête le thread si l'explorateur est mort
            }
        }
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void updatePosition(int column, int line) {
        explorer.getBlock().set(column, line);
    }

    public void randomMove() {
        List<Explorer> deadExplorers = new ArrayList<>();

        Block currentBlock = explorer.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Générer une direction aléatoire (0: haut, 1: bas, 2: gauche, 3: droite)
        int direction = Utility.getRandomNumber(0, 3);
        System.out.println("Explorateur à (" + newLine + ", " + newColumn + "), direction : " + direction);

        switch (direction) {
            case 0: newLine--; break; // Haut
            case 1: newLine++; break; // Bas
            case 2: newColumn--; break; // Gauche
            case 3: newColumn++; break; // Droite
        }

        // Vérifie que le déplacement est valide
        if (isValidMove(newLine, newColumn)) {
            Block newBlock = environment.getBlock(newLine, newColumn);

            // Vérifier si le nouveau bloc est un obstacle
            if (!Utility.isObstacleByBlock(newBlock, environment)) {
                updatePosition(newColumn, newLine);
                System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");

                // Vérifier si un trésor est présent sur ce bloc
                EnvironmentElement element = Utility.getElementFromBlock(environment, newBlock);
                if (element instanceof Treasure) {
                    Treasure treasure = (Treasure) element;
                    if (!treasure.isCollected()) {
                        treasure.collect(); // Collecte le trésor
                        this.environment.getElements().remove(treasure);
                    } else {
                        System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                    }
                }
            } else {
                System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
            }

            // Combat avec un animal
            if (Utility.isAnimalByBlock(newBlock, environment)) {
                System.out.println("A rencontré un animal");
                EnvironmentElement element = Utility.getElementFromBlock(environment, newBlock);
                if (element instanceof Animal animal) {
                    environmentManager.fight(explorer, animal);
                }
            }

            // Si l'explorateur est mort après le combat
            if (explorer.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
                running = false; // Arrêter le thread
            }

            updatePosition(newColumn, newLine);
            System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");
        } else {
            System.out.println("Déplacement non valide pour l'explorateur.");
        }
    }


    private boolean isValidMove(int line, int column) {
//        Block block = environment.getBlock(line, column);
//        return block != null && !(environment.isOnBorder(block));
        return !(Utility.isBlockOutOfMap(column, line));
    }

    public Explorer getExplorer() {
        return explorer;
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
    }

    //    public int getTrainId() {
//        return train.getId();
//    }
//
//    public int getPosition() {
//        return train.getCurrentPosition();
//    }
//
//    public BlockManager getBlockManager() {
//        return blockManager;
//    }
//
//    public void setBlockManager(BlockManager blockManager) {
//        this.blockManager = blockManager;
//    }
//
//    public boolean isAtTerminus() {
//        return atTerminus;
//    }
}
