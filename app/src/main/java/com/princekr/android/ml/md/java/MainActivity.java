package com.princekr.android.ml.md.java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        modeRecyclerView.setAdapter(new ModeItemAdapter(DetectionMode.values()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ModeItemAdapter extends RecyclerView.Adapter<ModeItemAdapter.ModeItemViewHolder> {

        private final DetectionMode[] detectionModes;

        public ModeItemAdapter(DetectionMode[] detectionModes) {
            this.detectionModes = detectionModes;
        }

        @NonNull
        @Override
        public ModeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ModeItemViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.detection_mode_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ModeItemViewHolder modeItemViewHolder, int position) {
            modeItemViewHolder.bindDetectionMode(detectionModes[position]);
        }

        @Override
        public int getItemCount() {
            return detectionModes.length;
        }


        private class ModeItemViewHolder extends RecyclerView.ViewHolder {

            private final TextView titleView;
            private final TextView subtitleView;

            public ModeItemViewHolder(@NonNull View view) {
                super(view);
                titleView = view.findViewById(R.id.mode_title);
                subtitleView = view.findViewById(R.id.mode_subtitle);
            }

            void bindDetectionMode(DetectionMode detectionMode) {
                titleView.setText(detectionMode.titleRestId);
                subtitleView.setText(detectionMode.subtitleResId);
                itemView.setOnClickListener(
                        view -> {
                            Activity activity = MainActivity.this;
                            switch (detectionMode) {
                                case ODT_LIVE:
                                    activity.startActivity(new Intent(activity, LiveObjectDetectionActivity.class));
                                    break;
                                case ODT_STATIC:
                                    Utils.openImagePicker(activity);
                                    break;
                                case BARCODE_LIVE:
                                    activity.startActivity(new Intent(activity, LiveBarcodeScanningActivity.class));
                                    break;
                            }
                        }
                );
            }
        }

    }
}
