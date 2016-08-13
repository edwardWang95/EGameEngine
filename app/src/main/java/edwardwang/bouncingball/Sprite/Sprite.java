package edwardwang.bouncingball.Sprite;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

import edwardwang.bouncingball.Info.PhoneInfo;
import edwardwang.bouncingball.PhysicsEngine.Axis;
import edwardwang.bouncingball.PhysicsEngine.RigidBody;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * http://obviam.net/index.php/sprite-animation-with-android/
 * http://gamecodeschool.com/android/coding-android-sprite-sheet-animations/
 *
 * Sprite generator website
 * http://www.piskelapp.com/p/agxzfnBpc2tlbC1hcHByEwsSBlBpc2tlbBiAgICqyqfCCAw/edit
 *
 * Add sprites to drawable
 * http://stackoverflow.com/questions/29047902/how-to-add-an-image-to-the-drawable-folder-in-android-studio
 *
 * Created by edwardwang on 7/23/16.
 */
public class Sprite {
    private static final String className = Sprite.class.getSimpleName();
    private Bitmap image;
    private boolean isMoving = false;
    //isVisible will be used primarily for background sprites
    private boolean isVisible = false;
    private SpriteType spriteType;

    //This will be used when sprite moves at a static pace
    private float pixelDistancePerSecond;   //the amount of pixels walked per second

    //Frame
    private int frameWidth, frameHeight;
    private int totalFrameCount; //the amount frame changes for sprite
    private int currentFrame;
    private long lastFrameChangeTime; //the amount of time since last frame change
    private int frameLengthInMilliseconds; //how long each frame should last
    private Rect frameToDraw; //define area of sprite sheet that represents 1 frame
    private RectF whereToDraw; //define area of screen of where to draw

    //Hitbox
    private SpriteHitBox spriteHitBox;
    private double hitBoxWidth;
    private double hitBoxHeight;

    //Location of sprite on the canvas/display
    private final double  edPixelToMeter = .1;   // % of edPixels that translate to a meter
    private Vector3DInt canvasPosition;
    private Vector3DInt eMapPosition;
    private boolean isOnGround;

    //Physics
    private RigidBody rigidBody;

    public Sprite(){
        canvasPosition = new Vector3DInt();
        canvasPosition.setX(10);
        canvasPosition.setY(10);
        rigidBody = new RigidBody();
        pixelDistancePerSecond = 300;
        frameWidth = 100;
        frameHeight = 100;
        totalFrameCount = 1;
        currentFrame = 0;
        lastFrameChangeTime = 0;
        frameLengthInMilliseconds = 100;
        frameToDraw = new Rect(0,0,frameWidth,frameHeight);
        whereToDraw = new RectF(canvasPosition.getX(), canvasPosition.getY(),
                canvasPosition.getX() + frameWidth, canvasPosition.getY() + frameHeight);
        //whereToDraw = new RectF(0, 0, frameWidth, frameHeight);
        isOnGround = true;
    }

    /*
    TODO:Need to update this so that its boundaries are defined by eMap
     */
    public boolean isWithinScreen(){
        /*
        return(positionX >= (0 - (frameWidth/2) + screenBufferX) &&
                positionX <=
                (PhoneInfo.getInstance().getScreenWidth() - (frameWidth/2) - screenBufferX)
                &&
                positionY >= (0 - (frameHeight/2) + screenBufferY) &&
                positionY <=
                (PhoneInfo.getInstance().getScreenHeight() - (frameHeight/2) - screenBufferY));
        */
        /*
        return(positionX >= 0 &&
                positionX <=
                        (PhoneInfo.getInstance().getScreenWidth())
                &&
                positionY >= 0 &&
                positionY <=
                        (PhoneInfo.getInstance().getScreenHeight()));
        */

        return(canvasPosition.getX() >= PhoneInfo.getInstance().getScreenLeft() &&
                canvasPosition.getX() <= PhoneInfo.getInstance().getScreenRight()
                &&
                canvasPosition.getY() >= PhoneInfo.getInstance().getScreenTop() &&
                canvasPosition.getY() <= PhoneInfo.getInstance().getScreenBottom());

    }

    /**
     * This chooses which frame in animation sequence.
     */
    public void getCurrentFrame(){
        long time = System.currentTimeMillis();
        if(isMoving()){
            if(time > lastFrameChangeTime + frameLengthInMilliseconds){
                lastFrameChangeTime = time;
                currentFrame++;
                currentFrame %= totalFrameCount;
            }
        }
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
        /*
        frameToDraw.bottom = frameToDraw.right + frameWidth;
        frameToDraw.top = frameToDraw.bottom + frameWidth;
        */
        //frameToDraw.bottom = currentFrame * frameWidth;
        //frameToDraw.top = currentFrame * frameWidth;
    }

    /**
     * Set all elements necessary to draw sprite on EMap
     */
    public void setupSpriteSettings(Bitmap image,boolean isVisible, SpriteType spriteType){
        this.image = image;
        this.isVisible = isVisible;
        this.spriteType = spriteType;

    }

