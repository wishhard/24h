package com.wishhard.h24.view;



import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.wishhard.h24.utils.ScreenUtility;

public class StopWatch extends AppCompatTextView {


    public StopWatch(Context context) {
        super(context);
        init(context);
    }

    public StopWatch(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }



    private void init(Context context) {
        ScreenUtility.initContext(context);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/phoneTimeDate.ttf");
        setTypeface(tf);
        setPadding(0,0,0,0);
        setIncludeFontPadding(false);

        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextColor(Color.YELLOW);
        stopWatchTextSize();


    }




    private int textSizeScalePercent(float landScapePercentage,float portraitPercentage)  {
        float p;
        if(ScreenUtility.isInLandscape()) {
            p = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    (float) ((ScreenUtility.getHeightInPx()*landScapePercentage)/100.0) + 0.5f,ScreenUtility.getResDisplayMetrics());
        } else {

            p = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    (float) ((ScreenUtility.getWidthInPx() * portraitPercentage) / 100.0) + 0.5f, ScreenUtility.getResDisplayMetrics());
        }

        return (int) p;
    }


    private void stopWatchTextSize() {
        if(ScreenUtility.isNormalScreen_And_hdpi()) {
            setTextSize(textSizeScalePercent(17.5f,11f));

        } else if(ScreenUtility.isNormalScreen_And_xhdpi() || ScreenUtility.isLargeScreen_And_xhdpi()) {
            setTextSize(textSizeScalePercent(10f,6f));

        } else if(ScreenUtility.isNormalScreen_And_xxhdpi()) {
            setTextSize(textSizeScalePercent(4.6f,2.7f));

        } else if(ScreenUtility.isNormalScreen_And_420dpi()) {
            setTextSize(textSizeScalePercent(5.5f,3.5f));

        } else if(ScreenUtility.isNormalScreen_And_560dpi()) {
            setTextSize(textSizeScalePercent(3.2f,2f));

        } else if(ScreenUtility.isLargeScreen_And_tvDpi()) {
            setTextSize(textSizeScalePercent(20f,14.5f));

        } else if(ScreenUtility.isXLargeScreen_And_xhdpi()) {
            setTextSize(textSizeScalePercent(8f,6f));

        } else if(ScreenUtility.screen_Dpi().equals(ScreenUtility.MDPI)) {
            if(ScreenUtility.isWidthOrHeight_1280() || ScreenUtility.isWidthOrHeight_1024() || ScreenUtility.isWidthOrHeight_480()) {
                setTextSize(textSizeScalePercent(36f,24f));
            }

        }
    }




}
