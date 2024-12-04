package data;

public class EnvironmentElement {
    private Block block;

    public EnvironmentElement(Block block){
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void setPosition(Block block) {
        this.block = block;
    }

}
