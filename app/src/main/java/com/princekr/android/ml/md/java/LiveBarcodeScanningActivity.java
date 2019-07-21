package com.princekr.android.ml.md.java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.camera.CameraSourcePreview;
import com.princekr.android.ml.md.java.camera.WorkflowModel;
import com.princekr.android.ml.md.java.camera.WorkflowModel.WorkflowState;

import java.util.Objects;

public class LiveBarcodeScanningActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "LiveBarcodeScanningActi";

    private CameraSourcePreview preview;
    private WorkflowModel workflowModel;
    private WorkflowState currentWorkflowState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_barcode);
        preview = findViewById(R.id.camera_preview);

        setupWorkflowMode();
    }


    @Override
    protected void onResume() {
        super.onResume();

        workflowModel.markCameraFrozen();
    }

    @Override
    public void onClick(View v) {

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
