package com.community.hundred.common.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.community.hundred.R;

public class DrawableUtils {

    public static Drawable setDrawable(Context mContext, TextView view, int ids) {
        Drawable drawableLeft = mContext.getDrawable(ids);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        view.setCompoundDrawables(drawableLeft, null, null, null);
        return drawableLeft;
    }
}
