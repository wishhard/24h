package com.wishhard.h24;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.wishhard.h24.settings.CustomSwitchPref;



public class SettingsActivity extends AppCompatActivity {
    public static final String COUNTUP_PREF_KEY = "com.wishhard.sw24_countup";
    public static final String KEEP_SCREEN_ON = "com.wishhard.sw24_keep_on";
    public static final String SOUND_EFFECT_KEY = "com.wishhard.sw24_sound_effect";

    private ActionBar actionBar;
    private ColorDrawable actionBarColor = new ColorDrawable(Color.parseColor("#696969"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBarSettings();


        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,new Text()).commit();



    }

    private void actionBarSettings() {
        actionBar.setTitle("Setting");
        actionBar.setBackgroundDrawable(actionBarColor);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_up_action);
        actionBar.setHomeButtonEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static class Text extends PreferenceFragmentCompat {
         private CustomSwitchPref countUpPref,keepScreenLightOnPref,soundEffectPref;
         private ContextThemeWrapper contextThemeWrapper;

        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            Context activityContext = getActivity();

            PreferenceScreen prefScreen = getPreferenceManager().createPreferenceScreen(activityContext);
            setPreferenceScreen(prefScreen);

            TypedValue themeTypedValue = new TypedValue();

            activityContext.getTheme().resolveAttribute(R.attr.preferenceTheme, themeTypedValue, true);


            contextThemeWrapper = new ContextThemeWrapper(activityContext, themeTypedValue.resourceId);

            countUpPref = new CustomSwitchPref(contextThemeWrapper);
            countUpPref.setKey(COUNTUP_PREF_KEY);
            countUpPref.setTitle(R.string.cu_pref_title);
            countUpPref.setDefaultValue(true);
            countUpPref.setLayoutResource(com.wishhard.h24.R.layout.pref_layout);


            keepScreenLightOnPref = new CustomSwitchPref(contextThemeWrapper);
            keepScreenLightOnPref.setKey(KEEP_SCREEN_ON);
            keepScreenLightOnPref.setTitle(R.string.kso_pref_title);
            keepScreenLightOnPref.setDefaultValue(false);
            keepScreenLightOnPref.setLayoutResource(com.wishhard.h24.R.layout.pref_layout);

            soundEffectPref = new CustomSwitchPref(contextThemeWrapper);
            soundEffectPref.setKey(SOUND_EFFECT_KEY);
            soundEffectPref.setTitle(R.string.se_pref_title);
            soundEffectPref.setDefaultValue(true);

            soundEffectPref.setLayoutResource(com.wishhard.h24.R.layout.pref_layout);


            prefScreen.addPreference(countUpPref);
            countUpPref.setSummary(prefSummary(COUNTUP_PREF_KEY,R.string.cu_checked_summary,R.string.cu_unchecked_summary));

            prefScreen.addPreference(keepScreenLightOnPref);
            keepScreenLightOnPref.setSummary(prefSummary(KEEP_SCREEN_ON,R.string.kso_checked_summary,R.string.kso_unchecked_summary));

            prefScreen.addPreference(soundEffectPref);
            soundEffectPref.setSummary(prefSummary(SOUND_EFFECT_KEY,R.string.se_checked_summary,R.string.se_unchecked_summary));

        }


        @Override
        public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            RecyclerView r = (RecyclerView) inflater.inflate(com.wishhard.h24.R.layout.pref_recycler_view_layout,parent,false);
            r.setLayoutManager(onCreateLayoutManager());
            return r;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            setDivider(getResources().getDrawable(com.wishhard.h24.R.drawable.the_divider));
        }


        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);

            countUpPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    qUpdate(COUNTUP_PREF_KEY,newValue);
                    preference.setSummary(prefSummary(COUNTUP_PREF_KEY,R.string.cu_checked_summary,R.string.cu_unchecked_summary));
                    return true;
                }
            });



            keepScreenLightOnPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    qUpdate(KEEP_SCREEN_ON,newValue);
                    preference.setSummary(prefSummary(KEEP_SCREEN_ON,R.string.kso_checked_summary,R.string.kso_unchecked_summary));
                    return true;
                }
            });

            soundEffectPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    qUpdate(SOUND_EFFECT_KEY,newValue);
                    preference.setSummary(prefSummary(SOUND_EFFECT_KEY,R.string.se_checked_summary,R.string.se_unchecked_summary));
                    return true;
                }
            });

        }


        private int prefSummary(String key,int checkedSummaryRes,int unCheckedSummaryRes) {

            Preference p = findPreference(key);
            if(p.getSharedPreferences().getBoolean(key,false)) {
                return checkedSummaryRes;
            }

            return unCheckedSummaryRes;

        }

        private void qUpdate(String key,Object nv) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, (Boolean) nv);
            editor.commit();
        }

    }

}
