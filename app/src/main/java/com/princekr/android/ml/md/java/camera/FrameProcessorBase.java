package com.princekr.android.ml.md.java.camera;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.GuardedBy;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.nio.ByteBuffer;

/**
 * Abstract base class of {@link FrameProcessor}.
 *
 * @param <T>
 */
public abstract class FrameProcessorBase<T> implements FrameProcessor {

    private static final String TAG = "FrameProcessorBase";

    // To keep the latest frame and its metadata.
    @GuardedBy("this")
    private ByteBuffer latestFrame;

    @GuardedBy("this")
    private FrameMetadata latestFrameMetadata;

    // To keep the frame and metadata in process.
    @GuardedBy("this")
    private ByteBuffer processingFrame;

    @GuardedBy("this")
    private FrameMetadata processingFrameMetadata;

    @Override
    public synchronized void process(
            ByteBuffer data, FrameMetadata frameMetadata, GraphicOverlay graphicOverlay) {
        latestFrame = data;
        latestFrameMetadata = frameMetadata;
        if (processingFrame == null || processingFrameMetadata == null) {
            processLatestFrame(graphicOverlay);
        }
    }

    private synchronized void processLatestFrame(GraphicOverlay graphicOverlay) {
        processingFrame = latestFrame;
        processingFrameMetadata = latestFrameMetadata;
        latestFrame = null;
        latestFrameMetadata = null;
        if (processingFrame != null && processingFrameMetadata != null) {
            FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                    .setWidth(processingFrameMetadata.width)
                    .setHeight(processingFrameMetadata.height)
                    .setRotation(processingFrameMetadata.rotation)
                    .build();
            FirebaseVisionImage image = FirebaseVisionImage.fromByteBuffer(processingFrame, metadata);
            long startMs = SystemClock.elapsedRealtime();
            detectInImage(image)
                    .addOnSuccessListener(
                            results -> {
                                Log.d(TAG, "Latency is: " + (SystemClock.elapsedRealtime() - startMs));
                                FrameProcessorBase.this.onSuccess(image, results, graphicOverlay);
                                processLatestFrame(graphicOverlay);
                            }
                    ).addOnFailureListener(FrameProcessorBase.this::onFailure);

        }
    }


    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    protected abstract void onSuccess(FirebaseVisionImage image, T results, GraphicOverlay graphicOverlay);

    protected abstract void onFailure(Exception e);
}
