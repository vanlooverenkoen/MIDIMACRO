package be.vanlooverenkoen.midimacro.ui.controls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import be.vanlooverenkoen.midimacro.MidiMacro;
import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiChannel;
import be.vanlooverenkoen.midimacro.model.MidiControl;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.model.MidiValue;

/**
 * Created by Koen on 2/01/2017.
 */

public abstract class Control extends View {
    protected float stroke;
    protected int color;
    protected int roundedCorners;
    protected Paint paint;

    protected MidiMessage midiMessage;

    protected boolean executeCallback;

    public Control(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Control);
        int midiChannel = a.getInteger(R.styleable.Control_midichannel, -1);
        int midiControl = a.getInteger(R.styleable.Control_midicontrol, -1);
        a.recycle();
        if (midiChannel >= 1 && midiChannel <= 16
                && midiControl >= 0 && midiControl <= 127)
            midiMessage = new MidiMessage(MidiChannel.valueOf("MIDI_CHANNEL_" + midiChannel)
                    , MidiControl.valueOf("MIDI_CONTROL_" + midiControl)
                    , MidiValue.MIDI_VALUE_0);
        init();
    }

    public Control(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Control, defStyleAttr, 0);
        int midiChannel = a.getInteger(R.styleable.Control_midichannel, -1);
        int midiControl = a.getInteger(R.styleable.Control_midicontrol, -1);
        a.recycle();
        if (midiChannel >= 1 && midiChannel <= 16
                && midiControl >= 0 && midiControl <= 127)
            midiMessage = new MidiMessage(MidiChannel.valueOf("MIDI_CHANNEL_" + midiChannel)
                    , MidiControl.valueOf("MIDI_CONTROL_" + midiControl)
                    , MidiValue.MIDI_VALUE_0);
        init();
    }

    private void init() {
        paint = new Paint();
        color = ContextCompat.getColor(MidiMacro.inst, R.color.colorPrimary);
        stroke = 3F;
        roundedCorners = 20;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return onTouch(event);
    }

    protected abstract boolean onTouch(MotionEvent event);

    protected abstract void updateControl(MidiMessage midiMessage);

    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MidiChannel midiChannel = (MidiChannel) intent.getSerializableExtra("midi_channel");
            MidiControl midiControl = (MidiControl) intent.getSerializableExtra("midi_control");
            MidiValue midiValue = (MidiValue) intent.getSerializableExtra("midi_value");
            MidiMessage midiMessage = new MidiMessage(midiChannel, midiControl, midiValue);
            System.out.println(midiMessage.toString());
            updateControl(midiMessage);
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            if (midiMessage != null)
                LocalBroadcastManager
                        .getInstance(getContext())
                        .registerReceiver(mHandler, new IntentFilter("INCOMING_MIDI_MSG_CH"
                                + midiMessage.getMidiChannel().getValue()
                                + "CC"
                                + midiMessage.getMidiControl().getValue()));
            executeCallback = true;
        } else {
            if (midiMessage != null)
                LocalBroadcastManager
                        .getInstance(getContext())
                        .registerReceiver(mHandler, new IntentFilter("INCOMING_MIDI_MSG_CH"
                                + midiMessage.getMidiChannel().getValue()
                                + "CC"
                                + midiMessage.getMidiControl().getValue()));
            executeCallback = false;
        }
    }

    protected void sendMidiMessage(MidiMessage midiMessage) {
        Intent i = new Intent("be.vanlooverenkoen.midimacro.send_midi_message");
        i.putExtra("midimessage", midiMessage);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(i);
    }
}
