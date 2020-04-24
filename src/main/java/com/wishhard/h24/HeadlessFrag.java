package com.wishhard.h24;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wishhard.h24.utils.SoundHelper;


public class HeadlessFrag extends Fragment {

    public static final String HEADLESS_FRAG_TAG = " com.wishhard.sw24.headless_tag";

    public static interface TaskStatusCallback {
        void onProgressUpdate(String[] t);
        void onPostExecute();

    }




    TaskStatusCallback mTaskStatusCallback;
    TimeAsync mTimeAsync;
    boolean isTaskExecuting = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        WatchActivity activity;
        activity = (WatchActivity) context;
        mTaskStatusCallback =  activity;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTaskStatusCallback = null;
    }



    private class TimeAsync extends AsyncTask<String,String[],Void> {

        @Override
        protected Void doInBackground(String... strings) {

            boolean countUp =  ((WatchActivity)getActivity()).isCountUp();
            int scends = timeToScends(strings[0],strings[1],strings[2]);

            SoundHelper soundHelper = (((WatchActivity) getActivity()).isSoundEffeect())? new SoundHelper(getActivity()):null;


            if(countUp) {
                while (scends <= 86400 && !isCancelled()) {

                    publishProgress(timetoString(scends));
                    if(soundHelper != null) {
                        soundHelper.playSound(scends);
                    }
                    scends++;

                    if(scends == 86401) {
                        break;
                    }
                    try {

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {


                while (scends >= 0 && !isCancelled()) {

                    publishProgress(timetoString(scends));
                    if(soundHelper != null) {
                        soundHelper.playSound(scends);
                    }
                    scends--;
                    if(scends == -1) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(soundHelper != null) {
                soundHelper.releaseSoundPool();
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(String[]... values) {
            if(mTaskStatusCallback != null) {
                mTaskStatusCallback.onProgressUpdate(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(mTaskStatusCallback != null) {
                mTaskStatusCallback.onPostExecute();
            }
        }
    }


    private int timeToScends(String hours, String minutes, String sce) {
        int h = Integer.parseInt(hours);
        int m = Integer.parseInt(minutes);
        int s = Integer.parseInt(sce);

        return ((h*60)*60)+(m*60)+s;
    }

    private String[] timetoString(int scends) {
        int h = scends/3600;
        int m = (scends%3600)/60;
        int s = scends%60;

        String[] t = new String[3];
        t[0] = (h >= 0 && h <= 9)? "0"+h: String.valueOf(h);
        t[1] =  (m >= 0 && m <= 9)? "0"+m:String.valueOf(m);
        t[2] = (s >= 0 && s <= 9)? "0"+s:String.valueOf(s);

        return t;
    }


    public void startBackgroundTask(String h,String m,String s) {
        if (!isTaskExecuting) {

            mTimeAsync = new TimeAsync();
            mTimeAsync.execute(h,m,s);
            isTaskExecuting = true;
        }
    }

    public void cancelBackgroundTask() {
        if (isTaskExecuting) {
            mTimeAsync.cancel(true);
            isTaskExecuting = false;
        }
    }

    public void upDateExecutingStatus(boolean isExecuting) {
        this.isTaskExecuting = isExecuting;
    }

}
