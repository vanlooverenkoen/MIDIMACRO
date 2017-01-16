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

/**
 * Created by Koen on 2/01/2017.
 */
public class ControlEncoder extends Control {
    int centerX;
    int centerY;
    double zeroDegreeX;
    double zeroDegreeY;
    private float angle;
    private final RectF middle;
    private final RectF arc;
    private final RectF touchArc;
    private float[] colors;
    private EncoderListener encoderListener;
    private float previousAngle;
    private boolean touchDown;
    private boolean toggled;

    public ControlEncoder(Context context, AttributeSet attrs) {
        super(context, attrs);
        angle = 120;
        arc = new RectF(0, 0, 0, 0);
        touchArc = new RectF(0, 0, 0, 0);
        middle = new RectF(0, 0, 0, 0);
        colors = new float[3];
    }

    public void setEncoderListener(EncoderListener encoderListener) {
        this.encoderListener = encoderListener;
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
        canvas.drawArc(arc, 0, 360, true, paint);

        //The Base of the Knob Stroke
        paint.setColor(color);
        arc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        canvas.drawArc(arc, 0, 360, true, paint);

        if (touchDown) {
            //The Touch area of the Knob, changed when you touch
            Color.colorToHSV(color, colors);
            colors[2] = colors[2] * 0.6F;
            newColor = Color.HSVToColor(colors);
            paint.setColor(newColor);
            paint.setStyle(Paint.Style.FILL);
            touchArc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
            canvas.drawArc(touchArc, angle, 40, true, paint);

            //The Touch area of the Knob, changed when you touch
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            touchArc.set(0 + stroke, 0 + stroke, valueToUse - stroke, valueToUse - stroke);
            canvas.drawArc(touchArc, angle, 40, true, paint);
        }

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
    }

    @Override
    protected boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchDown = true;
                updateOnTouch(event);
                break;
            default:
                touchDown = false;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void updateControl(MidiMessage midiMessage) {
        throw new RuntimeException("Not yet implemented");
    }

    private void updateOnTouch(MotionEvent event) {
        angle = (float) calculate360Angle(event.getX(), event.getY());
        if (encoderListener != null)
            if (angle > previousAngle + 25) {
                encoderListener.onUp();
                previousAngle = angle;
            } else if (angle < previousAngle - 25) {
                encoderListener.onDown();
                previousAngle = angle;
            }
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
        angle -= 20;
        return angle;
    }

    //region Listeners
    public interface EncoderListener {
        void onUp();

        void onDown();
    }
    //endregion
}
