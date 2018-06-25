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
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class register extends Dialog {

    private EditText user;
    private EditText password;
    private EditText phone;

    private ProgressBar progressBar;


    public register(@NonNull Context context) {
        super(context);
        setContentView(R.layout.register_dialog);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);


    }


    //获取验证
    private void getVerification() {
        Toast.makeText(getContext(), "功能未开放", Toast.LENGTH_LONG).show();
    }

    private void toRegister() {
        String userName = user.getText().toString();
        String passwd = password.getText().toString();
        String phonee = phone.getText().toString();

        if (!userName.equals("") && !passwd.equals("") && !phonee.equals("") && isPhone(phonee)) {
            progressBar.setVisibility(View.VISIBLE);    //显示状态条
            Map<String, String> map = new HashMap<>();
            map.put("name", userName);
            map.put("password", passwd);
            map.put("phone", phonee);
            OkhttpNet.doPost(getContext().getString(R.string.url_register), map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    json(response.body().string());
                }
            });
        } else if (!isPhone(phonee)) {
            Toast.makeText(getContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "用户名/密码/手机号不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    //解析json
    private void json(String respone) {
        Gson gson = new Gson();
        Message msg = handler.obtainMessage();
        try {
            JSON registerJson = gson.fromJson(respone, JSON.class);
            if (registerJson.getData().equals("400")) {
                msg.what = 400;
            } else if (registerJson.getData().equals("200")) {
                msg.what = 200;
                this.dismiss();
            }
        } catch (Exception e) {
            msg.what = 0;
        }
        handler.sendMessage(msg);
    }

    //显示弹框
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case 400:
                    Toast.makeText(getContext(), "用户名或手机号已存在", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getContext(), getContext().getString(R.string.error_0), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
            progressBar.setVisibility(View.GONE);

        }
    };


    /**
     * 检验是否为正确手机号
     *
     * @return
     */
    private boolean isPhone(String number) {
        String pattern = "0?(13|14|15|17|18|19)[0-9]{9}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(number);
        return m.matches();
    }

    /**
     * 屏幕区域点击进行响应
     */

    Dip dip = new Dip();

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        dip.setContext(getContext());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e("uuuuuuuuuu", String.valueOf(event));
            float x = event.getX();
            float y = event.getY();
            int are = 0;
            //验证码
            if (x >= dip.px(200) && x < dip.px(293) && y > dip.px(218) && y < dip.px(242)) {
                getVerification();
            }
            //注册按钮
            else if (x >= dip.px(38) && x < dip.px(295) && y >= dip.px(260) && y < dip.px(286)) {
                toRegister();
            }
            //返回登陆页面
            else if (x >= dip.px(238) && x < dip.px(295) && y >= dip.px(301) && y < dip.px(313)) {
                this.dismiss();
            }

        }
        return super.onTouchEvent(event);
    }

    class JSON {

        /**
         * data : 200
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }



}
