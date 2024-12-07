package config;

public class GameConfig {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;

    public static final int BLOCK_SIZE = 50;

    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 1000;

    public static final int MAX_BOMB_COUNT = 3;

    public static final int BOMB_EXPLOSION_DELAY = 3;
    public static final int NbRounds = 5;
}
