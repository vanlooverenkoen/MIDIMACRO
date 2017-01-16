package be.vanlooverenkoen.midimacro.ui;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import be.vanlooverenkoen.midimacro.MidiMacro;
import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiValue;
import be.vanlooverenkoen.midimacro.ui.controls.ControlButton;
import be.vanlooverenkoen.midimacro.ui.controls.ControlSliderH;
import be.vanlooverenkoen.midimacro.ui.controls.ControlSliderV;

/**
 * Created by Koen on 2/01/2017.
 */

public class SpecialMixerFragment {
    private final int amountOfSliders;
    private View view;
    private MixVolumeListener mixVolumeListener;

    List<ControlButton> deckABtns;
    List<ControlButton> deckBBtns;

    List<Integer> deckAIndexes;
    List<Integer> deckBIndexes;

    SpecialMixerFragment(View view, int amountOfSliders) {
        this.view = view;
        this.amountOfSliders = amountOfSliders;
        deckAIndexes = new ArrayList<>();
        deckBIndexes = new ArrayList<>();
        deckBBtns = new ArrayList<>();
        deckABtns = new ArrayList<>();
        deckBIndexes = new ArrayList<>();
    }

    void init() {
        ControlSliderH controlSliderH = (ControlSliderH) view.findViewById(R.id.mixer_slider);
        controlSliderH.setSliderListener(new ControlSliderV.SliderListener() {
            @Override
            public void onProgressChange(int progress) {
                if (progress > 63.5) {
                    progress = (int) (progress - 63.5);
                    progress = progress * 2;
                    if (progress > 127)
                        progress = 127;
                    else if (progress < 0)
                        progress = 0;
                    progress = 127 - progress;
                    for (Integer deckAIndex : deckAIndexes) {
                        mixVolumeListener.onMixVolumeListener(MidiValue.MIDI_VALUE_127, deckAIndex);
                    }
                    for (Integer deckBIndex : deckBIndexes) {
                        mixVolumeListener.onMixVolumeListener(MidiValue.valueOf("MIDI_VALUE_" + progress), deckBIndex);
                    }
                } else {
                    progress = progress * 2;
                    if (progress > 127)
                        progress = 127;
                    else if (progress < 0)
                        progress = 0;
                    for (Integer deckAIndex : deckAIndexes) {
                        mixVolumeListener.onMixVolumeListener(MidiValue.valueOf("MIDI_VALUE_" + progress), deckAIndex);
                    }
                    for (Integer deckBIndex : deckBIndexes) {
                        mixVolumeListener.onMixVolumeListener(MidiValue.MIDI_VALUE_127, deckBIndex);
                    }
                }
            }
        });

        for (int i = 1; i <= amountOfSliders; i++) {
            int resID = MidiMacro.inst.getResources().getIdentifier("mixer_a_toggle_" + i, "id", MidiMacro.inst.getPackageName());
            final ControlButton deckAControlBtn = (ControlButton) view.findViewById(resID);
            resID = MidiMacro.inst.getResources().getIdentifier("mixer_b_toggle_" + i, "id", MidiMacro.inst.getPackageName());
            final ControlButton deckBControlBtn = (ControlButton) view.findViewById(resID);

            final int tmpI = i - 1;
            if (deckAControlBtn != null) {
                deckAControlBtn.setToggleListener(new ControlButton.ToggleListener() {
                    @Override
                    public void onToggleChange(View view, boolean isChecked) {
                        if (isChecked) {
                            if (!deckAIndexes.contains(tmpI))
                                deckAIndexes.add(tmpI);
                            if (deckBBtns.get(tmpI).isChecked())
                                deckBBtns.get(tmpI).setChecked(false);
                        } else {
                            if (deckAIndexes.contains(tmpI))
                                deckAIndexes.remove(Integer.valueOf(tmpI));
                        }
                    }
                });
                deckABtns.add(deckAControlBtn);
            }
            if (deckBControlBtn != null) {
                deckBControlBtn.setToggleListener(new ControlButton.ToggleListener() {
                    @Override
                    public void onToggleChange(View view, boolean isChecked) {
                        if (isChecked) {
                            if (!deckBIndexes.contains(tmpI))
                                deckBIndexes.add(tmpI);
                            if (deckABtns.get(tmpI).isChecked())
                                deckABtns.get(tmpI).setChecked(false);
                        } else {
                            if (deckBIndexes.contains(tmpI))
                                deckBIndexes.remove(Integer.valueOf(tmpI));
                        }
                    }
                });
                deckBBtns.add(deckBControlBtn);
            }
        }
    }

    void onMixVolumesListener(MixVolumeListener mixVolumeListener) {
        this.mixVolumeListener = mixVolumeListener;
    }

    interface MixVolumeListener {
        void onMixVolumeListener(MidiValue midiValue, int sliderIndex);
    }
}
