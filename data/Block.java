package data;

public class Block {

    private int line;
    private int column;
    private boolean isObstacle;

    public Block(int line, int column) {
        this.line = line;
        this.column = column;
        this.isObstacle = false; // Par défaut, ce n'est pas un obstacle
    }

    public Block(int line, int column, boolean isObstacle) {
        this.line = line;
        this.column = column;
        this.isObstacle = isObstacle;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    @Override
    public String toString() {
        return "Block [line=" + line + ", column=" + column + ", isObstacle=" + isObstacle + "]";
    }
}
