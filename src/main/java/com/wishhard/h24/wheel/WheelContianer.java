package com.wishhard.h24.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;


public class WheelContianer extends LinearLayout {

    private Context mContext;
    private TheWheel wheel;

    public WheelContianer(Context context) {
        super(context);
        init(context);
    }

    public WheelContianer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        layoutSettings();

        wheel = new TheWheel(mContext);
        this.addView(wheel);

    }

    private void layoutSettings() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(Color.WHITE);
    }

    public void setItemsForWheel(List<String> list) {
        wheel.setItems(list);
    }

    public void setOnWheelListener(TheWheel.OnWheelListener onWheelListener) {
        wheel.setOnWheelListener(onWheelListener);
    }

    public void setSelection(int position) {
        wheel.setSeletion(position);
    }

    public String getSeletedItem() {
        return wheel.getSeletedItem();
    }

    public void setAllowMotionEvent(boolean allowMotionEvent) {
        wheel.wanaAllowMotionEvent(allowMotionEvent);
    }
}
