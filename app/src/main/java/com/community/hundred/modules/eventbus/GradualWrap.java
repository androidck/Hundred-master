package com.community.hundred.modules.eventbus;

import com.community.hundred.modules.entry.ColorInfo;

import java.util.List;

public class GradualWrap {

    public final String title;

    public final int vibrantColor;

    private GradualWrap(String title,int vibrantColor) {
        this.title = title;
        this.vibrantColor = vibrantColor;
    }

    public static GradualWrap getInstance(String title, int vibrantColor) {
        return new GradualWrap(title, vibrantColor);
    }

}
