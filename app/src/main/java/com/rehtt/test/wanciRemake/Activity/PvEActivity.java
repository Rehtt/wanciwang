package com.rehtt.test.wanciRemake.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
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


public class PvEActivity extends AppCompatActivity {

    Data data = new Data();
    private int Grade = data.getPvEGrade();

    TextView showWord;
    TextView showTime;
    TextView showSper;
    Button button;

    String voiceEnglish = null;
    boolean isEND = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_e);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEND)
                    Voice();
                else
                    Toast.makeText(PvEActivity.this, "游戏已结束", Toast.LENGTH_SHORT).show();
            }
        });
        showWord = (TextView) findViewById(R.id.show);
        showTime = (TextView) findViewById(R.id.time);
        showSper = (TextView) findViewById(R.id.voiceEnglish);
        showTime.setText("10");
        showSper.setText("");

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59e45c55");

        getWord();
    }

    private void getWord() {
        Map<String, String> map = new HashMap<>();
        map.put("grade", String.valueOf(Grade));
        OkhttpNet.doPost(getString(R.string.url_getWord), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str != null) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonArray array = parser.parse(str).getAsJsonArray();
                        List<GetWord> jsons = new ArrayList<>();

                        Gson gson = new Gson();
                        for (JsonElement jsonElement : array) {
                            GetWord json = gson.fromJson(jsonElement, GetWord.class);
                            jsons.add(json);

                        }
                        game(jsons);
                    }catch (Exception e){
                        Log.e("qwerrt",e.getMessage());
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PvEActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void game(final List<GetWord> list) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                List<String> id = new ArrayList<>();
                List<String> state = new ArrayList<>();
                int userFraction = 0;
                for (final GetWord getWord : list) {
                    Bundle bundle = new Bundle();
                    Message msg = new Message();
                    bundle.putString("show", "showEnglish");
                    bundle.putString("English", getWord.getEnglish());
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    boolean isTime = false;
                    boolean isTrue = false;
                    long start = System.currentTimeMillis();
                    long end = start;

                    int ti = 10;
                    while (!isTime && !isTrue) {
                        if (end - start < 10000) {
                            if (voiceEnglish != null && voiceEnglish.equalsIgnoreCase(getWord.getEnglish())) {
                                isTrue = true;
                                id.add(String.valueOf(getWord.getId()));
                                state.add("1");
                                userFraction++;
                            }
                            try {
                                Thread.sleep(1000);
                                ti--;
                                end = System.currentTimeMillis();
                                bundle = new Bundle();
                                msg = new Message();
                                bundle.putString("show", "Time");
                                bundle.putString("Time", String.valueOf(ti));
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            isTime = true;
                            id.add(String.valueOf(getWord.getId()));
                            state.add("0");
                        }
                    }

                }
                isEND = true;
                String ifFali = null;
                if (Grade != 0) {
                    //人机模式
                    int robotFraction = 15 - userFraction;
                    if (robotFraction > userFraction) {
                        ifFali = "1";
                    } else {
                        ifFali = "0";
                    }
                }

                long useTime = System.currentTimeMillis() - time;
                String upjson = upJson(id, state, useTime, ifFali);
                Log.e("json", upjson);
                OkhttpNet.doPost(getString(R.string.url_updateResult), upjson, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }
        }).start();


    }

    /**
     * @param idd      单词id
     * @param startee  单词状态
     * @param gameTime 游戏时间
     * @param ifFail   人机模式下胜负
     * @return
     */
    private String upJson(List<String> idd, List<String> startee, long gameTime, String ifFail) {

        String[] id = new String[idd.size()];
        String[] state = new String[startee.size()];
        idd.toArray(id);
        startee.toArray(state);
        List<UpData.DataBean> beans = new ArrayList<>();
        for (int i = 0; i < idd.size(); i++) {
            UpData.DataBean bean = new UpData.DataBean();
            bean.setId(id[i]);
            bean.setState(state[i]);
            beans.add(bean);
        }
        UpData upData = new UpData();
        upData.setData(beans);
        upData.setGameTime(String.valueOf(gameTime));
        upData.setIsFail(ifFail);
        upData.setUser(new Data().getUser());

        Gson gson = new Gson();
        String json = gson.toJson(upData);

        return json;

    }

    //语音识别数据
    private void Voice() {
        final RecognizerDialog recognizerDialog = new RecognizerDialog(this, null);
        recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "en_us");
        recognizerDialog.setParameter(SpeechConstant.ACCENT, null);
        recognizerDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if (!b) {
                    String str = recognizerResult.getResultString();
                    Gson gson = new Gson();
                    SpeechResult speechResult = gson.fromJson(str, SpeechResult.class);
                    for (SpeechResult.WsBean ws : speechResult.getWs()) {
                        for (SpeechResult.WsBean.CwBean cw : ws.getCw()) {
                            voiceEnglish = cw.getW();

                        }
                    }
                    Bundle bundle=new Bundle();
                    Message msg=new Message();
                    bundle.putString("show","voiceEnglish");
                    bundle.putString("voiceEnglish",voiceEnglish);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        recognizerDialog.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.getData().getString("show")) {
                case "showEnglish":
                    showWord.setText(msg.getData().getString("English"));
                    break;
                case "Time":
                    showTime.setText(msg.getData().getString("Time"));
                    break;
                case "voiceEnglish":
                    showSper.setText(msg.getData().getString("voiceEnglish"));
                    break;
                default:
                    break;
            }

        }
    };


    class GetWord {

        /**
         * id : 1042
         * english : limb
         * chinese : n.分支，突出物
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

    static class UpData {

        /**
         * user : ewq
         * data : [{"id":"2","state":"0"},{"id":"101","state":"1"}]
         * isFail : 1
         * gameTime : 2000
         */

        private String user;
        private String ifFail;
        private String gameTime;
        private List<UpData.DataBean> data;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getIsFail() {
            return ifFail;
        }

        public void setIsFail(String ifFail) {
            this.ifFail = ifFail;
        }

        public String getGameTime() {
            return gameTime;
        }

        public void setGameTime(String gameTime) {
            this.gameTime = gameTime;
        }

        public List<UpData.DataBean> getData() {
            return data;
        }

        public void setData(List<UpData.DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 2
             * state : 0
             */

            private String id;
            private String state;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }
        }
    }

    static class SpeechResult {

        /**
         * sn : 1
         * ls : false
         * bg : 0
         * ed : 0
         * ws : [{"bg":0,"cw":[{"sc":0,"w":"Apple"}]}]
         */

        private int sn;
        private boolean ls;
        private int bg;
        private int ed;
        private List<WsBean> ws;

        public int getSn() {
            return sn;
        }

        public void setSn(int sn) {
            this.sn = sn;
        }

        public boolean isLs() {
            return ls;
        }

        public void setLs(boolean ls) {
            this.ls = ls;
        }

        public int getBg() {
            return bg;
        }

        public void setBg(int bg) {
            this.bg = bg;
        }

        public int getEd() {
            return ed;
        }

        public void setEd(int ed) {
            this.ed = ed;
        }

        public List<WsBean> getWs() {
            return ws;
        }

        public void setWs(List<WsBean> ws) {
            this.ws = ws;
        }

        public static class WsBean {
            /**
             * bg : 0
             * cw : [{"sc":0,"w":"Apple"}]
             */

            private int bg;
            private List<CwBean> cw;

            public int getBg() {
                return bg;
            }

            public void setBg(int bg) {
                this.bg = bg;
            }

            public List<CwBean> getCw() {
                return cw;
            }

            public void setCw(List<CwBean> cw) {
                this.cw = cw;
            }

            public static class CwBean {
                /**
                 * sc : 0.0
                 * w : Apple
                 */

                private double sc;
                private String w;

                public double getSc() {
                    return sc;
                }

                public void setSc(double sc) {
                    this.sc = sc;
                }

                public String getW() {
                    return w;
                }

                public void setW(String w) {
                    this.w = w;
                }
            }
        }
    }

}
