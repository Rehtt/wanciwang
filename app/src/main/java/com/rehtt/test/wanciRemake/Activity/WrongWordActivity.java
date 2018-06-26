package com.rehtt.test.wanciRemake.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rehtt.test.wanciRemake.DialogActivity.LoadDialog;
import com.rehtt.test.wanciRemake.DialogActivity.LoginDialog;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WrongWordActivity extends AppCompatActivity {

    ListView wrongWordListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_word);
        wrongWordListView = (ListView) findViewById(R.id.WrongWordListView);
        getError();
        //显示加载框
        LoadDialog loadDialog=new LoadDialog(WrongWordActivity.this);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.show();


    }


    private void getError() {
        Map<String, String> map = new HashMap<>();
        map.put("userName", new Data().getUser());
        OkhttpNet.doPost(getString(R.string.url_wrongwrod), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonToList(response.body().string());
            }
        });
    }

    private void jsonToList(String str) {
        Bundle bundle = new Bundle();
        Message msg = new Message();
        try {
            Gson gson = new Gson();
            WrongWord wrongWord = gson.fromJson(str, WrongWord.class);
            List<WrongWord.DataBean> lists = wrongWord.getData();
            ArrayList list = new ArrayList();
            for (WrongWord.DataBean dataBean : lists) {
                list.add(dataBean.getEnglish() + "\n" + dataBean.getChinese());
            }
            bundle.putString("show", "list");
            bundle.putStringArrayList("list", list);
        } catch (Exception e) {
            bundle.putString("show", "error");
        }
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.getData().getString("show")) {
                case "error":
                    Toast.makeText(WrongWordActivity.this, "服务器发生错误", Toast.LENGTH_SHORT).show();
                    break;
                case "list":
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            WrongWordActivity.this,
                            android.R.layout.simple_list_item_1,
                            msg.getData().getStringArrayList("list"));
                    wrongWordListView.setAdapter(adapter);
                    loadDone();
                    break;
                default:
                    break;
            }

        }
    };


    //加载完成
    public void loadDone() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(WrongWordActivity.this);
        Intent intent = new Intent("LoadDone");
        localBroadcastManager.sendBroadcast(intent);
    }

    private class WrongWord {

        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public class DataBean {
            /**
             * id : 2
             * english : abide
             * chinese : vt.遵守 vt.忍受
             */

            private int id;
            private String english;
            private String chinese;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getChinese() {
                return chinese;
            }

            public void setChinese(String chinese) {
                this.chinese = chinese;
            }
        }
    }
}
