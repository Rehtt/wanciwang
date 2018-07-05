package com.rehtt.test.wanciRemake.DialogActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginDialog extends Dialog {

    private EditText user;
    private EditText password;
    private ProgressBar progressBar;

    String userName = "";
    CallBack callBack;

    public LoginDialog(@NonNull Context context, CallBack callBack) {
        super(context);
        setContentView(R.layout.login_dialog);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        this.callBack = callBack;

    }

    public interface CallBack {
        void callBack();
    }

    //找回密码按钮响应事件
    private void zhaohui() {
        Toast.makeText(getContext(), "功能未开放", Toast.LENGTH_LONG).show();
    }

    //注册按钮响应事件
    private void register() {
        com.rehtt.test.wanciRemake.DialogActivity.register register = new register(getContext());
        register.setCanceledOnTouchOutside(false);
        register.show();
    }

    //登陆按钮响应事件
    private void login() {
        userName = user.getText().toString();
        String passwd = password.getText().toString();
        if (!userName.equals("") && !passwd.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("userName", userName);
            map.put("passWord", passwd);
            OkhttpNet.doPost(getContext().getString(R.string.url_login), map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    json(response.body().string());

                }
            });
        } else {
            Toast.makeText(getContext(), "请输入用户名或密码", Toast.LENGTH_LONG).show();

        }
    }

    /**
     * json解析
     *
     * @param respone respone.body中的内容
     */
    private void json(String respone) {
        Message msg = handler.obtainMessage();
        Gson gson = new Gson();
        try {
            JSON j = gson.fromJson(respone, JSON.class);
            if (j.getData().equals("200")) {
                msg.what = 200;
                new Data().setUser(userName);
                //回调
                callBack.callBack();
                this.dismiss();

            } else {
                msg.what = 400;

            }
        } catch (Exception e) {
            msg.what = 0;

        }
        handler.sendMessage(msg);

    }

    //handle更新ui
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_LONG).show();
                    break;
                case 400:
                    Toast.makeText(getContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(getContext(), getContext().getString(R.string.error_0), Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
            progressBar.setVisibility(View.GONE);

        }
    };

    /**
     * 屏幕区域点击进行响应
     */

    Dip dip = new Dip();


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        dip.setContext(getContext());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            int are = 0;
            //登陆
            if (x > dip.px(24) && x < dip.px(296) && y > dip.px(200) && y < dip.px(228)) {
                login();
            }
            //找回密码
            else if (x > dip.px(45) && x < dip.px(102) && y > dip.px(248) && y < dip.px(269)) {
                zhaohui();
            }
            //注册
            else if (x > dip.px(223) && x < dip.px(285) && y > dip.px(248) && y < dip.px(269)) {
                register();
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
            Toast.makeText(getContext(), "再按一次退出", Toast.LENGTH_LONG).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }

    class JSON {

        /**
         * data : 200
         * msg  : 登陆成功
         */

        private String data;
        private String msg;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

