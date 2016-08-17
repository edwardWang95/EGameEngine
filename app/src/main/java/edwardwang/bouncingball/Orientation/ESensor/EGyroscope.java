package edwardwang.bouncingball.Orientation.ESensor;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import edwardwang.bouncingball.Orientation.MatrixFloat4By4;
import edwardwang.bouncingball.PhysicsEngine.Vector.Quaternion;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/17/16.
 */
public class EGyroscope implements ESensorSetup{


    private static final float NS2S = 1.0f / 1000000000.0f;
    /**
     * This is a filter-threshold for discarding Gyroscope measurements that are below a certain level and
     * potentially are only noise and not real motion. Values from the gyroscope are usually between 0 (stop) and
     * 10 (rapid rotation), so 0.1 seems to be a reasonable threshold to filter noise (usually smaller than 0.1) and
     * real motion (usually > 0.1). Note that there is a chance of missing real motion, if the use is turning the
     * device really slowly, so this value has to find a balance between accepting noise (threshold = 0) and missing
     * slow user-action (threshold > 0.5). 0.1 seems to work fine for most applications.
     *
     */
    private static final double EPSILON = .1f;
    private float timeStamp;
    private float angularSpeed; //omegaMagnitude
    private float thetaOverTwo, sinThetaOverTwo, cosThetaOverTwo;
    private Vector3DFloat nonNormalizedAxis;
    private Quaternion deltaQuaternion;
    private Quaternion currentOrientationQuaternion;
    private MatrixFloat4By4 deltaRotationMatrix;
    private MatrixFloat4By4 currentOrientationRotationMatrix;
    //private float[] deltaRotationVectorCalibrated = new float[4];
    //private float[] currentRotationMatrixCalibrated = new float[9];


    public EGyroscope(){
        nonNormalizedAxis = new Vector3DFloat();
        deltaQuaternion = new Quaternion();
        currentOrientationQuaternion = new Quaternion();
        deltaRotationMatrix = new MatrixFloat4By4();
        currentOrientationRotationMatrix = new MatrixFloat4By4();
    }


    @Override
    public void updateSensor(SensorEvent event) {
        if(timeStamp!=0){
            final float deltaTime = (event.timestamp - timeStamp) * NS2S;
            //Non-normalized axis of rotation sample
            nonNormalizedAxis.setX(event.values[0]);
            nonNormalizedAxis.setY(event.values[1]);
            nonNormalizedAxis.setZ(event.values[2]);

            //calculate angular speed of sample
            angularSpeed = calcAngularSpeed(nonNormalizedAxis);
            //normalize the rotation vector if it's big enough to get the axis
            if(angularSpeed > EPSILON){
                nonNormalizedAxis.setX(nonNormalizedAxis.getX()/angularSpeed);
                nonNormalizedAxis.setY(nonNormalizedAxis.getY() / angularSpeed);
                nonNormalizedAxis.setZ(nonNormalizedAxis.getZ() / angularSpeed);
            }
            /**
             * Integrate around the axis with the anuglar speed by the timeStep in order
             * to get a delta rotation from this sample over the timestep. Convert this
             * axis-angle representation of the delta rotation into a quaternion before turning
             * it into the rotation matrix.
             */
            thetaOverTwo = angularSpeed * deltaTime / 2.0f;
            sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
            cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
            deltaQuaternion.setX(sinThetaOverTwo * nonNormalizedAxis.getX());
            deltaQuaternion.setY(sinThetaOverTwo * nonNormalizedAxis.getY());
            deltaQuaternion.setZ(sinThetaOverTwo * nonNormalizedAxis.getZ());
            //to get update the currentOrientationQuaternion, W must be inverted
            deltaQuaternion.setW(-cosThetaOverTwo);

            deltaQuaternion.multiplyByQuat(currentOrientationQuaternion, currentOrientationQuaternion);

            Quaternion correctQuaternion = currentOrientationQuaternion.clone();
            correctQuaternion.setW(-correctQuaternion.getW());
            /*
            deltaRotationVectorCalibrated[0] = sinThetaOverTwo * nonNormalizedAxis.getX();
            deltaRotationVectorCalibrated[1] = sinThetaOverTwo * nonNormalizedAxis.getY();
            deltaRotationVectorCalibrated[2] = sinThetaOverTwo * nonNormalizedAxis.getZ();
            deltaRotationVectorCalibrated[3] = cosThetaOverTwo;


            SensorManager.getRotationMatrixFromVector(currentOrientationRotationMatrix.getMatrix(),
                    deltaRotationVectorCalibrated);
            currentRotationMatrixCalibrated = matrixMultiplication(
                    currentRotationMatrixCalibrated, deltaQuaternion.getArray());
            SensorManager.getOrientation(currentRotationMatrixCalibrated, gyroscopeArray);
            gyroscope.setX(gyroscopeArray[0]);
            gyroscope.setY(gyroscopeArray[1]);
            gyroscope.setZ(gyroscopeArray[2]);
            */
            SensorManager.getRotationMatrixFromVector(currentOrientationRotationMatrix.getMatrix(),
                    correctQuaternion.getArray());
        }
        timeStamp = event.timestamp;
    }


    private float calcAngularSpeed(Vector3DFloat nonNormalizedAxis){
        return ((nonNormalizedAxis.getX() * nonNormalizedAxis.getX()) +
                (nonNormalizedAxis.getY() * nonNormalizedAxis.getY()) +
                (nonNormalizedAxis.getZ() * nonNormalizedAxis.getZ()));
    }

    private MatrixFloat4By4 getGyroscopeMatrix(){
        return currentOrientationRotationMatrix;
    }

    /*
    private float[] matrixMultiplication(float[] a, float[] b){
        float[] result = new float[9];

        result[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
        result[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
        result[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];

        result[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
        result[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
        result[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];

        result[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
        result[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
        result[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];

        return result;
    }
    */
}