    public void setupDimensions(int frameWidth, int frameHeight, double hitBoxWidth, double hitBoxHeight,
                                int numOfCorners, int numOfEdges){
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.hitBoxWidth = hitBoxWidth;
        this.hitBoxHeight = hitBoxHeight;
        spriteHitBox = new SpriteHitBox(frameWidth,frameHeight,canvasPosition.getX(),canvasPosition.getY(),
                hitBoxWidth, hitBoxHeight, numOfCorners, numOfEdges);
    }

    public void setupLocation(Vector3DInt canvasPosition, Vector3DInt eMapPosition){
        int positionX = canvasPosition.getX();
        int positionY = canvasPosition.getY();
        this.canvasPosition = canvasPosition;
        this.eMapPosition = eMapPosition;
        this.frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
        this.whereToDraw = new RectF(positionX, positionY, positionX + frameWidth, positionY + frameHeight);
        this.frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
        this.whereToDraw = new RectF(positionX, positionY, positionX + frameWidth, positionY + frameHeight);
    }

    /**
     * Updates the canvas Position from the rigid body's delta distance. Method is
     * usually called after running physics engine.
     *
     * TODO:clean up this hacky way and incorporate the Vector objects into PhoneInfo
     */
    public void updateCanvasPosition(Axis axis) {
        int newX = 0, newY = 0;
        int mapOffSetWidth = PhoneInfo.getInstance().geteMapOffSets().getX();
        int mapOffSetHeight = PhoneInfo.getInstance().geteMapOffSets().getY();
        int deltaDistanceX, deltaDistanceY;

        switch (axis){
            case X:
                deltaDistanceX = rigidBody.getDeltaDistance().getX();
                //InfoLog.getInstance().generateLog(className, "DeltaX:" + deltaDistanceX);
                newX = canvasPosition.getX() + deltaDistanceX;
                if(newX < mapOffSetWidth){
                    newX = mapOffSetWidth;
                }else if(newX > (PhoneInfo.getInstance().getScreenWidth() - mapOffSetWidth)){
                    newX = (PhoneInfo.getInstance().getScreenWidth() - mapOffSetWidth);
                }
                canvasPosition.setX(newX);
                break;
            case Y:
                deltaDistanceY = rigidBody.getDeltaDistance().getY();
                newY = canvasPosition.getY() - deltaDistanceY;
                canvasPosition.setY(newY);
                break;
            case Z:
                break;
        }

        //InfoLog.getInstance().debugValue(className, "DeltaDistanceY: " + deltaDistanceY);

        //InfoLog.getInstance().debugValue(className, "PositionY: " + canvasPosition.getY());
    }

        /*
        if(deltaDistanceY > 0){
            InfoLog.getInstance().debugValue(className, "UP");
            newY = canvasPosition.getY() - deltaDistanceY;
        }else{
            InfoLog.getInstance().debugValue(className, "DOWN");
            newY = canvasPosition.getY() - deltaDistanceY;
        }
        */
        /*
        InfoLog.getInstance().debugValue(className, "PositionX: " + newX +
                " PositionY: " + newY);
        */


        //InfoLog.getInstance().debugValue(className, "PositionY: " + newY +
        //        " Traveled: " + rigidBody.getDeltaDistance().getY());


        //updateDeltaPosition();

        //rigidBody.updateVelocity(deltaPosition.getX(), deltaPosition.getY());


    /**
     * Sets given value to be positive if < 0
     */
    private void setPositive(int x){
        if(x < 0){
            x *= -1;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //Getter
    public Bitmap getImage() {
        return image;
    }

    public SpriteType getSpriteType(){return spriteType;}

    public Vector3DInt getCanvasPosition() {
        return canvasPosition;
    }

    public Vector3DInt geteMapPosition() {
        return eMapPosition;
    }

    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public float getPixelDistancePerSecond() {
        return pixelDistancePerSecond;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getTotalFrameCount() {
        return totalFrameCount;
    }

    public long getLastFrameChangeTime() {
        return lastFrameChangeTime;
    }

    public Rect getFrameToDraw() {
        return frameToDraw;
    }

    public RectF getWhereToDraw() {
        return whereToDraw;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public SpriteHitBox getSpriteHitBox() {
        return spriteHitBox;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //Setter

    /**
     * Set the sprite image and scale it to match frame size
     * @param image
     */
    public void setImage(Bitmap image) {
        this.image = Bitmap.createScaledBitmap(image, frameWidth * totalFrameCount, frameHeight, false);
    }

    public void setSpriteType(SpriteType spriteType) {
        this.spriteType = spriteType;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setPixelDistancePerSecond(float pixelDistancePerSecond) {
        this.pixelDistancePerSecond = pixelDistancePerSecond;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public void setTotalFrameCount(int totalFrameCount) {
        this.totalFrameCount = totalFrameCount;
    }

    public void setLastFrameChangeTime(long lastFrameChangeTime) {
        this.lastFrameChangeTime = lastFrameChangeTime;
    }

    public void setFrameToDraw(Rect frameToDraw) {
        this.frameToDraw = frameToDraw;
    }

    public void setWhereToDraw(RectF whereToDraw) {
        this.whereToDraw = whereToDraw;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setIsOnGround(boolean isOnGround) {
        this.isOnGround = isOnGround;
    }

    public void seteMapPosition(Vector3DInt eMapPosition) {
        this.eMapPosition = eMapPosition;
    }
}