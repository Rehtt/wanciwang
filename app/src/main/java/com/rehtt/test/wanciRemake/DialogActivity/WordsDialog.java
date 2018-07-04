package com.rehtt.test.wanciRemake.DialogActivity;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rehtt.test.wanciRemake.Activity.MyActivity;
import com.rehtt.test.wanciRemake.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordsDialog extends Dialog {
    Map<String,String>map;
    List<String> list=new ArrayList<>();
    public WordsDialog(@NonNull Context context,@NonNull Map<String,String>map) {
        super(context);
        setContentView(R.layout.words_dialog_layout);
        this.map=map;
        for (String key:map.keySet()){
            list.add(key);
        }
        ListView listView=(ListView)findViewById(R.id.words_list);
        Button button=(Button)findViewById(R.id.button2);
        MyAdapter myAdapter=new MyAdapter(context);
        listView.setAdapter(myAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    class MyAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;

        public MyAdapter(Context context) {
            this.layoutInflater.cloneInContext(context);
        }

        @Override
        public int getCount() {

            return list.size();
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
            convertView=layoutInflater.inflate(R.layout.list_words,null);
            TextView textView=(TextView)convertView.findViewById(R.id.textView9);
            TextView textView1=(TextView)convertView.findViewById(R.id.textView10);
            if (position == 0) {
                textView.setText("单词");
                textView1.setText("答题情况");
            }else {
                textView.setText(list.get(position));
                textView1.setText(map.get(list.get(position)));
            }

            return convertView;
        }
    }
}
