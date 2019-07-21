package com.princekr.android.ml.md.java.camera;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class WorkflowModel extends AndroidViewModel {

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


    public WorkflowModel(@NonNull Application application) {
        super(application);
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
}
