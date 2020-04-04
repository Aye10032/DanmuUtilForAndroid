package com.aye10032.danmuutilforandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DanmulistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmulist);

        TextView textView = findViewById(R.id.texttest);

        Intent intent = getIntent();
        String s = intent.getStringExtra("bvid");
        textView.setText(s);
    }
}
