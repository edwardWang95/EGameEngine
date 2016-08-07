package edwardwang.bouncingball.PhysicsEngine.Vector3D;

import edwardwang.bouncingball.PhysicsEngine.Direction;

/**
 * Created by edwardwang on 8/5/16.
 */
public class Vector3DDirection {
    Direction x,y, z;

    public Vector3DDirection() {
        x = Direction.STATIC;
        y = Direction.STATIC;
        z = Direction.STATIC;
    }

    public Direction getX() {
        return x;
    }

    public Direction getY() {
        return y;
    }

    public Direction getZ() {
        return z;
    }

    public void setX(Direction x) {
        this.x = x;
    }

    public void setY(Direction y) {
        this.y = y;
    }

    public void setZ(Direction z) {
        this.z = z;
    }
}
