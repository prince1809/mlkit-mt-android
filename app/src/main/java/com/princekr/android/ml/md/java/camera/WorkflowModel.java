package com.princekr.android.ml.md.java.camera;

import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.princekr.android.ml.md.java.objectdetection.DetectedObject;
import com.princekr.android.ml.md.java.productsearch.Product;
import com.princekr.android.ml.md.java.productsearch.SearchEngine;

import java.util.List;

import javax.annotation.Nullable;

/**
 * View model for handling application workflow based on camera preview.
 */
public class WorkflowModel extends AndroidViewModel implements SearchEngine.SearchResultListener {
    /**
     * State set of the application workflow.
     */
    public enum WorkflowState {
        NOT_STARTED,
        DETECTING,
        DETECTED,
        CONFIRMING,
        CONFIRMED,
        SEARCHING,
        SEARCHED
    }

    public final MutableLiveData<WorkflowState> workflowState = new MutableLiveData<>();

    private boolean isCameraLive = false;
    @Nullable
    private DetectedObject confirmedObject;


    public WorkflowModel(@NonNull Application application) {
        super(application);
    }

    @MainThread
    public void setWorkflowState(WorkflowState workflowState) {
        if (!workflowState.equals(WorkflowState.CONFIRMED)
                && !workflowState.equals(WorkflowState.SEARCHING)
                && !workflowState.equals(WorkflowState.SEARCHED)) {
            confirmedObject = null;
        }
        this.workflowState.setValue(workflowState);
    }

    public void markCameraLive() {
        isCameraLive = true;
    }


    public void markCameraFrozen() {
        isCameraLive = false;
    }

    public boolean isCameraLive() {
        return isCameraLive;
    }

    @Override
    public void onSearchCompleted(DetectedObject object, List<Product> productList) {

    }

}
