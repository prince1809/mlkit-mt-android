package com.princekr.android.ml.md.java.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

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
     * Base class for custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instance to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */
    public abstract static class Graphic {
        protected GraphicOverlay overlay;
        protected Context context;

        public Graphic(GraphicOverlay overlay, Context context) {
            this.overlay = overlay;
            this.context = context;
        }


        protected abstract void draw(Canvas canvas);

    }

    public GraphicOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
