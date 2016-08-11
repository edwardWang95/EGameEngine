package edwardwang.bouncingball.Sprite;

import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Hitbox will be useful when dealing with player sprites that interact
 * with the backgrounds.
 *
 * By Default, the hitbox will encompass the whole EPixel, but this can be
 * set when the game is initially being created.
 * Created by edwardwang on 7/27/16.
 */
public class SpriteHitBox {
    private static final String className = Sprite.class.getSimpleName();
    //Corners of hitbox
    public static final int TOPLEFT = 0;
    public static final int TOPRIGHT = 1;
    public static final int BOTTOMLEFT = 2;
    public static final int BOTTOMRIGHT = 3;

    //Sides of hitbox
    public static final int TOP = 4;
    public static final int LEFT = 5;
    public static final int RIGHT = 6;
    public static final int BOTTOM = 7;

    private int width, height;
    private Vector3DInt position;

    //hit box percentages
    //i.e. widthPerc = .75
    double widthPerc, heightPerc;
    //position offsets
    private int offsetWidth;
    private int offsetHeight;

    //Dimensions
    private int numOfCorners;
    private int numOfSides;
    private SpriteCorner[] spriteCornerList = new SpriteCorner[4];
    private SpriteEdge[] spriteEdgeList = new SpriteEdge[4];
    private boolean[] cornerCheckList = new boolean[4];
    private boolean[] sidesCheckList  = new boolean[4];

    public SpriteHitBox(int width, int height, int positionX, int positionY,
                        double widthPerc, double heightPerc){
        position = new Vector3DInt();

        this.width = width;
        this.height = height;
        position.setX(positionX);
        position.setY(positionY);
        this.widthPerc = widthPerc;
        this.heightPerc = heightPerc;

        cornerCheckList = new boolean[numOfCorners];
        sidesCheckList = new boolean[numOfSides];

        setupOffsets();
        setupCorners();
        setupSides();
    }

    private void setupOffsets(){
        offsetWidth = (width - (int)(width * widthPerc)) / 2;
        offsetHeight = (height - (int)(height * heightPerc)) / 2;
    }

    private void setupCorners(){
        //TopLeft
        SpriteCorner topLeft = spriteCornerList[TOPLEFT];
        topLeft.getCorner().setX(position.getX() + offsetWidth);
        topLeft.getCorner().setY(position.getY() + offsetHeight);

        //TopRight
        SpriteCorner topRight = spriteCornerList[TOPRIGHT];
        topRight.getCorner().setX(topLeft.getCorner().getX() + (int)(width * widthPerc));
        topRight.getCorner().setY(topLeft.getCorner().getY());

        //BottomLeft
        SpriteCorner bottomLeft = spriteCornerList[BOTTOMLEFT];
        bottomLeft.getCorner().setX(topLeft.getCorner().getX());
        bottomLeft.getCorner().setY(topLeft.getCorner().getY() + (int)(height * heightPerc));

        //BottomRight
        SpriteCorner bottomRight = spriteCornerList[BOTTOMRIGHT];
        bottomRight.getCorner().setX(topRight.getCorner().getX());
        bottomRight.getCorner().setY(bottomLeft.getCorner().getY());
    }

