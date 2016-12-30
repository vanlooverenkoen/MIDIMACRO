package be.vanlooverenkoen.midi.service.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Koen on 26/11/2016.
 */

public class SettingsSharedPreferencesEditor extends SharedPreferencesEditor {
    private final String VALUE = "SETTINGS";

    public SettingsSharedPreferencesEditor(Context context) {
        super(context);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(VALUE, MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return getSharedPreferences().getBoolean("first_run", true);
    }

    public void saveFirstRun(boolean firstRun) {
        getSharedPreferencesEditor().putBoolean("first_run", firstRun).apply();
    }

    public String getServer() {
        return getSharedPreferences().getString("server", null);
    }
}
