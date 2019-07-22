package com.princekr.android.ml.md.java.productsearch;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.princekr.android.ml.md.java.objectdetection.DetectedObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchEngine {

    private static final String TAG = "SearchEngine";

    public interface SearchResultListener {
        void onSearchCompleted(DetectedObject object, List<Product> productList);
    }

    private final RequestQueue searchRequestQueue;
    private final ExecutorService requestCreationExecutor;

    public SearchEngine(Context context) {
        this.searchRequestQueue = Volley.newRequestQueue(context);
        this.requestCreationExecutor = Executors.newSingleThreadExecutor();
    }
}
