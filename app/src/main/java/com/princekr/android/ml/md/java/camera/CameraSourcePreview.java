package com.princekr.android.ml.md.java.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.princekr.android.ml.md.R;

import java.io.IOException;

/**
 * Preview the camera image in the screen.
 */
public class CameraSourcePreview extends FrameLayout {

    private static final String TAG = "CameraSourcePreview";

    private final SurfaceView surfaceView;
    private GraphicOverlay graphicOverlay;
    private boolean startRequested = false;
    private boolean surfaceAvailable = false;
    private CameraSource cameraSource;
    private Size cameraPreviewSize;

    public CameraSourcePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        surfaceView = new SurfaceView(context);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        addView(surfaceView);
    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "onFinishInflate: started");
        super.onFinishInflate();
        graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
    }

    public void start(CameraSource cameraSource) throws IOException {
        this.cameraSource = cameraSource;
        startRequested = true;
        startIfReady();
    }

    private void startIfReady() throws IOException {
        if (startRequested && surfaceAvailable) {
            cameraSource.start(surfaceView.getHolder());
            requestLayout();

            if (graphicOverlay != null) {
                graphicOverlay.setCameraInfo(cameraSource);
                graphicOverlay.clear();
            }
            startRequested = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: started");
        int layoutWidth = right - left;
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated: started");
            surfaceAvailable = true;
            try {
                startIfReady();
            } catch (IOException e) {
                Log.e(TAG, "could not start camera source.", e);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            surfaceAvailable = false;
        }
    }
}
