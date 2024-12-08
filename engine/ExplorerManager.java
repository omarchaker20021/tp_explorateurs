package engine;

import data.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExplorerManager extends Thread {
    private Explorer explorer;

    private Environment environment;

    private EnvironmentManager environmentManager;
    private Treasure treasure;

    private ArrayList<Integer> pathToTreasure = null;

    private Iterator<Integer> pathStep = null;

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

    public Treasure getTreasure() {
        return treasure;
    }

    @Override
    public void run() {
//        Treasure treasure = null; // Positions des trésors trouvés

        int type = explorer.getExplorerType();
        while (running) {
            Utility.unitTime();
            Utility.unitTime();
//            if (environmentManager.getNbRounds() % 10 == 0){
//                Utility.unitTime();
//            }


            if (type == Explorer.COMMUNICATIVE_EXPLORER) { // Communicatif

//                Utility.unitTime();
                randomMove();
                Treasure treasure = scanForTreasure(); // Récupère uniquement les positions des trésors
                if (treasure != null) {
                    inform(treasure); // Transmet les positions au cognitif
                    for (ExplorerManager explorerManager : environmentManager.getExplorerManagers()){
//                        ExplorerManager explorerManager = null;
                        if (explorerManager.getExplorer().getExplorerType() == Explorer.COGNITIVE_EXPLORER
                            && explorerManager.getTreasure() == null
                            && !environmentManager.getAffectedTreasures().contains(treasure)){
                            explorerManager.setTreasure(treasure);
                            environmentManager.addAffectedTreasure(treasure);
                            // constructPathToTreasure(trea);
                            break;
                        }
                    }
                }
            } else if (type == Explorer.COGNITIVE_EXPLORER) { // Cognitif
                if (treasure != null) {
//                    if (pathToTreasure == null){
//                        constructPathToTreasure();
//                        pathStep = pathToTreasure.iterator();
//                    }
                    inform(treasure); // Transmet les positions au cognitif
                    moveToTreasure();
//                    pathStep.next();
//                    nextMoveToTreasure();
                }
                else {
                    randomMove();
                }
            } else if (type == Explorer.REACTIVE_EXPLORER) {
                randomMove();
            }

            // Vérifiez si l'explorateur est mort après le mouvement
            if (explorer.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
                ExplorerFactory.setExplorer(explorer); // Reset de la position
            }
        }
    }


    public void constructPathToTreasure() {

        int startRow = explorer.getLine();
        int startCol = explorer.getColumn();
        int treasureRow = treasure.getLine();
        int treasureCol = treasure.getColumn();

        pathToTreasure = new ArrayList<Integer>();
        int row = startRow, col = startCol;

        // Directions: [0: up, 1: down, 2: left, 3: right]
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        // Continue until we reach the treasure
        while (row != treasureRow || col != treasureCol) {
            int nextRow = row, nextCol = col;

            // Prioritize moving towards the treasure
            if (row < treasureRow) { // Move down
                nextRow = row + 1;
                nextCol = col;
            } else if (row > treasureRow) { // Move up
                nextRow = row - 1;
                nextCol = col;
            } else if (col < treasureCol) { // Move right
                nextRow = row;
                nextCol = col + 1;
            } else if (col > treasureCol) { // Move left
                nextRow = row;
                nextCol = col - 1;
            }

            // Check if the next move is valid
            if (isValidMove(nextRow, nextCol)) {
                // Determine the direction
                if (nextRow < row) {
                    pathToTreasure.add(0); // Up
                } else if (nextRow > row) {
                    pathToTreasure.add(1); // Down
                } else if (nextCol < col) {
                    pathToTreasure.add(2); // Left
                } else if (nextCol > col) {
                    pathToTreasure.add(3); // Right
                }

                // Update position
                row = nextRow;
                col = nextCol;
            } else {
                // If blocked, try a different valid direction
                boolean moved = false;
                for (int i = 0; i < 4; i++) {
                    int newRow = row + dRow[i];
                    int newCol = col + dCol[i];
                    if (isValidMove(newRow, newCol)) {
                        pathToTreasure.add(i);
                        row = newRow;
                        col = newCol;
                        moved = true;
                        break;
                    }
                }
                if (!moved) {
                    throw new RuntimeException("No valid path to the treasure!");
                }
            }
        }
    }

    public void nextMoveToTreasure(){
        if (pathStep.hasNext()){
            int direction = pathStep.next();
            moveInDirection(direction);
        }
//        else {
//            // Réinitialiser treasure à null
//            treasure = null;
//        }
    }

    public void moveInDirection(int direction) {
        Block currentBlock = explorer.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

//        System.out.println("Explorateur à (" + newLine + ", " + newColumn + "), direction : " + direction);

        // Appliquer le mouvement selon la direction
        switch (direction) {
            case 0:
                if(newLine / 4 != 0
                        && newColumn / 4 != 0){
                    newLine--;
                    break;
                }
            case 1: newLine++; break; // Bas
            case 2:
                if(newLine / 4 != 0
                        && newColumn / 4 != 0){
                    newColumn--;
                    break;
                } // Gauche
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

            // Vérifier si un animal est présent sur ce bloc
            else if (Utility.isAnimalByBlock(newBlock, environment)) {
                System.out.println("Un animal est détecté sur la position (" + newLine + ", " + newColumn + "). Combat engagé.");

                EnvironmentElement elementAnimal = Utility.getElementFromBlock(environment, newBlock);
                if (elementAnimal instanceof Animal animal) {
                    // Appeler la méthode fight
                    environmentManager.fight(explorer, animal);
                    environmentManager.increaseNbCombats();

                    // Vérifiez si l'explorateur est mort après le combat
                    if (explorer.getHealth() <= 0) {
                        System.out.println("L'explorateur est mort après le combat.");
                        pathToTreasure = null;
                        return; // Stoppe l'exécution de la méthode
                    }

                    // Vérifiez si l'animal est mort
                    if (animal.getHealth() <= 0) {
                        System.out.println("L'animal est mort après le combat.");
                    }
                } else {
                    System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                    return; // Arrête le mouvement
                }
            }
//            else if (((Utility.getElementFromBlock(environment, newBlock) instanceof Treasure))){
////                System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
//                return;
//            }
//            System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");

            updatePosition(newColumn, newLine);

            // Vérifier si un trésor est présent sur ce bloc
            EnvironmentElement element = Utility.getElementFromBlock(environment, newBlock);
            if (element instanceof Treasure treasure && explorer.getExplorerType() != Explorer.COMMUNICATIVE_EXPLORER) {
                if (!treasure.isCollected()) {
                    treasure.collect(); // Collecte le trésor
                    environmentManager.increaseNbCollectedTreasures();
                    this.environment.getElements().remove(treasure);
                    environment.getElementsByBlocks().remove(treasure.getBlock());
                    System.out.println("Trésor collecté !");
                } else {
                    System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                }
            }
        } else {
            System.out.println("Déplacement non valide pour l'explorateur.");
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
        if (this.treasure.isCollected()){
//            goToQG();
            treasure = null;
        }


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
//            System.out.println("Explorateur à (" + currentLine + ", " + currentColumn + ")");
//            System.out.println("Trésor à (" + treasureLine + ", " + treasureColumn + ")");

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
                randomMove();
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
            environmentManager.increaseNbCollectedTreasures();
            environment.getElements().remove(treasure); // Supprimer le trésor de l'environnement
            environment.getElementsByBlocks().remove(treasure.getBlock());
        }
//        else {
//            System.out.println("L'explorateur n'a pas pu atteindre le trésor.");
//        }
    }


    public void randomMove() {
        Block currentBlock = explorer.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Générer une direction aléatoire (0: haut, 1: bas, 2: gauche, 3: droite)
        int direction = Utility.getRandomNumber(0, 3);
//        System.out.println("Explorateur à (" + newLine + ", " + newColumn + "), direction : " + direction);

        moveInDirection(direction);
    }

    private boolean isValidMove(int line, int column) {
        return !(Utility.isBlockOutOfMap(column, line));
    }

    public Explorer getExplorer() {
        return explorer;
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
    }

    private List<EnvironmentElement> scannedElements = new ArrayList<>();
    private List<Block> scanForTreasures() {
        Block currentBlock = explorer.getBlock(); // Bloc actuel de l'explorateur
        scannedElements.clear(); // Réinitialiser la liste des éléments scannés
        List<Block> treasurePositions = new ArrayList<>(); // Liste pour les positions des trésors

        // Obtenir la zone actuelle
        int currentZone = Utility.getZoneByBlock(currentBlock);
        Block[][] blocksInZone = Utility.getBlocksByZone(environment, currentZone);

        // Parcourir les blocs de la zone pour trouver les trésors
        for (int i = 0; i < blocksInZone.length; i++) {
            for (int j = 0; j < blocksInZone[i].length; j++) {
                Block block = blocksInZone[i][j];
                EnvironmentElement element = Utility.getElementFromBlock(environment, block);
                if (element != null && element instanceof Treasure) { // Vérifie si l'élément est un trésor
                    treasurePositions.add(block); // Ajoute la position du trésor
                    System.out.println("Communicatif : Trésor trouvé à " + block);
                }
            }
        }

        return treasurePositions; // Retourne les positions des trésors
    }

    private Treasure scanForTreasure() {
        Block currentBlock = explorer.getBlock(); // Bloc actuel de l'explorateur
        scannedElements.clear(); // Réinitialiser la liste des éléments scannés
        List<Block> treasurePositions = new ArrayList<>(); // Liste pour les positions des trésors

        // Obtenir la zone actuelle
        int currentZone = Utility.getZoneByBlock(currentBlock);
        Block[][] blocksInZone = Utility.getBlocksByZone(environment, currentZone);

        // Parcourir les blocs de la zone pour trouver les trésors
        for (int i = 0; i < blocksInZone.length; i++) {
            for (int j = 0; j < blocksInZone[i].length; j++) {
                Block block = blocksInZone[i][j];
                EnvironmentElement element = Utility.getElementFromBlock(environment, block);
                if (element != null && element instanceof Treasure) { // Vérifie si l'élément est un trésor
                    System.out.println("Communicatif : Trésor trouvé à " + block);
                    return (Treasure)element; // Ajoute la position du trésor
                }
            }
        }
        return null;
//        return treasurePositions; // Retourne les positions des trésors
    }

    public void moveCommunicative() {
        Block currentBlock = explorer.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        boolean moved = false;
        int attempts = 0; // Limite le nombre d'essais pour éviter une boucle infinie

        while (!moved && attempts < 4) { // Maximum 4 directions à essayer
            int direction = Utility.getRandomNumber(0, 3); // Générer une direction aléatoire
            System.out.println("Essai de direction : " + direction);

            switch (direction) {
                case 0: newLine = currentBlock.getLine() - 1; newColumn = currentBlock.getColumn(); break; // Haut
                case 1: newLine = currentBlock.getLine() + 1; newColumn = currentBlock.getColumn(); break; // Bas
                case 2: newLine = currentBlock.getLine(); newColumn = currentBlock.getColumn() - 1; break; // Gauche
                case 3: newLine = currentBlock.getLine(); newColumn = currentBlock.getColumn() + 1; break; // Droite
            }

            // Vérifie que le déplacement est valide et non bloqué par un obstacle
            if (isValidMove(newLine, newColumn)) {
                Block newBlock = environment.getBlock(newLine, newColumn);

                if (!Utility.isObstacleByBlock(newBlock, environment)) {
                    updatePosition(newColumn, newLine);
                    System.out.println("Explorateur s'est déplacé vers : (" + newLine + ", " + newColumn + ")");
                    moved = true; // Déplacement effectué avec succès
                } else {
                    System.out.println("Obstacle détecté à (" + newLine + ", " + newColumn + "). Essai d'une autre direction.");
                }
            } else {
                System.out.println("Déplacement non valide pour la position (" + newLine + ", " + newColumn + ").");
            }

            attempts++;
        }

        if (!moved) {
            System.out.println("Aucune direction valide trouvée pour l'explorateur.");
        }
    }

    private List<EnvironmentElement> receivedElements= new ArrayList<>();
    private void inform(List<Block> treasurePositions) {
        System.out.println("Cognitif a reçu les positions des trésors : " + treasurePositions);
    }

    private void inform(Treasure treasure) {
        System.out.println("Cognitif a reçu les positions des trésors : " + treasure.getBlock());
    }

    private void setTreasure(Treasure treasure){
        this.treasure = treasure;
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
