package edwardwang.bouncingball.Orientation;


import android.content.Context;

import edwardwang.bouncingball.Orientation.ESensor.CalibratedGyroscope;
import edwardwang.bouncingball.Orientation.ESensor.ESensorManager;
import edwardwang.bouncingball.PhysicsEngine.Vector.Quaternion;

/**
 * Reference info is from --> sensorFusionDemo on github
 *
 * Developer can either grab a custom orientation provider or directly grab the
 * sensorManager.
 *
 * Created by edwardwang on 8/16/16.
 */
public class OrientationManager {
    private Context context;
    private ESensorManager sensorManager;
    private Orientation orientation;

    /**
     * Synctoken will be used for syncing reading/writing of sensor data from
     * sensor manager and custom orientation algorithms
     */
    private final Object syncToken = new Object();

    //Rotation matrix from vector
    private MatrixFloat4By4 currentOrientationRotationVector;

    //Custom Orientations
    private CalibratedGyroscope calibratedGyroscope;

    public OrientationManager() {
    }

    public void setupOrientationManager(Context context){
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////
    public ESensorManager getSensorManager() {
        if(sensorManager == null){
            sensorManager = new ESensorManager(context);
        }
        return sensorManager;
    }

    //Calibrate Gyroscope
    public CalibratedGyroscope getCalibratedGyroscope() {
        if(calibratedGyroscope == null){
            calibratedGyroscope = new CalibratedGyroscope(context, syncToken);
        }
        return calibratedGyroscope;
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * MIGHT delete
     * TODO:I don't necessarily like this system, I would rather be more flexible and
     * allow dev's to use get the custom calibration providers without setting anything
     * @param orientation
     */
    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public void setupCustomOrientation(){
        switch (orientation){
            case CalibratedGyroscope:
                calibratedGyroscope = new CalibratedGyroscope(context, syncToken);
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////
    //Getter
}

