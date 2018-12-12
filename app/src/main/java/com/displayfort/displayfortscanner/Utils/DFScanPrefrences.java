package com.displayfort.displayfortscanner.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Husain on 15-03-2016.
 */
public class DFScanPrefrences {


    private static final String SHARED_PREFERENCE_NAME = "DfScanSharedPreference";
    private static final String SHARED_PREFERENCE_NAME_TUTORIAL = "InfinitySharedPreferenceTutorial";

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesTutorial;

    public void setClearPrefrence() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public DFScanPrefrences(Context context) {
//        context = TrackerApplication.getInstance();
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.sharedPreferencesTutorial = context.getSharedPreferences(SHARED_PREFERENCE_NAME_TUTORIAL,
                Context.MODE_PRIVATE);
    }

    public void setUniqueIdSetIn(String uniqueId) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Set<String> set = getUniqueIdSetIn();
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(uniqueId);
        prefsEditor.putStringSet("uniqueIdIn", set);
        prefsEditor.commit();
    }

    public Set<String> getUniqueIdSetIn() {
        Set<String> set = sharedPreferences.getStringSet("uniqueIdIn", null);
        if (set == null) {
            set = new HashSet<>();
        }
        return set;
    }


    public void setUniqueIdSetOut(String uniqueId) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Set<String> set = getUniqueIdSetOut();
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(uniqueId);
        prefsEditor.putStringSet("uniqueIdOut", set);
        prefsEditor.commit();
    }

    public Set<String> getUniqueIdSetOut() {
        Set<String> set = sharedPreferences.getStringSet("uniqueIdOut", null);
        if (set == null) {
            set = new HashSet<>();
        }
        return set;
    }


}
