package com.game;

import java.awt.*;

/**
 * The Renderable interface is intended for objects that can be rendered onto a graphical
 * context. Any class implementing this interface should provide its own way of rendering
 * itself using a {@link Graphics2D} object.
 */
public interface Renderable {
    /**
     * Renders the object using the provided {@link Graphics2D} context.
     *
     * @param g The {@link Graphics2D} object used for drawing. This object provides the
     *          necessary methods to render graphics on a component.
     */
    void render(Graphics2D g);
}
