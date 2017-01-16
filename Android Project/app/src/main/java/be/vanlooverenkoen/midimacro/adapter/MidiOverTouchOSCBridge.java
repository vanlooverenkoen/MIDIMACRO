package be.vanlooverenkoen.midimacro.adapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.service.MidiService;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsMidiMode;

/**
 * Created by Koen on 1/01/2017.
 */

public class MidiOverTouchOSCBridge extends MidiService {
    private static final int SEND_PORT_TOUCHOSC_BRIDGE = 12101;
    private SharedPrefsMidiMode sharedPrefsMidiMode;
    private boolean connected;
    private String ip;
    private DatagramSocket clientSocket;


    public MidiOverTouchOSCBridge() {
        connected = false;
        sharedPrefsMidiMode = new SharedPrefsMidiMode();
        ip = sharedPrefsMidiMode.getTouchOSCBridgeIp();
    }

    @Override
    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ip == null || ip.isEmpty()) {
                        notifyOnError(IP_NOT_CONFIGURED);
                        return;
                    }
                    clientSocket = new DatagramSocket();
                    InetAddress ipAddress = InetAddress.getByName(ip);
                    clientSocket.connect(ipAddress, 12101);
                    byte[] sendData = {
                            0x2F, 0x70, 0x69, 0x6E, 0x67, 0x00, 0x00, 0x00, 0x2C, 0x00, 0x00, 0x00, 0x2F, 0x6D, 0x69, 0x64
                    };
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, SEND_PORT_TOUCHOSC_BRIDGE);
                    clientSocket.send(sendPacket);
                    connected = clientSocket.isConnected();
                    notifyOnConnected();
                } catch (IOException e) {
                    handleErrorMessages(e);
                    connected = false;
                }
            }
        }).start();
    }

    @Override
    public void disconnect() {
        if (connected)
            try {
                InetAddress IPAddress = InetAddress.getByName(sharedPrefsMidiMode.getTouchOSCBridgeIp());
                byte[] sendData = {
                        0x2F, 0x70, 0x6F, 0x6E, 0x67, 0x00, 0x00, 0x00, 0x2C, 0x00, 0x00, 0x00, 0x2F, 0x6D, 0x69, 0x64
                };
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SEND_PORT_TOUCHOSC_BRIDGE);
                clientSocket.send(sendPacket);
                notifyOnDisConnected();
            } catch (IOException e) {
                if (!e.getMessage().equals("Invalid argument"))
                    handleErrorMessages(e);
            } finally {
                clientSocket.disconnect();
                clientSocket.close();
                connected = false;
            }
    }

    @Override
    public void sendMessage(MidiMessage midiMessage) {
        if (connected && clientSocket.isConnected()) {
            try {
                int channel = midiMessage.getMidiChannel().getValue();
                channel = CHANGE_CONTROL_MIN + channel;
                int control = midiMessage.getMidiControl().getValue();
                int value = midiMessage.getMidiValue().getValue();
                InetAddress IPAddress = InetAddress.getByName(ip);
                byte[] sendData = {
                        0x2f, 0x6d, 0x69, 0x64, 0x69, 0x00, 0x00, 0x00, 0x2c, 0x6d, 0x00, 0x00, 0x00, (byte) value, (byte) control, (byte) channel
                };
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SEND_PORT_TOUCHOSC_BRIDGE);
                clientSocket.send(sendPacket);
                System.out.println(value);
            } catch (IOException e) {
                handleErrorMessages(e);
            }
        }
    }

    private void handleErrorMessages(IOException e) {
        switch (e.getMessage()) {
            case "Network is unreachable":
                notifyOnError(NETWORK_NOT_AVAILABLE);
                disconnect();
                break;
            case "Invalid argument":
                notifyOnError(COULD_NOT_CONNECT_IP);
                disconnect();
                break;
            default:
                notifyOnError(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }
}
