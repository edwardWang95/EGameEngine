package edwardwang.bouncingball.InteractionLayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import edwardwang.bouncingball.Info.InfoLog;

/**
 * Adjustable Values:
 * -sensorDelaySpeed (set how fast phone collects sensor updated values)
 * Created by edwardwang on 8/6/16.
 */
public class ESensorManager implements SensorEventListener{
    private static final String className = ESensorManager.class.getSimpleName();

    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    private int sensorDelaySpeed = SensorManager.SENSOR_DELAY_NORMAL;
    ////////////////////////////////////////////////////////////////////////////////
    //Gyroscope
    private Sensor gyroscope;

    //Accelerometer
    private Sensor accelerometer;

    //Rotation - measures orientation of device
    private Sensor rotation;

    //Gravity
    private Sensor gravity;

    ////////////////////////////////////////////////////////////////////////////////
    public ESensorManager(Context context){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorList = new ArrayList<>();
        setupSensors();
    }

    private void setupSensors(){
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        addSensorToList(gyroscope);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        addSensorToList(accelerometer);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        addSensorToList(rotation);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        addSensorToList(gravity);
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
        if(sensor == gyroscope){
            updateGyroscope(event);
        }else if(sensor == accelerometer){
            updateAccelerometer(event);
        }else if(sensor == rotation){
            updateRotation(event);
        }else if(sensor == gravity){
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
