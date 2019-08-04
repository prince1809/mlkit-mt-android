package com.princekr.android.ml.md.java;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.collect.ImmutableList;
import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.camera.CameraSource;
import com.princekr.android.ml.md.java.camera.CameraSourcePreview;
import com.princekr.android.ml.md.java.camera.GraphicOverlay;
import com.princekr.android.ml.md.java.camera.WorkflowModel;
import com.princekr.android.ml.md.java.productsearch.BottomSheetScrimView;
import com.princekr.android.ml.md.java.productsearch.ProductAdapter;
import com.princekr.android.ml.md.java.productsearch.SearchEngine;
import com.princekr.android.ml.md.java.settings.PreferenceUtils;

import java.util.Objects;

public class LiveObjectDetectionActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "LiveObjectActivity";

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private View settingsButton;
    private View flashButton;
    private View promptChip;
    private AnimatorSet promptChipAnimator;
    private ExtendedFloatingActionButton searchButton;
    private AnimatorSet searchButtonAnimator;
    private ProgressBar searchProgressBar;
    private WorkflowModel workflowModel;
    private WorkflowModel.WorkflowState currentWorkflowState;
    private SearchEngine searchEngine;

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private BottomSheetScrimView bottomSheetScrimView;
    private RecyclerView productRecyclerView;
    private TextView bottomSheetTitleView;
    private Bitmap objectThumbnailForBottomSheet;
    private boolean slidingSheetUpFromHiddenState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchEngine = new SearchEngine(getApplicationContext());

        setContentView(R.layout.activity_live_object);
        preview = findViewById(R.id.camera_preview);
        graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
        graphicOverlay.setOnClickListener(this);
        cameraSource = new CameraSource(graphicOverlay);

        promptChip = findViewById(R.id.bottom_prompt_chip);
        promptChipAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(promptChip);

        searchButton = findViewById(R.id.product_search_button);
        searchButton.setOnClickListener(this);
        searchButtonAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.search_button_enter);
        searchButtonAnimator.setTarget(searchButton);

        searchProgressBar = findViewById(R.id.search_progress_bar);

        setUpBottomSheet();

        findViewById(R.id.close_button).setOnClickListener(this);
        flashButton = findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    private void setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        Log.d(TAG, "Bottom sheet new state: " + newState);
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        Log.d(TAG, "onSlide method called");
                    }
                }
        );

        bottomSheetScrimView = findViewById(R.id.bottom_sheet_scrim_view);
        bottomSheetScrimView.setOnClickListener(this);

        bottomSheetTitleView = findViewById(R.id.bottom_sheet_title);
        productRecyclerView = findViewById(R.id.product_recycler_view);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(new ProductAdapter(ImmutableList.of()));
    }

    private void setUpWorkflowModel() {
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
                    Log.d(TAG, "current workflow state: " + currentWorkflowState.name());

                    if (PreferenceUtils.isAutoSearchEnabled(this)) {
                        //state
                    }
                }
        );
    }

    private void stateChangeInAutoSearchMode(WorkflowModel.WorkflowState workflowState) {

    }

    private void stateChangeInManualSearchMode(WorkflowModel workflowState) {

    }
}
