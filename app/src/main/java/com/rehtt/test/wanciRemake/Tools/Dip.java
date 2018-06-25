package com.rehtt.test.wanciRemake.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;

public class Dip {
    private Context context;

    public void setContext(Context mcontext){
        this.context=mcontext;
    }

    //对不同手机进行兼容
    //dp to px
    public float px(float dp) {
        float sc = context.getResources().getDisplayMetrics().density;
        return dp * sc + 0.5f;
    }

    //px to dp
    public float dp(float px) {
        float sc = context.getResources().getDisplayMetrics().density;
        return px / sc + 0.5f;
    }
}
