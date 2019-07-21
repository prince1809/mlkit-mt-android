package com.princekr.android.ml.md.java;

import android.content.Context;
import android.content.res.Configuration;

public class Utils {


    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }
}
