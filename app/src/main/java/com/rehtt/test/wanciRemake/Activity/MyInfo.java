package com.rehtt.test.wanciRemake.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
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

public class MyInfo {

    interface CallBack {
        void CallBack(ArrayList<String> list, String[] info);
    }


    public void MyInfo(String url, final CallBack callBack) {
        //获取头像
        final ArrayList<String> list = new ArrayList<>();
        final String[] infoNameA = {"头像", "昵称", "错词数量", "词汇数量", "胜率", "总场次", "对局最长游戏时间"};
        Map<String, String> map = new HashMap<>();
        map.put("userName", new Data().getUser());
        OkhttpNet.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    Gson gson = new Gson();
                    ge ge = gson.fromJson(str, MyInfo.ge.class);
                    list.add(ge.getPic());
                    list.add(ge.getUserName());
                    list.add(ge.getErrCount());
                    list.add(ge.getAllCount());
                    list.add(ge.getWin());
                    list.add(ge.getAllTime());
                    list.add(ge.getTime());
                } catch (Exception e) {
                }
                callBack.CallBack(list, infoNameA);
            }
        });


    }


    static class myAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<String> list;
        private String[] str;

        public myAdapter(Context context, ArrayList<String> list, String[] str) {
            layoutInflater = LayoutInflater.from(context);
            this.list = list;
            this.str = str;
        }

        @Override
        public int getCount() {
            return str.length;
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
            convertView = layoutInflater.inflate(R.layout.my_info_layout, null);
            TextView infoName = (TextView) convertView.findViewById(R.id.infoName);
            TextView info = (TextView) convertView.findViewById(R.id.info);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.infoImage);
            if (position == 0) {
                info.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                infoName.setText(str[position]);
                Glide.with(convertView).load(list.get(position)).apply(RequestOptions.bitmapTransform(new CircleCrop()).placeholder(R.drawable.ic_hourglass_empty_black_24dp).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView);

            } else {
                info.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                infoName.setText(str[position]);
                info.setText(list.get(position));
            }
            return convertView;
        }
    }

    //头像地址
    class tou {

        /**
         * data : http://threr.cn/userPicture/asd.jpg
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    //个人信息
    class ge {

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
}
