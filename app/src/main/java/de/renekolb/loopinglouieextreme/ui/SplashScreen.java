package de.renekolb.loopinglouieextreme.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.Random;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.PlayerProfiles.PlayerProfile;
import de.renekolb.loopinglouieextreme.PlayerProfiles.ProfileManager;
import de.renekolb.loopinglouieextreme.R;

public class SplashScreen extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;

    @SuppressLint("NewApi")
    private void loadSettings() {
        FullscreenActivity.BLE_SUPPORT = (Build.VERSION.SDK_INT >= 18) && getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState == null) {
            //only load once
            if (getResources().getBoolean(R.bool.is_tablet)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            loadSettings();
            FullscreenActivity.profileManager = new ProfileManager(this, FullscreenActivity.ServiceMessageHandler);

            int defaultProfileID = FullscreenActivity.profileManager.getDefaultProfileID();
            if(defaultProfileID == -1){
                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {

                        View dialogView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_user_name, null);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashScreen.this);

                        dialogBuilder.setView(dialogView);
                        final EditText input = (EditText) dialogView.findViewById(R.id.et_dialog_user_name_input);

                        dialogBuilder.setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newName = input.getText().toString();
                                        if(newName.trim().isEmpty()){
                                           newName = "Dummy Name "+new Random().nextInt(1000);
                                        }
                                        PlayerProfile pp = FullscreenActivity.profileManager.createNewPlayerProfile(newName);
                                        FullscreenActivity.profileManager.setDefaultProfileID(pp.getProfileID());


                                        Intent i = new Intent(SplashScreen.this, FullscreenActivity.class);
                                        startActivity(i);

                                        // close this activity
                                        finish();

                                    }
                                });
                        dialogBuilder.create().show();

                    }},SPLASH_TIME_OUT/2);

            }else {
                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(SplashScreen.this, FullscreenActivity.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //cannot close the app with pressing back!
        //super.onBackPressed();
    }
}
