package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import edwardwang.bouncingball.Orientation.MatrixFloat4By4;
import edwardwang.bouncingball.PhysicsEngine.Vector.Quaternion;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class ERotation implements ESensorSetup{
    private Vector3DFloat rotation;
    //need a 4x4 rotation matrix
    private MatrixFloat4By4 rotationMatrix;
    private Quaternion currentOrientationQuaternion;
    private Quaternion tempQuaternion;

    public ERotation(Vector3DFloat vector3DFloat) {
        rotation = vector3DFloat;
        rotationMatrix = new MatrixFloat4By4();
        currentOrientationQuaternion = new Quaternion();
        tempQuaternion = new Quaternion();
    }

    @Override
    public void updateSensor(SensorEvent event) {
        //convert rotation vector into 4x4 matrix
        SensorManager.getRotationMatrixFromVector(rotationMatrix.getMatrix(), event.values);
        SensorManager.getQuaternionFromVector(tempQuaternion.getArray(), event.values);
        currentOrientationQuaternion.setXYZW(tempQuaternion.getX(), tempQuaternion.getY(),
                tempQuaternion.getZ(), -tempQuaternion.getW());
        /*
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                SensorManager.AXIS_Z, rotationMatrix);
        SensorManager.getOrientation(rotationMatrix, orientationVals);
        //convert orientation from radians to degrees
        rotation.setX(convertRadiansToDegrees(orientationVals[0]));
        rotation.setY(convertRadiansToDegrees(orientationVals[1]));
        rotation.setZ(convertRadiansToDegrees(orientationVals[2]));
        */
    }

    private float convertRadiansToDegrees(float radians){
        return (float)Math.toDegrees(radians);
    }
}
