package com.aye10032.danmuutilforandroid.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BiliInfo{

    private static String apiURL1 = "https://api.bilibili.com/x/web-interface/view?";
    private static String apiURL2 = "&type=jsonp";
    private static String videourl_av = "https://www.bilibili.com/video/av";
    private static String videourl_bv = "https://www.bilibili.com/video/BV";
    private String apiURL;

    private String title = "";
    private String imgurl = "";
    private String videourl;

    private String headurl = "";
    private String up = "";
    private String mid = "";

    private int danmaku = 0;
    private int like = 0;
    private int coin = 0;
    private int favorite = 0;
    private int reply = 0;

    private Bitmap img = null;
    private Bitmap head = null;

    CloseableHttpClient httpclient;

    public BiliInfo(String avn) {
        httpclient = HttpClients.createDefault();

        if (avn.startsWith("a") || avn.startsWith("A")) {
            this.videourl = videourl_av + avn.substring(2);
            this.apiURL = apiURL1 + "aid=" + avn.substring(2) + apiURL2;
        } else {
            this.videourl = videourl_bv + avn.substring(2);
            this.apiURL = apiURL1 + "bvid=BV" + avn.substring(2) + apiURL2;
        }

        HttpGet httpget = new HttpGet(new String(apiURL));

        CloseableHttpResponse response = null;
        String body = null;
        try {
            response = httpclient.execute(httpget);


            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity httpEntity = response.getEntity();
                body = EntityUtils.toString(httpEntity, "UTF-8");
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(body);

            if (element.isJsonObject()) {
                System.out.println(element);
                JsonObject jsonObject = element.getAsJsonObject();

                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                this.title = dataJson.get("title").getAsString();
                this.imgurl = dataJson.get("pic").getAsString();
                this.mid = dataJson.get("bvid").getAsString();

                JsonObject ownerJson = dataJson.get("owner").getAsJsonObject();
                this.headurl = ownerJson.get("face").getAsString();
                this.up = ownerJson.get("name").getAsString();

                JsonObject statJson = dataJson.get("stat").getAsJsonObject();
                this.danmaku = statJson.get("danmaku").getAsInt();
                this.like = statJson.get("like").getAsInt();
                this.coin = statJson.get("coin").getAsInt();
                this.favorite = statJson.get("favorite").getAsInt();
                this.reply = statJson.get("reply").getAsInt();
            }

            head = returnBitMap(headurl);
            img = returnBitMap(imgurl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Bitmap returnBitMap(final String url){

        final Bitmap[] bitmap = new Bitmap[1];

        /*new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();*/
        URL imageurl = null;

        try {
            imageurl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert imageurl != null;
            HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap[0] = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap[0];
    }

    public String getTitle() {
        return this.title;
    }

    public String getImgurl() {
        return this.imgurl;
    }

    public String getVideourl() {
        return this.videourl;
    }

    public int getLike() {
        return like;
    }

    public int getCoin() {
        return coin;
    }

    public int getFavorite() {
        return favorite;
    }

    public String getMid() {
        return mid;
    }

    public int getDanmaku() {
        return danmaku;
    }

    public int getReply() {
        return reply;
    }

    public String getUp() {
        return up;
    }

    public String getHeadurl() {
        return headurl;
    }

    public Bitmap getHead() {
        return head;
    }

    public Bitmap getImg() {
        return img;
    }
}
