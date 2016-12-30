package be.vanlooverenkoen.midi.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Koen on 21/10/2016.
 */

public class MixerSlider extends SeekBar {

    public MixerSlider(Context context) {
        super(context);
    }

    public MixerSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MixerSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
