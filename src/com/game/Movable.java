package com.game;

import java.awt.geom.Rectangle2D;

/**
 * The Movable interface defines a contract for objects that have
 * the capability to move within a defined space.
 */
public interface Movable {

     /**
      * Moves the implementing object within the specified rectangle.
      *
      * @param square The rectangle within which the object should move.
      *               This parameter defines the boundaries or the area where
      *               the movement should be restricted or calculated.
      */
     void move(Rectangle2D square);
}
