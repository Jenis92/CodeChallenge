package com.game;

import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A task that is scheduled to run periodically to update the state of a {@link Snake}.
 * It handles moving the snake and allowing it to grow according to its behavior.
 * This class implements the {@link Runnable} interface to be used with a scheduled executor service.
 */
public class SnakeTask implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(SnakeTask.class.getName());

    private Movable movable;
    private Growable growable;
    private Rectangle2D square;

    /**
     * Constructs a new {@link SnakeTask} with the specified parameters.
     *
     * @param movable The object responsible for moving the snake.
     * @param growable The object responsible for growing the snake.
     * @param square The area (represented as a {@link Rectangle2D}) within which the snake moves and grows.
     */
    public SnakeTask(Movable movable, Growable growable, Rectangle2D square){
        this.movable = movable;
        this.growable = growable;
        this.square = square;
    }

    /**
     * Executes the task by moving the snake and allowing it to grow.
     * This method is called periodically by a scheduled executor service.
     */
    @Override
    public void run() {
        try{
            movable.move(square);
            growable.grow();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error executing snake task", e);
        }
    }
}
