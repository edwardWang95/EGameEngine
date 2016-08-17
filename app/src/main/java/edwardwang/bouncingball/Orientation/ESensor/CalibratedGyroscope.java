package edwardwang.bouncingball.Orientation.ESensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import edwardwang.bouncingball.Orientation.MatrixFloat4By4;
import edwardwang.bouncingball.PhysicsEngine.Vector.Quaternion;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DFloat;

/**
 * Created by edwardwang on 8/16/16.
 */
public class CalibratedGyroscope extends ESensorManager {

    private static final float nanoSecondToSecond = 1.0f / 1000000000.0f;
    /**
     * This is a filter-threshold for discarding Gyroscope measurements that are below
     * a certain level and potentially are only noise and not real motion. Values
     * from the gyroscope are usually between 0 (stop) and 10 (rapid rotation), so
     * 0.1 seems to be a reasonable threshold to filter noise (usually smaller than 0.1)
     * and real motion (usually > 0.1). Note that there is a chance of missing real
     * motion, if the use is turning the device really slowly, so this value has to
     * find a balance between accepting noise (threshold = 0) and missing
     * slow user-action (threshold > 0.5). 0.1 seems to work fine for most applications.
     *
     */
    private static final double EPSILON = .1f;

    private ESensorManager sensorManager;
    private Object syncToken;
    private MatrixFloat4By4 currentOrientationRotationVector;

    /**
     * The quaternion that stores the difference that is obtained by the gyroscope.
     * Basically it contains a rotational difference encoded into a quaternion.
     *
     * To obtain the absolute orientation one must add this into an initial position by
     * multiplying it with another quaternion
     */
    private Quaternion deltaQuaternion = new Quaternion();
    private Quaternion currentOrientationQuaternion;
    private Quaternion correctedQuaternion;

    // Get the time stamp of last gyroscope event
    private long timeStamp;
    private Vector3DFloat unNormalizedAxis = new Vector3DFloat();
    double thetaOverTwo, sinThetaOverTwo, cosThetaOverTwo;

    /**
     * Value giving the total velocity of the gyroscope (will be high, when the device
     * is moving fast and low when the device is standing still). This is usually a
     * value between 0 and 10 for normal motion. Heavy shaking can increase it to
     * about 25. Keep in mind, that these values are time-depended, so changing the
     * sampling rate of the sensor will affect this value!
     */
    private double gyroRotationVelocity = 0;

    public CalibratedGyroscope(Context context, Object syncToken, MatrixFloat4By4
                            currentOrientationRotationVector) {
        super(context);
        this.syncToken = syncToken;
        this.currentOrientationRotationVector = currentOrientationRotationVector;
        addESensorToList(ESensor.Gyroscope);
        setupSensors();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == getGyroscopeSensor()){
            if(timeStamp != 0){
                final float deltaTime = (event.timestamp - timeStamp) * nanoSecondToSecond;
                //grab axis of rotation --> not normalized yet
                unNormalizedAxis.setX(event.values[0]);
                unNormalizedAxis.setY(event.values[1]);
                unNormalizedAxis.setZ(event.values[2]);

                //calculate gyroscope rotation velocity
                gyroRotationVelocity = Math.sqrt((unNormalizedAxis.getX() * unNormalizedAxis.getX()) +
                        (unNormalizedAxis.getY() * unNormalizedAxis.getY()) +
                        (unNormalizedAxis.getZ() * unNormalizedAxis.getZ()));

                /**
                 * Integrate around axis over using the angular speed by the timeStep.
                 */
                thetaOverTwo = gyroRotationVelocity * deltaTime / 2.0f;
                sinThetaOverTwo = Math.sin(thetaOverTwo);
                cosThetaOverTwo = Math.cos(thetaOverTwo);
                //Convert the axis-angle representation into a quaternion
                deltaQuaternion.setX((float) (sinThetaOverTwo * unNormalizedAxis.getX()));
                deltaQuaternion.setY((float) (sinThetaOverTwo * unNormalizedAxis.getY()));
                deltaQuaternion.setZ((float) (sinThetaOverTwo * unNormalizedAxis.getZ()));
                deltaQuaternion.setW(-(float) cosThetaOverTwo);

                /**
                 *  This is from the repo's class
                 * // Matrix rendering in CubeRenderer does not seem to have this problem.
                 synchronized (syncToken) {
                 // Move current gyro orientation if gyroscope should be used
                 deltaQuaternion.multiplyByQuat(currentOrientationQuaternion, currentOrientationQuaternion);
                 }
                 */

                correctedQuaternion = currentOrientationQuaternion.clone();
                /**
                 * W is originally inverted because the currentOrientationQuaternion required it.
                 * Before converting the quaternion back into a matrix, w needs to be inverted again.
                 */
                correctedQuaternion.setW(-correctedQuaternion.getW());

                synchronized (syncToken){
                    SensorManager.getRotationMatrixFromVector(currentOrientationRotationVector.getArray(),
                            correctedQuaternion.getArray());
                }

            }
            timeStamp = event.timestamp;
        }
    }
}
