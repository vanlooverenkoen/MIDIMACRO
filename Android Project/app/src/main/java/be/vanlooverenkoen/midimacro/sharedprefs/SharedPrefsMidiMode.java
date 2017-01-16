package be.vanlooverenkoen.midimacro.sharedprefs;

import android.content.Context;

import be.vanlooverenkoen.midimacro.MidiMacro;

/**
 * Created by Koen on 1/01/2017.
 */

public class SharedPrefsMidiMode extends MidiMacroSharedPrefs {

    public SharedPrefsMidiMode() {
        super(MidiMacro.inst, "MidiMode");
    }

    public String getTouchOSCBridgeIp() {
        return getSharedPreferences().getString("midi_touch_osc_bridge_ip", "");
    }

    public void saveTouchOSCBridgeIp(String value) {
        getEditor().putString("midi_touch_osc_bridge_ip", value).apply();
    }

    public String getMidiMode() {
        return getSharedPreferences().getString("midi_mode", "Midi over TouchOSC Bridge");
    }

    public void saveMidiMode(String value) {
        getEditor().putString("midi_mode", value).apply();
    }

    public void saveMQTTIp(String input) {
        getEditor().putString("mqtt_ip", input).apply();
    }

    public String getMQTTIp() {
        return getSharedPreferences().getString("mqtt_ip", "");
    }

    public void saveLed(boolean checked) {
        getEditor().putBoolean("leds", checked).apply();
    }

    public boolean isLed() {
        return getSharedPreferences().getBoolean("leds", false);
    }
}
