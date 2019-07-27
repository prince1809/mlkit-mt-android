package com.princekr.android.ml.md.java.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.images.Size;
import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.camera.CameraSizePair;

public class PreferenceUtils {

    public static CameraSizePair getUserSpecifiedPreviewSize(Context context) {
        try {
            String previewSizePrefKey = context.getString(R.string.pref_key_rear_camera_preview_size);
            String pictureSizePrefKey = context.getString(R.string.pref_key_rear_camera_picture_size);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return new CameraSizePair(
                    Size.parseSize(sharedPreferences.getString(previewSizePrefKey, null)),
                    Size.parseSize(sharedPreferences.getString(pictureSizePrefKey, null)));
        } catch (Exception e) {
            return null;
        }

    }
}
