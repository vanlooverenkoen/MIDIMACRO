package be.vanlooverenkoen.midi.model;


public class MidiMessage {
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
}
