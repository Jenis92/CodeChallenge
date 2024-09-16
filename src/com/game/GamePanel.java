package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GamePanel represents the main game interface where users can create and interact with snakes.
 * It handles the graphical representation of the game, user inputs, and scheduling tasks for snakes.
 */
public class GamePanel extends JPanel {

    private static final int SQUARE_SIZE = 50;
    private static final int SPACING = 10;
    private static final long DEFAULT_SPEED = 100;
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private JComboBox<String> directionComboBox;
    private JButton createSnakeButton;
    private JTextField lengthField;
    private JTextField speedField;


    private List<Renderable> snakes;
    private Map<Point, Rectangle2D> squareMap;
    private ScheduledExecutorService scheduler;
    //private Map<Snake, ScheduledFuture<?>> snakeTasks;
    private List<ScheduledFuture<?>> snakeTasks;
    private SnakeFactory snakeFactory;


    /**
     * Constructs a GamePanel instance using the provided Builder.
     *
     * @param builder The builder instance containing configuration for the GamePanel.
     */
    protected GamePanel(Builder builder){
        this.snakeFactory = builder.snakeFactory;
        this.snakes = new ArrayList<>();
        this.squareMap = new HashMap<>();
        //this.snakeTasks = new HashMap<>();
        this.snakeTasks = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(10);


        this.setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        lengthField = new JTextField(3);
        speedField = new JTextField(3);

        directionComboBox = new JComboBox<>(new String[] { "Clockwise", "Anticlockwise"});
        createSnakeButton = new JButton("Create Snake");
        controlPanel.add(directionComboBox);
        controlPanel.add(new JLabel("Length:"));
        controlPanel.add(lengthField);
        controlPanel.add(new JLabel("Speed (ms):"));
        controlPanel.add(speedField);
        controlPanel.add(createSnakeButton);
        add(controlPanel, BorderLayout.SOUTH);

        createSnakeButton.addActionListener(this::handleCreateSnakeButtonClick);
    }

