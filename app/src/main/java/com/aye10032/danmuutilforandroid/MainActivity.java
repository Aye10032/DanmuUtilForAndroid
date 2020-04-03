package com.aye10032.danmuutilforandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aye10032.danmuutilforandroid.util.AyeCompile;
import com.aye10032.danmuutilforandroid.util.BiliInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText avinput;
    private TextView getText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avinput = findViewById(R.id.avET);
        getText = findViewById(R.id.searchTV);

        getText.setOnClickListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("text/")) {
                textReceive(intent);
            }
        }
    }



    private void textReceive(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            AyeCompile compile = new AyeCompile(sharedText);
            if (compile.hasAV()) {
                avinput.setText(compile.getAVString());
            }else if (compile.hasBV()){
                avinput.setText(compile.getBVString());
            }
        }

        Uri textUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (textUri != null) {
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(textUri);
                avinput.setText(inputStream2Byte(inputStream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private String inputStream2Byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        //指定编码格式为UIT-8
        return new String(bos.toByteArray(), "UTF-8");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchTV:
                String mid = avinput.getText().toString();
                AyeCompile compile = new AyeCompile(mid);
                boolean flag = false;
                if (compile.hasAV()){
                    mid = compile.getAVString();
                    flag = true;
                }else if (compile.hasBV()){
                    mid = compile.getBVString();
                    flag = true;
                }
                if (flag) {
                    System.out.println(new BiliInfo(mid).getTitle());
                }else {
                    Toast.makeText(MainActivity.this, "格式错误，请重试", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
