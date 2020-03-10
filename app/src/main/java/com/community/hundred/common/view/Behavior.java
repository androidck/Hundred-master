package com.community.hundred.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.community.hundred.R;
import com.hjq.toast.ToastUtils;

public class Behavior extends CoordinatorLayout.Behavior<View> {

    private int value;

    public static OnAbsListener listener;

    public Behavior() {
    }

    public Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        value += dyConsumed;
        listener.onAbs(value);
        View titleBar = coordinatorLayout.getRootView().findViewById(R.id.top_bar_title_layout);
        View topBarBg = coordinatorLayout.getRootView().findViewById(R.id.top_bar_bg);

        View arcBg = coordinatorLayout.getRootView().findViewById(R.id.arc_bg);
        int height = titleBar.getHeight();

        float abs = Math.min(1, Math.min(height, Math.max(value, 0F)) / height * 2);
        arcBg.setAlpha(abs);
        topBarBg.setAlpha(abs);

    }

    public interface OnAbsListener {
        void onAbs(int abs);
    }

    public static void setListener(OnAbsListener onAbsListener) {
        listener = onAbsListener;
    }
}
