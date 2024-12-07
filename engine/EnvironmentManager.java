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
    private EnvironmentManager environmentManager;

    public EnvironmentManager(Environment environment) {
        this.environment = environment;
        this.explorers = new ArrayList<>();
        this.explorerManagers = GameBuilder.buildInitMobile(environment, this);
        this.random = new Random();
    }

    public void addExplorer(Explorer explorer) {
        explorers.add(explorer);
        explorerManagers.add(new ExplorerManager(explorer, environment, environmentManager));
    }
    private boolean isValidMove(int line, int column) {
        Block block = environment.getBlock(line, column);
        return block != null && !block.isObstacle();
    }


    public void moveExplorers() {

        List<Explorer> deadExplorers = new ArrayList<>();

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

        explorers.removeAll(deadExplorers);
    }

    public void fight(Explorer explorer, Animal animal) {
        Random random = new Random();
        boolean explorerWins = random.nextBoolean(); // Génère aléatoirement true ou false

        if (explorerWins) {
            // L'explorateur gagne
            System.out.println("L'explorateur gagne le combat !");
            int damageToAnimal = random.nextInt(30) + 20; // Dégâts entre 20 et 50
            animal.setHealth(animal.getHealth() - damageToAnimal);
            System.out.println("L'animal perd " + damageToAnimal + " points de santé.");
        } else {
            // L'animal gagne
            System.out.println("L'animal gagne le combat !");
            int damageToExplorer = random.nextInt(30) + 20; // Dégâts entre 20 et 50
            explorer.setHealth(explorer.getHealth() - damageToExplorer);
            System.out.println("L'explorateur perd " + damageToExplorer + " points de santé.");
        }

        // Vérifiez si l'explorateur est mort
        if (explorer.getHealth() <= 0) {
            System.out.println("L'explorateur est mort.");
            explorers.remove(explorer); // Supprimer de la liste des explorateurs
        }

        // Vérifiez si l'animal est mort
        if (animal.getHealth() <= 0) {
            System.out.println("L'animal est mort.");
            //animal.getBlock().setAnimal(null); // Supprimer l'animal du bloc
            this.environment.getElements().remove(animal);
        }
    }





    public List<Explorer> getExplorers() {
        return explorers;
    }

    public ArrayList<ExplorerManager> getExplorerManagers() {
        return explorerManagers;
    }
}
