package edwardwang.bouncingball.PhysicsEngine.Vector;

/**
 * Created by edwardwang on 8/4/16.
 */
public class Vector3DLong {
    long x, y, z;

    public Vector3DLong() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3DLong(long x, long y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Vector3DLong(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setY(long y) {
        this.y = y;
    }

    public void setZ(long z) {
        this.z = z;
    }
}
