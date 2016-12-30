package be.vanlooverenkoen.midi.service.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Koen Van Looveren
 */
public abstract class SharedPreferencesEditor {

    private Context context;

    public SharedPreferencesEditor(Context context) {
        this.context = context;
    }

    abstract SharedPreferences getSharedPreferences();

    public SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }
    protected Context getContext() {
        return context;
    }
}
