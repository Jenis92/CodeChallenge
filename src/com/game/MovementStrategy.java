package com.game;

import java.awt.geom.Rectangle2D;

/**
 * The MovementStrategy interface defines a strategy for updating the movement direction
 * of a snake within a specified rectangular area.
 */
public interface MovementStrategy {
     /**
      * Updates the direction of the given snake based on its current position and the
      * specified rectangular area.
      *
      * @param snake The snake whose direction needs to be updated. This object will have its
      *              movement direction changed according to the strategy implemented.
      * @param square The rectangular area that constrains or defines the movement of the snake.
      *               This parameter provides context on where the snake is located and where it
      *               can move.
      */
     void updateDirection(Snake snake, Rectangle2D square);
}
