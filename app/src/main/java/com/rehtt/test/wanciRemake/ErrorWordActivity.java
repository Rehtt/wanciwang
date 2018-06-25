package com.rehtt.test.wanciRemake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rehtt.test.wanciRemake.Tools.SetFullScreen;

public class ErrorWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_word);
        new SetFullScreen(ErrorWordActivity.this);


    }
}
