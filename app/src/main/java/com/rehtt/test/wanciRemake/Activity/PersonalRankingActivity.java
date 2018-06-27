package com.rehtt.test.wanciRemake.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rehtt.test.wanciRemake.DialogActivity.LoadDialog;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class PersonalRankingActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_ranking);
        listView = (ListView) findViewById(R.id.PersonalRankingList);
        new SetFullScreen(this);
//        showLocalK();
        getAllWordRank();
    }

    //显示加载框
    private void showLocalK() {
        //显示加载框
        LoadDialog loadDialog = new LoadDialog(this);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.show();
    }

    //关闭加载框
    public void closeLocalK() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("LoadDone");
        localBroadcastManager.sendBroadcast(intent);
    }

    List<HashMap<String, String>> listt;

    private void getAllWordRank() {
        OkhttpNet.doPost(getString(R.string.url_getAllWordRank), "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonTo(response.body().string());
                handler.sendEmptyMessage(1);
            }
        });
    }

    byte[] bytes;

    private void jsonTo(String json) {
        listt = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement personalRanking : jsonArray) {
            Gson gson = new Gson();
            Json json1 = gson.fromJson(personalRanking, Json.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(json1.getId()));
            map.put("userName", json1.getUserName());
            map.put("wordNum", json1.getAllWords());
            map.put("p", "http://test.rehtt.com/test2.jpg");
            listt.add(map);

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                MyAdapter myAdapter = new MyAdapter(PersonalRankingActivity.this);
                listView.setAdapter(myAdapter);
//                closeLocalK();
            }

        }
    };

    class Json {
        /**
         * id : 1
         * userName : asd
         * passWord : asd
         * phone : asd
         * allWords : 11
         * errorWords : 2&3
         * allTime : 7
         * failTime : 5
         * gameTime : 666
         */

        private int id;
        private String userName;
        private String passWord;
        private String phone;
        private String allWords;
        private String errorWords;
        private int allTime;
        private int failTime;
        private String gameTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAllWords() {
            return allWords;
        }

        public void setAllWords(String allWords) {
            this.allWords = allWords;
        }

        public String getErrorWords() {
            return errorWords;
        }

        public void setErrorWords(String errorWords) {
            this.errorWords = errorWords;
        }

        public int getAllTime() {
            return allTime;
        }

        public void setAllTime(int allTime) {
            this.allTime = allTime;
        }

        public int getFailTime() {
            return failTime;
        }

        public void setFailTime(int failTime) {
            this.failTime = failTime;
        }

        public String getGameTime() {
            return gameTime;
        }

        public void setGameTime(String gameTime) {
            this.gameTime = gameTime;
        }
    }

    class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public MyAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listt.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.list_layout, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView word = (TextView) convertView.findViewById(R.id.wordNum);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
            ImageView ico = (ImageView) convertView.findViewById(R.id.ico);

            //显示排名
            if (position == 0)
                Glide.with(convertView).load(getDrawable(R.drawable.ic_one)).into(ico);
            else if (position == 1)
                Glide.with(convertView).load(getDrawable(R.drawable.ic_two)).into(ico);
            else if (position == 2)
                Glide.with(convertView).load(getDrawable(R.drawable.ic_three)).into(ico);
            else
                Glide.with(convertView).load(fontImage(String.valueOf(position + 1))).into(ico);

            //显示头像
            Glide.with(convertView)
                    .load(listt.get(position).get("p"))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
            //显示名字
            if (listt.get(position).get("userName") != null) {
                name.setText(listt.get(position).get("userName").toString());
            } else {
                name.setText("NULL");
            }
            //显示单词数量
            word.setText("总量：" + listt.get(position).get("wordNum"));
            return convertView;
        }
    }

    //文字转图片
    private Bitmap fontImage(String i) {
        float scale = getResources().getDisplayMetrics().scaledDensity;

        TextView tv = new TextView(PersonalRankingActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(i);
        tv.setTextSize(scale * 12);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setDrawingCacheEnabled(true);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());


        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }


}
