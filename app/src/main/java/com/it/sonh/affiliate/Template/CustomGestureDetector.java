package com.it.sonh.affiliate.Template;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

/**
 * Created by sonho on 2/27/2018.
 */

public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private ViewFlipper viewFlipper;
    public CustomGestureDetector(ViewFlipper viewFlipper){
        this.viewFlipper = viewFlipper;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Swipe left (next)
        if (e1.getX() > e2.getX()) {
            viewFlipper.showNext();
        }
        // Swipe right (previous)
        if (e1.getX() < e2.getX()) {
            viewFlipper.showPrevious();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
