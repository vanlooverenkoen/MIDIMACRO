import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koen on 26/11/2016.
 */
public class UDPTester {
    private static final boolean PRINT = true;
    private static final String broker = "tcp://vanlooverenkoen.be:1883";
    private static final String clientId = "MIDMACRO_MIDIBRIDGE_CONTROLLER";
    private static final String TOPIC_TO_SOFTWARE = "MIDIMACRO_TO_SOFTWARE";
    private static final String TOPIC_TO_HARDWARE = "MIDIMACRO_TO_HARDWARE";
    private static final String TOPIC_TO_HARDWARE_ARDUINO = "MIDIMACRO_TO_HARDWARE_ARDUINO";
    private static final String LOCALHOST = "localhost";
    private static MqttClient client = null;

    private static List<PreviousMidi> previousMidiList;

    public static void main(String[] args) {
        previousMidiList = new ArrayList<>();
        try {
            sendPing();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            client = new MqttClient(broker, clientId);
            client.connect();
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    throw new RuntimeException("Lost Connection");
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    handleMessage(message);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            client.subscribe(TOPIC_TO_SOFTWARE);
        } catch (MqttException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            receiveMidi();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void handleMessage(MqttMessage message) throws IOException {
        String messageString = message.toString();
        messageString = messageString.replace(" ", "");
        if (messageString.equals("NL") || messageString.equals("L")) {
            message.setPayload(messageString.getBytes());
            /*try {
                client.publish(TOPIC_TO_HARDWARE_ARDUINO, message);
                client.publish(TOPIC_TO_HARDWARE, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }*/
        } else {
            int channel = Integer.parseInt(messageString.split("-")[0]);
            int cc = 175;
            channel = cc + channel;
            int control = Integer.parseInt(messageString.split("-")[1]);
            int value = Integer.parseInt(messageString.split("-")[2]);
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(LOCALHOST);
            byte[] sendData = {
                    0x2f, 0x6d, 0x69, 0x64, 0x69, 0x00, 0x00, 0x00, 0x2c, 0x6d, 0x00, 0x00, 0x00, (byte) value, (byte) control, (byte) channel
            };
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 12101);
            clientSocket.send(sendPacket);
            print("MIDI SENDED " + channel + " - " + control + " - " + value);
        }
    }

    private static void sendPing() throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(LOCALHOST);
        byte[] sendData = {
                0x2F, 0x70, 0x69, 0x6E, 0x67, 0x00, 0x00, 0x00, 0x2C, 0x00, 0x00, 0x00, 0x2F, 0x6D, 0x69, 0x64
        };
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 12101);
        clientSocket.send(sendPacket);
        print("Sended");
    }

    private static void receiveMidi() throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(12102);
        byte[] receiveData = new byte[16];
        print("Ready to receive");
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            byte[] receivedPacketDate = receivePacket.getData();
            int channel = receivedPacketDate[15] & 0xFF;
            int control = receivedPacketDate[14];
            int value = receivedPacketDate[13];
            String messageString = channel + " - " + control + " - " + value + " - OUT";
            MqttMessage message = new MqttMessage();
            message.setPayload(messageString.getBytes());
            try {
                //client.publish(TOPIC_TO_HARDWARE, message);
                //print("Sended midi out to hardware: ->     " + messageString);
                PreviousMidi previousMidi = new PreviousMidi(channel, control, LocalDateTime.now());
                if (!previousMidiList.contains(previousMidi)) {
                    client.publish(TOPIC_TO_HARDWARE_ARDUINO, message);
                    previousMidiList.add(previousMidi);
                    print("Sended midi out to ARDUINO: ->     " + messageString);
                } else {
                    /*
                    int index = previousMidiList.indexOf(previousMidi);
                    PreviousMidi previousMidiSaved = previousMidiList.get(index);
                    long miliSecondsPassed = previousMidiSaved.getLocalDateTime().until(LocalDateTime.now(), ChronoUnit.MILLIS);
                    if (miliSecondsPassed > 7500) {
                        client.publish(TOPIC_TO_HARDWARE_ARDUINO, message);
                        previousMidiList.set(index, previousMidi);
                        print("Sended midi out to ARDUINO: ->     " + messageString);
                    }
                    */
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private static void print(String string) {
        if (PRINT)
            System.out.println(string);
    }
}
