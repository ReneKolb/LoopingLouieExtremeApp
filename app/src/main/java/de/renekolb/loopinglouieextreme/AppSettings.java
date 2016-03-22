package de.renekolb.loopinglouieextreme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    private static final String PREFS_NAME = "LoopingLouieSharedPreferences";

    private static final String KEY_ENABLE_DEBUG = "EnableDebug";
    private static final String KEY_DISABLE_BT_ON_EXIT = "DisableBTonExit";

    //private final Activity activity;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public AppSettings(Activity activity) {
        //this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public boolean isDebugEnabled() {
        return sharedPreferences.getBoolean(KEY_ENABLE_DEBUG, false);
    }

    public void setDebugEnable(boolean enable) {
        editor.putBoolean(KEY_ENABLE_DEBUG, enable);
        editor.commit();
    }

    public boolean getDisableBTonExit() {
        return sharedPreferences.getBoolean(KEY_DISABLE_BT_ON_EXIT, false);
    }

    public void setDisableBTonExit(boolean disable) {
        editor.putBoolean(KEY_DISABLE_BT_ON_EXIT, disable);
        editor.commit();
    }

}
