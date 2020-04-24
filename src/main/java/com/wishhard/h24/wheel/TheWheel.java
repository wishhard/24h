package com.wishhard.h24.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.wishhard.h24.utils.ScreenUtility;

import java.util.ArrayList;
import java.util.List;

public class TheWheel extends ScrollView {

    private static final int OFF_SET_DEFAULT =1;

    public static class OnWheelListener {
        public void onSelected(String item) {

        }
    }

    private boolean allowMotionEvent = true;

    private OnWheelListener onWheelListener;

    private Context mContext;

    private Paint mPaint;

    private Runnable mRunnable;

    private LinearLayout mContianer;

    private List<String> items;

    private int offSet = OFF_SET_DEFAULT;

    private int displayItemViewCount;

    private int itemHeight = 0;
    private int viewWidth = 0;

    int selectedIndex = 1;

    int newCheck = 30;

    private int initialY;

    private int[] selectedAreaBorder;

    public TheWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TheWheel(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext =  context;
        ScreenUtility.initContext(mContext);

        setVerticalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

            mContianer = new LinearLayout(mContext);
            mContianer.setOrientation(LinearLayout.VERTICAL);
            addView(mContianer);

            mRunnable = new Runnable() {
                @Override
                public void run() {
                    int newY = getScrollY();

                    if(initialY - newY == 0) {
                        final int remainder = initialY % itemHeight;
                        final int divided = initialY / itemHeight;

                            if(remainder == 0) {
                                selectedIndex = divided + offSet;

                                onSelectedCallBlack();
                            } else {
                                    if(remainder > itemHeight / 2) {
                                        TheWheel.this.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                TheWheel.this.smoothScrollTo(0,initialY - remainder + itemHeight);
                                                selectedIndex = divided + offSet + 1;

                                                onSelectedCallBlack();
                                            }
                                        });

                                    } else {
                                        TheWheel.this.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                TheWheel.this.smoothScrollTo(0,initialY - remainder);
                                                selectedIndex = divided + offSet;

                                                onSelectedCallBlack();
                                            }

                                        });
                                    }
                            }

                    } else {
                        initialY = getScrollY();
                        TheWheel.this.postDelayed(mRunnable,newCheck);
                    }
                }
            };


    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        refreshItemView(t);
    }

    @Override
    public void setBackground(Drawable background) {
        if(viewWidth == 0) {
            viewWidth = this.getWidth();
        }

        if(mPaint == null) {
            mPaint = new Paint();
        }

        background = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                mPaint.setColor(Color.BLACK);
                mPaint.setStrokeWidth(dp2pix(1f));
                canvas.drawLine(0  ,obtainSelectedAreaBorder()[0],viewWidth,
                        obtainSelectedAreaBorder()[0],mPaint);


                Shader shader = new LinearGradient(0, 94, 0, 0, Color.WHITE, Color.BLACK,
                        Shader.TileMode.MIRROR);

                mPaint.setAlpha(50);
                mPaint.setShader(shader);
                mPaint.setStrokeWidth(0f);
                canvas.drawRect(0,obtainSelectedAreaBorder()[0],viewWidth,obtainSelectedAreaBorder()[1],mPaint);

                mPaint.setColor(Color.BLACK);
                mPaint.setStrokeWidth(dp2pix(1f));
                canvas.drawLine(0  ,obtainSelectedAreaBorder()[1],viewWidth, obtainSelectedAreaBorder()[1],mPaint);


            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };

        super.setBackground(background);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        setBackground(null);


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
          if(allowMotionEvent) {
              if (ev.getAction() == MotionEvent.ACTION_UP) {
                  startTaxt();
              }
          } else {
              return false;
          }

        return super.onTouchEvent(ev);
    }

    protected void setItems(List<String> list) {
        if(items == null) {
            items = new ArrayList<>();
        }
        items.clear();
        items.addAll(list);


        for(int i = 0; i < offSet;i++) {
            items.add(0,"");
            items.add("");
        }

        initializeData();
    }

    protected void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }



    private void initializeData() {
        displayItemViewCount = offSet * 2 + 1;   //3 items in this case

        for (String item: items) {
                mContianer.addView(createTv(item));
        }

        refreshItemView(0);

    }

    private void startTaxt() {
        initialY = getScrollY();
        this.postDelayed(mRunnable,newCheck);
    }

    private TextView createTv(String item) {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/phoneTimeDate.ttf");

        TextView tv = new TextView(mContext);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        tv.setIncludeFontPadding(false);
        tv.setTypeface(tf);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dp2pix(10f), dp2pix(12f), dp2pix(10f), dp2pix(5f));

        if(itemHeight == 0) {
            itemHeight = getMeasuredViewHeight(tv);
            mContianer.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    itemHeight * displayItemViewCount));

            this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    itemHeight * displayItemViewCount));
        }

        return tv;
    }


    private void refreshItemView(int y){
        int pos = y / itemHeight + offSet;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if(remainder == 0) {
            pos = divided + offSet;
        } else {
            if(remainder > itemHeight / 2) {
                pos = divided + offSet + 1;
            }
        }

        int childSize = mContianer.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView tv = (TextView) mContianer.getChildAt(i);

            if(tv == null) {
                return;
            }
            if(pos == i) {
                tv.setTextColor(Color.BLACK);
            } else {
                tv.setTextColor(Color.parseColor("#999999"));
            }
        }
    }

    private int[] obtainSelectedAreaBorder() {
        if(selectedAreaBorder == null) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offSet;
            selectedAreaBorder[1] = itemHeight * (offSet + 1);
        }

        return selectedAreaBorder;
    }

    private void onSelectedCallBlack() {
        if(onWheelListener != null) {
            onWheelListener.onSelected(items.get(selectedIndex));
        }
    }

    protected void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offSet;
        this.post(new Runnable() {
            @Override
            public void run() {
                TheWheel.this.smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    protected void wanaAllowMotionEvent(boolean allowMotionEvent) {
        this.allowMotionEvent = allowMotionEvent;
    }

    protected String getSeletedItem() {
        return items.get(selectedIndex);
    }

    private int dp2pix(float value) {
        return (int) (value * ScreenUtility.getDensity() + 0.5f);
    }

    private int getMeasuredViewHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expand = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        v.measure(w,expand);
        return v.getMeasuredHeight();
    }

}
