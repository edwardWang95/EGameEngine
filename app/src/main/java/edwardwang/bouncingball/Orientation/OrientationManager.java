package edwardwang.bouncingball.Orientation;


import android.content.Context;

import edwardwang.bouncingball.Orientation.ESensor.ESensor;
import edwardwang.bouncingball.Orientation.ESensor.ESensorManager;

/**
 * Created by edwardwang on 8/16/16.
 */
public class OrientationManager {
    private ESensorManager sensorManager;
    private Orientation orientation;

    public void setupOrientationManager(Context context){
        sensorManager = new ESensorManager(context);
    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public void setupOrientation(){
        switch (orientation){
            case CalibratedGyroscope:
                sensorManager.addESensorToList(ESensor.Gyroscope);
                break;
        }
        sensorManager.setupSensors();
    }

    public ESensorManager getSensorManager() {
        sensorManager.setupSensors();
        return sensorManager;
    }

    /**
     * In the case that dev doesn't want to use orientation, catch for null reference
     * when checking on sensorManager
     */
    public void pause(){
        if(sensorManager != null){
            sensorManager.pauseSensors();
        }
    }

    public void resume(){
        if(sensorManager != null){
            sensorManager.resumeSensors();
        }
    }
}

