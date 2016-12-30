package be.vanlooverenkoen.midi.adapter.midi;

import org.eclipse.paho.client.mqttv3.MqttException;

import be.vanlooverenkoen.midi.model.MidiChannel;
import be.vanlooverenkoen.midi.model.MidiMessage;
import be.vanlooverenkoen.midi.model.MidiValue;
import be.vanlooverenkoen.midi.service.MQTTListener;
import be.vanlooverenkoen.midi.service.MQTTService;
import be.vanlooverenkoen.midi.service.Midi;
import be.vanlooverenkoen.midi.model.MidiControl;

public class MidiOverMQTT implements Midi, MQTTListener {

    private MQTTService mqttService;

    public MidiOverMQTT(MQTTService mqttService) {
        this.mqttService = mqttService;
    }

    @Override
    public void init() throws MqttException {
        mqttService.connect();
    }

    @Override
    public void sendChangeControl(MidiChannel midiChannel, MidiControl midiControl, MidiValue midiValue) {
        String message = midiChannel + " - " + midiControl + " - " + midiValue;
        System.out.println(message);
        try {
            mqttService.sendMessage(message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws MqttException {
        mqttService.disconnect();
    }

    @Override
    public void messageReceived(MidiMessage message) {
        System.out.println("Message Received");
    }
}
