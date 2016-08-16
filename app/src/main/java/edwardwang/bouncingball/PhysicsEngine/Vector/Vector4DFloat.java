package edwardwang.bouncingball.PhysicsEngine.Vector;

/**
 * Created by edwardwang on 8/16/16.
 */
public class Vector4DFloat {
    /**
     * Angle: w
     * Rotation axis: x,y,z
     */
    private float w = 0f,x = 0f,y = 0f,z = 0f;

    public Vector4DFloat(float w, float x, float y, float z){
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setVector4DFloat(Vector4DFloat vector4DFloat){
        this.w = vector4DFloat.getW();
        this.x = vector4DFloat.getX();
        this.y = vector4DFloat.getY();
        this.z = vector4DFloat.getZ();
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Getter

    public float getW() {
        return w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Setter

    public void setW(float w) {
        this.w = w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
