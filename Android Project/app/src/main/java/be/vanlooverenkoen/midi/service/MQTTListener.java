package be.vanlooverenkoen.midi.service;

import be.vanlooverenkoen.midi.model.MidiMessage;

/**
 * @author Koen Van Looveren
 */

public interface MQTTListener {
    void messageReceived(MidiMessage message);
}
