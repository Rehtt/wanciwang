package com.rehtt.test.wanciRemake.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.rehtt.test.wanciRemake.DialogActivity.LoadDialog;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

import java.util.ArrayList;

public class MyActivity extends AppCompatActivity {

    ImageView myInfo;
    ImageView personalRanking;
    ImageView wrongWord;
    ImageView vocabulary;
    ImageView more;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        new SetFullScreen(MyActivity.this);
        init();

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
    }

    public void Vocabulary(View view) {
        showAn("Vocabulary");
        showLocalK();
        new Vocabulary().getAll(getString(R.string.url_getAll), new Vocabulary.CallBack() {
            @Override
            public void putList(ArrayList list) {
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
    }

    public void WrongWord(View view) {
        showAn("WrongWord");
        showLocalK();
        new WrongWord().getError(getString(R.string.url_wrongwrod), new WrongWord.CallBack() {
            @Override
            public void putList(ArrayList arrayList) {

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
                default:
                    break;

            }
            closeLocalK();
        }
    };

    public void MyInfo(View view) {
        showAn("MyInfo");
    }
}
