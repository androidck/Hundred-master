package com.samluys.filtertab;

import android.content.Context;
import android.widget.PopupWindow;

import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.popupwindow.BasicSelectPopupWindow;
import com.samluys.filtertab.popupwindow.SeniorSelectPopupWindow;
import com.samluys.filtertab.popupwindow.SingleSelectPopupWindow;

import java.util.List;




public class PopupEntityLoaderImp implements IPopupLoader {

    @Override
    public PopupWindow getPopupEntity(Context context, List data, int filterType, int position, OnFilterToViewListener onFilterToViewListener, FilterTabView view) {
        PopupWindow popupWindow = null;
        switch (filterType) {
            case FilterTabConfig.FILTER_TYPE_SINGLE_SELECT:
                popupWindow = new SingleSelectPopupWindow(context,data,filterType,position, onFilterToViewListener);
                break;
            case FilterTabConfig.FILTER_TYPE_MORE_CONDITION:
                popupWindow = new BasicSelectPopupWindow(context,data,filterType,position, onFilterToViewListener);
                break;
            case FilterTabConfig.FILTER_TYPE_SENIOR_MORE_CONDITION:
                popupWindow = new SeniorSelectPopupWindow(context,data,filterType,position, onFilterToViewListener);
                break;
            default:
                break;
        }
        return popupWindow;

    }
}
