package com.rehtt.test.wanciRemake;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.rehtt.test.wanciRemake.Activity.PvEActivity;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        List<String>idd=new ArrayList<>();
        List<String>startee=new ArrayList<>();
        for (int i=0;i<5;i++){
            idd.add(String.valueOf(i));
            startee.add(String.valueOf(i+9));
        }
        for (int i=0;i<idd.size();i++){
            test upData=new test();
            upData.setId(idd.get(i));
            upData.setState(startee.get(i));
            list.add(upData);
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.print(json);
    }
    List<test> list=new ArrayList<>();



    class test{

        /**
         * id : 1
         * state : as
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


