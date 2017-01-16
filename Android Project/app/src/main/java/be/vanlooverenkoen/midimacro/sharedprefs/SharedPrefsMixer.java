package be.vanlooverenkoen.midimacro.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Koen on 1/01/2017.
 */

public class SharedPrefsMixer extends MidiMacroSharedPrefs {
    public SharedPrefsMixer(Context context) {
        super(context, "Mixer");
    }

    public String getMixerLayout() {
        return getSharedPreferences().getString("mixer_layout", "5 Sliders");
    }

    public void saveMixerLayout(String value) {
        getEditor().putString("mixer_layout", value).apply();
    }
}
