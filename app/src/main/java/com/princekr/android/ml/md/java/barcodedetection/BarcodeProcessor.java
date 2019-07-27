package com.princekr.android.ml.md.java.barcodedetection;

import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.MainThread;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.princekr.android.ml.md.java.camera.FrameProcessorBase;
import com.princekr.android.ml.md.java.camera.GraphicOverlay;
import com.princekr.android.ml.md.java.camera.WorkflowModel;

import java.io.IOException;
import java.util.List;

/**
 * A processor to run the barcode detector.
 */
public class BarcodeProcessor extends FrameProcessorBase<List<FirebaseVisionBarcode>> {

    private static final String TAG = "BarcodeProcessor";


    private final FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    private final WorkflowModel workflowModel;
    //private final CameraRet


    public BarcodeProcessor(GraphicOverlay graphicOverlay, WorkflowModel workflowModel) {
        this.workflowModel = workflowModel;
    }


    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @MainThread
    @Override
    protected void onSuccess(FirebaseVisionImage image, List<FirebaseVisionBarcode> results, GraphicOverlay graphicOverlay) {
        Log.d(TAG, "onSuccess: started");
        if (!workflowModel.isCameraLive()) {
            return;
        }

        Log.d(TAG, "Barcode result size: " + results.size());

        // Picks the barcode, if exists, that covers the center of graphic overlay.
        FirebaseVisionBarcode barcodeInCenter = null;
        for (FirebaseVisionBarcode barcode : results) {
            RectF box = graphicOverlay.translateRect(barcode.getBoundingBox());
            if (box.contains(graphicOverlay.getWidth() / 2f, graphicOverlay.getHeight() / 2f)) {
                barcodeInCenter = barcode;
                break;
            }
        }

        graphicOverlay.clear();
        if (barcodeInCenter == null) {

        } else {

        }
        graphicOverlay.invalidate();
    }

    @Override
    protected void onFailure(Exception e) {
        Log.e(TAG, "Barcode detection failed", e);
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close barcode detector!", e);
        }
    }
}
