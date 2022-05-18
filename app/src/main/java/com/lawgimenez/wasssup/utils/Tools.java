package com.lawgimenez.wasssup.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Lawrence Gimenez on 02/05/2017.
 * Copyright wasssup
 */

public class Tools {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
