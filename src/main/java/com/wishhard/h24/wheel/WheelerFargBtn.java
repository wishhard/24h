package com.wishhard.h24.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


import com.wishhard.h24.R;

public class WheelerFargBtn extends AppCompatButton {

    private Drawable bg;

    public WheelerFargBtn(Context context) {
        super(context);
        init(context);
    }

    public WheelerFargBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        wheelerFragBtnAttr(attrs);
        init(context);
    }

    private void init(Context context) {
        viewSetting();
    }

    private void wheelerFragBtnAttr(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,R.styleable.WheelerFargBtn,0,0);

        try {
            bg = a.getDrawable(R.styleable.WheelerFargBtn_wheeler_btn_bg);
        } finally {
            a.recycle();
        }
    }

    private void viewSetting() {
        setBackground(bg);
        setTextColor(Color.parseColor("#ffff00"));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setIncludeFontPadding(false);
    }
}