    private void setupSides(){
        spriteEdgeList[TOP] = new SpriteEdge(spriteCornerList[TOPLEFT].getCorner(),
                spriteCornerList[TOPRIGHT].getCorner());
        spriteEdgeList[LEFT] = new SpriteEdge(spriteCornerList[TOPLEFT].getCorner(),
                spriteCornerList[BOTTOMLEFT].getCorner());
        spriteEdgeList[RIGHT] = new SpriteEdge(spriteCornerList[TOPRIGHT].getCorner(),
                spriteCornerList[BOTTOMRIGHT].getCorner());
        spriteEdgeList[BOTTOM] = new SpriteEdge(spriteCornerList[BOTTOMLEFT].getCorner(),
                spriteCornerList[BOTTOMRIGHT].getCorner());
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Could be useful in 3D perspective. i.e. shooting a bullet into an enemy
     *
     * Hitbox in parameter is the platform, and we are checking that this
     * sprite's hitbox is WITHIN the hitbox parameters.
     * @param spriteHitBox
     * @return
     */
    public boolean isBeingIntersectedByHitBox(SpriteHitBox spriteHitBox){
        int positionX = 0;
        int positionY = 0;
        for(int i=0;i<cornerCheckList.length;i++){
            if(cornerCheckList[i]){
                switch (i){
                    case TOPLEFT:
                        positionX = spriteHitBox.getSpriteCornerList(TOPLEFT).getCorner().getX();
                        positionY = spriteHitBox.getSpriteCornerList(TOPLEFT).getCorner().getY();
                        break;
                    case TOPRIGHT:
                        positionX = spriteHitBox.getSpriteCornerList(TOPRIGHT).getCorner().getX();
                        positionY = spriteHitBox.getSpriteCornerList(TOPRIGHT).getCorner().getY();
                        break;
                    case BOTTOMLEFT:
                        positionX = spriteHitBox.getSpriteCornerList(BOTTOMLEFT).getCorner().getX();
                        positionY = spriteHitBox.getSpriteCornerList(BOTTOMLEFT).getCorner().getY();
                        break;
                    case BOTTOMRIGHT:
                        positionX = spriteHitBox.getSpriteCornerList(BOTTOMRIGHT).getCorner().getX();
                        positionY = spriteHitBox.getSpriteCornerList(BOTTOMRIGHT).getCorner().getY();
                        break;
                }
                if(!isWithinHitBox(positionX, positionY)){
                    return false;
                }
            }
        }

        //InfoLog.getInstance().debugValue(className, "Sprite is within platform hitbox");

        return true;
    }

    /**
     * Used when use interacts with sprite by tapping on it.
     *
     * Check the player sprite hitBox is
     * @param positionX
     * @param positionY
     * @return
     */
    private boolean isWithinHitBox(int positionX, int positionY){
        return (positionX >= spriteCornerList[TOPLEFT].getCorner().getX() &&
                positionX <= spriteCornerList[TOPRIGHT].getCorner().getX() &&
                positionY >= spriteCornerList[TOPLEFT].getCorner().getY() &&
                positionY <= spriteCornerList[BOTTOMLEFT].getCorner().getY());
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Version 1.0 collision. Spritebox is static shape of square for now.
     * @param spriteHitBox
     * @return
     */
    public boolean isCollidingWithObjectHitBox(SpriteHitBox objectHitBox){
        SpriteEdge spriteEdge = new SpriteEdge();
        SpriteEdge objectSpriteEdge = new SpriteEdge();
        for(int i=TOP;i<(BOTTOM + 1);i++){
            switch (i){
                case TOP:
                    spriteEdge = spriteEdgeList[TOP];
                    objectSpriteEdge = objectHitBox.getSpriteEdgeList(BOTTOM);
                    break;
                case LEFT:
                    spriteEdge = spriteEdgeList[LEFT];
                    objectSpriteEdge = objectHitBox.getSpriteEdgeList(RIGHT);
                    break;
                case RIGHT:
                    spriteEdge = spriteEdgeList[RIGHT];
                    objectSpriteEdge = objectHitBox.getSpriteEdgeList(LEFT);
                    break;
                case BOTTOM:
                    spriteEdge = spriteEdgeList[BOTTOM];
                    objectSpriteEdge = objectHitBox.getSpriteEdgeList(TOP);
                    break;
            }
            if(isCollidingWithSide(spriteEdge, objectSpriteEdge)){
                return true;
            }
        }

        //InfoLog.getInstance().debugValue(className, "Sprite is within platform hitbox");

        return false;
    }

    private boolean isCollidingWithSide(SpriteEdge spriteEdge, SpriteEdge objectSide){
        Vector3DDouble spritePosition = new Vector3DDouble();
        Vector3DDouble objectPosition = new Vector3DDouble();
        position.setX(spriteEdge.getStartCorner().getX());
        position.setY(spriteEdge.getStartCorner().getY());
        for(int i=0;i< spriteEdge.getEdgeLength();i++){
            position.setX(position.getX() + );

            //setup object side position

            for(int j=0;j<objectSide.getEdgeLength();j++){
                //update Position

                if(position.getX() == objectPosition.getX() &&
                        position.getY() == objectPosition.getY()){
                    return true;
                }
            }
        }
        return false;
    }

    /*
    Version 2.0 implementation
    public boolean isCollidingWithHitBoxSides(SpriteHitBox spriteHitBox){
        SpriteEdge spriteEdge;
        for(int i=0;i<sidesList.length;i++){
                check each and every side for collisions
                if(!isCollidingWithHitBox(positionX, positionY)){
                    return false;
                }
            }
        }

        //InfoLog.getInstance().debugValue(className, "Sprite is within platform hitbox");

        return true;
    }
    */

    public void setCornerCheckListStatus(int corner){
        cornerCheckList[corner] = true;
    }

    public void resetCornerCheckList(){
        for(int i=0;i<cornerCheckList.length;i++){
            cornerCheckList[i] = false;
        }
    }

    /**
     * These methods below allow the narrowing down to a specific corner
     * to see if it is within hitbox.
     * @param hitBox
     * @return
     */
    public boolean isTopLeftIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getSpriteCornerList(TOPLEFT).getCorner().getX(),
                hitBox.getSpriteCornerList(TOPLEFT).getCorner().getY());
    }

    public boolean isTopRightIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getSpriteCornerList(TOPRIGHT).getCorner().getX(),
                hitBox.getSpriteCornerList(TOPRIGHT).getCorner().getY());
    }

    public boolean isBottomLeftIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getSpriteCornerList(BOTTOMLEFT).getCorner().getX(),
                hitBox.getSpriteCornerList(BOTTOMLEFT).getCorner().getY());
    }

    public boolean isBottomRightIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getSpriteCornerList(BOTTOMRIGHT).getCorner().getX(),
                hitBox.getSpriteCornerList(BOTTOMRIGHT).getCorner().getY());
    }

    public Vector3DInt getPosition() {
        return position;
    }

    public SpriteEdge getSpriteEdgeList(int side) {
        return spriteEdgeList[side];
    }

    public SpriteCorner getSpriteCornerList(int corner) {
        return spriteCornerList[corner];
    }
}
