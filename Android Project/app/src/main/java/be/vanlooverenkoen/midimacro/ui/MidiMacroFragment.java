package be.vanlooverenkoen.midimacro.ui;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import be.vanlooverenkoen.midimacro.model.MidiMessage;

/**
 * Created by Koen on 1/01/2017.
 */
public abstract class MidiMacroFragment extends Fragment {

    private List<MidiMessageListener> midiMessageListenerList;

    abstract void onReceiveMidiMessage(MidiMessage midiMessage);

    void addOnSendMidiMessageListener(MidiMessageListener midiMessageListener) {
        if (midiMessageListenerList == null)
            midiMessageListenerList = new ArrayList<>();
        midiMessageListenerList.add(midiMessageListener);
    }

    protected void notifyOnSendMidiMessage(MidiMessage midiMessage) {
        if (midiMessageListenerList != null)
            for (MidiMessageListener midiMessageListener : midiMessageListenerList) {
                midiMessageListener.onSendMidiMessage(midiMessage);
            }
    }

    //region Listener
    public interface MidiMessageListener {
        void onSendMidiMessage(MidiMessage midiMessage);
    }
    //endregion
}
