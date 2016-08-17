package edwardwang.bouncingball.Orientation.ESensor;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DInt;

/**
 * The sensor manager will directly update the orientation information
 *
 * Orientation of the phone
 * -https://developer.android.com/guide/topics/sensors/sensors_overview.html
 *
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
 * -X: + when turning LEFT             === - when turning RIGHT
 * -Y: + when turning phone AWAY from user  === - when turning phone TOWARD user
 * -Z: + when moving DOWNWARDS the ground === - when moving UPWARDS to the sky
 *
 * Gravity
 * -Acceleration due to gravity on 3 axes.
 *
 * Magnetic Field
 *
 *
 * Created by edwardwang on 8/8/16.
 */
public class ESensorManager implements SensorEventListener {
    private static final String className = ESensorManager.class.getSimpleName();

    private SensorManager sensorManager;
    //List of enums of which sensors dev wants to use for their app
    private ArrayList<ESensor> eSensorList;
    private ArrayList<Sensor> sensorList;

    private int sensorDelaySpeed = SensorManager.SENSOR_DELAY_NORMAL;
    ////////////////////////////////////////////////////////////////////////////////
    //Gyroscope
    private Sensor gyroscopeSensor;
    private EGyroscope eGyroscope;
    private Vector3DFloat gyroscope;

    //Accelerometer
    private Sensor accelerometerSensor;
    private EAccelerometer eAccelerometer;
    private Vector3DFloat accelerometer;

    //Rotation - measures orientation of device
    private Sensor rotationSensor;
    private ERotation eRotation;
    private Vector3DFloat rotation;

    //Gravity
    private Sensor gravitySensor;
    private EGravity eGravity;
    private Vector3DFloat gravity;

    //Magnetic Field
    private Sensor magneticFieldSensor;
    private EMagneticField eMagneticField;
    private Vector3DFloat magneticField;

    ////////////////////////////////////////////////////////////////////////////////
    public ESensorManager(Context context){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        eSensorList = new ArrayList<>();
        sensorList = new ArrayList<>();
    }

    public void setupSensors() {
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
                case Magnetic_Field:
                    setupMagneticField();
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
        gyroscope = new Vector3DFloat();
        eGyroscope = new EGyroscope(gyroscope);

    }
    private void setupAccelerometer(){
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        addSensorToList(accelerometerSensor);
        accelerometer = new Vector3DFloat();
        eAccelerometer = new EAccelerometer(accelerometer);
    }
    private void setupRotation(){
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        addSensorToList(rotationSensor);
        rotation = new Vector3DFloat();
        eRotation = new ERotation(rotation);
    }
    private void setupGravity(){
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        addSensorToList(gravitySensor);
        gravity = new Vector3DFloat();
        eGravity = new EGravity(gravity);

    }

    private void setupMagneticField(){
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        addSensorToList(magneticFieldSensor);
        magneticField = new Vector3DFloat();
        eMagneticField = new EMagneticField(magneticField);
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
    public void resumeSensors(){
        for(Sensor sensor: sensorList){
            sensorManager.registerListener(this, sensor, sensorDelaySpeed);
        }
        InfoLog.getInstance().generateLog(className,
                InfoLog.getInstance().debug_StartESensorManager);
    }

    public void pauseSensors(){
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
            //updateGyroscope(event);
        }else if(sensor == accelerometerSensor){
            //updateAccelerometer(event);
        }else if(sensor == rotationSensor){
            eRotation.updateSensor(event);
        }else if(sensor == gravitySensor){
            //updateGravity(event);
        }else if(sensor == magneticFieldSensor){
            //updateMagneticField(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    ////////////////////////////////////////////////////////////////////////////////
    //Getter
    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public int getSensorDelaySpeed() {
        return sensorDelaySpeed;
    }

    //Gyroscope
    public Vector3DFloat getGryoscope() {
        return gyroscope;
    }

    //Accelerometer
    public Vector3DFloat getAccelerometer() {
        return accelerometer;
    }

    //Rotation
    public Vector3DFloat getRotation() {
        return rotation;
    }

    //Gravity
    public Vector3DFloat getGravity() {
        return gravity;
    }

    //////////////////////////////////////////
    public Sensor getGyroscopeSensor() {
        return gyroscopeSensor;
    }

    public Sensor getAccelerometerSensor() {
        return accelerometerSensor;
    }

    public Sensor getRotationSensor() {
        return rotationSensor;
    }

    public Sensor getGravitySensor() {
        return gravitySensor;
    }

    public Sensor getMagneticFieldSensor() {
        return magneticFieldSensor;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Setter
    public void setSensorDelaySpeed(int sensorDelaySpeed) {
        this.sensorDelaySpeed = sensorDelaySpeed;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Info Log - debug sensor information is being updated
    private void debugGyroscope(){
        InfoLog.getInstance().debugValue(className, "GyroscopeX: "+gyroscope.getX());
        InfoLog.getInstance().debugValue(className, "GyroscopeY: "+gyroscope.getY());
        InfoLog.getInstance().debugValue(className, "GyroscopeZ: "+gyroscope.getZ());
    }
    private void debugAccelerometer(){
        InfoLog.getInstance().debugValue(className, "AccelerometerX: "+accelerometer.getX());
        InfoLog.getInstance().debugValue(className, "AccelerometerY: "+accelerometer.getY());
        InfoLog.getInstance().debugValue(className, "AccelerometerZ: "+accelerometer.getZ());
    }
    private void debugRotation(){
        InfoLog.getInstance().debugValue(className, "OrientationX: "+rotation.getX());
        InfoLog.getInstance().debugValue(className, "OrientationY: "+rotation.getY());
        InfoLog.getInstance().debugValue(className, "OrientationZ: "+rotation.getZ());
    }

    private void debugGravity(){
        InfoLog.getInstance().debugValue(className, "GravityX: "+gravity.getX());
        InfoLog.getInstance().debugValue(className, "GravityY: "+gravity.getY());
        InfoLog.getInstance().debugValue(className, "GravityZ: "+gravity.getZ());
    }
}
