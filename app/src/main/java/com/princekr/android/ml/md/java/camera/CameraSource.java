package com.princekr.android.ml.md.java.camera;

import android.graphics.Camera;
import android.hardware.Camera.CameraInfo;

import com.google.android.gms.common.images.Size;

public class CameraSource {

    public static final int CAMERA_FACING_BACK = CameraInfo.CAMERA_FACING_BACK;

    private static final String TAG = "CameraSource";

    private Camera camera;

    private Size previewSize;

    /**
     * Dedicated thread and associated runnable for calling into the detector with frames, as the
     * frames become available from the camera.
     */
    private Thread processingThread;


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

        @Override
        public void run() {

        }
    }
}
