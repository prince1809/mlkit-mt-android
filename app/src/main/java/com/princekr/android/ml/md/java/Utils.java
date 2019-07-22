package com.princekr.android.ml.md.java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

public class Utils {


    static final int REQUEST_CODE_PHOTO_LIBRARY = 1;

    private static final String TAG = "Utils";


    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    static void openImagePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_LIBRARY);
    }
}
