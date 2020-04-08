package com.aye10032.danmuutilforandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;

import com.aye10032.danmuutilforandroid.data.UserDataClass;
import com.aye10032.danmuutilforandroid.util.BiliUtil;
import com.aye10032.danmuutilforandroid.util.CRC32Util;
import com.aye10032.danmuutilforandroid.util.ScreenUtil;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanmulistActivity extends AppCompatActivity {

    LinearLayout danmulistlayout;
    ScrollView scrollView;

    String[] cidlist;
    String[] partlist;

    EditText aimET;
    TextView listTV;
    TextView searchTV;
    int choice;
    CRC32Util crc32Util;
    List<String[]> danmulist;
    Map<String, UserDataClass> shaMap = new HashMap<>();

//    UserDataClass userDataClass = new UserDataClass();

    boolean flag;
    boolean loadfinish;

    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmulist);

        listTV = findViewById(R.id.listText);
        searchTV = findViewById(R.id.danmuTV);
        scrollView = findViewById(R.id.danmuSV);
        danmulistlayout = findViewById(R.id.danmuLL);
        aimET = findViewById(R.id.danmuET);
        flag = true;
        loadfinish = true;
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

        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aim = aimET.getText().toString();
                if (aim.equals("")) {
                    Toast.makeText(DanmulistActivity.this, "未输入关键词", Toast.LENGTH_SHORT).show();
                } else {
                    List<String[]> templist = new ArrayList<>();
                    for (String[] data : danmulist) {
                        if (data[0].contains(aim)) {
                            templist.add(data);
                        }
                    }
                    if (templist.size() != 0) {
                        danmulist.clear();
                        danmulist = templist;
                        danmulistlayout.removeAllViews();

                        page = 0;
                        updatePage();
                    } else {
                        Toast.makeText(DanmulistActivity.this, "未找到相应关键词", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View contentView = scrollView.getChildAt(0);
                if (contentView != null && contentView.getMeasuredHeight() == scrollView.getScrollY() + scrollView.getHeight() && loadfinish) {
                    if (page > danmulist.size()) {
                        Toast.makeText(DanmulistActivity.this, "已经是最后了", Toast.LENGTH_SHORT).show();
                    } else {
                        loadfinish = false;
                        updatePage();
                        Toast.makeText(DanmulistActivity.this, "已加载", Toast.LENGTH_SHORT).show();
                        loadfinish = true;
                    }
                }
            }
        });

        updateDanmu(0);
        updatePage();
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
                            updateDanmu(choice);
                            danmulistlayout.removeAllViews();
                            aimET.setText("");
                            page = 0;
                            updatePage();
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void updateDanmu(final int page) {
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
        System.out.println(danmulist.size());
    }

    private void updatePage() {
        for (int i = 0; i < 10; i++) {
            if (i + page < danmulist.size()) {
                String danmu = danmulist.get(i + page)[0];
                final String shaid = danmulist.get(i + page)[1];
                if (!shaMap.containsKey(shaid)) {
                    final String uid = crc32Util.solve(shaid);

                    Thread getInfo = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserDataClass userDataClass = new UserDataClass();
                            BiliUtil biliUtil = new BiliUtil();
                            try {
                                JsonObject userInfo = biliUtil.getUpInfo(uid)
                                        .get("data").getAsJsonObject()
                                        .get("card").getAsJsonObject();

                                String face = userInfo.get("face").getAsString();
                                userDataClass.setHead(biliUtil.returnBitMap(face));
                                userDataClass.setMid(uid);
                                userDataClass.setName(userInfo.get("name").getAsString());
                                userDataClass.setSign(userInfo.get("sign").getAsString());
                                userDataClass.setLevel(userInfo.getAsJsonObject("level_info").get("current_level").getAsInt());

                                shaMap.put(shaid, userDataClass);
                            } catch (NullPointerException e) {
                                e.getCause();
                            }
                        }
                    });

                    getInfo.start();
                    try {
                        getInfo.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                addCard(danmu, shaid);
            } else {
                break;
            }
        }
        page += 10;
    }

    private void addCard(String msg, String uid) {
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

        Bitmap head = shaMap.get(uid).getHead();
        headView.setImageBitmap(head);

        TextView danmuText = new TextView(this);
        LinearLayout.LayoutParams danmuParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        danmuParams.setMargins(ScreenUtil.dp2px(this, 10), 0, ScreenUtil.dp2px(this, 10), 0);
        danmuText.setLayoutParams(danmuParams);
        danmuText.setPadding(ScreenUtil.dp2px(this, 5), ScreenUtil.dp2px(this, 5), ScreenUtil.dp2px(this, 5),
                ScreenUtil.dp2px(this, 5));
        danmuText.setText(msg);
        danmuText.setTextSize(25);
        danmuText.setBackground(getDrawable(R.drawable.shape_danmu));

        linearLayout.addView(idText);
        linearLayout.addView(headView);
        linearLayout.addView(danmuText);

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeadDialog dialog = new HeadDialog(DanmulistActivity.this, idText.getText().toString());
                dialog.show();
            }
        });

        danmulistlayout.addView(linearLayout);
    }

    class HeadDialog extends Dialog {

        public HeadDialog(@NonNull Context context, String shaid) {
            this(context, R.style.cornerdialog, shaid);
        }

        public HeadDialog(@NonNull Context context, @StyleRes int themeResId, String shaid) {
            super(context, themeResId);
            init(shaid);
        }

        private void init(String shaid) {
            UserDataClass userData = shaMap.get(shaid);
            String name = userData.getName();
            Bitmap head = userData.getHead();
            final String uid = userData.getMid();
            String sign = userData.getSign();
            String level = "LV." + userData.getLevel();

            this.setContentView(R.layout.head_dialog_layout);
            ImageView headView = this.findViewById(R.id.cardHead);
            headView.setImageBitmap(head);

            TextView nameView = this.findViewById(R.id.cardName);
            nameView.setText(name);

            TextView uidView = this.findViewById(R.id.cardUid);
            uidView.setText(String.format("uid:%s", uid));

            TextView levelView = this.findViewById(R.id.cardLevel);
            levelView.setText(level);
            switch (userData.getLevel()) {
                case 0:
                    levelView.setBackgroundColor(getColor(R.color.colorLV0));
                    break;
                case 1:
                    levelView.setBackgroundColor(getColor(R.color.colorLV1));
                    break;
                case 2:
                    levelView.setBackgroundColor(getColor(R.color.colorLV2));
                    break;
                case 3:
                    levelView.setBackgroundColor(getColor(R.color.colorLV3));
                    break;
                case 4:
                    levelView.setBackgroundColor(getColor(R.color.colorLV4));
                    break;
                case 5:
                    levelView.setBackgroundColor(getColor(R.color.colorLV5));
                    break;
                case 6:
                    levelView.setBackgroundColor(getColor(R.color.colorLV6));
                    break;
            }

            TextView signView = this.findViewById(R.id.cardSign);
            signView.setText(sign);

            headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://space.bilibili.com/" + uid);
                    System.out.println(content_url);
                    intent.setData(content_url);
                    startActivity(intent);
                }
            });
        }
    }

}

