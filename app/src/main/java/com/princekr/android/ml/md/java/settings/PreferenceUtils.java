package com.princekr.android.ml.md.java.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;

import com.google.android.gms.common.images.Size;
import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.camera.CameraSizePair;

/**
 * Utility class to retrieve shared preferences.
 */
public class PreferenceUtils {

    public static boolean isAutoSearchEnabled(Context context) {
        return getBooleanPref(context, R.string.pref_key_enable_auto_search, true);
    }

    public static boolean isMultipleObjectsMode(Context context) {
        return getBooleanPref(context, R.string.pref_key_object_detector_enable_multiple_objects, false);
    }

    public static boolean isClassificationEnabled(Context context) {
        return getBooleanPref(context, R.string.pref_key_object_detector_enable_classification, false);
    }


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


    private static boolean getBooleanPref(Context context, @StringRes int prefKeyId, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(prefKeyId);
        return sharedPreferences.getBoolean(prefKey, defaultValue);
    }
}
