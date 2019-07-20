package com.princekr.android.ml.md.java.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CameraSourcePreview extends FrameLayout {

    private static final String TAG = "CameraSourcePreview";

    private final SurfaceView surfaceView;

    public CameraSourcePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        surfaceView = new SurfaceView(context);
    }
}
