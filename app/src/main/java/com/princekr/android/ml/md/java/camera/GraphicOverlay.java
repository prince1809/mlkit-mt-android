package com.princekr.android.ml.md.java.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.common.images.Size;
import com.princekr.android.ml.md.java.Utils;

import java.util.ArrayList;
import java.util.List;

public class GraphicOverlay extends View {
    private final Object lock = new Object();

    private int previewWidth;
    private float widthScaleFactor = 1.0f;
    private int previewHeight;
    private float heightScaleFactor = 1.0f;
    private final List<Graphic> graphics = new ArrayList<>();

    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instances to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */
    public abstract static class Graphic {
        protected final GraphicOverlay overlay;
        protected final Context context;

        protected Graphic(GraphicOverlay overlay) {
            this.overlay = overlay;
            this.context = overlay.getContext();
        }

        /** Draws the graphic on the supplied canvas. */
        protected abstract void draw(Canvas canvas);
    }

    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Removes all graphics from the overlay. */
    public void clear() {
        synchronized (lock) {
            graphics.clear();
        }
        postInvalidate();
    }

    /** Adds a graphic to the overlay. */
    public void add(Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
    }

    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform image
     * coordinates later.
     */
    public void setCameraInfo(CameraSource cameraSource) {
        Size previewSize = cameraSource.getPreviewSize();
        if (Utils.isPortraitMode(getContext())) {
            // Swap width and height when in portrait, since camera's natural orientation is landscape.
            previewWidth = previewSize.getHeight();
            previewHeight = previewSize.getWidth();
        } else {
            previewWidth = previewSize.getWidth();
            previewHeight = previewSize.getHeight();
        }
    }

    public float translateX(float x) {
        return x * widthScaleFactor;
    }

    public float translateY(float y) {
        return y * heightScaleFactor;
    }

    /**
     * Adjusts the {@code rect}'s coordinate from the preview's coordinate system to the view
     * coordinate system.
     */
    public RectF translateRect(Rect rect) {
        return new RectF(
                translateX(rect.left),
                translateY(rect.top),
                translateX(rect.right),
                translateY(rect.bottom));
    }

    /** Draws the overlay with its associated graphic objects. */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (previewWidth > 0 && previewHeight > 0) {
            widthScaleFactor = (float) getWidth() / previewWidth;
            heightScaleFactor = (float) getHeight() / previewHeight;
        }

        synchronized (lock) {
            for (Graphic graphic : graphics) {
                graphic.draw(canvas);
            }
        }
    }
}
