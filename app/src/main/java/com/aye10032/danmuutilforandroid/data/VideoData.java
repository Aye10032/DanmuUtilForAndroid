package com.aye10032.danmuutilforandroid.data;

import android.graphics.Bitmap;

public class VideoData {

    private String title = "";
    private Bitmap img = null;

    private String up = "";
    private String mid = "";

    private String[] cidlist;
    private String[] partlist;

    private boolean videoAvialible;

    public VideoData() {

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

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setCidlist(String[] cids) {
        this.cidlist = cids;
    }

    public void setPartlist(String[] partlist) {
        this.partlist = partlist;
    }

    public boolean isVideoAvialible() {
        return videoAvialible;
    }

    public String getTitle() {
        return this.title;
    }

    public Bitmap getImg() {
        return img;
    }

    public String getMid() {
        return mid;
    }

    public String getUp() {
        return up;
    }

    public String[] getCidlist() {
        return cidlist;
    }

    public String[] getPartlist() {
        return partlist;
    }

    public void setVideoAvialible(boolean videoAvialible) {
        this.videoAvialible = videoAvialible;
    }
}
