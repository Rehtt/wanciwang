package com.rehtt.test.wanciRemake.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rehtt.test.wanciRemake.DialogActivity.LoadDialog;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyActivity extends AppCompatActivity {

    ImageView myInfo;
    ImageView personalRanking;
    ImageView wrongWord;
    ImageView vocabulary;
    ImageView more;
    ListView listView;

    //选项
    String option = "MyInfo";
    ArrayList Id = new ArrayList();
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        new SetFullScreen(MyActivity.this);
        init();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (option) {
                    case "MyInfo":
                        break;
                    case "More":
                        break;
                    case "Vocabulary":
                        delete(getString(R.string.url_deleteFromAllWord), position, option);
                        break;
                    case "PersonalRanking":
                        break;
                    case "WrongWord":
                        delete(getString(R.string.url_deleteFromErrorWord), position, option);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    private void init() {
        myInfo = (ImageView) findViewById(R.id.MyInfo);
        personalRanking = (ImageView) findViewById(R.id.PersonalRanking);
        wrongWord = (ImageView) findViewById(R.id.WrongWord);
        vocabulary = (ImageView) findViewById(R.id.Vocabulary);
        more = (ImageView) findViewById(R.id.More);
        listView = (ListView) findViewById(R.id.ListView);
        showAn("MyInfo");
    }

    private void delete(String url, int position, final String view) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", new Data().getUser());
        map.put("id", String.valueOf(Id.get(position)));
        OkhttpNet.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("show", "delete");
                Log.e("qwe", res);
                try {
                    Gson gson = new Gson();
                    ress js = gson.fromJson(res, ress.class);
                    if (js.getData().equals("success")) {
                        bundle.putString("view", view);
                        message.what = 1;
                    } else
                        message.what = 0;
                } catch (Exception e) {
                    message.what = 0;
                }
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

    }

    class ress {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }


    //显示、隐藏按钮
    private void showAn(String i) {
        switch (i) {
            case "MyInfo":
                myInfo.setVisibility(View.VISIBLE);
                personalRanking.setVisibility(View.GONE);
                wrongWord.setVisibility(View.GONE);
                vocabulary.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case "WrongWord":
                myInfo.setVisibility(View.GONE);
                personalRanking.setVisibility(View.GONE);
                wrongWord.setVisibility(View.VISIBLE);
                vocabulary.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case "PersonalRanking":
                myInfo.setVisibility(View.GONE);
                personalRanking.setVisibility(View.VISIBLE);
                wrongWord.setVisibility(View.GONE);
                vocabulary.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case "Vocabulary":
                myInfo.setVisibility(View.GONE);
                personalRanking.setVisibility(View.GONE);
                wrongWord.setVisibility(View.GONE);
                vocabulary.setVisibility(View.VISIBLE);
                more.setVisibility(View.GONE);
                break;
            case "More":
                myInfo.setVisibility(View.GONE);
                personalRanking.setVisibility(View.GONE);
                wrongWord.setVisibility(View.GONE);
                vocabulary.setVisibility(View.GONE);
                more.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    //显示加载框
    private void showLocalK() {
        //显示加载框
        LoadDialog loadDialog = new LoadDialog(MyActivity.this);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.show();
    }

    //关闭加载框
    public void closeLocalK() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(MyActivity.this);
        Intent intent = new Intent("LoadDone");
        localBroadcastManager.sendBroadcast(intent);
    }


    public void More(View view) {
        showAn("More");
        option = "More";
        this.view = view;
    }

    public void Vocabulary(View view) {
        showAn("Vocabulary");
        option = "Vocabulary";
        showLocalK();
        this.view = view;

        new Vocabulary().getAll(getString(R.string.url_getAll), new Vocabulary.CallBack() {
            @Override
            public void putList(ArrayList list, ArrayList id) {
                Id = id;
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("show", "Vocabulary");
                bundle.putStringArrayList("Vocabulary", list);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

    }


    public void PersonalRanking(View view) {
        showAn("PersonalRanking");
        option = "PersonalRanking";
        this.view = view;


    }

    public void WrongWord(View view) {
        showAn("WrongWord");
        option = "WrongWord";
        showLocalK();
        this.view = view;
        new WrongWord().getError(getString(R.string.url_wrongwrod), new WrongWord.CallBack() {
            @Override
            public void putList(ArrayList arrayList, ArrayList id) {
                Id = id;
                Bundle bundle = new Bundle();
                Message message = new Message();
                bundle.putString("show", "WrongWord");
                bundle.putStringArrayList("WrongWord", arrayList);
                message.setData(bundle);
                handler.sendMessage(message);

            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.getData().getString("show")) {
                case "WrongWord":
                    ArrayAdapter WrongWord = new ArrayAdapter(MyActivity.this, android.R.layout.simple_list_item_1, msg.getData().getStringArrayList("WrongWord"));
                    listView.setAdapter(WrongWord);
                    break;
                case "Vocabulary":
                    ArrayAdapter Vocabulary = new ArrayAdapter(MyActivity.this, android.R.layout.simple_list_item_1, msg.getData().getStringArrayList("Vocabulary"));
                    listView.setAdapter(Vocabulary);
                    break;
                case "delete":
                    if (msg.what == 1) {
                        Toast.makeText(MyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        switch (msg.getData().getString("view")) {
                            case "Vocabulary":
                                Vocabulary(view);
                                break;
                            case "WrongWord":
                                WrongWord(view);
                                break;
                            default:
                                break;
                        }
                    } else
                        Toast.makeText(MyActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }

            closeLocalK();
        }
    };

    public void MyInfo(View view) {
        showAn("MyInfo");
        option = "MyInfo";
        this.view = view;
    }
}
