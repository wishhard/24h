package com.wishhard.h24.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.wishhard.h24.R;
import com.wishhard.h24.utils.ScreenUtility;


public class CenterOptions extends AppCompatImageButton {

    Drawable state1,state2,state3 = null;
    Bitmap bitmap1, bitmap2,bitmap3 = null;

    boolean hasStateList;



    public CenterOptions(Context context) {
        super(context);
        init(context);
    }

    public CenterOptions(Context context, AttributeSet attrs) {
        super(context, attrs);
        centerOptions(attrs);
        init(context);
    }

    private void init(Context context) {
        setPadding(0,0,0,0);
        ScreenUtility.initContext(context);
        setBackgroundColor(Color.TRANSPARENT);


        bitmap2 = loadFormAttr(state2);
        bitmap1 = loadFormAttr(state1);
        bitmap3 = loadFormAttr(state3);
        if(bitmap1 != null && !hasStateList) {
            setImageBitmap(bitmap1);
        } else {
            setStateListDrawable();
        }


    }

    private void centerOptions(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CenterOptions,0,0);

        try {
            state1 = a.getDrawable(R.styleable.CenterOptions_imageState1_src);
            state2 = a.getDrawable(R.styleable.CenterOptions_imageState2_src);
            state3 = a.getDrawable(R.styleable.CenterOptions_imageState3_src);
            hasStateList = a.getBoolean(R.styleable.CenterOptions_hasState_list,false);
        } finally {
            a.recycle();
        }
    }

    private Bitmap scaleToFill(Bitmap b, int width, int height)
    {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
                (int) (b.getHeight() * factorToUse), true);
    }

    private Bitmap loadFormAttr(Drawable drawable) {
        if(drawable != null) {
            int s = scalePercent(20);
            Bitmap b = ((BitmapDrawable)drawable).getBitmap();
            return scaleToFill(b,s,s);
        }

        return null;
    }

    private int scalePercent(int perentage) {

        if(ScreenUtility.isInLandscape()) {
            return (ScreenUtility.getHeightInPx()*perentage)/100;
        }

        return (ScreenUtility.getWidthInPx()*perentage)/100;
    }


    public void changeImage(boolean isPlaying) {

            if(!isPlaying) {

                if(isEnabled()) {
                    setImageBitmap(bitmap1);
                } else {
                    setImageBitmap(bitmap3);
                }
            } else {
                setImageBitmap(bitmap2);
            }
    }




    private void setStateListDrawable() {
        state1 = new BitmapDrawable(getResources(),bitmap1);
        state2 = new BitmapDrawable(getResources(),bitmap2);
        state3 = new BitmapDrawable(getResources(),bitmap3);

        StateListDrawable listDrawable = new StateListDrawableBuilder().setEnableDrawable(state1)
                .setPressedDrawable(state2)
                .setDisabledDrawable(state3).build();

        setBackground(listDrawable);
    }



}
