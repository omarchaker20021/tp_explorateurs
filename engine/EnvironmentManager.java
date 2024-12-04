package engine;

import data.Environment;
import data.Explorer;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentManager {

    private Environment environment;
    private List<Explorer> explorers;

    // Constructeur
    public EnvironmentManager(Environment environment) {
        this.environment = environment;
        this.explorers = new ArrayList<>();
    }

    // Ajouter un explorateur
    public void addExplorer(Explorer explorer) {
        explorers.add(explorer);
    }

    // Déplacer les explorateurs (à compléter plus tard)
    public void moveExplorers() {
        for (Explorer explorer : explorers) {
            // Ici, on ajoutera le code pour les déplacer
            System.out.println("Déplacement de l'explorateur à compléter");
        }
    }

    // Accesseurs
    public Environment getEnvironment() {
        return environment;
    }

    public List<Explorer> getExplorers() {
        return explorers;
    }
}
