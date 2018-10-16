package com.nathan.diff.util.plug;

import android.view.View;

/**
 * 用于返回view和监听接口
 * Created by nathan on 2017/7/24.
 */

public class KeyViewListerModel {

    private View view;

    private Object onUToAChoosingOnLister;


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Object getOnChoosingOnClick() {
        return onUToAChoosingOnLister;
    }

    public void setOnChoosingOnClick(Object onUToAChoosingOnLister) {
        this.onUToAChoosingOnLister = onUToAChoosingOnLister;
    }
}
