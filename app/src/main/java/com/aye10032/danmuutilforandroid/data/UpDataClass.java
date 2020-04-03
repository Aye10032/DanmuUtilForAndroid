package com.aye10032.danmuutilforandroid.data;

public class UpDataClass {

    private String name;
    private String mid;
    private String face;
    private String sign;

    public UpDataClass(){

    }

    public UpDataClass(String mid, String name, String face, String sign){
        setMid(mid);
        setName(name);
        setFace(face);
        setSign(sign);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setMid(String mid){
        this.mid = mid;
    }

    public String getMid(){
        return this.mid;
    }

    public void setFace(String face){
        this.face = face;
    }

    public String getFace(){
        return this.face;
    }

    public void setSign(String sign){
        this.sign = sign;
    }

    public String getSign(){
        return this.sign;
    }

}
