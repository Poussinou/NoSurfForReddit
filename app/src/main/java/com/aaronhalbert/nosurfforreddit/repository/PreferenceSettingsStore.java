package com.aaronhalbert.nosurfforreddit.repository;

import android.content.SharedPreferences;

import com.aaronhalbert.nosurfforreddit.activities.MainActivity;

public class PreferenceSettingsStore implements SettingsStore {
    private static final String KEY_NIGHT_MODE = "nightMode";
    private static final String KEY_AMOLED_NIGHT_MODE = "amoledNightMode";
    private static final String KEY_EXTERNAL_BROWSER = "externalBrowser";
    private static final String KEY_DEFAULT_PAGE_IS_SUBSCRIBED = "defaultPageIsSubscribed";

    private final SharedPreferences preferences;

    public PreferenceSettingsStore(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean isNightMode() {
        return preferences.getBoolean(KEY_NIGHT_MODE, true);
    }

    @Override
    public boolean isAmoledNightMode() {
        return preferences.getBoolean(KEY_AMOLED_NIGHT_MODE, false);
    }

    @Override
    public boolean isUseExternalBrowser() {
        return preferences.getBoolean(KEY_EXTERNAL_BROWSER, false);
    }

    @Override
    public boolean isDefaultPageSubscribed() {
        return preferences.getBoolean(KEY_DEFAULT_PAGE_IS_SUBSCRIBED, false);
    }

    @Override
    public void registerListener(MainActivity mainActivity) {
        preferences.registerOnSharedPreferenceChangeListener(mainActivity);
    }

    @Override
    public void unregisterListener(MainActivity mainActivity) {
        preferences.unregisterOnSharedPreferenceChangeListener(mainActivity);
    }
}
