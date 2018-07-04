package com.rehtt.test.wanciRemake.Tools;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class SetFullScreen {

    /**
     * 设置横屏，并全屏
     * @param context 指定的页面
     */
    public SetFullScreen(AppCompatActivity context) {
//        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //设置横屏
        context.getSupportActionBar().hide();   //隐藏标题栏
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
