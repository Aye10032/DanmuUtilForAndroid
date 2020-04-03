package com.aye10032.danmuutilforandroid.data;

import java.util.ArrayList;
import java.util.List;

public class VideoDanmuClass {

    private String aid;
    private String title;
    private String url;
    private List<DanmuDataClass> danmulist;

    public VideoDanmuClass(){
        danmulist = new ArrayList<DanmuDataClass>();
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<DanmuDataClass> getDanmulist() {
        return danmulist;
    }

    public void addDanmu(DanmuDataClass danmudata){
        String mid = danmudata.getMid();
        boolean exist = false;
        for (DanmuDataClass data : danmulist) {
            if (data.getMid().equals(mid)) {
                data.addDanmu(danmudata.getDanmu().get(0));
                exist = true;
                break;
            }
        }
        if (!exist) {
            String name = danmudata.getName();
            String face = danmudata.getFace();
            String sign = danmudata.getSign();
            List<String> danmu = danmudata.getDanmu();
            danmulist.add(new DanmuDataClass(mid, name, face, sign, danmu));
        }
    }
}
