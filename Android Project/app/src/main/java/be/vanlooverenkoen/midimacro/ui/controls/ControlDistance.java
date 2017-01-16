package be.vanlooverenkoen.midimacro.ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import be.vanlooverenkoen.midimacro.model.MidiMessage;

public class ControlDistance extends Control {
    private String distance;

    public ControlDistance(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControlDistance(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        distance = "";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(100);
        if (distance.length() == 2)
            canvas.drawText(distance, (float) (canvas.getWidth() / 2) - 50, (float) canvas.getHeight() / 2, paint);
        else if (distance.length() == 3)
            canvas.drawText(distance, (float) (canvas.getWidth() / 2) - 75, (float) canvas.getHeight() / 2, paint);
    }

    @Override
    protected boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    protected void updateControl(MidiMessage midiMessage) {
        distance = String.valueOf(midiMessage.getMidiValue().getValue());
        invalidate();
    }
}
