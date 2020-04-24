package com.wishhard.h24.view;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class StateListDrawableBuilder {

    private static final int[] STATE_PRESSED = new int[]{android.R.attr.state_pressed};
    private static final int[] STATE_DISABLED = new int[]{-android.R.attr.state_enabled};
    private static final int[] STATE_ENABLED = new int[]{android.R.attr.state_enabled};


    private Drawable pressedDrawable;
    private Drawable enabledDrawable;
    private Drawable disabledDrawable;


    public StateListDrawableBuilder setPressedDrawable(Drawable pressedDrawable) {
        this.pressedDrawable = pressedDrawable;
        return this;
    }

    public StateListDrawableBuilder setDisabledDrawable(Drawable disabledDrawable) {
        this.disabledDrawable = disabledDrawable;
        return this;
    }

    public StateListDrawableBuilder setEnableDrawable(Drawable enabledDrawable) {
        this.enabledDrawable = enabledDrawable;
        return this;
    }

    public StateListDrawable build() {
        StateListDrawable listDrawable = new StateListDrawable();

        if(this.pressedDrawable != null) {
            listDrawable.addState(STATE_PRESSED,pressedDrawable);
        }

        if(this.disabledDrawable != null) {
            listDrawable.addState(STATE_DISABLED,disabledDrawable);
        }

        if(this.enabledDrawable != null) {
            listDrawable.addState(STATE_ENABLED,enabledDrawable);
        }

        return listDrawable;

    }



}
