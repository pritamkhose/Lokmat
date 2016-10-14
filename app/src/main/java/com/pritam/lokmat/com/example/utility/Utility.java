package com.pritam.lokmat.com.example.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Pritam on 10/13/2016.
 */
public class Utility {

    public static boolean isConnected(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo.isConnected();
        }
        catch(Exception e)
        {
            //System.out.println("isConnected:::ERROR::::"+e.getMessage());
            return false;
        }

    }
}
