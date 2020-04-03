package com.aye10032.danmuutilforandroid.data;

import java.util.ArrayList;
import java.util.List;

public class DanmuClass {

    private List<UpVideoClass> uplist;

    public DanmuClass(){
        uplist = new ArrayList<UpVideoClass>();
    }

    public List<UpVideoClass> getUplist(){
        return this.uplist;
    }

    public void addUp(UpVideoClass up){
        uplist.add(up);
    }

}
