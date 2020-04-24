package com.wishhard.h24.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wishhard.h24.R;
import com.wishhard.h24.utils.ScreenUtility;


public class TopOptions extends AppCompatImageView  {

    private Drawable drawable = null;

    public TopOptions(Context context) {
        super(context);
        init(context);
    }

    public TopOptions(Context context, AttributeSet attrs) {
        super(context, attrs);
        topOpionCustomAttrValues(attrs);
        init(context);
    }

    private void init(Context context) {
        setPadding(0,0,0,0);
        ScreenUtility.initContext(context);




        if(loadFormAttr() != null) {
            setImageBitmap(loadFormAttr());
        }

    }


    private void topOpionCustomAttrValues(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TopOptions,0,0);
        try {
            drawable = a.getDrawable(R.styleable.TopOptions_imageSrc);
        } finally {
            a.recycle();
        }
    }

    private Bitmap loadFormAttr() {
        if(drawable != null) {
            int s = scalePercent(9);
            Bitmap b = ((BitmapDrawable)drawable).getBitmap();
            return scaleToFill(b,s,s);
        }

        return null;
    }

    private Bitmap scaleToFill(Bitmap b, int width, int height)
    {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
                (int) (b.getHeight() * factorToUse), true);
    }

    private int scalePercent(int perentage) {

        if(ScreenUtility.isInLandscape()) {
            return (ScreenUtility.getHeightInPx()*perentage)/100;
        }

        return (ScreenUtility.getWidthInPx()*perentage)/100;
    }


    public void topOpensMarginStart(FrameLayout.LayoutParams lp) {
        lp.setMarginEnd(scalePercent(16));
    }



}
