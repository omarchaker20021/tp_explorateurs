package view;

import config.GameConfig;
import data.Environment;
import engine.EnvironmentManager;
import engine.ExplorerManager;
import engine.GameBuilder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */
public class MainGui extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

//    private Environment environment;

    private final static Dimension preferredSize = new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

    private EnvironmentManager manager;

    private GameDisplay dashboard;

    public MainGui(String title) {
        super(title);
        init();
    }

    private void init() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

//        KeyControls keyControls = new KeyControls();
//        JTextField textField = new JTextField();
//        textField.addKeyListener(keyControls);
//        contentPane.add(textField, BorderLayout.SOUTH);
        Environment environment = GameBuilder.buildMap();
        manager = new EnvironmentManager(environment);

//        manager = GameBuilder.buildInitMobile(map);
        dashboard = new GameDisplay(environment, manager);

//        MouseControls mouseControls = new MouseControls();
//        dashboard.addMouseListener(mouseControls);

        dashboard.setPreferredSize(preferredSize);
        contentPane.add(dashboard, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setPreferredSize(preferredSize);
        setResizable(false);
    }

    @Override
    public void run() {
        while (true) {

//            System.out.println("Test");
            for (ExplorerManager explorerManager : manager.getExplorerManagers()){
                if (!explorerManager.isAlive())
                    explorerManager.start();
            }

            try {
                Thread.sleep(GameConfig.GAME_SPEED);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }



            dashboard.repaint();
//            manager.nextRound();
//            dashboard.repaint();

        }
    }

//    private class KeyControls implements KeyListener {
//
//        @Override
//        public void keyPressed(KeyEvent event) {
//            char keyChar = event.getKeyChar();
//            switch (keyChar) {
//
//                case 'q':
//                    manager.moveLeftAirCraft();
//                    break;
//                case 'd':
//                    manager.moveRightAirCraft();
//                    break;
//                case 'm':
//                    manager.generateMissile();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        @Override
//        public void keyTyped(KeyEvent e) {
//
//        }
//
//        @Override
//        public void keyReleased(KeyEvent e) {
//
//        }
//    }
//
//    private class MouseControls implements MouseListener {
//
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            int x = e.getX();
//            int y = e.getY();
//
//            Block bombPosition = dashboard.getBombPosition(x, y);
//            manager.putBomb(bombPosition);
//        }
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//
//        }
//    }

}
