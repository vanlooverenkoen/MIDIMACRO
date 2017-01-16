package be.vanlooverenkoen.midimacro.adapter.mqtt;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import be.vanlooverenkoen.midimacro.MidiMacro;
import be.vanlooverenkoen.midimacro.model.MidiChannel;
import be.vanlooverenkoen.midimacro.model.MidiControl;
import be.vanlooverenkoen.midimacro.model.MidiValue;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsMidiMode;

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
            try {
                MemoryPersistence memoryPersistence = new MemoryPersistence();
                client = new MqttClient(SERVER, CLIENT_ID + "_HARDWARE", memoryPersistence);
                client.connect();
                client.setCallback(this);
                client.subscribe(TOPIC_HARDWARE);
                client.subscribe(TOPIC_HARDWARE_ANDROID);
                MqttClient softwareClient = new MqttClient(SERVER, CLIENT_ID + "_SOFTWARE", memoryPersistence);
                softwareClient.connect();
                softwareClient.setCallback(this);
                //softwareClient.subscribe(TOPIC_SOFTWARE);
            } catch (IllegalArgumentException i) {
                throw new MqttException(MqttException.REASON_CODE_BROKER_UNAVAILABLE);
            }
        }
    }

    @Override
    public void sendMessage(final String messageString, final boolean arduino) throws MqttException {
        if (client != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MqttMessage message = new MqttMessage();
                    message.setPayload(messageString
                            .getBytes());
                    try {
                        if (arduino)
                            client.publish(TOPIC_HARDWARE_ARDUINO, message);
                        else
                            client.publish(TOPIC_SOFTWARE, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else
            throw new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION);
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
        if (topic.equals(TOPIC_HARDWARE_ANDROID)) {
            SharedPrefsMidiMode sharedPrefsMidiMode = new SharedPrefsMidiMode();
            if (messageString.equals("NL")) {
                System.out.println("Leds Off");
                sharedPrefsMidiMode.saveLed(false);
//                notifyShowLeds(false);
            } else if (messageString.equals("L")) {
                System.out.println("Leds On");
                sharedPrefsMidiMode.saveLed(true);
//                notifyShowLeds(true);
            } else {

                messageString = messageString.replace(" ", "");
                int channel = Integer.parseInt(messageString.split("-")[0]);
                int control = Integer.parseInt(messageString.split("-")[1]);
                int value = Integer.parseInt(messageString.split("-")[2]);
                MidiChannel midiChannel = MidiChannel.valueOf("MIDI_CHANNEL_" + channel);
                MidiControl midiControl = MidiControl.valueOf("MIDI_CONTROL_" + control);
                MidiValue midiValue = MidiValue.valueOf("MIDI_VALUE_" + value);
                Intent i = new Intent("INCOMING_MIDI_MSG_CH"
                        + midiChannel.getValue()
                        + "CC"
                        + midiControl.getValue());
                i.putExtra("midi_channel", midiChannel);
                i.putExtra("midi_control", midiControl);
                i.putExtra("midi_value", midiValue);
                LocalBroadcastManager.getInstance(MidiMacro.inst).sendBroadcast(i);
                /*
                    if (topic.equals(TOPIC_HARDWARE))
                        notifyListenersHardware(new MidiMessage(midiChannel, midiControl, midiValue));
                    else if (topic.equals(TOPIC_SOFTWARE))
                        notifyListenersSoftware(new MidiMessage(midiChannel, midiControl, midiValue));
                */
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
