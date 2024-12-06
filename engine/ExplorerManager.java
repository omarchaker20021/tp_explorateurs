package engine;

import data.Block;
import data.Environment;
import data.EnvironmentElement;
import data.Explorer;
import data.Treasure;

public class ExplorerManager extends Thread {
    private Explorer explorer;

    private Environment environment;

    /**
     * The trains has arrived at the terminus.
     */
    private boolean atTerminus = false;

    /**
     * The train departs from the start point.
     */
    private boolean running = true;

    public ExplorerManager(Explorer explorer, Environment environment) {
        this.explorer = explorer;
        this.environment = environment;
    }

    @Override
    public void run() {
        while (running) {
            Utility.unitTime();

//            Block position = explorer.getBlock();

            randomMove();
            System.out.println("Movement");


            // The train will stay for a while at each station.
//            Station station = line.getStation(position);
//            if (station != null) {
//                for (int stay = 1; stay <= station.getStayTime(); stay++) {
//                    SimulationUtility.unitTime();
//                }
//            }

//            int speed = train.getSpeed();
//            int blockEndPoint = blockManager.getEndPoint();


//            if (position + speed >= blockEndPoint) {
//
//                // We need to ask the entry into the block.
//                if (blockEndPoint == line.getTotalLength()) {
//                    atTerminus = true;
//                } else {
//                    BlockManager nextBlockManager = simulation.getBlockManager(blockEndPoint);
//                    nextBlockManager.enter(this);
//                }
//            } else {
//
//                // The trains advances on the line.
//                updatePosition(position + speed);
//            }
        }

        // The train leaves the last occupied block.
//        blockManager.exit();
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
                    } else {
                        System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                    }
                }
            } else {
                System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
            }
        } else {
            System.out.println("Déplacement non valide pour l'explorateur.");
        }
    }



    private boolean isValidMove(int line, int column) {
        Block block = environment.getBlock(line, column);
        return block != null && !(environment.isOnBorder(block));
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
