package com.rehtt.test.wanciRemake.Activity;

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

public class WrongWord {


    private ArrayList list = new ArrayList();
    private ArrayList id=new ArrayList();

    public void getError(String url, final CallBack listt) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", new Data().getUser());
        OkhttpNet.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonToList(response.body().string());
                listt.putList(list,id);
            }
        });
    }

    private void jsonToList(String str) {
        try {
            Gson gson = new Gson();
            WrongWordJ wrongWord = gson.fromJson(str, WrongWordJ.class);
            List<WrongWordJ.DataBean> lists = wrongWord.getData();

            for (WrongWordJ.DataBean dataBean : lists) {
                list.add(dataBean.getEnglish() + "\n" + dataBean.getChinese());
                id.add(dataBean.getId());
            }

        } catch (Exception e) {

        }

    }


    public interface CallBack {
        void putList(ArrayList arrayList,ArrayList id);
    }


    private class WrongWordJ {

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
