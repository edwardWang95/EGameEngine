package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class ERotation implements ESensorSetup{
    private Vector3DFloat rotation;
    //need a 4x4 rotation matrix
    private float[] rotationMatrix = new float[16];
    private float[] orientationVals = new float[3];

    public ERotation(Vector3DFloat vector3DFloat) {
        rotation = vector3DFloat;
        rotationMatrix[0] = 1;
        rotationMatrix[4] = 1;
        rotationMatrix[8] = 1;
        rotationMatrix[12] = 1;
    }

    @Override
    public void updateSensor(SensorEvent event) {
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
}
