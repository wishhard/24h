package com.wishhard.h24.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wishhard.h24.utils.ScreenUtility;

import java.util.ArrayList;
import java.util.List;


public class TheFrame extends FrameLayout  {
     private static final String TIME_MIN_LIMIT = "00:00:00";
     private static final String TIME_MAX_LIMIT = "24:00:00";

     private List<TopOptions> topOpions = new ArrayList<>();
     private List<CenterOptions> centerOptions = new ArrayList<>();

     private boolean isPlaying = false;
     private boolean isVisibilityGone = true;

     private ClickEvent listener;



    public TheFrame(@NonNull Context context) {
        super(context);
        init(context);


    }

    public TheFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {

        ScreenUtility.initContext(context);
        theFramePadding();
        setSaveEnabled(true);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTheFrameVisibleWithAnim();
            }
        });


    }




    private void theFramePadding() {

        int toDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,scalePercent(2),ScreenUtility.getResDisplayMetrics());
        if(ScreenUtility.isInLandscape()) {
            setPadding(toDp, toDp, toDp + toDp, toDp);
        } else {
            setPadding(toDp, toDp, toDp, toDp+toDp);
        }
    }


    private void initTopOpons() {

        try {
            for(int i = 0; i < ViewFinals.viewIdsForTopOptions().size(); i++ ) {
                TopOptions tot = findViewById(ViewFinals.viewIdsForTopOptions().get(i));
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.END| Gravity.TOP;

                if(i > 0) {
                    tot.topOpensMarginStart(lp);
                }

                tot.setLayoutParams(lp);
                topOpions.add(tot);
            }

            topOptionsClickable();
        } catch (Exception e) {

        }

    }

    private void initCenterOptions() {

        try {
            for(int i = 0; i < ViewFinals.viewIdsForCenterOptions().size();i++) {
                CenterOptions co = findViewById(ViewFinals.viewIdsForCenterOptions().get(i));
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity  = Gravity.CENTER;

                if(i == 1) {
                    lp.setMarginStart(scalePercent(16));
                } else {
                    lp.setMarginEnd(scalePercent(16));
                }

                co.setLayoutParams(lp);

                centerOptions.add(co);
            }
           centerOptionsClickable();
        } catch (Exception e) {

        }
    }

    private void topOptionsClickable() {
        topOpions.get(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.setings();
                }
            }
        });

        topOpions.get(1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.setTimer();
                }
            }
        });
    }



    private void centerOptionsClickable() {

        centerOptions.get(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                if(listener != null) {
                     listener.resetTimer();
                }
            }
        });

        centerOptions.get(1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (!isPlaying) {
                        isPlaying = true;
                    } else {
                        isPlaying = false;
                    }

                    centerOptions.get(1).changeImage(isPlaying);

                    if (listener != null) {
                        listener.playAndPase(isPlaying);
                    }

            }
        });
    }


    public void initViews() {
           initTopOpons();
           initCenterOptions();
    }



    public void setOnClickEventListener(ClickEvent listener) {
        this.listener = listener;
    }


    public interface ClickEvent {
        void setings();
        void setTimer();
        void resetTimer();
        void playAndPase(boolean isPlaying);
    }

    public void makeTheFrameVisibleWithAnim() {

        if(!this.isVisibilityGone()) {
            this.animate().alpha(0.0f)
                    .setDuration(400)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            TheFrame.this.setVisibility(GONE);
                        }
                    });

                    isVisibilityGone = true;
        } else {
            this.setVisibility(VISIBLE);
            this.animate().alpha(1f)
                    .setDuration(300)
                    .setListener(null);

            isVisibilityGone = false;
        }
    }


    public boolean isVisibilityGone() {
        return this.isVisibilityGone;
    }

    public void setVisibilityGone(boolean visibilityGone) {
        isVisibilityGone = visibilityGone;
    }


    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void upDateViewSavedState() {
        if(!isVisibilityGone) {
            setVisibility(VISIBLE);
        }

    }


    public void enablePlayAndReset(boolean countUp,String timeStr) {
        if(!countUp && timeStr.equals(TIME_MIN_LIMIT) || countUp && timeStr.equals(TIME_MAX_LIMIT)) {
            centerOptions.get(1).setEnabled(false);
        } else if(!timeStr.equals(TIME_MIN_LIMIT) || !timeStr.equals(TIME_MAX_LIMIT)) {
            centerOptions.get(1).setEnabled(true);
        }

        centerOptions.get(1).changeImage(isPlaying);

        enableReset(isPlaying);
    }


    public void enableReset(boolean playing) {
        centerOptions.get(0).setEnabled(!playing);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superSate = super.onSaveInstanceState();
        final FrameSavedState frameSavedState = new FrameSavedState(superSate);
        frameSavedState.isVisibilityGone = this.isVisibilityGone;
        frameSavedState.isPlaying = this.isPlaying;

        return frameSavedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final FrameSavedState frameSavedState = (FrameSavedState) state;
        super.onRestoreInstanceState(frameSavedState.getSuperState());
        isPlaying = frameSavedState.isPlaying;
        setVisibilityGone(frameSavedState.isVisibilityGone);
    }

    private int scalePercent(int perentage) {

        if(ScreenUtility.isInLandscape()) {
            return (ScreenUtility.getHeightInPx()*perentage)/100;
        }

        return (ScreenUtility.getWidthInPx()*perentage)/100;
    }


    private static class FrameSavedState extends BaseSavedState {
        boolean isPlaying;
        boolean isVisibilityGone;

        public static final Parcelable.Creator<FrameSavedState> CREATOR = new ClassLoaderCreator<FrameSavedState>() {
            @Override
            public FrameSavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new FrameSavedState(source);
            }

            @Override
            public FrameSavedState createFromParcel(Parcel source) {
                return new FrameSavedState(source);
            }

            @Override
            public FrameSavedState[] newArray(int size) {
                return new FrameSavedState[size];
            }
        };


        FrameSavedState(Parcelable superState) {
            super(superState);
        }

        private FrameSavedState(Parcel in) {
            super(in);
            isPlaying = in.readInt() == 0;
            isVisibilityGone = in.readInt() == 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isPlaying? 1 : 0);
            out.writeInt(isVisibilityGone? 1 : 0);
        }

    }

}
