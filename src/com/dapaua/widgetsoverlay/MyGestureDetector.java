package com.dapaua.widgetsoverlay;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

class MyGestureDetector extends SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	int SWIPE_MIN_DISTANCE = 120;
    	int SWIPE_MAX_OFF_PATH = 250;
    	int SWIPE_THRESHOLD_VELOCITY = 200;
    	
    	try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println( "Left Swipe");
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	System.out.println( "Right Swipe");
            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }
}
