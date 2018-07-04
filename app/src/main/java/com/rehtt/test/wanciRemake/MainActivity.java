package com.rehtt.test.wanciRemake;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.rehtt.test.wanciRemake.Activity.MyActivity;
import com.rehtt.test.wanciRemake.Activity.PersonalRankingActivity;
import com.rehtt.test.wanciRemake.Activity.PvEActivity;
import com.rehtt.test.wanciRemake.DialogActivity.LoginDialog;
import com.rehtt.test.wanciRemake.DialogActivity.PvEDialog;
import com.rehtt.test.wanciRemake.DialogActivity.register;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    ArrayList<ImageView> imageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置横屏全屏
        initPermission();
        new SetFullScreen(MainActivity.this);
        //显示登陆框
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.show();


    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    Dip dip = new Dip();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dip.setContext(MainActivity.this);
        Log.e("qwe", "eqweqe");

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
//            Log.e("qwe",dip.dp(x)+"||"+dip.dp(y));
            if (x >= dip.px(440) && x < dip.px(555)) {
                if (y >= dip.px(54) && y < dip.px(86)) {
//                    new PvEDialog(MainActivity.this).show();
                    startActivity(new Intent().setClass(MainActivity.this,PvEActivity.class));
                } else if (y >= dip.px(105) && y < dip.px(147)) {
                    new Data().setPvEGrade(0);
                    startActivity(new Intent().setClass(MainActivity.this, PvEActivity.class));
                } else if (y >= dip.px(165) && y < dip.px(201)) {

                } else if (y >= dip.px(221) && y < dip.px(258)) {
                    startActivity(new Intent().setClass(MainActivity.this, PersonalRankingActivity.class));
                } else if (y >= dip.px(273) && y < dip.px(313)) {
                    startActivity(new Intent().setClass(MainActivity.this, MyActivity.class));
                }
            }


        }
        return super.onTouchEvent(event);
    }

    //双击退出程序
    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_LONG).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败\n将会影响程序运行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
