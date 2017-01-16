package be.vanlooverenkoen.midimacro.sharedprefs;

import be.vanlooverenkoen.midimacro.MidiMacro;

/**
 * Created by Koen on 1/01/2017.
 */

public class SharedPrefsGeneral extends MidiMacroSharedPrefs {

    public SharedPrefsGeneral() {
        super(MidiMacro.inst, "General");
    }

    public boolean getFirstRun() {
        return getSharedPreferences().getBoolean("first_run", true);
    }

    public void saveFirstRun(boolean value) {
        getEditor().putBoolean("first_run", value).apply();
    }
}