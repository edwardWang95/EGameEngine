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
        try{
            slope = ((endCorner.getY()-startCorner.getY())/
                    (endCorner.getX() - startCorner.getX()));
        }catch(ArithmeticException e){
            slope = 0;
        }
    }

    public Vector3DInt getPoint(int counter){
        int x = 0, y = 0;
        if(slope < 0){
            x = startCorner.getX() - (int)(counter * slope);
        }else if(slope > 0){
            x = startCorner.getX() + (int)(counter * slope);
        }else{
            x = startCorner.getX();
        }
        y = ((int)(slope * x) + startCorner.getY());
        return (new Vector3DInt(x,y));
    }

    public boolean containsPoint(Vector3DInt edgePosition){
        int x = edgePosition.getX();
        int y = edgePosition.getY();


        Redo this method

        return(startCorner.getX() <= x &&
                x <= endCorner.getX() &&
                startCorner.getY() <= y &&
                y <= endCorner.getY());
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
