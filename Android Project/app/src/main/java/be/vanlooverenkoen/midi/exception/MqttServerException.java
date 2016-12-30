package be.vanlooverenkoen.midi.exception;

/**
 * Created by Koen on 24/11/2016.
 */

public class MqttServerException extends RuntimeException {
    public MqttServerException(String message) {
        super(message);
    }

    public MqttServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
