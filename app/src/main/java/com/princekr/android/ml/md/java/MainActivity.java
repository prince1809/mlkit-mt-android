package com.princekr.android.ml.md.java;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.princekr.android.ml.md.R;

public class MainActivity extends AppCompatActivity {

    private enum DetectionMode {
        ODT_LIVE(R.string.mode_odt_live_title, R.string.mode_odt_static_subtitle),
        ODT_STATIC(R.string.mode_odt_static_title, R.string.mode_odt_static_subtitle),
        BARCODE_LIVE(R.string.mode_barcode_live_title, R.string.mode_barcode_live_subtitle);

        private final int titleRestId;
        private final int subtitleResId;

        DetectionMode(int titleRestId, int subtitleResId) {
            this.titleRestId = titleRestId;
            this.subtitleResId = subtitleResId;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        RecyclerView modeRecyclerView = findViewById(R.id.mode_recycler_view);
        modeRecyclerView.setHasFixedSize(true);
        modeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //modeRecyclerView.setAdapter();
    }
}
