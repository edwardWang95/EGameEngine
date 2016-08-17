package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public interface ESensorSetup {
    void updateSensor(SensorEvent event);
}
