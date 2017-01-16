package be.vanlooverenkoen.midimacro.service;

import java.util.ArrayList;
import java.util.List;

import be.vanlooverenkoen.midimacro.exception.MidiServiceException;
import be.vanlooverenkoen.midimacro.model.MidiMessage;

public abstract class MidiService {
    public static final String NOT_CONNECTED_MIDI_OVERUSB = "Could not connect with Midi over USB";
    public static final String COULD_NOT_CONNECT_IP = "Could not connect with given IP";
    public static final String IP_NOT_CONFIGURED = "Ip for MidiBridge is not configured";
    public static final String NETWORK_NOT_AVAILABLE = "Network is not available";

    protected static final int CHANGE_CONTROL_MIN = 175;

    private List<MidiServiceListener> midiServiceListenerList;

    public abstract void connect() throws MidiServiceException;

    public abstract void disconnect() throws MidiServiceException;

    public abstract void sendMessage(MidiMessage midiMessage) throws MidiServiceException;

    public void setMidiServiceListenerList(MidiServiceListener midiServiceListener) {
        if (this.midiServiceListenerList == null)
            this.midiServiceListenerList = new ArrayList<>();
        this.midiServiceListenerList.add(midiServiceListener);
    }

    public void notifyOnError(String error) {
        notifyOnError(error, null);
    }

    public void notifyOnError(String error, Throwable e) {
        if (midiServiceListenerList != null)
            for (MidiServiceListener midiServiceListener : midiServiceListenerList) {
                if (e == null)
                    midiServiceListener.onMidiError(error);
                else
                    midiServiceListener.onMidiError(error, e);
            }
    }

    public void notifyOnConnected() {
        if (midiServiceListenerList != null)
            for (MidiServiceListener midiServiceListener : midiServiceListenerList) {
                midiServiceListener.onMidiConnected();
            }
    }

    public void notifyOnDisConnected() {
        if (midiServiceListenerList != null)
            for (MidiServiceListener midiServiceListener : midiServiceListenerList) {
                midiServiceListener.onMidiDisconnected();
            }
    }

    public void notifyOnReceiveMidiMessage(MidiMessage midiMessage) {
        if (midiServiceListenerList != null)
            for (MidiServiceListener midiServiceListener : midiServiceListenerList) {
                midiServiceListener.onReceiveMidiMessage(midiMessage);
            }
    }
}
