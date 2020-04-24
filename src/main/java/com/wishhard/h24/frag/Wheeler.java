package com.wishhard.h24.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishhard.h24.R;
import com.wishhard.h24.WatchActivity;
import com.wishhard.h24.shared_pref_util.TimeSharedPref;
import com.wishhard.h24.wheel.TheWheel;
import com.wishhard.h24.wheel.WheelContianer;
import com.wishhard.h24.wheel.WheelerFargBtn;

import java.util.Arrays;

public class Wheeler extends Fragment {
    public static final String DEFUALT_VALUE = "00";
    private static final String SEC_30 = "30";

    public static final String[] PREF_KEYS_FOR_TIME = new String[]{"com.wishhard.sw24.frag_hour","com.wishhard.sw24.frag_minute",
    "com.wishhard.sw24.frag_seconds"};


    private static final String[] HOURS = new String[]{"00","01","02","03","04","05","06","07","08","09","10"
    ,"11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

    private static final String[] MINUTES_AND_SECONDS = new String[]{"00","01","02","03","04","05","06","07","08","09","10"
            ,"11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"
    ,"31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"
    ,"51","52","53","54","55","56","57","58","59"};

    private WheelContianer hour,min,sec;

    WheelerFargBtn setBtn,cancelBtn;

     private String hourStr,minStr,secStr;

     private TimeSharedPref timeSharedPref;

     private WheelerFragmentListener mWheelerFragmentListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wheeler_layout,container,false);

        timeSharedPref = TimeSharedPref.getInstance(getActivity());
        assignPrefValuesToVars();

        hour = v.findViewById(R.id.hours);
        hour.setItemsForWheel(Arrays.asList(HOURS));
        hour.setSelection(getPos(HOURS,hourStr));

        min = v.findViewById(R.id.minutes);
        min.setItemsForWheel(Arrays.asList(MINUTES_AND_SECONDS));
        min.setSelection(getPos(MINUTES_AND_SECONDS,minStr));

        sec = v.findViewById(R.id.seconds);
        sec.setItemsForWheel(Arrays.asList(MINUTES_AND_SECONDS));
        sec.setSelection(getPos(MINUTES_AND_SECONDS,secStr));

        hour.setOnWheelListener(new TheWheel.OnWheelListener() {

            @Override
            public void onSelected(String item) {

                if(item.equals("24")) {
                    min.setSelection(getPos(MINUTES_AND_SECONDS,"00"));
                    min.setAllowMotionEvent(false);
                    sec.setSelection(getPos(MINUTES_AND_SECONDS,"00"));
                    sec.setAllowMotionEvent(false);
                } else {
                    min.setAllowMotionEvent(true);
                    sec.setAllowMotionEvent(true);
                }
            }
        });

        setBtn = v.findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 saveValuesFromWeelToPref();
                 done();
            }
        });

        cancelBtn = v.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WatchActivity)getActivity()).onCanncelDiagolOressed();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof WheelerFragmentListener)) throw new AssertionError();
        mWheelerFragmentListener = (WheelerFragmentListener) context;
    }

    private void saveValuesFromWeelToPref() {
        timeSharedPref.setValue(PREF_KEYS_FOR_TIME[0],hour.getSeletedItem());
        timeSharedPref.setValue(PREF_KEYS_FOR_TIME[1],min.getSeletedItem());
        timeSharedPref.setValue(PREF_KEYS_FOR_TIME[2],sec.getSeletedItem());
    }

    private void assignPrefValuesToVars() {
        hourStr = timeSharedPref.getStringValue(PREF_KEYS_FOR_TIME[0],DEFUALT_VALUE);
        minStr = timeSharedPref.getStringValue(PREF_KEYS_FOR_TIME[1],DEFUALT_VALUE);
        secStr = timeSharedPref.getStringValue(PREF_KEYS_FOR_TIME[2],DEFUALT_VALUE);


    }

    private int getPos(String[] arr,String value) {
        return Arrays.asList(arr).indexOf(value);
    }

    public interface WheelerFragmentListener {
        void timeStringValues(String hourStr,String minStr,String secString);

    }

    private void done() {
        if(mWheelerFragmentListener == null) {
            throw new AssertionError();
        }

        mWheelerFragmentListener.timeStringValues(hour.getSeletedItem(),min.getSeletedItem(),sec.getSeletedItem());
    }




}
