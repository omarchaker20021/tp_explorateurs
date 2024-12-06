import view.MainGui;

public class TestGUI {
    public static void main(String[] args) {
        MainGui mainGui = new MainGui("Fenêtre");
        Thread guiThread = new Thread(mainGui);
        guiThread.start();
    }
}


