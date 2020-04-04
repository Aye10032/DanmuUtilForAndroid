package com.aye10032.danmuutilforandroid.data;

import android.graphics.Bitmap;

public class VideoData {

    private String title = "";
    private Bitmap img = null;

    private Bitmap head = null;
    private String up = "";

    private int view = 0;
    private int danmaku = 0;
    private int like = 0;
    private int coin = 0;
    private int favorite = 0;
    private int reply = 0;

    public VideoData(){

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setDanmaku(int danmaku) {
        this.danmaku = danmaku;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }

    public String getTitle() {
        return this.title;
    }

    public Bitmap getImg() {
        return img;
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

    public Bitmap getHead() {
        return head;
    }
}
