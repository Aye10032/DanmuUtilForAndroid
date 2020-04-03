package com.aye10032.danmuutilforandroid.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AyeCompile {

    private String msg = "";
    private boolean hasCode = false;

    private static Pattern av_pattern = Pattern.compile("([aA])([vV])\\d+");
    private Matcher av_matcher;

    private static Pattern bv_pattern = Pattern.compile("([bB])([vV])[(0-9)|(A-Z)|(a-z)]{10}");
    private Matcher bv_matcher;

    public AyeCompile(String msg) {
        this.msg = msg;
        av_matcher = av_pattern.matcher(this.msg);
        bv_matcher = bv_pattern.matcher(this.msg);

    }

    public boolean hasAV() {
        this.hasCode = av_matcher.find();
        av_matcher.reset();
        return hasCode;
    }

    public boolean hasBV() {
        this.hasCode = bv_matcher.find();
        bv_matcher.reset();
        return hasCode;
    }

    public String getAVString() {
        String avn = "";
        av_matcher.find();
        avn = av_matcher.group();
        av_matcher.reset();
        return avn;
    }

    public String getBVString() {
        String avn = "";
        bv_matcher.find();
        avn = bv_matcher.group();
        bv_matcher.reset();
        return avn;
    }

}
