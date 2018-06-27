package com.rehtt.test.wanciRemake.Activity;

import com.google.gson.Gson;
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

public class Vocabulary {


    public interface CallBack {
        void putList(ArrayList list,ArrayList id);
    }

    private CallBack callBack;

    public ArrayList list = new ArrayList<>();
    private ArrayList id=new ArrayList();

    public void getAll(String url, final CallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", new Data().getUser());
        OkhttpNet.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonTo(response.body().string());
                callBack.putList(list,id);
            }
        });
    }

    public void jsonTo(String json) {
        try {
            Gson gson = new Gson();
            Vocabulary vocabulary = gson.fromJson(json, Vocabulary.class);
            List<DataBean> dataBeanList = vocabulary.getData();
            for (DataBean dataBean : dataBeanList) {
                list.add(dataBean.getEnglish() + "\n" + dataBean.getChinese());
                id.add(dataBean.getId());
            }

        }catch (Exception e){
            list.add(null);
            id.add(null);
        }

    }

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
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
