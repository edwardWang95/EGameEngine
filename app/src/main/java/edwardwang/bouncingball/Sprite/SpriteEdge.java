package edwardwang.bouncingball.Sprite;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Created by edwardwang on 8/10/16.
 */
public class SpriteEdge {
    private static final String className = SpriteEdge.class.getSimpleName();
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
        this.slope = calculateSlope(startCorner, endCorner);
    }

    private double calculateSlope(Vector3DInt startPosition, Vector3DInt endPosition){
        double slope;
        try{
            slope = ((endPosition.getY()-startPosition.getY())/
                    (endPosition.getX() - startPosition.getX()));
        }catch(ArithmeticException e){
            slope = 0;
        }
        return slope;
    }

    public Vector3DInt getPoint(int counter){
        int x,y;
        if(slope < 0){
            x = startCorner.getX() - (int)(counter * slope);
        }else if(slope > 0){
            x = startCorner.getX() + (int)(counter * slope);
        }else{
            x = startCorner.getX();
        }
        y = ((int)(slope * x) + startCorner.getY() + counter);
        return (new Vector3DInt(x,y));
    }

    public boolean containsPoint(Vector3DInt edgePosition, int edge){
        int x = edgePosition.getX();
        int y = edgePosition.getY();
        if(isAtCorner(x,y)){
            return true;
        }

        if(isBetweenStartEndCorners(x, y, edge)){
            return (slope == calculateSlope(edgePosition, endCorner));
        }
        return false;
    }

    /**
     * Check if the given position is one of this edge's corner. It is more efficient
     * to immediately return true for containtsPoint method if the given position is
     * a corner.
     * @param x
     * @param y
     * @return cornerStatus
     */
    private boolean isAtCorner(int x, int y){
        if(x == startCorner.getX() && y == startCorner.getY()){
            return true;
        }else if(x == endCorner.getX() && y == endCorner.getY()){
            return true;
        }
        return false;
    }

    /**
     * Make sure the position is in between the corners to alleviate
     * processing power/time used.
     * @param x
     * @param y
     * @return cornerStatus
     */
    private boolean isBetweenStartEndCorners(int x, int y, int edge){
        /*
        InfoLog.getInstance().debugValue(className, "StartCorner: "+startCorner.getX()+", "+startCorner.getY());
        InfoLog.getInstance().debugValue(className, "EndCorner: "+endCorner.getX()+", "+endCorner.getY());
        InfoLog.getInstance().debugValue(className, "Object: "+x+", "+y);
        */
        if (edge == SpriteHitBox.TOP || edge == SpriteHitBox.BOTTOM){
            return (x >= startCorner.getX() && x <= endCorner.getX());
        }else if(edge == SpriteHitBox.LEFT || edge == SpriteHitBox.RIGHT){
            return (y >= startCorner.getY() && y <= endCorner.getY());
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////
    //Getter

    public Vector3DInt getStartCorner() {
        return startCorner;
    }

    public Vector3DInt getEndCorner() {
        return endCorner;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    //////////////////////////////////////////////////////////////////////////////
    //Setter

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
