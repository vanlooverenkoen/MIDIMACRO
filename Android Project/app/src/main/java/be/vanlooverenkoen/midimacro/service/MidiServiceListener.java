package be.vanlooverenkoen.midimacro.service;

import be.vanlooverenkoen.midimacro.model.MidiMessage;

public interface MidiServiceListener {
    void onMidiConnected();

    void onMidiDisconnected();

    void onMidiError(String error);

    void onMidiError(String error, Throwable e);

    void onReceiveMidiMessage(MidiMessage midiMessage);
}
