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

    private int width, height;
    private Vector3DInt position;
    private Vector3DInt topLeft;
    private Vector3DInt topRight;
    private Vector3DInt bottomLeft;
    private Vector3DInt bottomRight;
    //hit box percentages
    //i.e. widthPerc = .75
    double widthPerc, heightPerc;
    //position offsets
    private int offsetWidth;
    private int offsetHeight;

    //corner CheckList used in PhysicsEngine
    private boolean[] cornerCheckList = new boolean[4];

    public SpriteHitBox(int width, int height, int positionX, int positionY,
                        double widthPerc, double heightPerc){
        position = new Vector3DInt();
        topLeft = new Vector3DInt();
        topRight = new Vector3DInt();
        bottomLeft = new Vector3DInt();
        bottomRight = new Vector3DInt();

        this.width = width;
        this.height = height;
        position.setX(positionX);
        position.setY(positionY);
        this.widthPerc = widthPerc;
        this.heightPerc = heightPerc;
        setupOffsets();
        setupHitBoxPositions();
    }

    private void setupOffsets(){
        offsetWidth = (width - (int)(width * widthPerc)) / 2;
        offsetHeight = (height - (int)(height * heightPerc)) / 2;
    }

    private void setupHitBoxPositions(){
        //TopLeft
        topLeft.setX(position.getX() + offsetWidth);
        topLeft.setY(position.getY() + offsetHeight);
        //TopRight
        topRight.setX(topLeft.getX() + (int)(width * widthPerc));
        topRight.setY(topLeft.getY());
        //BottomLeft
        bottomLeft.setX(topLeft.getX());
        bottomLeft.setY(topLeft.getY() + (int)(height * heightPerc));
        //BottomRight
        bottomRight.setX(topRight.getX());
        bottomRight.setY(bottomLeft.getY());
    }

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
                        positionX = spriteHitBox.getTopLeft().getX();
                        positionY = spriteHitBox.getTopLeft().getY();
                        break;
                    case TOPRIGHT:
                        positionX = spriteHitBox.getTopRight().getX();
                        positionY = spriteHitBox.getTopRight().getY();
                        break;
                    case BOTTOMLEFT:
                        positionX = spriteHitBox.getBottomLeft().getX();
                        positionY = spriteHitBox.getBottomLeft().getY();
                        break;
                    case BOTTOMRIGHT:
                        positionX = spriteHitBox.getBottomRight().getX();
                        positionY = spriteHitBox.getBottomRight().getY();
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
        return (positionX >= topLeft.getX() &&
                positionX <= topRight.getX() &&
                positionY >= topLeft.getY() &&
                positionY <= bottomLeft.getY());
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
        return isWithinHitBox(hitBox.getTopLeft().getX(),
                hitBox.getTopLeft().getY());
    }

    public boolean isTopRightIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getTopRight().getX(),
                hitBox.getTopRight().getY());
    }

    public boolean isBottomLeftIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getBottomLeft().getX(),
                hitBox.getBottomLeft().getY());
    }

    public boolean isBottomRightIntersectingHitBox(SpriteHitBox hitBox){
        return isWithinHitBox(hitBox.getBottomRight().getX(),
                hitBox.getBottomRight().getY());
    }

    public Vector3DInt getPosition() {
        return position;
    }

    public Vector3DInt getTopLeft() {
        return topLeft;
    }

    public Vector3DInt getTopRight() {
        return topRight;
    }

    public Vector3DInt getBottomLeft() {
        return bottomLeft;
    }

    public Vector3DInt getBottomRight() {
        return bottomRight;
    }
}
