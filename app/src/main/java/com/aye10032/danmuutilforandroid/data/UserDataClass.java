package com.aye10032.danmuutilforandroid.data;

import android.graphics.Bitmap;

public class UserDataClass {

    private String mid;
    private String name;
    private Bitmap head;
    private String sign;

    public UserDataClass(){

    }

    public UserDataClass(String mid, String name, Bitmap head, String sign) {
        setMid(mid);
        setName(name);
        setHead(head);
        setSign(sign);
    }


    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
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
