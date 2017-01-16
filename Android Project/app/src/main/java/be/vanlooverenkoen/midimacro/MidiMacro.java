package be.vanlooverenkoen.midimacro;

import android.app.Application;

import com.google.firebase.crash.FirebaseCrash;

public class MidiMacro extends Application {

    public static MidiMacro inst;

    public MidiMacro() {
        inst = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseCrash.log(String.format("%S      ----->      MIDIMACRO Android Version", getString(R.string.app_version)));
    }
}
