package com.princekr.android.ml.md.java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.princekr.android.ml.md.R;

public class LiveBarcodeScanningActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LiveBarcodeScanningActi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_barcode);
        //preview = findViewById(R.id.camera_preview);

    }

    @Override
    public void onClick(View v) {

    }
}
