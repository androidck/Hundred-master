package com.community.hundred.modules.entry;

import com.community.hundred.modules.ui.main.fragment.entry.BannerEntry;

/**
 * 作者：Zhout
 * 时间：2019/3/12 13:33
 * 描述：banner图片颜色渐变Bean
 * <p>
 * Vibrant （有活力）
 * Vibrant dark（有活力 暗色）
 * Vibrant light（有活力 亮色）
 * Muted （柔和）
 * Muted dark（柔和 暗色）
 * Muted light（柔和 亮色）
 */
public class ColorInfo {
    private BannerEntry imgUrl;
/*    private int vibrantColor;
    private int vibrantDarkColor;
    private int vibrantLightColor;
    private int mutedColor;
    private int mutedDarkColor;
    private int mutedLightColor;*/

    private int vibrantColor = 0xFFFFD428;
    private int vibrantDarkColor = 0xFFFFD428;
    private int vibrantLightColor = 0xFFFFD428;
    private int mutedColor = 0xFFFFD428;
    private int mutedDarkColor = 0xFFFFD428;
    private int mutedLightColor = 0xFFFFD428;

    public BannerEntry getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(BannerEntry imgUrl) {
        this.imgUrl = imgUrl;
    }


    public int getVibrantColor() {
        return vibrantColor;
    }

    public void setVibrantColor(int vibrantColor) {
        this.vibrantColor = vibrantColor;
    }

    public int getVibrantDarkColor() {
        return vibrantDarkColor;
    }

    public void setVibrantDarkColor(int vibrantDarkColor) {
        this.vibrantDarkColor = vibrantDarkColor;
    }

    public int getVibrantLightColor() {
        return vibrantLightColor;
    }

    public void setVibrantLightColor(int vibrantLightColor) {
        this.vibrantLightColor = vibrantLightColor;
    }

    public int getMutedColor() {
        return mutedColor;
    }

    public void setMutedColor(int mutedColor) {
        this.mutedColor = mutedColor;
    }

    public int getMutedDarkColor() {
        return mutedDarkColor;
    }

    public void setMutedDarkColor(int mutedDarkColor) {
        this.mutedDarkColor = mutedDarkColor;
    }

    public int getMutedLightColor() {
        return mutedLightColor;
    }

    public void setMutedLightColor(int mutedLightColor) {
        this.mutedLightColor = mutedLightColor;
    }
}
