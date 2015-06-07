package pl.mradtke.myseekbar.widgets;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

import pl.mradtke.myseekbar.R;

import static android.graphics.Paint.Style;

/**
 * Created by michal.radtke@gmail.com on 2015-06-05.
 * <p/>
 * This class extends SeekBar class and implements two features: view drag & drop and seek bar change listener.
 */
public class CustomSeekBar extends SeekBar implements SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    private static final String SECONDS_CHAR = "s";
    private static final long TIMER_DELAY = 0;
    private static final long TIMER_PERIOD = 1000;

    private Timer timer;
    private Activity activity;
    private int timeCounterColor;
    private int secondsCounter;

    public CustomSeekBar(Context context) {
        super(context);
    }

    /**
     * This constructor is called automatically when we create instance of this class. It is responsible for getting
     * view attributes from xml setting of this view.
     *
     * @param context is need to getting access to styled attributed of view.
     * @param attrs   are attributes of view (getting from xml).
     */
    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomSeekBar,
                0, 0
        );
        try {
            // set a color of the seek bar timer (from xml, default black)
            this.timeCounterColor = attributes.getColor(R.styleable.CustomSeekBar_timeCounterColor, Color.BLACK);
        } finally {
            attributes.recycle();
        }
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //if progress is lower than 25%, then sets it on 0% and reset seconds counter
        if (progress < 25) {
            seekBar.setProgress(0);
            secondsCounter = 0;
        }
        //if progress is lower than 75% and greater than 25%, then set it on 50% and reset seconds counter
        else if (progress < 75 && progress > 25) {
            seekBar.setProgress(50);
            secondsCounter = 0;
        }
        //if progress is greater than 75%, then set it on 100% and reset seconds counter
        else if (progress > 75) {
            seekBar.setProgress(100);
            secondsCounter = 0;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (timer == null) {
            runTimer(seekBar, timeCounterColor);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //if user touch this view, then he can drag and drop it wherever he want
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method passes activity instance for this class. Activity is need to working on ui thread.
     *
     * @param activity is passed from activity where this class is initialized
     */
    public void init(Activity activity) {
        this.activity = activity;
    }

    /**
     * This method is responsible for running timer when user set a progress of the seek bar first time. User cans
     * choose color of seek bar timer both from code and xml.
     *
     * @param seekBar is a seek bar instance on which this method will be working
     * @param color   is a color of timer in seek bar
     */
    private void runTimer(final SeekBar seekBar, final int color) {
        if (activity == null) {
            throw new NullPointerException("You have to first init CustomSeekBar class");
        }
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //decode drawable background to bitmap
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.seek_bg)
                                .copy(Bitmap.Config.ARGB_8888, true);

                        // initialize paint object and set text properties (style, color, size)
                        Paint paint = new Paint();
                        paint.setStyle(Style.FILL);
                        paint.setColor(color);
                        paint.setTextSize(35);

                        // initialize canvas on which this method will draw a time from timer
                        Canvas canvas = new Canvas(bm);
                        canvas.drawText(secondsCounter + SECONDS_CHAR, 0, seekBar.getHeight() + 40, paint);
                        // set new seek bar background with prepared time
                        seekBar.setBackground(new BitmapDrawable(bm));
                        //increase second counter
                        secondsCounter++;
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, TIMER_DELAY, TIMER_PERIOD);
    }

    /**
     * This method stops the timer if exists.
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * This method sets a color of the seek bar timer (from code).
     *
     * @param color is a specified color
     */
    public void setTimeCounterColor(int color) {
        this.timeCounterColor = color;
    }
}
