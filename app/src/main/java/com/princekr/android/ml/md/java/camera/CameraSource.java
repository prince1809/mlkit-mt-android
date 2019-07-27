package com.princekr.android.ml.md.java.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.android.gms.common.images.Size;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.princekr.android.ml.md.java.Utils;
import com.princekr.android.ml.md.java.settings.PreferenceUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class CameraSource {

    public static final int CAMERA_FACING_BACK = CameraInfo.CAMERA_FACING_BACK;

    private static final String TAG = "CameraSource";

    private static final int IMAGE_FORMAT = ImageFormat.NV21;
    private static final int MIN_CAMERA_PREVIEW_WIDTH = 400;
    private static final int MAX_CAMERA_PREVIEW_WIDTH = 1300;
    private static final int DEFAULT_REQUESTED_CAMERA_PREVIEW_WIDTH = 640;
    private static final int DEFAULT_REQUESTED_CAMERA_PREVIEW_HEIGHT = 360;
    private static final float REQUESTED_CAMERA_FPS = 30.0f;


    private Camera camera;
    @FirebaseVisionImageMetadata.Rotation
    private int rotation;

    private Size previewSize;

    /**
     * Dedicated thread and associated runnable for calling into the detector with frames, as the
     * frames become available from the camera.
     */
    private Thread processingThread;
    private final FrameProcessingRunnable processingRunnable = new FrameProcessingRunnable();


    private final Object processorLock = new Object();
    private FrameProcessor frameProcessor;

    /**
     * Map to convert between a byte array, received from the camera, and its associated byte buffer.
     * We use byte buffers internally because this is a  more efficient way to call into native code
     * later (avoids a potential copy).
     *
     * <p><b>Note:</b> uses IdentityHashMap here instead of HashMap because the behavior of an array's
     * equals, hashCode and toString method is both useless and unexpected. IdentityHashMap enforces
     * identity ('==') check on the keys.
     */
    private final Map<byte[], ByteBuffer> bytesToByteBuffer = new IdentityHashMap<>();

    private final Context context;
    private final GraphicOverlay graphicOverlay;

    public CameraSource(GraphicOverlay graphicOverlay) {
        this.context = graphicOverlay.getContext();
        this.graphicOverlay = graphicOverlay;
    }

    /**
     * Opens the camera and start sending preview frames to the underlying detector. The supplied
     * surface holder is used for the preview frames can be displayed to the user.
     *
     * @param surfaceHolder the surface holder to use for the preview frames.
     * @throws IOException if the supplied surface holder could not be used as the preview display.
     */
    synchronized void start(SurfaceHolder surfaceHolder) throws IOException {
        if (camera != null) {
            return;
        }

        camera = createCamera();
        camera.setPreviewDisplay(surfaceHolder);
        camera.startPreview();

        processingThread = new Thread(processingRunnable);
        processingRunnable.setActive(true);
        processingThread.start();
    }


    synchronized void stop() {

    }

    /**
     * Stops the camera and releases the resources of the camera and underlying detector.
     */
    public void release() {
        graphicOverlay.clear();
        synchronized (processorLock) {
            stop();
            if (frameProcessor != null) {
                frameProcessor.stop();
            }
        }
    }


    /**
     * Return the preview size that is currently used by the underlying camera.
     *
     * @return
     */
    Size getPreviewSize() {
        return previewSize;
    }

    /**
     * Opens the camera and applies the user settings
     *
     * @return
     * @throws IOException
     */
    private Camera createCamera() throws IOException {
        Camera camera = Camera.open();
        if (camera == null) {
            throw new IOException("There is no back-facing camera");
        }

        Camera.Parameters parameters = camera.getParameters();
        setPreviewAndPictureSize(camera, parameters);


        camera.setParameters(parameters);

        //camera.setPreviewCallbackWithBuffer(processingRunnable::setNextFrame);
        return camera;
    }

    private void setPreviewAndPictureSize(Camera camera, Camera.Parameters parameters) throws IOException {
        // Gives priority to the preview size specified by the user if exists.
        CameraSizePair sizePair = PreferenceUtils.getUserSpecifiedPreviewSize(context);
        if (sizePair == null) {
            // camera preview size is based on the landscape mode, so we need to also used the aspect
            // ratio of the display in the same mode for comparision.
            float displayAspectRatioInLandscape;
            if (Utils.isPortraitMode(graphicOverlay.getContext())) {
                displayAspectRatioInLandscape = (float) graphicOverlay.getHeight() / graphicOverlay.getWidth();
            } else {
                displayAspectRatioInLandscape = (float) graphicOverlay.getWidth() / graphicOverlay.getHeight();
            }

            sizePair = selectSizePair(camera, displayAspectRatioInLandscape);
        }

        if (sizePair == null) {
            throw new IOException("Could not find suitable preview size");
        }

        previewSize = sizePair.preview;
        Log.v(TAG, "camera preview size: " + previewSize);
        parameters.setPreviewSize(previewSize.getWidth(), previewSize.getHeight());
        //PreferenceUtils.save

        Size pictureSize = sizePair.picture;
        if (pictureSize != null) {
            Log.i(TAG, "camera picture size: " + pictureSize);
            parameters.setPictureSize(pictureSize.getWidth(), pictureSize.getHeight());
        }
    }


    /**
     * Selects the most suitable preview and picture size, given the display aspect ratio in landscape
     * mode.
     * <p>It's firstly trying to pick the one that has the closest aspect ratio to display with its
     * width be in the specified range [{@link #MIN_CAMERA_PREVIEW_WIDTH}, {@link #MAX_CAMERA_PREVIEW_WIDTH}].
     * If there're multiple candidates, choose the one having longest width.
     *
     * <p>If the above looking up failed, chooses the one that has the minimum sum of the differences
     * between the desired value and the actual values for width and height.
     *
     * <p>Even though we only need to find the preview size, it's necessary to find the preview
     * size and the picture size of the camera together, because these need to have the same aspect
     * ratio. On some hardware, if you would only set the preview size, you will get a distorted
     * image.
     *
     * @param camera                        the camera to select a preview size from
     * @param displayAspectRatioInLandscape
     * @return the selected preview size and picture size pair
     */
    private static CameraSizePair selectSizePair(Camera camera, float displayAspectRatioInLandscape) {
        List<CameraSizePair> validPreviewSizes = Utils.generateValidPreviewSizeList(camera);

        CameraSizePair selectedPair = null;
        // Picks the preview size that has closest aspect ratio to display preview.
        float minAspectRatioDiff = Float.MAX_VALUE;
        for (CameraSizePair sizePair : validPreviewSizes) {
            Size previewSize = sizePair.preview;
            if (previewSize.getWidth() < MIN_CAMERA_PREVIEW_WIDTH || previewSize.getWidth() > MIN_CAMERA_PREVIEW_WIDTH) {
                continue;
            }

            float previewAspectRatio = (float) previewSize.getWidth() / previewSize.getHeight();
            float aspectRatioDiff = Math.abs(displayAspectRatioInLandscape - previewAspectRatio);
            if (Math.abs(aspectRatioDiff - minAspectRatioDiff) < Utils.ASPECT_RATION_TOLERANCE) {
                if (selectedPair == null || selectedPair.preview.getWidth() < sizePair.preview.getWidth()) {
                    selectedPair = sizePair;
                }
            } else if (aspectRatioDiff < minAspectRatioDiff) {
                minAspectRatioDiff = aspectRatioDiff;
                selectedPair = sizePair;
            }
        }

        if (selectedPair == null) {
            // Pick the one that has the minimum sum of the differences between the desired values and
            // the actual values for width and height.
            int minDiff = Integer.MAX_VALUE;
            for (CameraSizePair sizePair : validPreviewSizes) {
                Size size = sizePair.preview;
                int diff = Math.abs(size.getWidth() - CameraSource.DEFAULT_REQUESTED_CAMERA_PREVIEW_WIDTH)
                        + Math.abs(size.getHeight() - CameraSource.DEFAULT_REQUESTED_CAMERA_PREVIEW_HEIGHT);
                if (diff < minDiff) {
                    selectedPair = sizePair;
                    minDiff = diff;
                }
            }
        }

        return selectedPair;
    }

    /**
     * This runnable controls access to the underlying receiver, calling it to process when
     * available from the camera. This is designed to run detection on frames as fast as possible
     * (i.e., without unnecessary context switching or waiting on the next frame).
     *
     * <p>While detection is running on a frame, new frame may be received from the camera. As these
     * frames comes in, the most frame is held onto as pending. As soon as detection and its
     * associated processing is done for the previous frame, detection on the mostly recently received
     * frame will immediately start on the same thread.
     */
    private class FrameProcessingRunnable implements Runnable {

        private final Object lock = new Object();
        private boolean active = true;

        private ByteBuffer pendingFrameData;

        FrameProcessingRunnable() {
        }


        /**
         * Marks the runnable as active/not active. Signals any blocked threads to continue.
         *
         * @param active
         */
        void setActive(boolean active) {
            synchronized (lock) {
                this.active = active;
                lock.notifyAll();
            }
        }

        @Override
        public void run() {

        }
    }
}
