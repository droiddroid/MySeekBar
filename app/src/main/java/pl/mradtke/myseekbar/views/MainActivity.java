package pl.mradtke.myseekbar.views;

import android.app.Activity;
import android.os.Bundle;

import pl.mradtke.myseekbar.R;
import pl.mradtke.myseekbar.listeners.CustomDragListener;
import pl.mradtke.myseekbar.widgets.CustomSeekBar;

/**
 * Created by michal.radtke@gmail.com on 2015-06-05.
 * <p/>
 * This is a main activity class for this application.
 */
public class MainActivity extends Activity {

    private CustomSeekBar customSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customSeekBar = (CustomSeekBar) findViewById(R.id.seekBar);
        customSeekBar.init(this);
        customSeekBarTimerFeature(customSeekBar);
        // Google doesn't provide on long click event for seek bars and on touch event cannot work with on seek
        // progress change listener. On long click event can be created by own implementation (working with threads)
        // but probably the solution will be ugly and kind of a hack. In that's way I implemented required features
        // but You must switch between drag & drop and progress change in the code. To do this please comment
        // customSeekBarTimerFeature method and uncomment customSeekBarDragAndDropFeature method.

        //customSeekBarDragAndDropFeature(customSeekBar);
    }

    /**
     * When app goes to background stop the timer.
     */
    @Override
    protected void onPause() {
        super.onPause();
        customSeekBar.stopTimer();
    }

    /**
     * This method is responsible for enabled timer feature for custom seek bar. User cans set a color of the seek
     * bar timer.
     *
     * @param customSeekBar is a instance of the custom seek bar on which this method works.
     */
    private void customSeekBarTimerFeature(CustomSeekBar customSeekBar) {
        customSeekBar.setTimeCounterColor(getResources().getColor(R.color.light_green));
        customSeekBar.setOnSeekBarChangeListener(customSeekBar);
    }

    /**
     * This method is responsible for enabled drag and drop feature for custom seek bar.
     *
     * @param customSeekBar is a instance of the custom seek bar on which this method works.
     */
    private void customSeekBarDragAndDropFeature(CustomSeekBar customSeekBar) {
        //at firsts we have to sets on custom drag listener for a view fo this activity
        findViewById(R.id.mainView).setOnDragListener(new CustomDragListener());
        customSeekBar.setOnTouchListener(customSeekBar);
    }
}
