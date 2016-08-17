package edwardwang.bouncingball.PhysicsEngine.Vector;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector4DFloat;

/**
 * Reference repo:
 * https://github.com/apacha/sensor-fusion-demo
 *
 * A quaternion is a 4-D vector that is used to represent rotations of a rigid body in a 3D space.
 * It contains an angle, encoded into the w component, while 3 components represents the rotation axis
 * [x,y,z].
 *
 *
 *
 * Created by edwardwang on 8/16/16.
 */
public class Quaternion extends Vector4DFloat{
    private boolean isPointsUpdated = false;

    public Quaternion(){
        super();
    }
    public Quaternion(float w, float x, float y, float z) {
        super(w, x, y, z);
    }

    /**
     * Setup an identity matrix/quaternion. Because everything is 0 on creation
     * only W needs to be set to 1.
     */
    private void setupIdentityQuaternion(){
        setW(1);
    }

    @Override
    public Quaternion clone(){
        Quaternion clone = new Quaternion();
        clone.copyVector4DFloat(this);
        return clone;
    }

    public float[] getArray(){
        float points[] = {getW(), getX(), getY(), getZ()};
        return points;
    }
}
