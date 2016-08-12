package edwardwang.bouncingball.PhysicsEngine.Vector3D;

/**
 * Created by edwardwang on 8/4/16.
 */
public class Vector3DDouble {
    double x, y, z;

    public Vector3DDouble() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3DDouble(double x, double y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Vector3DDouble(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
