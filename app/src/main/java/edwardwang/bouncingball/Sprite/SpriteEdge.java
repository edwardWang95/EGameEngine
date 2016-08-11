package edwardwang.bouncingball.Sprite;

import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Created by edwardwang on 8/10/16.
 */
public class SpriteEdge {
    private Vector3DInt startCorner = new Vector3DInt();
    private Vector3DInt endCorner = new Vector3DInt();
    private double edgeLength;
    private double slope;

    public SpriteEdge() {
    }

    public SpriteEdge(Vector3DInt startCorner, Vector3DInt endCorner){
        this.startCorner = startCorner;
        this.endCorner = endCorner;
        updateEdgeLengthFromCorners();
        updateSlope();
    }

    public void updateEdgeLengthFromCorners(){
        int x = endCorner.getX() - startCorner.getX();
        int y = endCorner.getY() - startCorner.getY();
        int z = endCorner.getZ() - startCorner.getZ();
        //Currently only supporting 2D
        edgeLength = (int)Math.sqrt((x*x) + (y*y));
    }

    public void updateSlope(){
        slope = ((endCorner.getY()-startCorner.getY())/
                (endCorner.getX() - startCorner.getX()));
    }

    public boolean containsPoint(int x, int y){
        return(startCorner.getX() <= x && x <= endCorner.getX() &&
                startCorner.getY() <= y && y <= endCorner.getY());
    }

    public Vector3DInt getStartCorner() {
        return startCorner;
    }

    public Vector3DInt getEndCorner() {
        return endCorner;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    public double getSlope() {
        return slope;
    }

    public void setStartCorner(Vector3DInt startCorner) {
        this.startCorner = startCorner;
        updateEdgeLengthFromCorners();
        updateSlope();
    }

    public void setEndCorner(Vector3DInt endCorner) {
        this.endCorner = endCorner;
        updateEdgeLengthFromCorners();
        updateSlope();
    }
}
