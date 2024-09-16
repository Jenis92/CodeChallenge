package com.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GamePanel} class.
 * These tests cover the functionality of creating snakes, validating inputs,
 * and painting components.
 */
public class GamePanelTest {

    private GamePanel gamePanel;

    /**
     * Sets up the GamePanel instance before each test.
     * Initializes the GamePanel with a ConcreteSnakeFactory and sets its preferred size.
     */
    @BeforeEach
    public void setUp() {
        SnakeFactory snakeFactory = new ConcreteSnakeFactory();
        gamePanel = GamePanel.createWithFactory(snakeFactory);
        gamePanel.setPreferredSize(new Dimension(400, 400));
    }

    /**
     * Helper method to access private fields using reflection.
     *
     * @param fieldName The name of the field to access.
     * @return The value of the private field.
     * @throws NoSuchFieldException   If the field does not exist.
     * @throws IllegalAccessException If access to the field is denied.
     */
    private Object getPrivateField(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = GamePanel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(gamePanel);
    }

    /**
     * Helper method to invoke private methods using reflection.
     *
     * @param methodName      The name of the method to invoke.
     * @param parameterTypes  The parameter types of the method.
     * @param parameters      The arguments to pass to the method.
     * @return The result of the method invocation.
     * @throws Exception If an error occurs while invoking the method.
     */
    private Object invokePrivateMethod(String methodName, Class<?>[] parameterTypes, Object... parameters)
            throws Exception {
        Method method = GamePanel.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(gamePanel, parameters);
    }

    /**
     * Tests the functionality of the 'Create Snake' button with valid inputs.
     * Verifies that clicking the button results in adding a snake to the list of snakes.
     */
    @Test
    public void testCreateSnakeButtonClick_ValidInputs() throws Exception {
        SwingUtilities.invokeLater(() -> {
            try {
                JTextField lengthField = (JTextField) getPrivateField("lengthField");
                JTextField speedField = (JTextField) getPrivateField("speedField");
                JComboBox<String> directionComboBox = (JComboBox<String>) getPrivateField("directionComboBox");
                JButton createSnakeButton = (JButton) getPrivateField("createSnakeButton");

                lengthField.setText("5");
                speedField.setText("100");
                directionComboBox.setSelectedItem("Clockwise");

                createSnakeButton.doClick();

                SwingUtilities.invokeLater(() -> {
                    try {

                        List<Renderable> snakes = (List<Renderable>) getPrivateField("snakes");
                        assertFalse(snakes.isEmpty(), "Snakes list should not be empty");

                    } catch (Exception e) {
                        fail("Exception occurred while validating the snake creation: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                fail("Exception occurred while accessing private fields: " + e.getMessage());
            }
        });
    }

    /**
     * Tests input validation with valid inputs.
     * Verifies that the validation result indicates valid inputs and correct values.
     */
    @Test
    public void testValidateInputs_Valid() throws Exception {
        SwingUtilities.invokeLater(() -> {
            try {
                JTextField lengthField = (JTextField) getPrivateField("lengthField");
                JTextField speedField = (JTextField) getPrivateField("speedField");
                JComboBox<String> directionComboBox = (JComboBox<String>) getPrivateField("directionComboBox");

                lengthField.setText("5");
                speedField.setText("100");
                directionComboBox.setSelectedItem("Clockwise");

                ValidationResult result = (ValidationResult) invokePrivateMethod("validateInputs", new Class<?>[]{});

                assertTrue(result.isValid(), "Inputs should be valid");
                assertEquals(5, result.getLength(), "Length should be 5");
                assertEquals(100L, result.getSpeed(), "Speed should be 100");
                assertEquals("Clockwise", result.getDirection(), "Direction should be Clockwise");
            } catch (Exception e) {
                fail("Exception occurred while accessing private fields or validating inputs: " + e.getMessage());
            }
        });
    }

    /**
     * Tests input validation with invalid inputs.
     * Verifies that the validation result indicates invalid inputs and provides an error message.
     */
    @Test
    public void testValidateInputs_Invalid() throws Exception {
        SwingUtilities.invokeLater(() -> {
            try {
                JTextField lengthField = (JTextField) getPrivateField("lengthField");
                JTextField speedField = (JTextField) getPrivateField("speedField");
                JComboBox<String> directionComboBox = (JComboBox<String>) getPrivateField("directionComboBox");

                lengthField.setText("0"); // Invalid length
                speedField.setText("abc"); // Invalid speed
                directionComboBox.setSelectedItem("InvalidDirection"); // Invalid direction

                ValidationResult result = (ValidationResult) invokePrivateMethod("validateInputs", new Class<?>[]{});

                assertFalse(result.isValid(), "Inputs should be invalid");
                assertNotNull(result.getErrorMessage(), "Error message should be present");
            } catch (Exception e) {
                fail("Exception occurred while accessing private fields or validating inputs: " + e.getMessage());
            }
        });
    }

    /**
     * Tests the painting of the GamePanel component.
     * Sets up a test square and calls the paintComponent method.
     */
    @Test
    public void testPaintComponent() {
        // Set up the panel size
        gamePanel.setSize(500, 500);

        // Add a square to the squareMap for testing
        setFieldValue("squareMap", new HashMap<Point, Rectangle2D>() {{
            put(new Point(10, 10), new Rectangle2D.Double(10, 10, 50, 50));
        }});

        Graphics g = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB).getGraphics();

        gamePanel.paintComponent(g);

    }

    /**
     * Helper method to set private fields using reflection.
     *
     * @param fieldName The name of the field to set.
     * @param value     The value to set the field to.
     */
    private void setFieldValue(String fieldName, Object value) {
        try {
            Field field = GamePanel.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(gamePanel, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }

}
