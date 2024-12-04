package data;

public class Explorer extends EnvironmentElement {

    // Constantes associées aux attributs des types
    public static final int COMMUNICATIVE_HEALTH = 100;
    public static final int COMMUNICATIVE_STRENGTH = 80;

    public static final int COGNITIVE_HEALTH = 70;
    public static final int COGNITIVE_STRENGTH = 50;

    public static final int REACTIVE_HEALTH = 80;
    public static final int REACTIVE_STRENGTH = 40;
    public static final int REACTIVE_EXPLORER = 0;
    public static final int COMMUNICATIVE_EXPLORER = 1;
    public static final int COGNITIVE_EXPLORER = 2;
    // Attributs
    private int health;
    private int strength;

    // Constante
    public static final int EXPLORER_TYPE = 1;

    // Constructeur
    public Explorer(int health, int strength) {
        super();
        this.health = health;
        this.strength = strength;
    }

    public Explorer(Block block, int health, int strength) {
        super(block);
        this.health = health;
        this.strength = strength;
    }

    // Getters et Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public static int getExplorerType() {
        return EXPLORER_TYPE;
    }
}
