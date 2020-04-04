package com.aye10032.danmuutilforandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aye10032.danmuutilforandroid.data.VideoData;
import com.aye10032.danmuutilforandroid.util.AyeCompile;
import com.aye10032.danmuutilforandroid.util.BiliInfo;
import com.aye10032.danmuutilforandroid.util.ScreenUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity{

    private EditText avinput;
    BiliInfo biliInfo;
    VideoData data = new VideoData();
    LinearLayout searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avinput = findViewById(R.id.avET);
        TextView getText = findViewById(R.id.searchTV);
        searchlist = findViewById(R.id.searchresultLL);

        getText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    final String finalMid = mid;
                    Thread getmsg = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            biliInfo = new BiliInfo(finalMid);
                            data.setTitle(biliInfo.getTitle());
                            data.setCoin(biliInfo.getCoin());
                            data.setDanmaku(biliInfo.getDanmaku());
                            data.setFavorite(biliInfo.getFavorite());
                            data.setHead(biliInfo.getHead());
                            data.setImg(biliInfo.getImg());
                            data.setLike(biliInfo.getLike());
                            data.setReply(biliInfo.getReply());
                            data.setUp(biliInfo.getUp());
                            data.setView(biliInfo.getView());
                        }
                    });
                    getmsg.start();
                    try {
                        getmsg.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    addCard();
                }else {
                    Toast.makeText(MainActivity.this, "格式错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("text/")) {
                textReceive(intent);
            }
        }
    }

    private void addCard(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        ImageView imageView = new ImageView(this);
        int imageWidth = ScreenUtil.dp2px(this, 140);
        int imageHeight = ScreenUtil.dp2px(this, 140);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
        imageView.setPadding(10, 10, 10, 10);
        imageView.setImageBitmap(data.getImg());

        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setText(data.getTitle());

        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        searchlist.addView(linearLayout);
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
}
