package edwardwang.bouncingball.InteractionLayer.ESensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.InteractionLayer.InteractionSetup;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DFloat;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Accelerometer
 * -Acceleration along 3 axes in m/s^2
 *
 * Gyroscope
 * -Rate at device rotations along 3 axes. Integrate over time to get orientation.
 * -https://www.youtube.com/watch?v=zVoxUMitLV8
 *
 * Rotation
 * -Orientation of device as a combination of an angle around an axis
 * -http://stackoverflow.com/questions/14740808/android-problems-calculating-the-orientation-of-the-device
 *
 * Gravity
 * -Acceleration due to gravity on 3 axes.
 *
 * Created by edwardwang on 8/8/16.
 */
public class ESensorManager implements InteractionSetup, SensorEventListener {
    private static final String className = ESensorManager.class.getSimpleName();

    private SensorManager sensorManager;
    //List of enums of which sensors dev wants to use for their app
    private ArrayList<ESensor> eSensorList;
    private ArrayList<Sensor> sensorList;

    private int sensorDelaySpeed = SensorManager.SENSOR_DELAY_NORMAL;
    ////////////////////////////////////////////////////////////////////////////////
    //Gyroscope
    private Sensor gyroscopeSensor;
    private Vector3DInt gryoscope;

    //Accelerometer
    private Sensor accelerometerSensor;
    private Vector3DDouble accelerometer;

    //Rotation - measures orientation of device
    private Sensor rotationSensor;
    private Vector3DFloat rotation;
        //need a 4x4 rotation matrix
    private float[] rotationMatrix = new float[16];
    private float[] orientationVals = new float[3];

    //Gravity
    private Sensor gravitySensor;
    private Vector3DDouble gravity;

    ////////////////////////////////////////////////////////////////////////////////
    public ESensorManager(Context context){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        eSensorList = new ArrayList<>();
        sensorList = new ArrayList<>();
    }

    @Override
    public void setup() {
        for(ESensor eSensor: eSensorList){
            switch (eSensor){
                case Gyroscope:
                    setupGyroscope();
                    break;
                case Accelerometer:
                    setupAccelerometer();
                    break;
                case Rotation:
                    setupRotation();
                    break;
                case Gravity:
                    setupGravity();
                    break;
            }
        }
        InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_SetupESensorManager);
    }

    public void addESensorToList(ESensor eSensor){
        eSensorList.add(eSensor);
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Setup
    private void setupGyroscope(){
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        addSensorToList(gyroscopeSensor);
    }
    private void setupAccelerometer(){
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        addSensorToList(accelerometerSensor);
    }
    private void setupRotation(){
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        addSensorToList(rotationSensor);
        rotationMatrix[0] = 1;
        rotationMatrix[4] = 1;
        rotationMatrix[8] = 1;
        rotationMatrix[12] = 1;
    }
    private void setupGravity(){
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        addSensorToList(gravitySensor);
    }

    private void addSensorToList(Sensor sensor){
        if(sensor!=null){
            sensorList.add(sensor);
        }else{
            InfoLog.getInstance().generateLog(className,
                    InfoLog.getInstance().error_SensorNotFound +
                            sensor.getStringType());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Sensor pause/resume
    public void startSensors(){
        for(Sensor sensor: sensorList){
            sensorManager.registerListener(this, sensor, sensorDelaySpeed);
        }
        InfoLog.getInstance().generateLog(className,
                InfoLog.getInstance().debug_StartESensorManager);
    }

    public void stopSensors(){
        sensorManager.unregisterListener(this);
        InfoLog.getInstance().generateLog(className,
                InfoLog.getInstance().debug_StopESensorManager);
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Sensors Implemented Methods

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor == gyroscopeSensor){
            updateGyroscope(event);
        }else if(sensor == accelerometerSensor){
            updateAccelerometer(event);
        }else if(sensor == rotationSensor){
            updateRotation(event);
        }else if(sensor == gravitySensor){
            updateGravity(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    ////////////////////////////////////////////////////////////////////////////////
    //Gyroscope
    private void updateGyroscope(SensorEvent event){

    }

    ////////////////////////////////////////////////////////////////////////////////
    //Accelerometer
    private void updateAccelerometer(SensorEvent event){

    }

    ////////////////////////////////////////////////////////////////////////////////
    //Rotation
    private void updateRotation(SensorEvent event){
        //convert rotation vector into 4x4 matrix
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                SensorManager.AXIS_Z, rotationMatrix);
        SensorManager.getOrientation(rotationMatrix, orientationVals);
        //convert orientation from radians to degrees
        rotation.setX(convertRadiansToDegrees(orientationVals[0]));
        rotation.setY(convertRadiansToDegrees(orientationVals[1]));
        rotation.setZ(convertRadiansToDegrees(orientationVals[2]));

        InfoLog.getInstance().debugValue(className, "OrientationX: "+rotation.getX());
        InfoLog.getInstance().debugValue(className, "OrientationY: "+rotation.getY());
        InfoLog.getInstance().debugValue(className, "OrientationZ: "+rotation.getZ());
    }

    private float convertRadiansToDegrees(float radians){
        return (float)Math.toDegrees(radians);
    }
    ////////////////////////////////////////////////////////////////////////////////
    //Gravity
    private void updateGravity(SensorEvent event){

    }
    ////////////////////////////////////////////////////////////////////////////////
    //Getter
    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public int getSensorDelaySpeed() {
        return sensorDelaySpeed;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Setter

    public void setSensorDelaySpeed(int sensorDelaySpeed) {
        this.sensorDelaySpeed = sensorDelaySpeed;
    }
}
