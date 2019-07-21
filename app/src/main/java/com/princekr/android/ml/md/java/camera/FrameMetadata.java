package com.princekr.android.ml.md.java.camera;

/**
 * Metadata info of a camera frame
 */
public class FrameMetadata {

    final int width;
    final int height;
    final int rotation;

    public FrameMetadata(int width, int height, int rotation) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}
