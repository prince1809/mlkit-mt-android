package com.princekr.android.ml.md.java;

import android.animation.AnimatorSet;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.princekr.android.ml.md.R;
import com.princekr.android.ml.md.java.camera.CameraSource;
import com.princekr.android.ml.md.java.camera.CameraSourcePreview;
import com.princekr.android.ml.md.java.camera.GraphicOverlay;
import com.princekr.android.ml.md.java.camera.WorkflowModel;
import com.princekr.android.ml.md.java.productsearch.BottomSheetScrimView;
import com.princekr.android.ml.md.java.productsearch.SearchEngine;

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
    private WorkflowModel.WorkflowState workflowState;
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
    }

    @Override
    public void onClick(View v) {

    }
}
