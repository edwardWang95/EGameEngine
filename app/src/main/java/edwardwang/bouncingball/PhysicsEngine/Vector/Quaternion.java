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
public class Quaternion{
    private boolean isPointsUpdated = false;
    private float[] quaternion = new float[4];

    public Quaternion(){
        quaternion[0] = 0;
        quaternion[1] = 0;
        quaternion[2] = 0;
        quaternion[3] = 0;
    }

    public void setXYZW(float x, float y, float z, float w){
        setX(x);
        setY(y);
        setZ(z);
        setW(w);
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Getter
    public float[] getArray(){
        return quaternion;
    }
    public float getX(){
        return quaternion[0];
    }
    public float getY(){
        return quaternion[1];
    }
    public float getZ(){
        return quaternion[2];
    }
    public float getW(){
        return quaternion[3];
    }


    ////////////////////////////////////////////////////////////////////////////////
    //Setter
    public void setX(float x){
        quaternion[0] = x;
    }
    public void setY(float y){
        quaternion[1] = y;
    }
    public void setZ(float z){
        quaternion[2] = z;
    }
    public void setW(float w){
        quaternion[3] = w;
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
        clone.setX(quaternion[0]);
        clone.setY(quaternion[1]);
        clone.setZ(quaternion[2]);
        clone.setW(quaternion[3]);
        return clone;
    }

    /**
     * Multiply this quaternion by the currentQuaternion and save the results to the
     * current Quaternion.
     */
    public void multiplyByQuat(Quaternion input, Quaternion output) {
        Vector4DFloat inputCopy = new Vector4DFloat();
        if (input != output) {
            //w = w1w2 - x1x2 - y1y2 - z1z2
            output.getArray()[3] = (quaternion[3] * input.getArray()[3] - quaternion[0] * input.getArray()[0] -
                    quaternion[1] * input.getArray()[1] - quaternion[2] * input.getArray()[2]);
            //x = w1x2 + x1w2 + y1z2 - z1y2
            output.getArray()[0] = (quaternion[3] * input.getArray()[0] + quaternion[0] * input.getArray()[3] +
                    quaternion[1] * input.getArray()[2] - quaternion[2] * input.getArray()[1]);
            //y = w1y2 + y1w2 + z1x2 - x1z2
            output.getArray()[1] = (quaternion[3] * input.getArray()[1] + quaternion[1] * input.getArray()[3] +
                    quaternion[2] * input.getArray()[0] - quaternion[0] * input.getArray()[2]);
            //z = w1z2 + z1w2 + x1y2 - y1x2
            output.getArray()[2] = (quaternion[3] * input.getArray()[2] + quaternion[2] * input.getArray()[3] +
                    quaternion[0] * input.getArray()[1] - quaternion[1] * input.getArray()[0]);
        } else {
            inputCopy.setX(input.getArray()[0]);
            inputCopy.setY(input.getArray()[1]);
            inputCopy.setZ(input.getArray()[2]);
            inputCopy.setW(input.getArray()[3]);

            //w = w1w2 - x1x2 - y1y2 - z1z2
            output.getArray()[3] = (quaternion[3] * inputCopy.getW() - quaternion[0] * inputCopy.getX() -
                    quaternion[1] * inputCopy.getY() - quaternion[2] * inputCopy.getZ());
            //x = w1x2 + x1w2 + y1z2 - z1y2
            output.getArray()[0] = (quaternion[3] * inputCopy.getX() + quaternion[0] * getW() +
                    quaternion[1] * inputCopy.getZ() - quaternion[2] * inputCopy.getY());
            //y = w1y2 + y1w2 + z1x2 - x1z2
            output.getArray()[1] = (quaternion[3] * inputCopy.getY() + quaternion[1] * inputCopy.getW() +
                    quaternion[2] * inputCopy.getX() - quaternion[0] * inputCopy.getZ());
            //z = w1z2 + z1w2 + x1y2 - y1x2
            output.getArray()[2] = (quaternion[3] * inputCopy.getZ() + quaternion[2] * inputCopy.getW() +
                    quaternion[0] * inputCopy.getY() - quaternion[1] * inputCopy.getX());
        }

    }
}
