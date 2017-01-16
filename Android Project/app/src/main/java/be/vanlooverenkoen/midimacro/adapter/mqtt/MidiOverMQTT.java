package be.vanlooverenkoen.midimacro.adapter.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

import be.vanlooverenkoen.midimacro.exception.MidiServiceException;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.service.MidiService;

/**
 * Created by Koen on 15/01/2017.
 */

public class MidiOverMQTT extends MidiService {

    private MQTTService mosquitto;

    public MidiOverMQTT(Mosquitto mosquitto) {
        this.mosquitto = mosquitto;

    }

    @Override
    public void connect() throws MidiServiceException {
        try {
            mosquitto.connect();
        } catch (MqttException e) {
            throw new MidiServiceException("Could not connect", e);
        }
    }

    @Override
    public void disconnect() throws MidiServiceException {
        try {
            mosquitto.disconnect();
        } catch (MqttException e) {
            throw new MidiServiceException("Could not disconnect", e);
        }
    }

    @Override
    public void sendMessage(MidiMessage midiMessage) throws MidiServiceException {
        try {
            mosquitto.sendMessage(midiMessage.toString(), false);
            System.out.println(midiMessage.toString());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void showLed(boolean led) {
        try {
            if (led)
                mosquitto.sendMessage("L", true);
            else
                mosquitto.sendMessage("NL", true);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
