package edwardwang.bouncingball.PhysicsEngine.Vector;

/**
 * Created by edwardwang on 8/4/16.
 */
public class Vector3DInt {
    int x, y, z;

    public Vector3DInt() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3DInt(int x, int y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Vector3DInt(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
