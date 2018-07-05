package com.rehtt.test.wanciRemake;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rehtt.test.wanciRemake.Activity.MyActivity;
import com.rehtt.test.wanciRemake.Activity.PersonalRankingActivity;
import com.rehtt.test.wanciRemake.Activity.PvEActivity;
import com.rehtt.test.wanciRemake.DialogActivity.LoginDialog;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        LoginDialog loginDialog = new LoginDialog(this, new LoginDialog.CallBack() {
            @Override
            public void callBack() {
                getInfo();
            }
        });
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.show();

    }

    //个人信息
    void getInfo() {
        final Data data = new Data();
        Map<String, String> map = new HashMap<>();
        map.put("userName", data.getUser());
        OkhttpNet.doPost(getString(R.string.url_getPersonInf), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                try {
                    Info info = gson.fromJson(response.body().string(), Info.class);
                    //获取头像
                    data.setPic(info.getPic());
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "服务器发生错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //个人信息json解析
    class Info {

        /**
         * pic : http://threr.cn/userPicture/asd.jpg
         * errCount : 1
         * allCount : 1
         * win : 45%
         * allTime : 11
         * time : 666
         * userName : asd
         */

        private String pic;
        private String errCount;
        private String allCount;
        private String win;
        private String allTime;
        private String time;
        private String userName;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getErrCount() {
            return errCount;
        }

        public void setErrCount(String errCount) {
            this.errCount = errCount;
        }

        public String getAllCount() {
            return allCount;
        }

        public void setAllCount(String allCount) {
            this.allCount = allCount;
        }

        public String getWin() {
            return win;
        }

        public void setWin(String win) {
            this.win = win;
        }

        public String getAllTime() {
            return allTime;
        }

        public void setAllTime(String allTime) {
            this.allTime = allTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
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
                    new Data().setPvEGrade(1);
                    startActivity(new Intent().setClass(MainActivity.this, PvEActivity.class));
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
