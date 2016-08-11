package edwardwang.bouncingball.Sprite;

import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Created by edwardwang on 8/10/16.
 */
public class SpriteCorner {
    private Vector3DInt corner = new Vector3DInt();

    public SpriteCorner(Vector3DInt corner){
        this.corner = corner;
    }

    public Vector3DInt getCorner() {
        return corner;
    }

    public void setCorner(Vector3DInt corner) {
        this.corner = corner;
    }
}

