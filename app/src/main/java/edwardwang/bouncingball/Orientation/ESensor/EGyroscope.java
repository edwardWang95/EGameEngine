package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class EGyroscope implements ESensorSetup{
    private Vector3DFloat gyroscope;

    public EGyroscope(Vector3DFloat gyroscope){
        this.gyroscope = gyroscope;
    }


    @Override
    public void updateSensor(SensorEvent event) {

    }
}
