package com.aye10032.danmuutilforandroid.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class BiliInfo {

    protected String apiURL1 = "https://api.bilibili.com/x/web-interface/view?";
    protected String apiURL2 = "&type=jsonp";
    protected String apiURL;

    private String title = "";
    private String imgurl = "";
    private String videourl_av = "https://www.bilibili.com/video/av";
    private String videourl_bv = "https://www.bilibili.com/video/BV";
    private String videourl;

    private String headurl = "";
    private String up = "";

    private int view = 0;
    private int danmaku = 0;
    private int like = 0;
    private int coin = 0;
    private int favorite = 0;
    private int reply = 0;

    public BiliInfo(String avn) {
        if (avn.startsWith("a") || avn.startsWith("A")) {
            this.videourl = videourl_av + avn.substring(2);
            this.apiURL = apiURL1 + "aid=" + avn.substring(2) + apiURL2;
        } else {
            this.videourl = videourl_bv + avn.substring(2);
            this.apiURL = apiURL1 + "bvid=BV" + avn.substring(2) + apiURL2;
        }

        String body = null;
        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(apiURL)
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                body = new String(response.body().bytes());
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(body);

            if (element.isJsonObject()) {
                System.out.println(element);
                JsonObject jsonObject = element.getAsJsonObject();

                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                this.title = dataJson.get("title").getAsString();
                this.imgurl = dataJson.get("pic").getAsString();

                JsonObject ownerJson = dataJson.get("owner").getAsJsonObject();
                this.headurl = ownerJson.get("face").getAsString();
                this.up = ownerJson.get("name").getAsString();

                JsonObject statJson = dataJson.get("stat").getAsJsonObject();
                this.view = statJson.get("view").getAsInt();
                this.danmaku = statJson.get("danmaku").getAsInt();
                this.like = statJson.get("like").getAsInt();
                this.coin = statJson.get("coin").getAsInt();
                this.favorite = statJson.get("favorite").getAsInt();
                this.reply = statJson.get("reply").getAsInt();
            }

            /*downloadImg(headurl, "head");
            downloadImg(imgurl, "img");*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public void downloadImg(String imgurl, String filename) {
        try {
            URL img = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) img.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] data = readInputStream(inStream);
            File imageFile = new File(appDirectory + "\\image\\" + filename + ".jpg");
            FileOutputStream outStream = new FileOutputStream(imageFile);
            outStream.write(data);
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private byte[] readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();

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

    public int getView() {
        return view;
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
}
