/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.gvt;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.EventListener;
import java.util.Map;
import org.apache.batik.ext.awt.image.renderable.ClipRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.event.GraphicsNodeEvent;
import org.apache.batik.gvt.event.GraphicsNodeKeyEvent;
import org.apache.batik.gvt.event.GraphicsNodeKeyListener;
import org.apache.batik.gvt.event.GraphicsNodeMouseEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseListener;
import org.apache.batik.gvt.filter.Mask;
import org.apache.batik.gvt.filter.GraphicsNodeRable;

/**
 * The base class for all graphics nodes. A GraphicsNode encapsulates
 * graphical attributes and can perform atomic operations of a complex
 * rendering.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author <a href="mailto:etissandier@ilog.fr">Emmanuel Tissandier</a>
 * @version $Id$
 */
public interface GraphicsNode {

    /**
     * Indicates that this graphics node can be the target for events when it
     * is visible and when the mouse is over the "painted" area.
     */
    public static final int VISIBLE_PAINTED = 0;

    /**
     * Indicates that this graphics node can be the target for events when it
     * is visible and when the mouse is over the filled area if any.
     */
    public static final int VISIBLE_FILL = 1;

    /**
     * Indicates that this graphics node can be the target for events when it
     * is visible and when the mouse is over the stroked area if any.
     */
    public static final int VISIBLE_STROKE = 2;

    /**
     * Indicates that this graphics node can be the target for events when it
     * is visible and whatever is the filled and stroked area.
     */
    public static final int VISIBLE = 3;

    /**
     * Indicates that this graphics node can be the target for events when the
     * mouse is over the painted area whatever or not it is the visible.
     */
    public static final int PAINTED = 4;

    /**
     * Indicates that this graphics node can be the target for events when the
     * mouse is over the filled area whatever or not it is the visible.
     */
    public static final int FILL = 5;

    /**
     * Indicates that this graphics node can be the target for events when the
     * mouse is over the stroked area whatever or not it is the visible.
     */
    public static final int STROKE = 6;

    /**
     * Indicates that this graphics node can be the target for events if any
     * cases.
     */
    public static final int ALL = 7;

    /**
     * Indicates that this graphics node can not be the target for events.
     */
    public static final int NONE = 8;

    /**
     * The identity affine transform matrix used to draw renderable images.
     */
    public static final AffineTransform IDENTITY = new AffineTransform();

    //
    // Properties methods
    //

    /**
     * Returns the type that describes how this graphics node reacts to events.
     *
     * @return VISIBLE_PAINTED | VISIBLE_FILL | VISIBLE_STROKE | VISIBLE |
     * PAINTED | FILL | STROKE | ALL | NONE
     */
    int getPointerEventType();

    /**
     * Sets the type that describes how this graphics node reacts to events.
     *
     * @param pointerEventType VISIBLE_PAINTED | VISIBLE_FILL | VISIBLE_STROKE |
     * VISIBLE | PAINTED | FILL | STROKE | ALL | NONE
     */
    void setPointerEventType(int pointerEventType);

    /**
     * Sets the transform of this node.
     *
     * @param newTransform the new transform of this node
     */
    void setTransform(AffineTransform newTransform);

    /**
     * Returns the transform of this node or null if any.
     */
    AffineTransform getTransform();

    /**
     * Returns the inverse transform for this node.
     */
    AffineTransform getInverseTransform();

    /**
     * Returns the concatenated transform of this node. i.e., this
     * node's transform preconcatenated with it's parent's transforms.
     */
    AffineTransform getGlobalTransform();

    /**
     * Sets the composite of this node.
     *
     * @param composite the composite of this node
     */
    void setComposite(Composite newComposite);

    /**
     * Returns the composite of this node or null if any.
     */
    Composite getComposite();

    /**
     * Sets if this node is visible or not depending on the specified value.
     *
     * @param isVisible If true this node is visible
     */
    void setVisible(boolean isVisible);

    /**
     * Returns true if this node is visible, false otherwise.
     */
    boolean isVisible();

    /**
     * Sets the clipping filter of this node.
     *
     * @param newClipper the new clipping filter of this node
     */
    void setClip(ClipRable newClipper);

    /**
     * Returns the clipping filter of this node or null if any.
     */
    ClipRable getClip();

    /**
     * Maps the specified key to the specified value in the rendering hints of
     * this node.
     *
     * @param key the key of the hint to be set
     * @param value the value indicating preferences for the specified
     * hint category.
     */
    void setRenderingHint(RenderingHints.Key key, Object value);

    /**
     * Copies all of the mappings from the specified Map to the
     * rendering hints of this node.
     *
     * @param hints the rendering hints to be set
     */
    void setRenderingHints(Map hints);

    /**
     * Sets the rendering hints of this node.
     *
     * @param newHints the new rendering hints of this node
     */
    void setRenderingHints(RenderingHints newHints);

    /**
     * Returns the rendering hints of this node or null if any.
     */
    RenderingHints getRenderingHints();

    /**
     * Sets the mask of this node.
     *
     * @param newMask the new mask of this node
     */
    void setMask(Mask newMask);

    /**
     * Returns the mask of this node or null if any.
     */
    Mask getMask();

