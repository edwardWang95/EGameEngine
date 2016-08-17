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
    private Vector3DInt gyroscope;

    //Accelerometer
    private Sensor accelerometerSensor;
    private Vector3DDouble accelerometer;

    //Rotation - measures orientation of device
    private Sensor rotationSensor;
    private final int rotationLEFT = 30;
    private final int rotationRIGHT = -30;
    private Vector3DFloat rotation;
    //need a 4x4 rotation matrix
    private float[] rotationMatrix = new float[16];
    private float[] orientationVals = new float[3];

    //Gravity
    private Sensor gravitySensor;
    private Vector3DDouble gravity;

    //Magnetic Field
    private Sensor magneticFieldSensor;
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
    }
    private void setupAccelerometer(){
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        addSensorToList(accelerometerSensor);
    }
    private void setupRotation(){
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        rotation = new Vector3DFloat();
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

    private void setupMagneticField(){
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        addSensorToList(magneticFieldSensor);
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
            updateGyroscope(event);
        }else if(sensor == accelerometerSensor){
            updateAccelerometer(event);
        }else if(sensor == rotationSensor){
            updateRotation(event);
        }else if(sensor == gravitySensor){
            updateGravity(event);
        }else if(sensor == magneticFieldSensor){
            updateMagneticField(event);
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
    }

    private float convertRadiansToDegrees(float radians){
        return (float)Math.toDegrees(radians);
    }
    ////////////////////////////////////////////////////////////////////////////////
    //Gravity
    private void updateGravity(SensorEvent event){

    }
    ////////////////////////////////////////////////////////////////////////////////
    //Magnetic Field
    private void updateMagneticField(SensorEvent event){

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
    public Vector3DInt getGryoscope() {
        return gyroscope;
    }

    //Accelerometer
    public Vector3DDouble getAccelerometer() {
        return accelerometer;
    }

    //Rotation
    public Vector3DFloat getRotation() {
        return rotation;
    }

    public int getRotationLEFT() {
        return rotationLEFT;
    }

    public int getRotationRIGHT() {
        return rotationRIGHT;
    }

    //Gravity
    public Vector3DDouble getGravity() {
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
