package com.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MainFrame} class.
 * Tests the initialization of the MainFrame and interaction with its contained GamePanel.
 */
public class MainFrameTest {

    private MainFrame mainFrame;

    /**
     * Sets up the MainFrame instance before each test.
     * Initializes the MainFrame and waits for it to be visible.
     */
    @BeforeEach
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new MainFrame();
        });

        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Tests the initialization of the MainFrame.
     * Verifies that the MainFrame is visible, has the correct dimensions,
     * and contains a GamePanel.
     */
    @Test
    public void testMainFrameInitialization() {
        assertTrue(mainFrame.isVisible(), "MainFrame should be visible.");

        assertEquals(400, mainFrame.getWidth(), "MainFrame width should be 400.");
        assertEquals(400, mainFrame.getHeight(), "MainFrame height should be 400.");

        Component[] components = mainFrame.getContentPane().getComponents();
        boolean hasGamePanel = false;
        for (Component component : components) {
            if (component instanceof GamePanel) {
                hasGamePanel = true;
                break;
            }
        }
        assertTrue(hasGamePanel, "MainFrame should contain a GamePanel.");
    }

    /**
     * Tests the interaction with the GamePanel inside the MainFrame.
     * Sets text fields and clicks the 'Create Snake' button to verify that
     * a snake is created and added to the list.
     */
    @Test
    public void testGamePanelInteraction() {
        Component[] components = mainFrame.getContentPane().getComponents();
        GamePanel gamePanel = null;
        for (Component component : components) {
            if (component instanceof GamePanel) {
                gamePanel = (GamePanel) component;
                break;
            }
        }

        assertNotNull(gamePanel, "GamePanel should not be null.");

        JTextField lengthField = (JTextField) getFieldValue(gamePanel, "lengthField");
        JTextField speedField = (JTextField) getFieldValue(gamePanel, "speedField");
        JComboBox<String> directionComboBox = (JComboBox<String>) getFieldValue(gamePanel, "directionComboBox");
        JButton createSnakeButton = (JButton) getFieldValue(gamePanel, "createSnakeButton");

        lengthField.setText("5");
        speedField.setText("200");
        directionComboBox.setSelectedItem("Clockwise");

        createSnakeButton.doClick();

        List snakes = (List) getFieldValue(gamePanel, "snakes");
        assertFalse(snakes.isEmpty(), "There should be a snake created.");
        assertEquals(1, snakes.size(), "There should be one snake.");
    }

    /**
     * Helper method to access private fields of an object using reflection.
     *
     * @param obj        The object from which to access the field.
     * @param fieldName  The name of the field to access.
     * @return The value of the field.
     */
    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + fieldName, e);
        }
    }
}
