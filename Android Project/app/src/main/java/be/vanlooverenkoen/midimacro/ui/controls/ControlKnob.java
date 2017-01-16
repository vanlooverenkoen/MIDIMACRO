package be.vanlooverenkoen.midimacro.ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.model.MidiValue;

/**
 * Created by Koen on 2/01/2017.
 */

public class ControlKnob extends Control {
    private final float ROTATION_DEGREE_START = 240;
    private final float FULL_ROTATION_ANGLE = 300;
    int centerX;
    int centerY;
    double zeroDegreeX;
    double zeroDegreeY;
    private float progressAngle;
    private final RectF middle;
    private final RectF arc;
    private final RectF touchArc;
    private float[] colors;
    private KnobListener knobListener;
    private double progress;

    public ControlKnob(Context context, AttributeSet attrs) {
        super(context, attrs);
        arc = new RectF(0, 0, 0, 0);
        touchArc = new RectF(0, 0, 0, 0);
        middle = new RectF(0, 0, 0, 0);
        colors = new float[3];
        executeCallback = true;
        setProgressWithoutCallback(0);
        /*
        Handler handler = new Handler();
        for (int i = -5; i <= 200; i++) {
            final int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (finalI % 2 == 0)
                        setProgress(finalI);
                    else
                        setProgressWithoutCallback(finalI);
                }
            }, 100 * i);
        }
        */
    }

    public void setKnobListener(KnobListener knobListener) {
        this.knobListener = knobListener;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        progressAngle = calculateProgressAngle(progress, FULL_ROTATION_ANGLE);
        executeCallback = true;
        invalidate();
    }

    public void setProgressWithoutCallback(double progress) {
        this.progress = progress;
        progressAngle = calculateProgressAngle(progress, FULL_ROTATION_ANGLE);
        executeCallback = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        int valueToUse = 0;
        if (canvas.getHeight() < canvas.getWidth())
            valueToUse = canvas.getHeight();
        else
            valueToUse = canvas.getWidth();
        centerX = valueToUse / 2;
        centerY = valueToUse / 2;
        zeroDegreeX = valueToUse;
        zeroDegreeY = valueToUse / 2;

        //The Base of the Knob
        Color.colorToHSV(color, colors);
        colors[2] = colors[2] * 0.3F;
        int newColor = Color.HSVToColor(colors);
        paint.setColor(newColor);
        arc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(arc, -ROTATION_DEGREE_START, FULL_ROTATION_ANGLE, true, paint);

        //The Touch area of the Knob, changed when you touch
        Color.colorToHSV(color, colors);
        colors[2] = colors[2] * 0.6F;
        newColor = Color.HSVToColor(colors);
        paint.setColor(newColor);
        paint.setStyle(Paint.Style.FILL);
        touchArc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
        canvas.drawArc(touchArc, -ROTATION_DEGREE_START, progressAngle, true, paint);

        //The Touch area of the Knob, changed when you touch
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        touchArc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
        canvas.drawArc(touchArc, -ROTATION_DEGREE_START, progressAngle, true, paint);

        //The Base of the Knob Stroke
        paint.setColor(color);
        arc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        canvas.drawArc(arc, -ROTATION_DEGREE_START, FULL_ROTATION_ANGLE, true, paint);

        //the middle of the Knob, the "empty" space
        float f = (float) valueToUse * 0.25F;
        paint.setColor(ContextCompat.getColor(getContext(), R.color.windowBgDark));
        paint.setStyle(Paint.Style.FILL);
        middle.set(f + stroke, f + stroke, ((arc.width() + f) - f * 2.0F) - stroke, ((arc.height() + f) - f * 2.0F) - stroke);
        canvas.drawArc(middle, 0, 360, true, paint);

        //the middle of the Knob
        f = (float) valueToUse * 0.3F;
        paint.setStyle(Paint.Style.FILL);
        Color.colorToHSV(color, colors);
        colors[2] = colors[2] * 0.3F;
        newColor = Color.HSVToColor(colors);
        paint.setColor(newColor);
        middle.set(f + stroke, f + stroke, ((arc.width() + f) - f * 2.0F) - stroke, ((arc.height() + f) - f * 2.0F) - stroke);
        canvas.drawArc(middle, 0, 360, true, paint);

        //the middle of the Knob stroke
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        middle.set(f + stroke, f + stroke, ((arc.width() + f) - f * 2.0F) - stroke, ((arc.height() + f) - f * 2.0F) - stroke);
        canvas.drawArc(middle, 0, 360, true, paint);

        //Alarm listeners
        if (executeCallback) {
            if (knobListener != null) {
                knobListener.onProgressChange((int) progress);
            }
            if (midiMessage != null) {
                midiMessage.setValue(MidiValue.valueOf("MIDI_VALUE_" + (int) progress));
                sendMidiMessage(midiMessage);
            }
        }
    }

    @Override
    protected boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                updateOnTouch(event);
                break;
        }
        return true;
    }

    @Override
    protected void updateControl(MidiMessage midiMessage) {
        setProgressWithoutCallback(midiMessage.getMidiValue().getValue());
    }

    private void updateOnTouch(MotionEvent event) {
        progressAngle = (float) calculate360Angle(event.getX(), event.getY());
        this.progress = calculateProgress(progressAngle, FULL_ROTATION_ANGLE);
        invalidate();
    }

    private double calculate360Angle(double x, double y) {
        x = x - centerX;
        y = y - centerY;
        double magnitude = Math.sqrt(x * x + y * y);
        double angle = 0;
        if (magnitude > 0)
            angle = Math.acos(x / magnitude);
        angle = angle * 180 / Math.PI;
        if (y < 0)
            angle = 360 - angle;
        angle -= 120;
        if (angle < 0)
            angle += ROTATION_DEGREE_START + 120;
        if (angle > 330)
            angle = 0;
        if (angle < 330 && angle > FULL_ROTATION_ANGLE)
            angle = FULL_ROTATION_ANGLE;
        return angle;
    }

    private int calculateProgress(float angle, float rotation) {
        double tmp = angle / (double) rotation;
        return (int) (tmp * 127);
    }

    private int calculateProgressAngle(double progress, float rotation) {
        if (progress < 0)
            progress = 0;
        if (progress > 127)
            progress = 127;
        return (int) ((progress / (double) 127) * rotation);
    }


    //region Listeners
    public interface KnobListener {
        void onProgressChange(int progress);
    }
    //endregion
}
