package edwardwang.bouncingball.PhysicsEngine.Vector2D;

/**
 * Created by edwardwang on 8/5/16.
 */
public class Vector2DDirection {
    Direction x,y;

    public Vector2DDirection() {
        x = Direction.STATIC;
        y = Direction.STATIC;
    }

    public Direction getX() {
        return x;
    }

    public Direction getY() {
        return y;
    }

    public void setX(Direction x) {
        this.x = x;
    }

    public void setY(Direction y) {
        this.y = y;
    }
}
