package com.aye10032.danmuutilforandroid;

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

import androidx.appcompat.app.AppCompatActivity;

import com.aye10032.danmuutilforandroid.data.VideoData;
import com.aye10032.danmuutilforandroid.util.AyeCompile;
import com.aye10032.danmuutilforandroid.util.BiliInfo;
import com.aye10032.danmuutilforandroid.util.ScreenUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText avinput;
    BiliInfo biliInfo;
    VideoData data = new VideoData();
    LinearLayout searchlist;
    Map<String, List<String[]>> searchmap = new HashMap<>();

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
                if (compile.hasAV()) {
                    mid = compile.getAVString();
                    flag = true;
                } else if (compile.hasBV()) {
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
                            data.setImg(biliInfo.getImg());
                            data.setUp(biliInfo.getUp());
                            data.setMid(biliInfo.getMid());
                            data.setCidlist(biliInfo.getCid());
                            data.setPartlist(biliInfo.getPart());
                        }
                    });
                    getmsg.start();
                    try {
                        getmsg.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<String[]> list = new ArrayList<>();
                    list.add(data.getCidlist());
                    list.add(data.getPartlist());
                    searchmap.put(data.getMid(), list);
                    addCard();
                } else {
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

    private void addCard() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setBackgroundResource(R.drawable.shape_ll);

        LinearLayout.LayoutParams layoutParam
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ScreenUtil.dp2px(this, 110));
        layoutParam.setMargins(0, ScreenUtil.dp2px(this, 10), 0, 0);

        linearLayout.setLayoutParams(layoutParam);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imgParam
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 3.5f);
        imgParam.setMargins(ScreenUtil.dp2px(this, 10), 0, 0, 0);
        imageView.setLayoutParams(imgParam);
        imageView.setImageBitmap(data.getImg());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setGravity(Gravity.CENTER);
        titleLayout.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 2.0f));

        TextView titleText = new TextView(this);
        titleText.setTextSize(20);
        titleText.setTextColor(Color.BLACK);
        titleText.setGravity(Gravity.CENTER);
        titleText.setText(data.getTitle());
        LinearLayout.LayoutParams titleParam
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        titleParam.setMargins(ScreenUtil.dp2px(this, 10), 0, ScreenUtil.dp2px(this, 10), 0);
        titleText.setLayoutParams(titleParam);

        final TextView midText = new TextView(this);
        midText.setGravity(Gravity.LEFT);
        midText.setText(data.getMid());
        LinearLayout.LayoutParams midParam
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);
        midParam.setMargins(ScreenUtil.dp2px(this, 10), 0, ScreenUtil.dp2px(this, 10), 0);
        midText.setLayoutParams(midParam);

        TextView upText = new TextView(this);
        upText.setGravity(Gravity.LEFT);
        upText.setText(String.format("up主:%s", data.getUp()));
        LinearLayout.LayoutParams upParam
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);
        upParam.setMargins(ScreenUtil.dp2px(this, 10), 0, ScreenUtil.dp2px(this, 10), 0);
        upText.setLayoutParams(upParam);

        titleLayout.addView(titleText);
        titleLayout.addView(midText);
        titleLayout.addView(upText);

        linearLayout.addView(imageView);
        linearLayout.addView(titleLayout);
        searchlist.addView(linearLayout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bv = midText.getText().toString();
                String[] cid = Objects.requireNonNull(searchmap.get(bv)).get(0);
                String[] part = Objects.requireNonNull(searchmap.get(bv)).get(1);
                Intent intent = new Intent(MainActivity.this, DanmulistActivity.class);
                intent.putExtra("bvid", bv);
                intent.putExtra("cid", cid);
                intent.putExtra("part", part);
                startActivity(intent);
            }
        });
    }

    private void textReceive(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            AyeCompile compile = new AyeCompile(sharedText);
            if (compile.hasAV()) {
                avinput.setText(compile.getAVString());
            } else if (compile.hasBV()) {
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
