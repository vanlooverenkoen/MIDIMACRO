package be.vanlooverenkoen.midimacro.ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import be.vanlooverenkoen.midimacro.model.MidiMessage;

/**
 * Created by Koen on 2/01/2017.
 */

public class ControlSliderH extends ControlSliderV {
    public ControlSliderH(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double progress = this.tmpProgress;
        if (calculateTouchEventProgress) {
            progress = (int) ((canvas.getWidth() - thumbThickness / 2 - stroke) - position);
            if (progress < 0)
                progress = 0;
            else if (progress > canvas.getWidth())
                progress = canvas.getWidth();
            progress = (progress * 127) / getWidth();
        }
        if (this.progress != progress) {
            this.progress = progress;
            if (executeCallback) {
                if (sliderListener != null)
                    sliderListener.onProgressChange((int) progress);
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
                position = (float) ((canvas.getWidth() - thumbThickness / 2) - ((progress * canvas.getWidth()) / 127));
            else
                position = (float) (canvas.getWidth() - ((progress * canvas.getWidth()) / 127));
        }
        if (centered) {
            if (position < 0)
                progresRect.set(0 + stroke
                        , 0 + stroke
                        , (canvas.getWidth() / 2) - stroke
                        , canvas.getHeight() - stroke);
            else if (position > canvas.getWidth() - thumbThickness - stroke)
                progresRect.set((canvas.getWidth() / 2) - stroke,
                        0 + stroke
                        , canvas.getWidth() - stroke
                        , canvas.getHeight() - stroke);
            else {
                if (progress >= 63.5)
                    progresRect.set(position + stroke
                            , 0 + stroke
                            , canvas.getWidth() / 2 - stroke
                            , canvas.getHeight() - stroke);
                else
                    progresRect.set(canvas.getWidth() / 2 - stroke
                            , 0 + stroke
                            , position + thumbThickness - stroke
                            , canvas.getHeight() - stroke);
            }
        } else {
            if (position < 0)
                progresRect.set(0 + stroke
                        , 0 + stroke
                        , canvas.getWidth() - stroke
                        , canvas.getHeight() - stroke);
            else if (position > canvas.getWidth() - thumbThickness - stroke)
                progresRect.set(canvas.getWidth() - stroke,
                        0 + stroke
                        , canvas.getWidth() - thumbThickness - stroke
                        , canvas.getHeight() - stroke);
            else {
                progresRect.set(position + stroke
                        , 0 + stroke
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
        thumbRect.set(position + stroke
                , 0 + stroke
                , position + thumbThickness + stroke
                , canvas.getHeight() - stroke);
        paint.setColor(color);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        if (position < 0)
            thumbRect.set(0 + stroke, thumbRect.top, thumbThickness + stroke, thumbRect.bottom);
        else if (position > canvas.getWidth() - thumbThickness - stroke)
            thumbRect.set(canvas.getWidth() - stroke - thumbThickness
                    , thumbRect.top
                    , canvas.getWidth() - stroke
                    , thumbRect.bottom);
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

    @Override
    protected void handleTouchEvent(MotionEvent motionevent) {
        if (motionevent.getAction() == MotionEvent.ACTION_MOVE
                || motionevent.getAction() == MotionEvent.ACTION_DOWN) {
            position = motionevent.getX();
            calculateTouchEventProgress = true;
            invalidate();
        }
    }

    @Override
    public void setSliderListener(SliderListener sliderListener) {
        super.setSliderListener(sliderListener);
    }

    @Override
    protected void updateControl(MidiMessage midiMessage) {
        setProgressWithoutCallback(midiMessage.getMidiValue().getValue());
    }
}
