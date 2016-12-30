package be.vanlooverenkoen.midi.exception;

import java.io.IOException;

/**
 * Created by Koen on 21/10/2016.
 */
public class MidiException extends Exception {
    public MidiException(String message, IOException e) {
        super(message, e);
    }

    public MidiException(String message, NullPointerException e) {
        super(message, e);
    }
}
