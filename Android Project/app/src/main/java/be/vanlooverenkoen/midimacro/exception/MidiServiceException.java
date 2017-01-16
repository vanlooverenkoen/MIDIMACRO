package be.vanlooverenkoen.midimacro.exception;

/**
 * Created by Koen on 2/01/2017.
 */
public class MidiServiceException extends Exception {
    public MidiServiceException(String message) {
        super(message);
    }

    public MidiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
