package be.vanlooverenkoen.midi.service;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.vanlooverenkoen.midi.model.MidiMessage;

/**
 * @author Koen Van Looveren
 */

public abstract class MQTTService {

    protected static final String TOPIC_SOFTWARE = "MIDIMACRO_TO_SOFTWARE";
    protected static final String TOPIC_HARDWARE = "MIDIMACRO_TO_HARDWARE";

    private List<MQTTListener> mqttListenersSoftware;
    private List<MQTTListener> mqttListenersHardware;

    public abstract void connect() throws MqttException;

    public abstract void sendMessage(String messageString) throws MqttException;

    public abstract void disconnect() throws MqttException;

    public void setMqttListenersSoftware(MQTTListener mqttCallback) {
        if (mqttListenersSoftware == null)
            mqttListenersSoftware = new ArrayList<>();
        this.mqttListenersSoftware.add(mqttCallback);
    }

    protected List<MQTTListener> getMqttListenersSoftware() {
        if (mqttListenersSoftware == null)
            return Collections.emptyList();
        return mqttListenersSoftware;
    }

    protected List<MQTTListener> getMqttListenersHardware() {
        if (mqttListenersHardware == null)
            return Collections.emptyList();
        return mqttListenersHardware;
    }

    protected void notifyListenersSoftware(MidiMessage midiMessage) {
        if (mqttListenersSoftware != null)
            for (MQTTListener mqttListener : mqttListenersSoftware) {
                mqttListener.messageReceived(midiMessage);
            }
    }

    protected void notifyListenersHardware(MidiMessage midiMessage) {
        if (mqttListenersSoftware != null)
            for (MQTTListener mqttListener : mqttListenersSoftware) {
                mqttListener.messageReceived(midiMessage);
            }
    }

    public void setMqttListenerHardware(MQTTListener mqttListenerHardware) {
        if (mqttListenersHardware == null)
            mqttListenersHardware = new ArrayList<>();
        this.mqttListenersHardware.add(mqttListenerHardware);
    }
}
