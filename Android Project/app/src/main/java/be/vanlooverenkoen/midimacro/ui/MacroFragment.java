package be.vanlooverenkoen.midimacro.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiMessage;

/**
 * Created by Koen on 1/01/2017.
 */

public class MacroFragment extends MidiMacroFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("MACRO");
        View view = inflater.inflate(R.layout.fragment_macro, container, false);
        if (view != null) {
        } else {
        }
        return view;
    }

    @Override
    void onReceiveMidiMessage(MidiMessage midiMessage) {

    }
}
