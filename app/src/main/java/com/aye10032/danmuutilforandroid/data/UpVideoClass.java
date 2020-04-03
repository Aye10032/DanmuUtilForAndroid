package com.aye10032.danmuutilforandroid.data;

import java.util.ArrayList;
import java.util.List;

public class UpVideoClass {

    private String mid;
    private String name;
    private List<VideoDanmuClass> videolist;

    public UpVideoClass(){
        videolist = new ArrayList<VideoDanmuClass>();
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMid() {
        return mid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<VideoDanmuClass> getVideolist() {
        return videolist;
    }

    public void addVideoList(VideoDanmuClass videoDanmuClass){
        videolist.add(videoDanmuClass);
    }
}
