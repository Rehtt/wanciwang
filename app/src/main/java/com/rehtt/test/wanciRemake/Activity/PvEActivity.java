package com.rehtt.test.wanciRemake.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
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
import com.rehtt.test.wanciRemake.DialogActivity.LoadDialog;
import com.rehtt.test.wanciRemake.DialogActivity.WordsDialog;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.OkhttpNet;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    TextView GlobalTime;
    Button button;

    String word = "";

    //初始化
    int blood1 = 5;
    int blood2 = 5;
    int p1 = 0;
    int p2 = 0;

    String voiceEnglish = null;
    boolean isEND = false;
    LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_e);
        new SetFullScreen(this);

        button = (Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEND)
                    Voice();
                else
                    Toast.makeText(PvEActivity.this, "游戏已结束", Toast.LENGTH_SHORT).show();
            }
        });
        showWord = (TextView) findViewById(R.id.word);
        showTime = (TextView) findViewById(R.id.time);
        showSper = (TextView) findViewById(R.id.voiceEnglish);
        GlobalTime = (TextView) findViewById(R.id.globalTime);
        showTime.setText("10");
        showSper.setText("");

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59e45c55");

        getWord();

        //显示加载框
        loadDialog = new LoadDialog(PvEActivity.this);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.show();

        imageView = (ImageView) findViewById(R.id.blood1);
        imageView2 = (ImageView) findViewById(R.id.blood2);
        po1 = (ImageView) findViewById(R.id.portrait1);
        po2 = (ImageView) findViewById(R.id.portrait2);
        po1_x = (ImageView) findViewById(R.id.p1);
        po2_x = (ImageView) findViewById(R.id.p2);
        po1_x.setVisibility(View.GONE);
        po2_x.setVisibility(View.GONE);


        imageView.setImageBitmap(bloodStrip(blood1, p1, 1));
        imageView2.setImageBitmap(bloodStrip(blood2, p2, 2));

        Glide.with(this).load("http://test.rehtt.com/test2.jpg").apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(po1);
        Glide.with(this).load("http://test.rehtt.com/test.png").apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(po2);
    }

    ImageView imageView;
    ImageView imageView2;
    ImageView po1;
    ImageView po2;
    ImageView po1_x;
    ImageView po2_x;

    /*
     * blood            剩余血量（0-5）
     * p                赢的数次（0-2）
     * idd              玩家（1-2）
     */
    @SuppressLint("Range")
    private Bitmap bloodStrip(int blood, int p, int idd) {
        float blood_w = 480;          //血条长度
        float blood_h = 15;           //血条高度
        float blood_top = 10;        //血条离画布顶部距离
        float blood_left = 0;         //血条离画布左边距离
        float blood_oblique = 22;     //血条倾斜
        float blood_block = 5;        //血块

        float roundCount1_edge = 30;    //大小
        float roundCount1_top = 50;     //离画布顶部的距离
        float roundCount1_left = 30;    //离画布左侧的距离
        float roundCount1_oblique = 20; //倾斜

        float roundCount2_edge = 40;    //大小
        float roundCount2_top = 45;     //离画布顶部的距离
        float roundCount2_left = 100;    //离画布左侧的距离
        float roundCount2_oblique = 30; //倾斜

        Dip dip = new Dip();
        dip.setContext(this);

        Bitmap bitmap1;
        bitmap1 = Bitmap.createBitmap(520, 85, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap1);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(dip.px(1));

        //血条
        Path path = new Path();
        float[] pk_k = new float[]{blood_oblique, blood_top, blood_w + blood_oblique, blood_top, blood_w + blood_oblique, blood_top, blood_w, blood_top + blood_h, blood_w, blood_top + blood_h, blood_left, blood_top + blood_h, blood_left, blood_top + blood_h, blood_oblique, blood_top};
        canvas.drawLines(pk_k, paint);

        //根据剩余血量填充
        float blood_blok = blood_w / blood_block;   //计算每块血量长度
        float blood_fillTop = blood_blok * blood + blood_oblique;   //填充顶部的长度
        float blood_fillDown = blood_blok * blood;                  //填充底部的长度
        path.lineTo(blood_oblique, blood_top);
        path.lineTo(blood_fillTop, blood_top);
        path.lineTo(blood_fillDown, blood_top + blood_h);
        path.lineTo(blood_left, blood_top + blood_h);
        path.lineTo(blood_oblique, blood_top);
        path.close();
        canvas.drawPath(path, paint);


        if (p > 0) {
            //计数1填充
            path = new Path();
            path.lineTo(roundCount1_left + roundCount1_oblique, roundCount1_top);
            path.lineTo(roundCount1_left + roundCount1_oblique + roundCount1_edge, roundCount1_top);
            path.lineTo(roundCount1_left + roundCount1_edge, roundCount1_top + roundCount1_edge);
            path.lineTo(roundCount1_left, roundCount1_top + roundCount1_edge);
            path.lineTo(roundCount1_left + roundCount1_oblique, roundCount1_top);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (p == 2) {
            //计数2填充
            path = new Path();
            path.lineTo(roundCount2_left + roundCount2_oblique, roundCount2_top);
            path.lineTo(roundCount2_left + roundCount2_oblique + roundCount2_edge, roundCount2_top);
            path.lineTo(roundCount2_left + roundCount2_edge, roundCount2_top + roundCount2_edge);
            path.lineTo(roundCount2_left, roundCount2_top + roundCount2_edge);
            path.lineTo(roundCount2_left + roundCount2_oblique, roundCount2_top);
            path.close();
            canvas.drawPath(path, paint);
        }
        //计数1
        float[] k1 = new float[]{roundCount1_left + roundCount1_oblique, roundCount1_top, roundCount1_left + roundCount1_oblique + roundCount1_edge, roundCount1_top, roundCount1_left + roundCount1_oblique + roundCount1_edge, roundCount1_top, roundCount1_left + roundCount1_edge, roundCount1_top + roundCount1_edge, roundCount1_left + roundCount1_edge, roundCount1_top + roundCount1_edge, roundCount1_left, roundCount1_top + roundCount1_edge, roundCount1_left, roundCount1_top + roundCount1_edge, roundCount1_left + roundCount1_oblique, roundCount1_top};
        canvas.drawLines(k1, paint);


        //计数2
        float[] k2 = new float[]{roundCount2_left + roundCount2_oblique, roundCount2_top, roundCount2_left + roundCount2_oblique + roundCount2_edge, roundCount2_top, roundCount2_left + roundCount2_oblique + roundCount2_edge, roundCount2_top, roundCount2_left + roundCount2_edge, roundCount2_top + roundCount2_edge, roundCount2_left + roundCount2_edge, roundCount2_top + roundCount2_edge, roundCount2_left, roundCount2_top + roundCount2_edge, roundCount2_left, roundCount2_top + roundCount2_edge, roundCount2_left + roundCount2_oblique, roundCount2_top};
        canvas.drawLines(k2, paint);

        if (idd == 1) {
            return bitmap1;
        } else if (idd == 2) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1);
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);

            return bitmap2;
        } else
            return null;

    }

    List<GetWord> words=new ArrayList<>();      //
    //获取单词
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
                        words=jsons;
                        game(jsons);
                    } catch (Exception e) {
                        Log.e("qwerrt", e.getMessage());
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

    //动画
    private AlphaAnimation animation(int i) {
        AlphaAnimation alphaAnimation;
        switch (i) {
            case 1:
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setRepeatCount(1);
                return alphaAnimation;
            case 2:
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(3000);
                alphaAnimation.setFillAfter(true);
                return alphaAnimation;
            default:
                return null;
        }
    }


    long startTime = System.currentTimeMillis();
    //单词id及状态
    List<String> id = new ArrayList<>();
    List<String> state = new ArrayList<>();

    //游戏过程
    private void game(final List<GetWord> list) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                int userFraction = 0;
                Bundle bundle = new Bundle();
                Message msg = new Message();
                bundle.putString("show", "loadDialog");
                msg.setData(bundle);
                handler.sendMessage(msg);


                int g = 0;    //局数
                int w = 0;

                for (int i = 0; i < list.size(); i++) {
                    if (w < 5) {
                        int t = 3;
                        boolean tt = true;
                        while (tt) {
                            if (t != 0) {
                                try {
                                    bundle = new Bundle();
                                    msg = new Message();
                                    bundle.putString("show", "ready");
                                    bundle.putString("ready", String.valueOf(t));
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                    --t;
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                tt = false;
                            }

                        }
                        bundle = new Bundle();
                        msg = new Message();
                        bundle.putString("show", "showEnglish");
                        bundle.putString("English", list.get(i).getEnglish());
                        bundle.putString("Chinese", list.get(i).getChinese());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        boolean isTime = false;
                        boolean isTrue = false;
                        long start = System.currentTimeMillis();
                        long end = start;
                        int blood = 0;    //谁扣了血

                        int ti = 10;        //时间
                        while (!isTime && !isTrue) {
                            if (end - start < 10000) {
                                if (voiceEnglish != null && voiceEnglish.equalsIgnoreCase(list.get(i).getEnglish())) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PvEActivity.this, "true", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    isTrue = true;
                                    id.add(String.valueOf(list.get(i).getId()));
                                    state.add("1");
                                    userFraction++;
                                    blood = 2;

                                    --blood2;
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
                                id.add(String.valueOf(list.get(i).getId()));
                                state.add("0");
                                blood = 1;

                                --blood1;
                            }
                        }
                        //刷新血条
                        final int finalBlood = blood;
                        bundle = new Bundle();
                        msg = new Message();
                        bundle.putString("show", "bloods");
                        bundle.putInt("fork", finalBlood);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        ++w;
                    } else {
                        //新的一局
                        w = 0;
                        --i;
                        ++g;
                        p1 = blood1 > blood2 ? ++p1 : p1;
                        p2 = blood2 > blood1 ? ++p2 : p2;
                        final String who = blood1 > blood2 ? "玩家" : "电脑";
                        blood1 = 5;
                        blood2 = 5;
                        final int finalG = g;
                        bundle = new Bundle();
                        msg = new Message();
                        bundle.putString("show", "bloods");
                        bundle.putInt("fork", 0);
                        bundle.putString("bureau", "bureau");
                        bundle.putString("bureau_n", String.valueOf(finalG));
                        bundle.putString("bureau_who", who);
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                        try {
                            Thread.sleep(2000);
                            bundle = new Bundle();
                            msg = new Message();
                            bundle.putString("show", "clearAnimation");
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                isEND = true;
//                String ifFali = null;
//                if (Grade != 0) {
//                    //人机模式
//                    int robotFraction = 15 - userFraction;
//                    ifFali = robotFraction > userFraction ? "1" : "0";
//                }
                bundle = new Bundle();
                msg = new Message();
                bundle.putString("show", "end");
                bundle.putString("whoWin", p1 > p2 ? "玩家" : "电脑");
                msg.setData(bundle);
                handler.sendMessage(msg);

                long useTime = System.currentTimeMillis() - startTime;
                upJson(useTime, p1 > p2 ? "0" : "1");

            }
        }).start();


    }

    /**
     * 上传结果
     *
     * @param gameTime 游戏时间
     * @param ifFail   人机模式下胜负
     * @return
     */
    private void upJson( long gameTime, String ifFail) {

        for (int i = 0; i < id.size(); i++) {
            UpData upData = new UpData();
            upData.setId(id.get(i));
            upData.setState(state.get(i));
            upDataList.add(upData);
        }
        Gson gson = new Gson();
        String json = gson.toJson(upDataList);
        Map<String, String> map = new HashMap<>();
        map.put("userName", new Data().getUser());
        map.put("ifFail", ifFail);
        map.put("gameTime", String.valueOf(gameTime));
        map.put("data", json);
        Log.e("qwe", "\n" + map.get("userName") + "\n" + map.get("ifFail") + "\n" + map.get("gameTime") + "\n" + map.get("data"));

        OkhttpNet.doPost(getString(R.string.url_updateResult), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("qwe", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

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
                    Bundle bundle = new Bundle();
                    Message msg = new Message();
                    bundle.putString("show", "voiceEnglish");
                    bundle.putString("voiceEnglish", voiceEnglish);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        recognizerDialog.show();
        TextView txt = (TextView) recognizerDialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText(word);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void useTime() {
        long time = System.currentTimeMillis();
        SimpleDateFormat ft =
                new SimpleDateFormat("mm:ss");
        String useTime = ft.format(time - startTime);
        GlobalTime.setText(useTime);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.getData().getString("show")) {
                case "loadDialog":
                    loadDialog.dismiss();
                    break;
                case "ready":
                    showWord.setText("准备");
                    showTime.setText(msg.getData().getString("ready"));
                    useTime();
                    break;
                case "showEnglish":
                    word = msg.getData().getString("English");
                    showWord.setText(word + "\n" + msg.getData().getString("Chinese"));

                    break;
                case "Time":
                    showTime.setText(msg.getData().getString("Time"));
                    useTime();
                    break;
                case "voiceEnglish":
                    showSper.setText(msg.getData().getString("voiceEnglish"));
                    break;
                case "bloods":
                    imageView.setImageBitmap(bloodStrip(blood1, p1, 1));
                    imageView2.setImageBitmap(bloodStrip(blood2, p2, 2));
                    if (msg.getData().getInt("fork") == 1) {
                        po1_x.setVisibility(View.VISIBLE);
                        po1_x.startAnimation(animation(1));

                    } else if (msg.getData().getInt("fork") == 2) {
                        po2_x.setVisibility(View.VISIBLE);
                        po2_x.startAnimation(animation(1));
                    }
                    break;
                case "end":
                    imageView.setImageBitmap(bloodStrip(blood1, p1, 1));
                    imageView2.setImageBitmap(bloodStrip(blood2, p2, 2));
                    showWord.setText(msg.getData().getString("whoWin") + "胜");
                    AlertDialog alertDialog = new AlertDialog.Builder(PvEActivity.this)
                            .setTitle("游戏结束")
                            .setMessage(p1 > p2 ? "玩家" : "电脑" + "胜")
                            .setNegativeButton("返回主页", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PvEActivity.this.finish();
                                }
                            })
                            .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bloodStrip(5, 0, 1);
                                    bloodStrip(5, 0, 2);
                                    getWord();
                                }
                            })
                            .setNeutralButton("查看对战情况", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String,String>map=new HashMap<>();
                                    for (int i=0;i<state.size();i++){
                                        map.put(words.get(i).getEnglish(),state.get(i));
                                    }
                                    new WordsDialog(PvEActivity.this,map).show();
                                }
                            })
                            .create();
                    alertDialog.show();


                    break;
                case "clearAnimation":
                    showWord.clearAnimation();
                    break;
                default:
                    break;
            }
            if (msg.getData().getString("bureau") != null) {
                showWord.setText("第" + msg.getData().getString("bureau_n") + "局" + msg.getData().getString("bureau_who") + "胜");
                showWord.startAnimation(animation(2));
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

    private List<UpData> upDataList = new ArrayList<>();

    class UpData {

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
