package be.vanlooverenkoen.midi.model;

public enum MidiChannel {

    MIDI_CHANNEL_1(1),
    MIDI_CHANNEL_2(2),
    MIDI_CHANNEL_3(3),
    MIDI_CHANNEL_4(4),
    MIDI_CHANNEL_5(5),
    MIDI_CHANNEL_6(6),
    MIDI_CHANNEL_7(7),
    MIDI_CHANNEL_8(8),
    MIDI_CHANNEL_9(9),
    MIDI_CHANNEL_10(10),
    MIDI_CHANNEL_11(11),
    MIDI_CHANNEL_12(12),
    MIDI_CHANNEL_13(13),
    MIDI_CHANNEL_14(14),
    MIDI_CHANNEL_15(15),
    MIDI_CHANNEL_16(16);

    private final int value;

    MidiChannel(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
