package edwardwang.bouncingball.Interaction.ESplitScreen;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.Info.PhoneInfo;
import edwardwang.bouncingball.Interaction.InteractionSetup;
import edwardwang.bouncingball.PhysicsEngine.Direction;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DDirection;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Created by edwardwang on 8/9/16.
 */
public class ESplitScreenManager implements InteractionSetup{
    private static final String className = ESplitScreenManager.class.getSimpleName();
    private Context context;
    private View view;
    private int midScreen;
    private Direction direction;
    private int positionX;
    private boolean isScreenTouched = false;
    private boolean isMoving = false;

    public ESplitScreenManager(Context context, View view){
        this.context = context;
        this.view = view;
    }


    @Override
    public void setup() {
        midScreen = PhoneInfo.getInstance().getScreenWidth() / 2;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        isScreenTouched = true;
                        positionX = (int)event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isMoving = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isScreenTouched = false;
                        break;
                }
                return true;
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////
    //Getter
    public int getPositionX() {
        return positionX;
    }

    public boolean isScreenTouched() {
        return isScreenTouched;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Direction getDirection(){
        if(positionX <= midScreen){
            direction = Direction.LEFT;
            //InfoLog.getInstance().generateLog(className, "LEFT");
        }else if(positionX > midScreen){
            direction = Direction.RIGHT;
            //InfoLog.getInstance().generateLog(className, "RIGHT");
        }
        return direction;
    }
}
