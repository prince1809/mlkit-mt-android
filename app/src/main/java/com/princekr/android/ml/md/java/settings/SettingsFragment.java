package com.princekr.android.ml.md.java.settings;

import android.hardware.Camera;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.Utils;
import com.princekr.android.ml.md.java.camera.CameraSizePair;
import com.princekr.android.ml.md.java.camera.CameraSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configure App settings
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setUpRearCameraPreviewSizePreference();
    }

    public void setUpRearCameraPreviewSizePreference() {
        ListPreference previewSizePreference = (ListPreference) findPreference(getString(R.string.pref_key_rear_camera_preview_size));
        if (previewSizePreference == null) {
            return;
        }

        Camera camera = null;
        try {
            camera = Camera.open(CameraSource.CAMERA_FACING_BACK);
            List<CameraSizePair> previewSizeList = Utils.generateValidPreviewSizeList(camera);
            String[] previewSizeStringValues = new String[previewSizeList.size()];
            Map<String, String> previewToPictureSizeMap = new HashMap<>();
            for (int i = 0; i < previewSizeList.size(); i++) {
                CameraSizePair sizePair = previewSizeList.get(i);
                previewSizeStringValues[i] = sizePair.preview.toString();
                if (sizePair.picture != null) {
                    previewToPictureSizeMap.put(
                            sizePair.preview.toString(), sizePair.picture.toString());
                }
            }
            previewSizePreference.setEntries(previewSizeStringValues);
            previewSizePreference.setEntryValues(previewSizeStringValues);
            previewSizePreference.setSummary(previewSizePreference.getEntry());
        } catch (Exception e) {
            // If there is no camera fpr tjee given camera id, hide  the corresponding preference.
            if (previewSizePreference.getParent() != null) {
                previewSizePreference.getParent().removePreference(previewSizePreference);
            }
        } finally {
            if (camera != null) {
                camera.release();
            }
        }

    }
}
