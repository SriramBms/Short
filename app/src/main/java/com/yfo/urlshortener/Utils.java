package com.yfo.urlshortener;

import android.util.Log;

/**
 * Created by Sriram on 7/21/2016.
 */
public class Utils {

    public static boolean IS_DEBUG = true;

    public static void printLog(String tag, String msg){
        if(IS_DEBUG)
        Log.v(tag, msg);
    }

    //TODO: JSON utils for file read/write

}
