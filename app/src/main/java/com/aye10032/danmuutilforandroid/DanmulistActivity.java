package com.aye10032.danmuutilforandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aye10032.danmuutilforandroid.data.UserDataClass;
import com.aye10032.danmuutilforandroid.util.BiliUtil;
import com.aye10032.danmuutilforandroid.util.CRC32Util;
import com.aye10032.danmuutilforandroid.util.ScreenUtil;
import com.google.gson.JsonObject;

import java.util.List;

public class DanmulistActivity extends AppCompatActivity {

    LinearLayout danmulistlayout;

    String[] cidlist;
    String[] partlist;

    TextView listTV;
    TextView searchTV;
    int choice;
    CRC32Util crc32Util;
    List<String[]> danmulist;

    UserDataClass userDataClass = new UserDataClass();

    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmulist);

        listTV = findViewById(R.id.listText);
        searchTV = findViewById(R.id.danmuTV);
        danmulistlayout = findViewById(R.id.danmuLL);
        flag = true;
        crc32Util = new CRC32Util();

        Intent intent = getIntent();
        String s = intent.getStringExtra("bvid");
        cidlist = intent.getStringArrayExtra("cid");
        partlist = intent.getStringArrayExtra("part");


        listTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog(partlist);
            }
        });

        updatePage(0);
    }

    private void showSingleChoiceDialog(final String[] list) {
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(DanmulistActivity.this);
        singleChoiceDialog.setTitle("分P选择");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(list, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choice != -1) {
                            Toast.makeText(DanmulistActivity.this,
                                    "你选择了" + list[choice],
                                    Toast.LENGTH_SHORT).show();
                            updatePage(choice);
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void updatePage(final int page) {
        Thread getDanmu = new Thread(new Runnable() {
            @Override
            public void run() {
                BiliUtil biliUtil = new BiliUtil();
                if (cidlist != null) {
                    String xmlString = biliUtil.getDanmu(cidlist[page]);
                    danmulist = biliUtil.getDanmuData(xmlString);
                }
            }
        });

        getDanmu.start();
        try {
            getDanmu.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 6; i++) {

            String danmu = danmulist.get(i)[0];
            final String uid = crc32Util.solve(danmulist.get(i)[1]);

            Thread getInfo = new Thread(new Runnable() {
                @Override
                public void run() {
                    BiliUtil biliUtil = new BiliUtil();
                    try {
                        JsonObject userInfo = new BiliUtil().getUpInfo(uid)
                                .get("data").getAsJsonObject()
                                .get("card").getAsJsonObject();

                        String face = userInfo.get("face").getAsString();
                        System.out.println(face);
                        userDataClass.setHead(biliUtil.returnBitMap(face));
                        userDataClass.setMid(uid);
                        userDataClass.setName(userInfo.get("name").getAsString());
                        userDataClass.setSign(userInfo.get("sign").getAsString());
                    } catch (NullPointerException e) {
                        e.getCause();
                    }
                }
            });

            try {
                getInfo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            addCard(userDataClass.getHead(), danmu, userDataClass.getMid());
        }
    }

    private void addCard(Bitmap head, String msg, String uid) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        if (flag) {
            linearLayout.setBackgroundColor(getColor(R.color.colorBG1));
            flag = false;
        } else {
            linearLayout.setBackgroundColor(getColor(R.color.colorBG2));
            flag = true;
        }
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(0, ScreenUtil.dp2px(this, 10), 0, ScreenUtil.dp2px(this, 10));

        final TextView idText = new TextView(this);
        idText.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil.dp2px(this, 0), ScreenUtil.dp2px(this, 0)));
        idText.setCursorVisible(false);
        idText.setText(uid);

        ImageView headView = new ImageView(this);
        LinearLayout.LayoutParams headParam
                = new LinearLayout.LayoutParams(ScreenUtil.dp2px(this, 50), ScreenUtil.dp2px(this, 50));
        headParam.setMargins(ScreenUtil.dp2px(this, 10), 0, 0, 0);
        headView.setLayoutParams(headParam);
        headView.setImageBitmap(head);

        TextView danmuText = new TextView(this);
        LinearLayout.LayoutParams danmuParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        danmuParams.setMargins(ScreenUtil.dp2px(this, 10), 0, ScreenUtil.dp2px(this, 10), 0);
        danmuText.setLayoutParams(danmuParams);
        danmuText.setPadding(ScreenUtil.dp2px(this, 5), ScreenUtil.dp2px(this, 5), ScreenUtil.dp2px(this, 5), ScreenUtil.dp2px(this, 5));
        danmuText.setText(msg);
        danmuText.setTextSize(25);
        danmuText.setBackground(getDrawable(R.drawable.shape_danmu));

        linearLayout.addView(idText);
        linearLayout.addView(headView);
        linearLayout.addView(danmuText);

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(idText.getText());
            }
        });

        danmulistlayout.addView(linearLayout);
    }
}

