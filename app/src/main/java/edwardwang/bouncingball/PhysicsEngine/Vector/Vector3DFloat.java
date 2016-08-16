package edwardwang.bouncingball.PhysicsEngine.Vector;

/**
 * Created by edwardwang on 8/5/16.
 */
public class Vector3DFloat {
    float x, y, z;

    public Vector3DFloat() {
        x = 0f;
        y = 0f;
        z = 0f;
    }

    public Vector3DFloat(float x, float y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Vector3DFloat(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
