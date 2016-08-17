package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class EMagneticField implements ESensorSetup{
    private Vector3DFloat magneticField;

    public EMagneticField(Vector3DFloat magneticField){
        this.magneticField = magneticField;
    }

    @Override
    public void updateSensor(SensorEvent event) {

    }
}
