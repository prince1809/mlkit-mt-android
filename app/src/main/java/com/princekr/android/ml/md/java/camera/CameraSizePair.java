package com.princekr.android.ml.md.java.camera;

import android.hardware.Camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.images.Size;


/**
 * Stores a preview size and get a corresponding same-aspect-ratio picture size. To avoid distorted
 * preview images on some devices, the picture size must be set to a size that is the same aspect
 * ration as the preview size or the preview may end up being distorted. If the picture size is null,
 * then there is no picture size.
 */
public class CameraSizePair {
    public final Size preview;
    @Nullable
    public final Size picture;

    public CameraSizePair(@NonNull Camera.Size previewSize, @Nullable Camera.Size pictureSize) {
        this.preview = new Size(previewSize.width, previewSize.height);
        this.picture = pictureSize != null ? new Size(pictureSize.width, pictureSize.height) : null;
    }

    public CameraSizePair(Size preview, @Nullable Size picture) {
        this.preview = preview;
        this.picture = picture;
    }
}
