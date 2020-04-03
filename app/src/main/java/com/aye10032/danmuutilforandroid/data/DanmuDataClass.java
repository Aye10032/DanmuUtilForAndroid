package com.aye10032.danmuutilforandroid.data;

import java.util.ArrayList;
import java.util.List;

public class DanmuDataClass {

    private String mid;
    private String name;
    private String face;
    private String sign;
    private List<String> danmu;

    public  DanmuDataClass(){

    }

    public DanmuDataClass(String mid, String name, String face, String sign, List<String> danmu) {
        setMid(mid);
        setName(name);
        setFace(face);
        setSign(sign);
        setDanmu(danmu);
    }

    public List<String> getDanmu() {
        return danmu;
    }

    public void setDanmu(List<String> danmu){
        this.danmu = danmu;
    }

    public void addDanmu(String danmu) {
        this.danmu.add(danmu);
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
