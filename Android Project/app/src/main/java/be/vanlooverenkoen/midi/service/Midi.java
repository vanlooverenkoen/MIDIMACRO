package be.vanlooverenkoen.midi.service;

import org.eclipse.paho.client.mqttv3.MqttException;

import be.vanlooverenkoen.midi.model.MidiChannel;
import be.vanlooverenkoen.midi.model.MidiControl;
import be.vanlooverenkoen.midi.model.MidiValue;

public interface Midi {
    void init() throws MqttException;

    void sendChangeControl(MidiChannel midiChannel, MidiControl midiControl, MidiValue value);

    void destroy() throws MqttException;
}
