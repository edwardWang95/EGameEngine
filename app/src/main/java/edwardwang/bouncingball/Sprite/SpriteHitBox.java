package edwardwang.bouncingball.Sprite;

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
    private SpriteSide[] spriteSideList = new SpriteSide[4];
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
        spriteSideList[TOP] = new SpriteSide(spriteCornerList[TOPLEFT].getCorner(),
                spriteCornerList[TOPRIGHT].getCorner());
        spriteSideList[LEFT] = new SpriteSide(spriteCornerList[TOPLEFT].getCorner(),
                spriteCornerList[BOTTOMLEFT].getCorner());
        spriteSideList[RIGHT] = new SpriteSide(spriteCornerList[TOPRIGHT].getCorner(),
                spriteCornerList[BOTTOMRIGHT].getCorner());
        spriteSideList[BOTTOM] = new SpriteSide(spriteCornerList[BOTTOMLEFT].getCorner(),
                spriteCornerList[BOTTOMRIGHT].getCorner());
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
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
     * Seperate to intersecting, because collisions will be handled at edges rather
     * than allowing objects to intersect with each other.
     * @param spriteHitBox
     * @return
     */
    public boolean isCollidingWithHitBox(SpriteHitBox spriteHitBox){
        SpriteSide spriteSide;
        for(int i=0;i<sidesCheckList.length;i++){
            if(sidesCheckList[i]){
                switch (i){
                    //Top collides with bottom of sprite hitbox
                    case TOP:
                        spriteSide = spriteHitBox.get
                        break;
                    //Left collides with right
                    case LEFT:
                        positionX = spriteHitBox.getTopRight().getX();
                        positionY = spriteHitBox.getTopRight().getY();
                        break;
                    //Right collides with left
                    case RIGHT:
                        positionX = spriteHitBox.getBottomLeft().getX();
                        positionY = spriteHitBox.getBottomLeft().getY();
                        break;
                    //Bottom collides with left
                    case BOTTOM:
                        positionX = spriteHitBox.getBottomRight().getX();
                        positionY = spriteHitBox.getBottomRight().getY();
                        break;
                }
                if(!isCollidingWithHitBox(positionX, positionY)){
                    return false;
                }
            }
        }

        //InfoLog.getInstance().debugValue(className, "Sprite is within platform hitbox");

        return true;
    }

    private boolean isCollidingWithSide(int corner, int positionX, int positionY){
        switch(corner){
            case TOP:
                //return (positionX == bottomLeft.getX() && positionY == )
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case BOTTOM:
                break;
        }

        return (positionX == topLeft.getX() &&
                positionX == topRight.getX() &&
                positionY >== topLeft.getY() &&
                positionY <== bottomLeft.getY());
    }

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

    public SpriteSide getSpriteSideList(int side) {
        return spriteSideList[side];
    }

    public SpriteCorner getSpriteCornerList(int corner) {
        return spriteCornerList[corner];
    }
}