    /**
     * Paints the component by rendering the grid and snakes.
     *
     * @param g The Graphics object used for painting.
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.setColor(Color.BLUE);
        for(int row = 0; row < getNumRows(); row++){
            for(int col = 0; col < getNumCols(); col++){
                int x = col * (SQUARE_SIZE + SPACING) + SPACING;
                int y = row * (SQUARE_SIZE + SPACING) + SPACING;
                Rectangle2D sqr = squareMap.get(new Point(x, y));
                if(sqr != null){
                    g2.fill(new Rectangle2D.Double(x, y, SQUARE_SIZE, SQUARE_SIZE));
                } else{
                    break;
                }
            }
        }

        for(Renderable renderable : snakes){
            g2.setColor(Color.RED);
            renderable.render(g2);
        }

    }

    /**
     * Handles the click event for creating a new snake.
     * Validates inputs, creates a new snake, and schedules its task.
     *
     * @param actionEvent The ActionEvent triggered by the button click.
     */
    private void handleCreateSnakeButtonClick(ActionEvent actionEvent) {
        try{
            ValidationResult result = validateInputs();
            if(!result.isValid()){
                LOGGER.warning("Validation failed: "+result.getErrorMessage());
                JOptionPane.showMessageDialog(GamePanel.this, result.getErrorMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //If validation is successful, proceed with snake creation
            int[] position  = findAvailablePosition();
            if(position.length == 0){
                LOGGER.warning("No available space for the snake");
                JOptionPane.showMessageDialog(GamePanel.this,"No available space for the snake", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int x = position[0];
            int y = position[1];

            if(snakeFactory == null){
                throw new IllegalArgumentException("SnakeFactory is not initialized");
            }
            Snake newSnake = snakeFactory.createSnake(result.getLength(),result.getDirection(),x, y, result.getSpeed());
            snakes.add(newSnake);

            // Schedule the snake's task
            ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() ->{
                try{
                    Rectangle2D square = squareMap.get(new Point((int) newSnake.getSnakeStartX(), (int) newSnake.getSnakeStartY()));
                    if(square != null){
                        SnakeTask task = new SnakeTask(newSnake, newSnake, square);
                        task.run();
                        SwingUtilities.invokeLater(GamePanel.this::repaint);
                    }
                } catch (Exception ex){
                    LOGGER.log(Level.SEVERE, "Error updating snake position", ex);
                }
            }, 0, (long)newSnake.getSpeed(), TimeUnit.MILLISECONDS);
            //snakeTasks.put(newSnake, future);
            snakeTasks.add(future);
        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", ex);
            JOptionPane.showMessageDialog(GamePanel.this,"An unexpected error occurred. Please try again.","Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validates user inputs from the text fields and combo box.
     *
     * @return A ValidationResult object containing validation results.
     */
    private ValidationResult validateInputs(){
        StringBuilder errors = new StringBuilder();
        int length = 0;
        long speed = 0;
        String direction = (String) directionComboBox.getSelectedItem();

        //validate length
        String lengthText = lengthField.getText().trim();
        try{
            length = validateLength(lengthText);
        } catch(IllegalArgumentException ex){
            errors.append(ex.getMessage()).append("\n");
            LOGGER.warning("Length validation failed: "+ex.getMessage());
        }

        //validate speed
        String speedText = speedField.getText().trim();
        try{
            speed = validateSpeed(speedText);
        } catch (IllegalArgumentException ex){
            errors.append(ex.getMessage()).append("\n");
            LOGGER.warning("Speed validation failed: "+ex.getMessage());
        }

        //validate direction
        try{
            validateDirection(direction);
        } catch (IllegalArgumentException ex){
            errors.append(ex.getMessage()).append("\n");
            LOGGER.warning("Direction validation failed: "+ex.getMessage());
        }

        boolean isValid = errors.length() == 0;
        return new ValidationResult(isValid, isValid ? null : errors.toString(), length, speed, direction);
    }

    /**
     * Validates the length input.
     *
     * @param lengthText The length input text.
     * @return The validated length as an integer.
     * @throws IllegalArgumentException If the length is invalid.
     */
    private int validateLength(String lengthText) {
        try{
            int length = Integer.parseInt(lengthText);
            if(length <= 0){
                throw new IllegalArgumentException("Length must be a positive integer");
            }
            return length;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid length. Please enter a positive integer");
        }
    }

    /**
     * Validates the speed input.
     *
     * @param speedText The speed input text.
     * @return The validated speed as a long.
     * @throws IllegalArgumentException If the speed is invalid.
     */
    private long validateSpeed(String speedText) {
        try{
            long speed = speedText.isEmpty() ? DEFAULT_SPEED : Long.parseLong(speedText);
            if(speed<= 0){
                throw new IllegalArgumentException("Speed must be a positive integer");
            }
            return speed;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid speed. Please enter a positive integer");
        }
    }

    /**
     * Validates the direction input.
     *
     * @param direction The direction input.
     * @throws IllegalArgumentException If the direction is invalid.
     */
    private void validateDirection(String direction) {
        if(!"Clockwise".equalsIgnoreCase(direction) && !"Anticlockwise".equalsIgnoreCase(direction)){
            throw new IllegalArgumentException("Invalid direction. Please select 'Clockwise' or 'Anticlockwise'");
        }
    }

    /**
     * Finds an available position on the grid to place a new snake.
     *
     * @return An array containing the x and y coordinates of the available position, or an empty array if none is found.
     */
    private int[] findAvailablePosition() {
        try{
            for(int row = 0; row < getNumRows(); row++){
                for(int col = 0; col < getNumCols(); col++){
                    int x = col * (SQUARE_SIZE + SPACING) + SPACING;
                    int y = row * (SQUARE_SIZE + SPACING) + SPACING;
                    Point p = new Point(x, y);
                    if(squareMap.get(p) == null){
                        Rectangle2D square = new Rectangle2D.Double(x, y, SQUARE_SIZE, SQUARE_SIZE);
                        squareMap.put(p, square);
                        return new int[]{x,y};
                    }

                }
            }
        } catch(Exception ex){
            LOGGER.log(Level.SEVERE,"Error finding available psoition", ex);
        }
        return new int[] {};
    }

    /**
     * Calculates the number of rows in the panel based on its height.
     *
     * @return The number of rows.
     */
    private int getNumRows(){
        return (getHeight() -SPACING) / (SQUARE_SIZE + SPACING);
    }

    /**
     * Calculates the number of columns in the panel based on its width.
     *
     * @return The number of columns.
     */
    private int getNumCols(){
        return (getWidth() -SPACING) / (SQUARE_SIZE + SPACING);
    }


    /**
     * Cancels all scheduled tasks when the panel is removed from the display.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        cancelAllTasks();
    }

    /**
     * Cancels all scheduled tasks associated with the snakes.
     */
    private void cancelAllTasks() {
        for (ScheduledFuture<?> task : snakeTasks) {
            task.cancel(true);
        }
        snakeTasks.clear();
    }

    /**
     * Creates a GamePanel instance using the provided SnakeFactory.
     *
     * @param snakeFactory The factory for creating snakes.
     * @return A new GamePanel instance.
     */
    public static  GamePanel createWithFactory(SnakeFactory snakeFactory){
        return new Builder().withSnakeFactory(snakeFactory).build();
    }

    /**
     * Builder class for creating GamePanel instances with customized configuration.
     */
    public static class Builder{
        public SnakeFactory snakeFactory;

        /**
         * Sets the SnakeFactory for the Builder.
         *
         * @param snakeFactory The factory for creating snakes.
         * @return The current Builder instance.
         */
        public Builder withSnakeFactory(SnakeFactory snakeFactory){
            this.snakeFactory = snakeFactory;
            return this;
        }

        /**
         * Builds a GamePanel instance.
         *
         * @return A new GamePanel instance.
         * @throws IllegalStateException If the SnakeFactory is not set.
         */
        public GamePanel build(){
            if(snakeFactory == null){
                throw new IllegalStateException("SnakeFactory must be set");
            }
            return new GamePanel(this);
        }
    }
}
