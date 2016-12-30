package be.vanlooverenkoen.midi.adapter.midi;

import android.media.midi.MidiInputPort;

import java.io.IOException;

import be.vanlooverenkoen.midi.exception.MidiException;

/**
 * Created by Koen on 21/10/2016.
 */

public class MidiOverUsb {
    private static final int CHANGE_CONTROL_MIN = 176;
    private static final int COMMAND = 0;
    private static final int DATA_1 = 1;
    private static final int DATA_2 = 2;
    private static final int NUM_BYTES = 3;

    public static void sendChangeControl(MidiInputPort midiInputPort, int channel, int control, boolean on) throws MidiException {
        if (on) {
            sendChangeControl(midiInputPort, channel, control, 127);
        } else {
            sendChangeControl(midiInputPort, channel, control, 0);
        }
    }

    public static void sendChangeControl(MidiInputPort midiInputPort, int channel, int control, int value) throws MidiException {
        byte[] buffer = new byte[32];
        channel--;
        buffer[COMMAND] = (byte) (CHANGE_CONTROL_MIN + channel);
        buffer[DATA_1] = (byte) control;
        buffer[DATA_2] = (byte) value;
        int offset = 0; // post is non-blocking
        try {
            midiInputPort.send(buffer, offset, NUM_BYTES);
            System.out.println("midi send");
        } catch (IOException e) {
            throw new MidiException("Could not send midi message (CHANGE CONTROL)", e);
        } catch (NullPointerException e) {
            throw new MidiException("Could not send midi message (NULL)", e);
        }
    }
}
