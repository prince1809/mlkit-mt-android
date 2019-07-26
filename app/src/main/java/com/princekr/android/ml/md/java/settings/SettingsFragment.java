package com.princekr.android.ml.md.java.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.princekr.android.ml.md.R;

/**
 * Configure App settings
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
