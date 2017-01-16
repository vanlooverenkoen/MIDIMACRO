package be.vanlooverenkoen.midimacro.ui.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.model.MidiValue;

public class ControlButton extends Control {
    private RectF rect;
    private RectF toggleRect;
    private boolean toggleButton;
    private int padding;
    private float roundedCornersToggleBtn;

    private boolean toggled;

    private ButtonListener buttonListener;
    private ToggleListener toggleListener;

    public ControlButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ControlButton);
        toggleButton = a.getBoolean(R.styleable.ControlButton_toggle, false);
        a.recycle();
        init();
    }

    public ControlButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ControlButton, defStyle, 0);
        toggleButton = a.getBoolean(R.styleable.ControlButton_toggle, false);
        a.recycle();
        init();
    }

    private void init() {
        toggled = false;
        roundedCornersToggleBtn = 10;
        padding = 20;
        rect = new RectF();
        toggleRect = new RectF();
        setCheckedWithoutCallback(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Background
        float colors[] = new float[3];
        //covert color to hue saturation, value
        Color.colorToHSV(color, colors);
        int i;
        if (toggled && !getToggleButton()) {
            //clicked color
            colors[2] = colors[2] * 1.0F;
        } else {
            //Released color
            colors[2] = colors[2] * 0.3F;
        }
        i = Color.HSVToColor(colors);
        paint.setColor(i);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        rect.set(0 + stroke
                , 0 + stroke
                , canvas.getWidth() - stroke
                , canvas.getHeight() - stroke);
        canvas.drawRoundRect(rect
                , roundedCorners
                , roundedCorners
                , paint);

        //Button Stroke
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        canvas.drawRoundRect(rect
                , roundedCorners
                , roundedCorners
                , paint);

        //Toggle Button on Toggle
        if (getToggleButton() && toggled) {
            paint.setStyle(android.graphics.Paint.Style.FILL);
            toggleRect.set(0 + padding + stroke
                    , 0 + padding + stroke
                    , canvas.getWidth() - padding - stroke
                    , canvas.getHeight() - padding - stroke);
            canvas.drawRoundRect(toggleRect
                    , roundedCornersToggleBtn
                    , roundedCornersToggleBtn
                    , paint);
        }
        if (executeCallback) {
            if (getToggleButton()) {
                if (toggleListener != null) {
                    toggleListener.onToggleChange(this, toggled);

                }
                //For MQTT
                if (midiMessage != null)
                    if (toggled) {
                        midiMessage.setValue(MidiValue.MIDI_VALUE_127);
                        sendMidiMessage(midiMessage);
                    } else {
                        midiMessage.setValue(MidiValue.MIDI_VALUE_0);
                        sendMidiMessage(midiMessage);
                    }
            } else {
                if (buttonListener != null)
                    if (toggled) {
                        buttonListener.onButtonClicked(this);
                    } else {
                        buttonListener.onButtonReleased(this);
                    }
                if (midiMessage != null)
                    //for MQTT
                    if (toggled) {
                        midiMessage.setValue(MidiValue.MIDI_VALUE_127);
                        sendMidiMessage(midiMessage);
                    } else {
                        midiMessage.setValue(MidiValue.MIDI_VALUE_0);
                        sendMidiMessage(midiMessage);
                    }
            }
        }
        executeCallback = true;
    }

    @Override
    protected boolean onTouch(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_CANCEL) {
            handleTouchEvent(event);
        }
        return event.getAction() != MotionEvent.ACTION_CANCEL;
    }

    @Override
    protected void updateControl(MidiMessage midiMessage) {
        if (midiMessage.getMidiValue().getValue() == 127)
            setChecked(true);
        else if (midiMessage.getMidiValue().getValue() == 0)
            setChecked(false);
    }

    private void handleTouchEvent(MotionEvent motionevent) {
        if (motionevent.getAction() == MotionEvent.ACTION_DOWN) {
            if (getToggleButton()) {
                toggled = !this.toggled;
            } else {
                toggled = true;
            }
            setChecked(toggled);
        } else if (motionevent.getAction() == MotionEvent.ACTION_UP) {
            if (!getToggleButton()) {
                toggled = false;
                setChecked(false);
            }
        }
    }

    public void setChecked(boolean toggled) {
        this.toggled = toggled;
        executeCallback = true;
        invalidate();
    }

    public boolean isChecked() {
        return toggled;
    }


    public void setCheckedWithoutCallback(boolean toggled) {
        this.toggled = toggled;
        executeCallback = false;
        invalidate();
    }

    public void setButtonListener(ButtonListener buttonListener) {
        if (toggleButton)
            throw new IllegalArgumentException("You can't set a ButtonListener for a togglebutton");
        this.buttonListener = buttonListener;
    }

    public void setToggleListener(ToggleListener toggleListener) {
        if (!toggleButton)
            throw new IllegalArgumentException("You can't set a ToggleListener for a normal button");
        this.toggleListener = toggleListener;
    }

    public boolean getToggleButton() {
        return toggleButton;
    }

    //region Listeners
    private interface ButtonListener {
        void onButtonClicked(View view);

        void onButtonReleased(View view);
    }

    public interface ToggleListener {
        void onToggleChange(View view, boolean isChecked);
    }
    //endregion
}
