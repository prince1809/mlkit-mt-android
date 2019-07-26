package com.princekr.android.ml.md.java;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.chip.Chip;
import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.camera.CameraSource;
import com.princekr.android.ml.md.java.camera.CameraSourcePreview;
import com.princekr.android.ml.md.java.camera.GraphicOverlay;
import com.princekr.android.ml.md.java.camera.WorkflowModel;
import com.princekr.android.ml.md.java.camera.WorkflowModel.WorkflowState;
import com.princekr.android.ml.md.java.settings.SettingsActivity;

import java.util.Objects;

public class LiveBarcodeScanningActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "LiveBarcodeActivity";

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private View settingsButton;
    private View flashButton;
    private Chip promptChip;
    private AnimatorSet promptChipAnimator;
    private WorkflowModel workflowModel;
    private WorkflowState currentWorkflowState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_barcode);
        preview = findViewById(R.id.camera_preview);
        graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
        graphicOverlay.setOnClickListener(this);
        cameraSource = new CameraSource(graphicOverlay);

        promptChip = findViewById(R.id.bottom_prompt_chip);
        promptChipAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(promptChip);

        findViewById(R.id.close_button).setOnClickListener(this);
        flashButton = findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        setupWorkflowMode();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //workflowModel.markCameraFrozen();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: started");
        int id = view.getId();
        if (id == R.id.close_button) {
            onBackPressed();
        } else if (id == R.id.flash_button) {
            if (flashButton.isSelected()) {
                flashButton.setSelected(false);
                //cameraSource.update
            } else {
                flashButton.setSelected(true);
                //cameraSource.upate
            }
        } else if (id == R.id.settings_button) {
            // set as disabled to prevent the user from clicking on it too fast.
            settingsButton.setEnabled(false);
            startActivity(new Intent(this, SettingsActivity.class));
        }
    }

    private void startCameraPreview() {
        if (!workflowModel.isCameraLive()) {

        }
    }

    private void stopCameraPreview() {

    }

    private void setupWorkflowMode() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel.workflowState.observe(
                this,
                workflowState -> {
                    if (workflowState == null || Objects.equals(currentWorkflowState, workflowState)) {
                        return;
                    }

                    currentWorkflowState = workflowState;
                    Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

                    switch (workflowState) {
                        case DETECTING:
                            break;
                        case CONFIRMING:
                            break;
                        case SEARCHING:
                            break;
                        case DETECTED:
                        case SEARCHED:
                            break;
                        default:
                            break;
                    }

                    //boolean
                });
    }
}
