package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class EGravity implements ESensorSetup {
    private Vector3DFloat gravity;

    public EGravity(Vector3DFloat gravity){
        this.gravity = gravity;
    }

    @Override
    public void updateSensor(SensorEvent event) {

    }
}
