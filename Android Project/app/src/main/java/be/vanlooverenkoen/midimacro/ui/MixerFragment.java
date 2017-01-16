package be.vanlooverenkoen.midimacro.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiChannel;
import be.vanlooverenkoen.midimacro.model.MidiControl;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.model.MidiValue;
import be.vanlooverenkoen.midimacro.sharedprefs.SharedPrefsMixer;
import be.vanlooverenkoen.midimacro.ui.controls.ControlButton;
import be.vanlooverenkoen.midimacro.ui.controls.ControlSliderV;

/**
 * Created by Koen on 1/01/2017.
 */

public class MixerFragment extends MidiMacroFragment {

    private int amountOfSlider;
    private boolean pause;
    private String layout;

    private SharedPrefsMixer sharedPrefsMixer;

    private List<ControlSliderV> controlSliderVs;
    private List<ControlButton> controlButtons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("MIXER");
        sharedPrefsMixer = new SharedPrefsMixer(getContext());
        layout = sharedPrefsMixer.getMixerLayout();
        View view;
        switch (layout) {
            case "2 Sliders":
                view = inflater.inflate(R.layout.fragment_mixer_2_sliders, container, false);
                amountOfSlider = 2;
                break;
            case "4 Sliders":
                view = inflater.inflate(R.layout.fragment_mixer_4_sliders, container, false);
                amountOfSlider = 4;
                break;
            case "8 Sliders":
                view = inflater.inflate(R.layout.fragment_mixer_8_sliders, container, false);
                amountOfSlider = 8;
                break;
            case "12 Sliders":
                view = inflater.inflate(R.layout.fragment_mixer_12_sliders, container, false);
                amountOfSlider = 12;
                break;
            case "Mixer 4 Sliders":
                view = inflater.inflate(R.layout.fragment_mixer_4_sliders_mixer, container, false);
                amountOfSlider = 4;
                SpecialMixerFragment specialMixerFragment = new SpecialMixerFragment(view, amountOfSlider);
                specialMixerFragment.init();
                specialMixerFragment.onMixVolumesListener(new SpecialMixerFragment.MixVolumeListener() {
                    @Override
                    public void onMixVolumeListener(MidiValue midiValue, int sliderIndex) {
                        if (controlSliderVs.size() > sliderIndex) {
                            controlSliderVs.get(sliderIndex).setProgress(midiValue.getValue());
                        }
                    }
                });
                break;
            case "Knobs":
                view = inflater.inflate(R.layout.fragment_mixer_knob, container, false);
                KnobMixerFragment knobMixerFragment = new KnobMixerFragment(view, getChildFragmentManager());
                knobMixerFragment.init();
                break;
            case "5 Sliders":
            default:
                view = inflater.inflate(R.layout.fragment_mixer_5_sliders, container, false);
                amountOfSlider = 5;
                break;
        }
        if (view != null) {
            controlSliderVs = new ArrayList<>();
            controlButtons = new ArrayList<>();
            for (int i = 1; i <= amountOfSlider; i++) {
                int resID = getResources().getIdentifier("slider_" + i, "id", getContext().getPackageName());
                ControlSliderV controlSliderV = ((ControlSliderV) view.findViewById(resID));
                if (controlSliderV != null) {
                    final int finalI = i;
                    controlSliderV.setSliderListener(new ControlSliderV.SliderListener() {
                        @Override
                        public void onProgressChange(int progress) {
                            MidiChannel channel = MidiChannel.MIDI_CHANNEL_1;
                            MidiControl control = MidiControl.valueOf("MIDI_CONTROL_" + finalI);
                            MidiValue value = MidiValue.valueOf("MIDI_VALUE_" + progress);
                            MidiMessage midiMessage = new MidiMessage(channel, control, value);
                            notifyOnSendMidiMessage(midiMessage);
                        }
                    });
                    controlSliderVs.add(controlSliderV);
                }
                resID = getResources().getIdentifier("toggle_" + i, "id", getContext().getPackageName());
                ControlButton controlButton = ((ControlButton) view.findViewById(resID));
                if (controlButton != null) {
                    final int finalI = i;
                    controlButton.setToggleListener(new ControlButton.ToggleListener() {
                        @Override
                        public void onToggleChange(View view, boolean isChecked) {
                            MidiChannel channel = MidiChannel.MIDI_CHANNEL_2;
                            MidiControl control = MidiControl.valueOf("MIDI_CONTROL_" + finalI);
                            MidiValue value;
                            value = MidiValue.MIDI_VALUE_127;
                            MidiMessage midiMessage = new MidiMessage(channel, control, value);
                            notifyOnSendMidiMessage(midiMessage);
                            value = MidiValue.MIDI_VALUE_0;
                            midiMessage = new MidiMessage(channel, control, value);
                            notifyOnSendMidiMessage(midiMessage);
                        }
                    });
                    controlButtons.add(controlButton);
                }
            }
        } else {
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pause) {
            if (!layout.equals(sharedPrefsMixer.getMixerLayout()))
                ((MainActivity) getActivity()).setFragment(new MixerFragment());
            pause = false;
        }
    }

    @Override
    void onReceiveMidiMessage(MidiMessage midiMessage) {
        if (midiMessage.getMidiChannel() == MidiChannel.MIDI_CHANNEL_1) {
            int index = midiMessage.getMidiControl().getValue();
            int value = midiMessage.getMidiValue().getValue();
            if (value >= 1 && value < controlSliderVs.size()) {
                value--;
                controlSliderVs.get(index).setProgressWithoutCallback(value);
            }
        }
    }
}
