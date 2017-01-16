package be.vanlooverenkoen.midimacro.ui.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.MidiMessage;
import be.vanlooverenkoen.midimacro.model.MidiValue;

public class ControlSliderV extends Control {
    protected final int thumbThickness = 60;
    protected double progress;
    protected double tmpProgress;
    protected RectF rect;
    protected RectF progresRect;
    protected RectF thumbRect;
    private int padding;
    protected float position;
    protected boolean calculateTouchEventProgress;
    protected SliderListener sliderListener;
    protected boolean centered;

    public ControlSliderV(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ControlSliderV);
        centered = a.getBoolean(R.styleable.ControlSliderV_centered, false);
        a.recycle();
        init();
    }

    public ControlSliderV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ControlSliderV, defStyle, 0);
        centered = a.getBoolean(R.styleable.ControlSliderV_centered, false);
        a.recycle();
        init();
    }

    private void init() {
        progress = 0;
        position = 0;
        padding = 20;
        calculateTouchEventProgress = false;
        rect = new RectF();
        progresRect = new RectF();
        thumbRect = new RectF();
        executeCallback = true;
        if (centered)
            setProgressWithoutCallback(63);
        else
            setProgressWithoutCallback(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double progress = this.tmpProgress;
        if (calculateTouchEventProgress) {
            progress = (int) ((canvas.getHeight() - thumbThickness / 2 - stroke) - position);
            if (progress < 0)
                progress = 0;
            else if (progress > canvas.getHeight())
                progress = canvas.getHeight();
            progress = (progress * 127) / getHeight();

        }
        if (this.progress != progress) {
            this.progress = progress;
            this.tmpProgress = this.progress;
            if (executeCallback) {
                if (sliderListener != null)
                    sliderListener.onProgressChange((int) this.progress);
                if (midiMessage != null) {
                    midiMessage.setValue(MidiValue.valueOf("MIDI_VALUE_" + (int) this.progress));
                    sendMidiMessage(midiMessage);
                }
            }
        }
        executeCallback = true;
        //Slider With no Progress Background
        float colors[] = new float[3];
        //covert color to hue saturation, value
        Color.colorToHSV(color, colors);
        int i;
        colors[2] = colors[2] * 0.3F;
        i = Color.HSVToColor(colors);
        paint.setColor(i);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        paint.setAntiAlias(true);
        rect.set(0 + stroke
                , 0 + stroke
                , canvas.getWidth() - stroke
                , canvas.getHeight() - stroke);
        canvas.drawRoundRect(rect
                , roundedCorners
                , roundedCorners
                , paint);

        //Slider Progress Background
        if (!calculateTouchEventProgress) {
            if (centered)
                position = (float) ((canvas.getHeight() - thumbThickness / 2) - ((progress * canvas.getHeight()) / 127));
            else
                position = (float) (canvas.getHeight() - ((progress * canvas.getHeight()) / 127));
        }
        if (centered) {
            if (position < 0)
                progresRect.set(0 + stroke
                        , 0 + stroke
                        , canvas.getWidth() - stroke
                        , (canvas.getHeight() / 2) - stroke);
            else if (position > canvas.getHeight() - thumbThickness - stroke)
                progresRect.set(0 + stroke
                        , (canvas.getHeight() / 2) - stroke
                        , canvas.getWidth() - stroke
                        , canvas.getHeight() - stroke);
            else {
                if (progress >= 63.5)
                    progresRect.set(0 + stroke
                            , position + stroke
                            , canvas.getWidth() - stroke
                            , (canvas.getHeight() / 2) - stroke);
                else
                    progresRect.set(0 + stroke
                            , (canvas.getHeight() / 2) - stroke
                            , canvas.getWidth() - stroke
                            , position + thumbThickness + stroke);
            }
        } else {
            if (position < 0)
                progresRect.set(0 + stroke
                        , 0 + stroke
                        , canvas.getWidth() - stroke
                        , canvas.getHeight() - stroke);
            else if (position > canvas.getHeight() - thumbThickness - stroke)
                progresRect.set(0 + stroke
                        , canvas.getHeight() - thumbThickness - stroke
                        , canvas.getWidth() - stroke
                        , canvas.getHeight() - stroke);
            else {
                progresRect.set(0 + stroke
                        , position + stroke
                        , canvas.getWidth() - stroke
                        , canvas.getHeight() - stroke);
            }
        }
        Color.colorToHSV(color, colors);
        colors[2] = colors[2] * 0.6F;
        i = Color.HSVToColor(colors);
        paint.setColor(i);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        canvas.drawRoundRect(progresRect
                , roundedCorners
                , roundedCorners
                , paint);

        //Thumb Slider
        thumbRect.set(0 + stroke
                , position + stroke
                , canvas.getWidth() - stroke
                , position + thumbThickness + stroke);
        paint.setColor(color);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        if (position < 0)
            thumbRect.set(thumbRect.left, 0 + stroke, thumbRect.right, thumbThickness + stroke);
        else if (position > canvas.getHeight() - thumbThickness - stroke)
            thumbRect.set(thumbRect.left
                    , canvas.getHeight() - stroke - thumbThickness
                    , thumbRect.right
                    , canvas.getHeight() - stroke);
        canvas.drawRoundRect(thumbRect
                , roundedCorners
                , roundedCorners
                , paint);

        //Stroke Slider
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        canvas.drawRoundRect(rect
                , roundedCorners
                , roundedCorners
                , paint);

        calculateTouchEventProgress = false;
    }

    public void setProgress(double value) {
        tmpProgress = value;
        executeCallback = true;
        invalidate();
    }

    public void setProgressWithoutCallback(double value) {
        tmpProgress = value;
        executeCallback = false;
        invalidate();
    }

    @Override
    protected boolean onTouch(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_CANCEL) {
            handleTouchEvent(event);
        }
        return event.getAction() != MotionEvent.ACTION_CANCEL;
    }

    protected void handleTouchEvent(MotionEvent motionevent) {
        if (motionevent.getAction() == MotionEvent.ACTION_MOVE
                || motionevent.getAction() == MotionEvent.ACTION_DOWN) {
            position = motionevent.getY();
            calculateTouchEventProgress = true;
            invalidate();
        }
    }

    public void setSliderListener(SliderListener sliderListener) {
        this.sliderListener = sliderListener;
    }


    @Override
    protected void updateControl(MidiMessage midiMessage) {
        setProgressWithoutCallback(midiMessage.getMidiValue().getValue());
    }

    //region Listeners
    public interface SliderListener {
        void onProgressChange(int progress);
    }
    //endregion
}
