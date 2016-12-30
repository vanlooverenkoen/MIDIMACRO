package be.vanlooverenkoen.midi.ui.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class MixerSliderVertical extends MixerSlider implements SeekBar.OnSeekBarChangeListener {

    private boolean callback;
    private OnSeekBarChangeListener onSeekBarChangeListener;

    public MixerSliderVertical(Context context) {
        super(context);
        init();
    }

    public MixerSliderVertical(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MixerSliderVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        callback = true;
        super.setOnSeekBarChangeListener(this);
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public synchronized void setProgressAnimateWithoutCallback(int progress) {
        callback = false;
        setProgress(progress, true);
        callback = true;
    }

    public synchronized void setProgressWithoutCallback(int progress) {
        callback = false;
        setProgress(progress);
        callback = true;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (onSeekBarChangeListener != null && callback)
            onSeekBarChangeListener.onProgressChanged(seekBar, i, b);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (onSeekBarChangeListener != null)
            onSeekBarChangeListener.onStartTrackingTouch(seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (onSeekBarChangeListener != null)
            onSeekBarChangeListener.onStopTrackingTouch(seekBar);
    }
}