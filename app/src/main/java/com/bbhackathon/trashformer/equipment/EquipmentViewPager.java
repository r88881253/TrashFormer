package com.bbhackathon.trashformer.equipment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EquipmentViewPager extends ViewPager {

    private boolean enableSwipe;

    public EquipmentViewPager(Context context) {
        super(context);
        init();
    }

    public EquipmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        enableSwipe = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return enableSwipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return enableSwipe && super.onTouchEvent(event);

    }

    public void setEnableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
    }

    public boolean isEnableSwipe() {
        return enableSwipe;
    }
}
