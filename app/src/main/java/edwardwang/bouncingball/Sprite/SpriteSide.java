package edwardwang.bouncingball.Sprite;

import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Created by edwardwang on 8/10/16.
 */
public class SpriteSide {
    private Vector3DInt startCorner = new Vector3DInt();
    private Vector3DInt endCorner = new Vector3DInt();

    public SpriteSide(Vector3DInt startCorner, Vector3DInt endCorner){
        this.startCorner = startCorner;
        this.endCorner = endCorner;
    }

    public Vector3DInt getStartCorner() {
        return startCorner;
    }

    public Vector3DInt getEndCorner() {
        return endCorner;
    }

    public void setStartCorner(Vector3DInt startCorner) {
        this.startCorner = startCorner;
    }

    public void setEndCorner(Vector3DInt endCorner) {
        this.endCorner = endCorner;
    }
}
