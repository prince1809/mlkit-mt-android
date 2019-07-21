package com.princekr.android.ml.md.java.barcodedetection;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.princekr.android.ml.md.java.camera.GraphicOverlay;
import com.princekr.android.ml.md.java.camera.GraphicOverlay.Graphic;

public class BarcodeGraphicBase extends Graphic {

    private final Paint boxPaint;
    private final Paint scrimPaint;
    private final Paint eraserPain;

    public BarcodeGraphicBase(GraphicOverlay overlay) {
        super(overlay);

        boxPaint = new Paint();

        scrimPaint = new Paint();

        eraserPain = new Paint();
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
    }
}
