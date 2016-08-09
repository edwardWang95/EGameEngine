package edwardwang.bouncingball.Interaction.EGesture;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import edwardwang.bouncingball.Interaction.InteractionSetup;

/**
 * Created by edwardwang on 8/8/16.
 */
public class EGestureManager implements InteractionSetup, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{
    private static final String className = EGestureManager.class.getSimpleName();

    private GestureDetectorCompat mDetector;

    private Context context;

    public EGestureManager(Context context){
        this.context = context;
    }

    @Override
    public void setup() {

    }

    ////////////////////////////////////////////////////////////////////////
    //Gesture Override methods
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
