package com.wishhard.h24;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.wishhard.h24.frag.Wheeler;
import com.wishhard.h24.shared_pref_util.TimeSharedPref;
import com.wishhard.h24.utils.ScreenUtility;
import com.wishhard.h24.view.StopWatch;
import com.wishhard.h24.view.TheFrame;

public class WatchActivity extends AppCompatActivity implements HeadlessFrag.TaskStatusCallback, TheFrame.ClickEvent
, Wheeler.WheelerFragmentListener{

    private static final String TIME_KEY = "24_time_key";

    private static final String[] TIME_INSTENT_STATE_KEY = new String[] {
            "hor_kry","min_key","sec_key"
    };

    private static final String[] REMAINING_TIME_PRAF = new String[]{"com.wishhard.remainder_h","com.wishhard.remainder_m",
            "com.wishhard.remainder_s"};
    public static final String TIME_MIN_LIMIT = "00:00:00";
    private static final String TIME_MAX_LIMIT = "24:00:00";


    private StopWatch stopWatch;
     private HeadlessFrag frag;

     private TheFrame theFrame;

     private SharedPreferences defaultSharedPreferences;


    private InterstitialAd mInterstitialAd;

     View mContent;

     TimeSharedPref timeSharedPref;

      private String h = Wheeler.DEFUALT_VALUE,m = Wheeler.DEFUALT_VALUE,s = Wheeler.DEFUALT_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        noWheelerOnCreate();

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ScreenUtility.initContext(this);

        timeSharedPref = TimeSharedPref.getInstance(this);
        setHMSFromPrafs();

        theFrame = findViewById(R.id.mainFrame);
        theFrame.initViews();
        theFrame.setOnClickEventListener(this);

        mContent = findViewById(com.wishhard.h24.R.id.act);


        stopWatch = findViewById(R.id.sw_tv);
        stopWatch.setText(setTimeStr(h,m,s));


           if(savedInstanceState != null) {
               String strTime = savedInstanceState.getString(TIME_KEY);

               h = savedInstanceState.getString(TIME_INSTENT_STATE_KEY[0]);
               m = savedInstanceState.getString(TIME_INSTENT_STATE_KEY[1]);
               s = savedInstanceState.getString(TIME_INSTENT_STATE_KEY[2]);


               if(strTime.equals("")) {
                   stopWatch.setText(setTimeStr(h,m,strTime));
               } else {
                   stopWatch.setText(strTime);
               }
           }


        FragmentManager mgr = getSupportFragmentManager();
        frag = (HeadlessFrag) mgr.findFragmentByTag(HeadlessFrag.HEADLESS_FRAG_TAG);

        if(frag == null) {
            frag = new HeadlessFrag();
            mgr.beginTransaction().add(frag,HeadlessFrag.HEADLESS_FRAG_TAG).commit();
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3133582463179859/3445815489");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                startActivity(new Intent(WatchActivity.this,SettingsActivity.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noWheelerOnCreate();
                theFrame.makeTheFrameVisibleWithAnim();
                if(theFrame.isPlaying()) {
                    theFrame.setPlaying(false);
                    if(frag != null) {
                        frag.cancelBackgroundTask();
                    }
                    theFrame.enablePlayAndReset(isCountUp(),stopWatch.getText().toString());
                }
            }
        });

    }



    @Override
    protected void onUserLeaveHint() {
        saveRemainingTimeToPraf();
        noWheelerOnCreate();
        if(frag != null) {
            frag.cancelBackgroundTask();
        }
        theFrame.setPlaying(false);
        super.onUserLeaveHint();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TIME_KEY,stopWatch.getText().toString());

        outState.putString(TIME_INSTENT_STATE_KEY[0],h);
        outState.putString(TIME_INSTENT_STATE_KEY[1],m);
        outState.putString(TIME_INSTENT_STATE_KEY[2],s);
    }

    @Override
    public void onBackPressed() {
        saveRemainingTimeToPraf();
        if(frag != null) {
            frag.cancelBackgroundTask();
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theFrame.enablePlayAndReset(isCountUp(),stopWatch.getText().toString());
        theFrame.upDateViewSavedState();
        screenLight(theFrame.isPlaying());

    }

    @Override
    protected void onPause() {
       doWhenScreenIsOff();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
              doWhenPowerLongPressed();
              return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onProgressUpdate(String[] t) {
        h = t[0];
        m = t[1];
        s = t[2];

        stopWatch.setText(setTimeStr(h,m,s));
    }

    @Override
    public void onPostExecute() {

          if(frag != null) {
              frag.upDateExecutingStatus(false);
          }

              saveRemainingTimeToPraf(Wheeler.DEFUALT_VALUE,Wheeler.DEFUALT_VALUE,Wheeler.DEFUALT_VALUE);
          theFrame.setPlaying(false);
          theFrame.enablePlayAndReset(isCountUp(),setTimeStr(h,m,s));
          screenLight(theFrame.isPlaying());
    }


    @Override
    public void setings() {
            if(mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                startActivity(new Intent(this,SettingsActivity.class));
            }
            theFrame.setVisibility(View.GONE);
            theFrame.setVisibilityGone(true);
    }

    @Override
    public void setTimer() {
                Wheeler wheelerFrag = new Wheeler();
                theFrame.makeTheFrameVisibleWithAnim();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).add(com.wishhard.h24.R.id.wheeler, wheelerFrag).commit();

    }

    @Override
    public void resetTimer() {
        resetShopWatch();
    }

    @Override
    public void playAndPase(boolean isPlaying) {
        screenLight(isPlaying);
        theFrame.enableReset(isPlaying);
        startAndShopWatch(isPlaying);
        theFrame.makeTheFrameVisibleWithAnim();
    }

    public void onCanncelDiagolOressed() {
        Wheeler wheelerFrag = (Wheeler) getSupportFragmentManager().findFragmentById(com.wishhard.h24.R.id.wheeler);
        getSupportFragmentManager().beginTransaction().remove(wheelerFrag).commit();
    }


    private void noWheelerOnCreate() {
        Wheeler wheelerFrag = (Wheeler) getSupportFragmentManager().findFragmentById(com.wishhard.h24.R.id.wheeler);
        if(wheelerFrag != null) {
            getSupportFragmentManager().beginTransaction().remove(wheelerFrag).commit();
        }
    }


    private void startAndShopWatch(boolean b) {
        if(b) {
            if(frag != null) {
                frag.startBackgroundTask(h,m,s);
            }

        } else {
            if(frag != null) {
                frag.cancelBackgroundTask();
            }
        }
    }

    private String setTimeStr(String h,String m,String s) {
        return String.format("%s:%s:%s",h,m,s);
    }


    private void saveRemainingTimeToPraf() {
        if(!stopWatch.getText().toString().equals("")) {
            timeSharedPref.setValue(REMAINING_TIME_PRAF[0],h);
            timeSharedPref.setValue(REMAINING_TIME_PRAF[1],m);
            timeSharedPref.setValue(REMAINING_TIME_PRAF[2],s);
        }

    }

    private void saveRemainingTimeToPraf(String ho,String mi,String se) {
            timeSharedPref.setValue(REMAINING_TIME_PRAF[0],ho);
            timeSharedPref.setValue(REMAINING_TIME_PRAF[1],mi);
            timeSharedPref.setValue(REMAINING_TIME_PRAF[2],se);
    }

    private String[] getRemainingTimeForPraf() {
        String[] s = new String[3];
        s[0] = timeSharedPref.getStringValue(REMAINING_TIME_PRAF[0],Wheeler.DEFUALT_VALUE);
        s[1] = timeSharedPref.getStringValue(REMAINING_TIME_PRAF[1],Wheeler.DEFUALT_VALUE);
        s[2] = timeSharedPref.getStringValue(REMAINING_TIME_PRAF[2],Wheeler.DEFUALT_VALUE);

        return s;
    }

    public boolean isCountUp() {
        return defaultSharedPreferences.getBoolean(SettingsActivity.COUNTUP_PREF_KEY,true);
    }

    private boolean isKeepScreenOn() {
        return defaultSharedPreferences.getBoolean(SettingsActivity.KEEP_SCREEN_ON,false);
    }

    public boolean isSoundEffeect() {
        return defaultSharedPreferences.getBoolean(SettingsActivity.SOUND_EFFECT_KEY,true);
    }

    private void doWhenScreenIsOff() {
        if(theFrame.isPlaying() && !ScreenUtility.isScreenOn()) {
            if(frag != null) {
                frag.cancelBackgroundTask();
            }
            theFrame.setPlaying(false);
        }
    }

    private void doWhenPowerLongPressed() {
        saveRemainingTimeToPraf();
        if(frag != null) {
            frag.cancelBackgroundTask();
        }
        theFrame.setPlaying(false);
        theFrame.enablePlayAndReset(isCountUp(),setTimeStr(h,m,s));
    }

    private void screenLight(boolean isPlaying) {
        if(isKeepScreenOn()) {
            if(isPlaying) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }

    private String[] getWheelerPrafValue() {
        String[] s = new String[3];
        s[0] = timeSharedPref.getStringValue(Wheeler.PREF_KEYS_FOR_TIME[0],Wheeler.DEFUALT_VALUE);
        s[1] = timeSharedPref.getStringValue(Wheeler.PREF_KEYS_FOR_TIME[1],Wheeler.DEFUALT_VALUE);
        s[2] = timeSharedPref.getStringValue(Wheeler.PREF_KEYS_FOR_TIME[2],Wheeler.DEFUALT_VALUE);
        return s;
    }

    private void setHMSFromPrafs() {
        String[] remainingTime = getRemainingTimeForPraf();

        h = remainingTime[0];
        m = remainingTime[1];
        s = remainingTime[2];
    }

    private void resetShopWatch() {
        String[] forWheeler = getWheelerPrafValue();

        h = forWheeler[0];
        m = forWheeler[1];
        s = forWheeler[2];

        stopWatch.setText(setTimeStr(h,m,s));

        if(!isCountUp() && stopWatch.getText().equals(TIME_MIN_LIMIT)) {
            new StyleableToast.Builder(this).text("Can't countdown from " +
                    TIME_MIN_LIMIT + ". CountUp is unchecked!").textColor(Color.BLACK).
                    backgroundColor(Color.YELLOW).show();
        } else if(isCountUp() && stopWatch.getText().equals(TIME_MAX_LIMIT)) {
            new StyleableToast.Builder(this).text("Can't count up from " +
                    TIME_MAX_LIMIT + ". CountUp is checked!").textColor(Color.BLACK).
                    backgroundColor(Color.YELLOW).show();
        }

        theFrame.enablePlayAndReset(isCountUp(),setTimeStr(h,m,s));
    }


    @Override
    public void timeStringValues(String hourStr, String minStr, String secStr) {
              saveRemainingTimeToPraf(Wheeler.DEFUALT_VALUE,Wheeler.DEFUALT_VALUE,Wheeler.DEFUALT_VALUE);
              h = hourStr;
              m = minStr;
              s = secStr;
              stopWatch.setText(setTimeStr(h,m,s));
              theFrame.enablePlayAndReset(isCountUp(),setTimeStr(h,m,s));


        Wheeler wheelerFrag = (Wheeler) getSupportFragmentManager().findFragmentById(com.wishhard.h24.R.id.wheeler);
        getSupportFragmentManager().beginTransaction().remove(wheelerFrag).commit();

    }


}
