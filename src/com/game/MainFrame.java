package com.game;

import javax.swing.*;
import java.awt.*;

/**
 * The MainFrame class represents the main window of the application.
 * It extends JFrame and sets up the initial layout and configuration
 * for the game panel and its components.
 */
public class MainFrame extends JFrame {

    /**
     * Constructs a new MainFrame instance.
     * This constructor sets up the layout, size, and default behavior
     * of the JFrame, and initializes the GamePanel with a SnakeFactory.
     */
    public MainFrame(){
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(400,400);
        SnakeFactory factory = new ConcreteSnakeFactory();
        GamePanel gamePanel = GamePanel.createWithFactory(factory);
        add(gamePanel, BorderLayout.CENTER);
    }
}
