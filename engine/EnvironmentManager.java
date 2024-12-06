package engine;

import data.Block;
import data.*;
import data.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnvironmentManager {
    private Environment environment;


    private ArrayList<ExplorerManager> explorerManagers;

    private List<Explorer> explorers;
    private Random random;

    public EnvironmentManager(Environment environment) {
        this.environment = environment;
        this.explorers = new ArrayList<>();
        this.explorerManagers = GameBuilder.buildInitMobile(environment);
        this.random = new Random();
    }

    public void addExplorer(Explorer explorer) {
        explorers.add(explorer);
        explorerManagers.add(new ExplorerManager(explorer, environment));
    }
    private boolean isValidMove(int line, int column) {
        Block block = environment.getBlock(line, column);
        return block != null && !block.isObstacle();
    }


    public void moveExplorers() {
        for (Explorer explorer : explorers) {
            Block currentBlock = explorer.getBlock();
            int newLine = currentBlock.getLine();
            int newColumn = currentBlock.getColumn();

            // Générer une direction aléatoire (0: haut, 1: bas, 2: gauche, 3: droite)
            int direction = random.nextInt(4);
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
                explorer.setBlock(newBlock);
                System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");
            } else {
                System.out.println("Déplacement non valide pour l'explorateur.");
            }
        }
    }




    public List<Explorer> getExplorers() {
        return explorers;
    }

    public ArrayList<ExplorerManager> getExplorerManagers() {
        return explorerManagers;
    }
}
