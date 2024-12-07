package engine;

import data.*;

import java.util.ArrayList;
import java.util.List;

public class ExplorerManager extends Thread {
    private Explorer explorer;

    private Environment environment;

    private EnvironmentManager environmentManager;
    private Treasure treasure;

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

            randomMove();
            scan();
            moveToTreasure();

            // Vérifiez si l'explorateur est mort après le mouvement
            if (explorer.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
//                running = false; // Arrête le thread si l'explorateur est mort
                // Reset de la position de l'explorateur
                explorer.setBlock(GameBuilder.generateExplorerPosition());
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
    public void moveToTreasure() {
        if (treasure == null) {
            System.out.println("Aucun trésor assigné.");
            return;
        }

        // Récupérer les coordonnées du trésor
        int treasureLine = treasure.getBlock().getLine();
        int treasureColumn = treasure.getBlock().getColumn();

        // Coordonnées actuelles de l'explorateur
        int currentLine = explorer.getBlock().getLine();
        int currentColumn = explorer.getBlock().getColumn();

        // Boucle de déplacement vers le trésor
        if (currentLine != treasureLine || currentColumn != treasureColumn) {
            System.out.println("Explorateur à (" + currentLine + ", " + currentColumn + ")");
            System.out.println("Trésor à (" + treasureLine + ", " + treasureColumn + ")");

            // Calcul du prochain mouvement
            if (currentLine < treasureLine) {
                currentLine++; // Avancer vers le bas
            } else if (currentLine > treasureLine) {
                currentLine--; // Avancer vers le haut
            } else if (currentColumn < treasureColumn) {
                currentColumn++; // Avancer vers la droite
            } else if (currentColumn > treasureColumn) {
                currentColumn--; // Avancer vers la gauche
            }

            // Vérification des obstacles avant de déplacer
            Block nextBlock = environment.getBlock(currentLine, currentColumn);
            if (Utility.isObstacleByBlock(nextBlock, environment)) {
                System.out.println("Obstacle détecté à la position (" + currentLine + ", " + currentColumn + "). Mouvement annulé.");
            }
            else {
            	// Mettre à jour la position de l'explorateur
                updatePosition(currentColumn, currentLine);
            }

            
        }

        // Vérifier si l'explorateur atteint le trésor
        if (currentLine == treasureLine && currentColumn == treasureColumn) {
            System.out.println("Explorateur a atteint le trésor !");
            treasure.collect(); // Collecter le trésor
            environment.getElements().remove(treasure); // Supprimer le trésor de l'environnement
        } else {
            System.out.println("L'explorateur n'a pas pu atteindre le trésor.");
        }
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
            if (Utility.isObstacleByBlock(newBlock, environment)) {
                System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                return; // Arrête le mouvement
            }

            // Si pas d'obstacle, mettre à jour la position
            updatePosition(newColumn, newLine);
            System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");

            // Vérifier si un trésor est présent sur ce bloc
            EnvironmentElement element = Utility.getElementFromBlock(environment, newBlock);
            if (element instanceof Treasure treasure) {
                if (!treasure.isCollected()) {
                    treasure.collect(); // Collecte le trésor
                    this.environment.getElements().remove(treasure);
                    System.out.println("Trésor collecté !");
                } else {
                    System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                }
            }

            // Vérifier si un animal est présent sur ce bloc
            if (Utility.isAnimalByBlock(newBlock, environment)) {
                System.out.println("Un animal est détecté sur la position (" + newLine + ", " + newColumn + "). Combat engagé.");

                EnvironmentElement elementAnimal = Utility.getElementFromBlock(environment, newBlock);
                if (elementAnimal instanceof Animal animal) {
                    // Appeler la méthode fight
                    environmentManager.fight(explorer, animal);

                    // Vérifiez si l'explorateur est mort après le combat
                    if (explorer.getHealth() <= 0) {
                        System.out.println("L'explorateur est mort après le combat.");
//                        running = false; // Arrête le thread
                        return; // Stoppe l'exécution de la méthode
                    }

                    // Vérifiez si l'animal est mort
                    if (animal.getHealth() <= 0) {
                        System.out.println("L'animal est mort après le combat.");
                    }
                }
            }
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

//    public void exploreZone(int zoneId) {
//        if (visitedZones.contains(zoneId)) {
//            System.out.println("Zone " + zoneId + " déjà visitée. Exploration ignorée.");
//            return; // La zone a déjà été explorée
//        }
//
//        visitedZones.add(zoneId); // Marque la zone comme visitée
//
//        // Récupérer les blocs de la zone grâce à Utility
//        List<Block> zoneBlocks = Utility.getBlocksFromZone(zoneId, environment);
//
//        List<EnvironmentElement> zoneElements = new ArrayList<>();
//        List<Block> treasurePositions = new ArrayList<>();
//
//        for (Block block : zoneBlocks) {
//            EnvironmentElement element = Utility.getElementFromBlock(environment, block);
//            if (element != null) {
//                zoneElements.add(element);
//                // Si c'est un trésor, enregistrer sa position
//                if (element instanceof Treasure) {
//                    treasurePositions.add(block);
//                }
//            }
//        }
//
//        // Rapporter les résultats
//        System.out.println("Exploration de la zone " + zoneId + " terminée.");
//        System.out.println("Éléments trouvés : " + zoneElements);
//        System.out.println("Trésors détectés aux positions : " + treasurePositions);
//    }

    private List<EnvironmentElement> scannedElements = new ArrayList<>();

    private void scan() {
        Block currentBlock = explorer.getBlock();
        scannedElements.clear();
        EnvironmentElement element = Utility.getElementFromBlock(environment, currentBlock);

        if (element != null) {
            scannedElements.add(element);
            informCognitiveAgent();
        }
    }

    private void informCognitiveAgent() {
        System.out.println("Objet : " + scannedElements);
        scannedElements.clear();
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
