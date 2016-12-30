package be.vanlooverenkoen.midi.adapter.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import be.vanlooverenkoen.midi.exception.MqttServerException;
import be.vanlooverenkoen.midi.model.MidiChannel;
import be.vanlooverenkoen.midi.model.MidiControl;
import be.vanlooverenkoen.midi.model.MidiMessage;
import be.vanlooverenkoen.midi.model.MidiValue;
import be.vanlooverenkoen.midi.service.MQTTService;

public class Mosquitto extends MQTTService implements MqttCallback {
    private final String CLIENT_ID;
    private final String SERVER;

    private MqttClient client;

    public Mosquitto(String server, String clientId) {
        this.SERVER = server;
        this.CLIENT_ID = clientId;
    }

    @Override
    public void connect() throws MqttException {
        if (SERVER != null && !SERVER.isEmpty()) {
            MemoryPersistence memoryPersistence = new MemoryPersistence();
            client = new MqttClient(SERVER, CLIENT_ID + "_HARDWARE", memoryPersistence);
            client.connect();
            client.setCallback(this);
            client.subscribe(TOPIC_HARDWARE);
            MqttClient softwareClient = new MqttClient(SERVER, CLIENT_ID + "_SOFTWARE", memoryPersistence);
            softwareClient.connect();
            softwareClient.setCallback(this);
            softwareClient.subscribe(TOPIC_SOFTWARE);
        }
    }

    @Override
    public void sendMessage(final String messageString) throws MqttException, MqttServerException {
        if (client != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MqttMessage message = new MqttMessage();
                    message.setPayload(messageString
                            .getBytes());
                    try {
                        client.publish(TOPIC_SOFTWARE, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else
            throw new MqttServerException("Never connected");
    }

    @Override
    public void disconnect() throws MqttException {
        if (client != null)
            client.disconnect();
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String messageString = message.toString();
        messageString = messageString.replace(" ", "");
        int channel = Integer.parseInt(messageString.split("-")[0]);
        channel = channel - 175;
        int control = Integer.parseInt(messageString.split("-")[1]);
        int value = Integer.parseInt(messageString.split("-")[2]);
        MidiChannel midiChannel = MidiChannel.valueOf("MIDI_CHANNEL_" + channel);
        MidiControl midiControl = MidiControl.valueOf("MIDI_CONTROL_" + control);
        MidiValue midiValue = MidiValue.valueOf("MIDI_VALUE_" + value);
        if (topic.equals(TOPIC_HARDWARE))
            notifyListenersHardware(new MidiMessage(midiChannel, midiControl, midiValue));
        else if (topic.equals(TOPIC_SOFTWARE))
            notifyListenersSoftware(new MidiMessage(midiChannel, midiControl, midiValue));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
