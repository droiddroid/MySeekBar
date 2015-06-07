package pl.mradtke.myseekbar.listeners;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by michal.radtke@gmail.com on 2015-06-05.
 * <p/>
 * This class is a custom drag listener and is responsible for drag and drop feature.
 */
public class CustomDragListener implements View.OnDragListener {

    @Override
    public boolean onDrag(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                View seekBarView = (View) event.getLocalState();
                seekBarView.setX(event.getX());
                seekBarView.setY(event.getY());

                ViewGroup seekBarParentView = (ViewGroup) seekBarView.getParent();
                seekBarParentView.removeView(seekBarView);

                RelativeLayout layoutForDrop = (RelativeLayout) view;
                layoutForDrop.addView(seekBarView);
                seekBarView.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
            default:
                break;
        }
        return true;
    }
}
