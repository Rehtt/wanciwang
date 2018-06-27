package com.rehtt.test.wanciRemake;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {

        List<HashMap<String,String>>list=new ArrayList<>();
        for (int i=0;i<10;i++){
            HashMap<String, String> map =new HashMap<>();
            map.put("i", String.valueOf(i));
            list.add(map);
        }

        for(HashMap<String, String> list1:list){
            System.out.print(list1.get("i")+"\n");
        }
    }

}


