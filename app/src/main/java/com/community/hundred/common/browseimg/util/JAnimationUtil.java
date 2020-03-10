package com.community.hundred.common.browseimg.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

import com.community.hundred.common.base.MyApplication;
import com.community.hundred.common.browseimg.beans.JPhotosInfos;


/**
 * Created by jincancan on 17/2/13.
 * Description: 基本动画执行工具
 */

public class JAnimationUtil {

    private static final long ANIM_TIME = 300;

    public static void startExitViewScaleAnim(View target, float originalScale, JPhotosInfos infos, Animator.AnimatorListener animatorListener){

        float pivotX;
        float pivotY;
        float animImgStartHeight;
        float animImgStartWidth;
        int width = infos.getWidth();
        int height = infos.getHeight();
        int localX = infos.getLeft();
        int localY = infos.getTop();

        float windowScale = JWindowUtil.getWindowScale(MyApplication.getInstances());
        float imgScale = JBitmapUtils.getImgScale(width, height);


        if(imgScale >= windowScale){
            animImgStartHeight = JWindowUtil.getWindowHeight(MyApplication.getInstances()) * originalScale;
            pivotX = localX / (1 - originalScale);
            pivotY = (localY - (animImgStartHeight - height) / 2) / (1 - originalScale);
        }else{
            animImgStartWidth = JWindowUtil.getWindowWidth(MyApplication.getInstances()) * originalScale;
            pivotX = (localX - (animImgStartWidth - width) / 2) / (1 - originalScale);
            pivotY = localY / (1 - originalScale);
        }
        target.setPivotX(pivotX);
        target.setPivotY(pivotY);

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(target, View.SCALE_X,
                target.getScaleX(),
                originalScale);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(target, View.SCALE_Y,
                target.getScaleY(),
                originalScale);
        ObjectAnimator animatorTransX = ObjectAnimator.ofFloat(target, View.TRANSLATION_X,
                target.getTranslationX() +
                        (JWindowUtil.getWindowWidth(MyApplication.getInstances()) / 2 * (1 - target.getScaleX()) -
                                target.getPivotX() * (1 - target.getScaleX())),
                0);
        ObjectAnimator animatorTransY = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y,
                target.getTranslationY() +
                        (JWindowUtil.getWindowHeight(MyApplication.getInstances()) / 2 * (1 - target.getScaleY()) -
                                target.getPivotY() * (1 - target.getScaleY())),
                0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY, animatorTransX, animatorTransY);
        set.setDuration(ANIM_TIME);
        set.start();

        set.addListener(animatorListener);

    }

    public static void startEnterViewScaleAnim(View target, float originalScale, JPhotosInfos infos, Animator.AnimatorListener animatorListener){

//        float mScale;
        float pivotX;
        float pivotY;
        float animImgStartHeight;
        float animImgStartWidth;
        int width = infos.getWidth();
        int height = infos.getHeight();
        int localX = infos.getLeft();
        int localY = infos.getTop();

        float windowScale = JWindowUtil.getWindowScale(MyApplication.getInstances());
        float imgScale = JBitmapUtils.getImgScale(width, height);

        if(imgScale >= windowScale){
            animImgStartHeight = JWindowUtil.getWindowHeight(MyApplication.getInstances()) * originalScale;
            pivotX = localX / (1 - originalScale);
            pivotY = (localY - (animImgStartHeight - height) / 2) / (1 - originalScale);
        }else{
            animImgStartWidth = JWindowUtil.getWindowWidth(MyApplication.getInstances()) * originalScale;
            pivotX = (localX - (animImgStartWidth - width) / 2) / (1 - originalScale);
            pivotY = localY / (1 - originalScale);
        }

        target.setPivotX(pivotX);
        target.setPivotY(pivotY);

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(target, View.SCALE_X,
                originalScale,
                1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(target, View.SCALE_Y,
                originalScale,
                1.0f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY);
        set.setDuration(ANIM_TIME);
        set.start();
        set.addListener(animatorListener);
    }

    public static void startEnterViewAlphaAnim(final View target, float originalScale){
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(ANIM_TIME);
        valueAnimator.setFloatValues(originalScale, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                target.setBackgroundColor(Color.parseColor(JStringUtils.getBlackAlphaBg((float)animation.getAnimatedValue())));
            }
        });
        valueAnimator.start();
    }

}
