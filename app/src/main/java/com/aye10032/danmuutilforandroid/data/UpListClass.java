package com.aye10032.danmuutilforandroid.data;

import java.util.ArrayList;
import java.util.List;

public class UpListClass {

    private int total;
    private List<UpDataClass> uplist;

    public UpListClass() {
        total = 0;
        uplist  = new ArrayList<UpDataClass>();
    }

    public int size() {
        total = uplist.size();

        return total;
    }

    public void addUp(UpDataClass updata) {
        String UUID = updata.getMid();
        for (UpDataClass up : uplist) {
            if (up.getMid().equals(UUID)) {
                up.setFace(updata.getFace());
                up.setName(updata.getName());
                up.setSign(updata.getSign());
                break;
            }
        }

        uplist.add(updata);
        size();
    }

    public List<UpDataClass> getUplist() {
        return uplist;
    }

    public boolean existUp(String UUID) {
        for (UpDataClass updata : uplist) {
            if (updata.getMid().equals(UUID)) {
                return true;
            }
        }
        return false;
    }

}
