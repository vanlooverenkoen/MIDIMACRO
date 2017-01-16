package be.vanlooverenkoen.midimacro.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Koen on 1/01/2017.
 */
public class MidiMacroSharedPrefs {
    private final Context context;
    private final String name;

    public MidiMacroSharedPrefs(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    /**
     * Gives you the right SharedPreferences object where you can get all the saved values
     *
     * @return the right SharedPreferences Object
     */
    protected SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * Gives you the right Editor object where you can store all your values
     *
     * @return the right Editor Object
     */
    protected SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.edit();
    }

}
