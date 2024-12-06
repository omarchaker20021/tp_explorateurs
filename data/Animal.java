package data;

public class Animal extends Objet {
    private int health;

    public Animal(Block block, int health){
        super(block);
        this.health = health;
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int health){
        this.health = health;
    }

//    public int getStrength() {
//        return 0;
//    }
}