    /**
     * Sets the filter of this node.
     *
     * @param newFilter the new filter of this node
     */
    void setFilter(Filter newFilter);

    /**
     * Returns the filter of this node or null if any.
     */
    Filter getFilter();

    /**
     * Returns the GraphicsNodeRable for this node.
     * The GraphicsNodeRable is the Renderable (Filter) before any
     * of the filter operations have been applied.
     */
    GraphicsNodeRable getGraphicsNodeRable();

    //
    // Drawing methods
    //

    /**
     * Paints this node.
     *
     * @param g2d the Graphics2D to use
     */
    void paint(Graphics2D g2d);

    /**
     * Paints this node without applying Filter, Mask, Composite, and clip.
     *
     * @param g2d the Graphics2D to use
     */
    void primitivePaint(Graphics2D g2d);

    //
    // Event support methods
    //

    /**
     * Dispatches the specified event to the interested registered listeners.
     * @param evt the event to dispatch
     */
    void dispatchEvent(GraphicsNodeEvent evt);

    /**
     * Adds the specified graphics node mouse listener to receive graphics node
     * mouse events from this node.
     *
     * @param l the graphics node mouse listener to add
     */
    void addGraphicsNodeMouseListener(GraphicsNodeMouseListener l);

    /**
     * Removes the specified graphics node mouse listener so that it no longer
     * receives graphics node mouse events from this node.
     *
     * @param l the graphics node mouse listener to remove
     */
    void removeGraphicsNodeMouseListener(GraphicsNodeMouseListener l);

    /**
     * Adds the specified graphics node key listener to receive graphics node
     * key events from this node.
     *
     * @param l the graphics node key listener to add
     */
    void addGraphicsNodeKeyListener(GraphicsNodeKeyListener l);

    /**
     * Removes the specified graphics node key listener so that it no longer
     * receives graphics node key events from this node.
     *
     * @param l the graphics node key listener to remove
     */
    void removeGraphicsNodeKeyListener(GraphicsNodeKeyListener l);

    /**
     * Dispatches a graphics node mouse event to this node or one of its child.
     *
     * @param evt the evt to dispatch
     */
    void processMouseEvent(GraphicsNodeMouseEvent evt);

    /**
     * Dispatches a graphics node key event to this node or one of its child.
     *
     * @param evt the evt to dispatch
     */
    void processKeyEvent(GraphicsNodeKeyEvent evt);

    /**
     * Returns an array of listeners that were added to this node and of the
     * specified type.
     *
     * @param listenerType the type of the listeners to return
     */
    EventListener [] getListeners(Class listenerType);

    //
    // Structural methods
    //

    /**
     * Returns the parent of this node or null if any.
     */
    CompositeGraphicsNode getParent();

    /**
     * Returns the root of the GVT tree or null if the node is not part of a GVT
     * tree.
     */
    RootGraphicsNode getRoot();

    //
    // Geometric methods
    //

    /**
     * Returns the bounds of this node in user space. This includes primitive
     * paint, filtering, clipping and masking.
     */
    Rectangle2D getBounds();

    /**
     * Returns the bounds of this node after applying the input transform
     * (if any), concatenated with this node's transform (if any).
     *
     * @param txf the affine transform with which this node's transform should
     *        be concatenated. Should not be null.
     */
    Rectangle2D getTransformedBounds(AffineTransform txf);

    /**
     * Returns the bounds of the area covered by this node's primitive paint.
     */
    Rectangle2D getPrimitiveBounds();

    /**
     * Returns the bounds of this node's primitivePaint after applying the input
     * transform (if any), concatenated with this node's transform (if any).
     *
     * @param txf the affine transform with which this node's transform should
     *        be concatenated. Should not be null.
     */
    Rectangle2D getTransformedPrimitiveBounds(AffineTransform txf);

    /**
     * Returns the bounds of the area covered by this node, without taking any
     * of its rendering attribute into account. i.e., exclusive of any clipping,
     * masking, filtering or stroking, for example.
     */
    Rectangle2D getGeometryBounds();

    /**
     * Returns the bounds of the area covered by this node, without taking any
     * of its rendering attribute into accoun. i.e., exclusive of any clipping,
     * masking, filtering or stroking, for example. The returned value is
     * transformed by the concatenation of the input transform and this node's
     * transform.
     *
     * @param txf the affine transform with which this node's transform should
     *        be concatenated. Should not be null.
     */
    Rectangle2D getTransformedGeometryBounds(AffineTransform txf);

    /**
     * Returns true if the specified Point2D is inside the boundary of this
     * node, false otherwise.
     *
     * @param p the specified Point2D in the user space
     */
    boolean contains(Point2D p);

    /**
     * Returns true if the interior of this node intersects the interior of a
     * specified Rectangle2D, false otherwise.
     *
     * @param r the specified Rectangle2D in the user node space
     */
    boolean intersects(Rectangle2D r);

    /**
     * Returns the GraphicsNode containing point p if this node or one of its
     * children is sensitive to mouse events at p.
     *
     * @param p the specified Point2D in the user space
     */
    GraphicsNode nodeHitAt(Point2D p);

    /**
     * Returns the outline of this node.
     */
    Shape getOutline();
}
