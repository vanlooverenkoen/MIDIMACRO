package be.vanlooverenkoen.midimacro.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsGeneral;

/**
 * Splash screen activity that is only shown when the app is initializing (no loading simulation)
 */
public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPrefsGeneral sharedPrefsGeneral = new SharedPrefsGeneral();
                //just for testing
                //sharedPrefsGeneral.saveFirstRun(true);
                boolean firstTime = sharedPrefsGeneral.getFirstRun();
                Intent intent;
                if (firstTime)
                    intent = new Intent(SplashScreenActivity.this, AppIntroActivity.class);
                else
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 300);
    }
}