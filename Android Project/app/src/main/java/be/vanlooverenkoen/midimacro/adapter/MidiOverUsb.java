package be.vanlooverenkoen.midimacro.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;

import java.io.IOException;

import be.vanlooverenkoen.midimacro.MidiMacro;
import be.vanlooverenkoen.midimacro.exception.MidiServiceException;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.service.MidiService;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MidiOverUsb extends MidiService {

    private static final int COMMAND = 0;
    private static final int DATA_1 = 1;
    private static final int DATA_2 = 2;
    private static final int NUM_BYTES = 3;

    private MidiManager m;
    private MidiManager.DeviceCallback d;
    private MidiInputPort inputPort;
    private static final int exclusive = 0;

    @Override
    public void connect() throws MidiServiceException {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M &&
                MidiMacro.inst.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            m = (MidiManager) MidiMacro.inst.getSystemService(Context.MIDI_SERVICE);
            MidiDeviceInfo[] infos = m.getDevices();
            d = new MidiManager.DeviceCallback() {
                public void onDeviceAdded(final MidiDeviceInfo info) {
                    openDevice(info);
                }

                public void onDeviceRemoved(MidiDeviceInfo info) {
                    notifyOnDisConnected();
                    inputPort = null;
                }
            };
            m.registerDeviceCallback(d, new Handler(Looper.getMainLooper()));
            if (infos.length == 0)
                notifyOnError(NOT_CONNECTED_MIDI_OVERUSB);
            for (MidiDeviceInfo info : infos) {
                openDevice(info);
            }
        } else
            throw new MidiServiceException(String.format("Device is not supported SDK (%d)", Build.VERSION.SDK_INT));
    }


    private void openDevice(final MidiDeviceInfo info) {
        m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {
            @Override
            public void onDeviceOpened(MidiDevice device) {
                if (device == null) {
                    notifyOnError(NOT_CONNECTED_MIDI_OVERUSB);
                } else {
                    inputPort = device.openInputPort(exclusive);
                    if (inputPort == null) {
                        notifyOnError(NOT_CONNECTED_MIDI_OVERUSB);
                    } else {
                        notifyOnConnected();
                    }
                }
            }
        }, new Handler(Looper.getMainLooper()));
    }

    @Override
    public void disconnect() throws MidiServiceException {
        try {
            if (inputPort != null)
                inputPort.close();
            m.unregisterDeviceCallback(d);
            m = null;
            inputPort = null;
            notifyOnDisConnected();
        } catch (IOException e) {
            throw new MidiServiceException("Could not close", e);
        }
    }

    @Override
    public void sendMessage(MidiMessage midiMessage) throws MidiServiceException {
        byte[] buffer = new byte[32];
        int channel = midiMessage.getMidiChannel().getValue();
        channel = channel + CHANGE_CONTROL_MIN;
        buffer[COMMAND] = (byte) channel;
        buffer[DATA_1] = (byte) midiMessage.getMidiControl().getValue();
        buffer[DATA_2] = (byte) midiMessage.getMidiValue().getValue();
        int offset = 0; // post is non-blocking
        try {
            inputPort.send(buffer, offset, NUM_BYTES);
        } catch (IOException e) {
            throw new MidiServiceException("Could not send midi message (CHANGE CONTROL)", e);
        } catch (NullPointerException e) {
            throw new MidiServiceException("Could not send midi message (NULL)", e);
        }
    }
}
