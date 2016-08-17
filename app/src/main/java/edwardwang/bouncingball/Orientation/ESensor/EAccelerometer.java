package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class EAccelerometer implements ESensorSetup {

    private Vector3DFloat accelerometer;

    public EAccelerometer(Vector3DFloat accelerometer){
        this.accelerometer = accelerometer;
    }

    @Override
    public void updateSensor(SensorEvent event) {
        accelerometer.setX(event.values[0]);
        accelerometer.setY(event.values[1]);
        accelerometer.setZ(event.values[2]);
    }
}
