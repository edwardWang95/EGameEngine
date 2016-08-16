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
    public Quaternion(float w, float x, float y, float z) {
        super(w, x, y, z);
    }



}
