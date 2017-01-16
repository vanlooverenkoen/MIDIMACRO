package be.vanlooverenkoen.midimacro.model;


import java.io.Serializable;

public class MidiMessage implements Serializable {
    private MidiChannel midiChannel;
    private MidiControl midiControl;
    private MidiValue midiValue;

    public MidiMessage(MidiChannel midiChannel, MidiControl midiControl, MidiValue midiValue) {
        this.midiChannel = midiChannel;
        this.midiControl = midiControl;
        this.midiValue = midiValue;
    }

    public MidiChannel getMidiChannel() {
        return midiChannel;
    }

    public MidiControl getMidiControl() {
        return midiControl;
    }

    public MidiValue getMidiValue() {
        return midiValue;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", midiChannel, midiControl, midiValue);
    }

    public void setValue(MidiValue midiValue) {
        this.midiValue = midiValue;
    }
}
