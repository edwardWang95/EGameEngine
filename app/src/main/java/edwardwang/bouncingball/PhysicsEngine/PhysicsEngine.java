package edwardwang.bouncingball.PhysicsEngine;

import edwardwang.bouncingball.Map.EMap;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DDirection;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DInt;
import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteHitBox;

/**
 * Info websites
 * http://gamedev.stackexchange.com
 * http://gamedev.stackexchange.com/questions/11262/advice-needed-for-a-physics-engine
 * https://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html
 *
 * Need to implement
 * -Center of mass
 * -Friction
 * -Momentum
 * -Reflection
 *
 * Created by edwardwang on 7/23/16.
 */
public class PhysicsEngine {
    private static final String className = PhysicsEngine.class.getSimpleName();

    //forces
    public static final double GRAVITY = -9.81;
    public static final double NORMAL = 9.81;

    //Physics
    private Action action;
    private float timeElapsed;

    //Collision Detection
    private EMap eMap;

    public void setEMap(EMap eMap){
        this.eMap = eMap;
    }

    //////////////////////////////////////////////////////////////////////
    public void setSpriteAction(Action action, Sprite sprite){
        this.action = action;
        switch (action){
            case JUMP:
                sprite.setIsOnGround(false);
                sprite.getRigidBody().resetVelocityY();
                sprite.getRigidBody().setDirectionY(Direction.UP);
                break;
            case WALK:
                sprite.setIsOnGround(true);
                break;
            case RUN:
                sprite.setIsOnGround(true);
                break;
        }
    }

    public void resetTimeElapsed(){
        timeElapsed = 0;
    }

    /**
     * Updates the values of sprite's rigid body data variables and calls
     * for sprite to update its given position.
     * @param sprite
     * @param deltaTime
     * @param ePixelPerMeter
     */
    public void updateSpriteLocation(Sprite sprite, Axis axis,
                                     float deltaTime, double timeFactor,
                                     float ePixelPerMeter){
        //setupSensors references
        RigidBody rigidBody = sprite.getRigidBody();
        Vector3DDouble netAccel = rigidBody.getNetAccel();
        Vector3DDouble velocity = rigidBody.getVelocity();
        Vector3DInt deltaDistance = rigidBody.getDeltaDistance();
        //Directions
        Vector3DDirection direction = rigidBody.getDirection();
        Direction previousDirectionX = direction.getX();
        Direction previousDirectionY = direction.getY();
        Direction previousDirectionZ = direction.getZ();
        //update sprite onGround status
        if(deltaTime != 0){
            timeElapsed += (deltaTime * timeFactor);
            updateNetAccel(netAccel, axis, sprite.isOnGround());
            //updateVelocity(velocity, netAccel, deltaTime);
            updateVelocity(velocity, netAccel, axis, timeElapsed);
            updateDeltaDistanceAndDirection(deltaDistance, direction, velocity, axis,
                    timeElapsed, ePixelPerMeter, eMap.getePixelWidth(), eMap.getePixelHeight());
            //if direction change is updated, reset deltaTime
            if(!direction.getY().equals(previousDirectionY)){
                resetTimeElapsed();
            }
            //update canvas and eMap position for sprite
            sprite.updateCanvasPosition(axis);
            sprite.seteMapPosition(eMap.getEMapPosition(sprite.getCanvasPosition()));
        }
    }

    /**
     * Update netAccel based on the isOnGround boolean.
     * @param isOnGround
     * @return netAccelleration
     */
    private void updateNetAccel(Vector3DDouble netAccel, Axis axis, boolean isOnGround){
        double accelX = 0;
        double accelY = 0;
        double accelZ = 0;
        if(isOnGround){
            switch (axis){
                case X:
                    accelX = 0;
                    netAccel.setX(accelX);
                    break;
                case Y:
                    accelY += PhysicsEngine.GRAVITY + PhysicsEngine.NORMAL;
                    netAccel.setY(accelY);
                    break;
                case Z:
                    break;
            }
        }else{
            switch (axis){
                case X:
                    accelX = 0;
                    netAccel.setX(accelX);
                    break;
                case Y:
                    accelY += PhysicsEngine.GRAVITY;
                    netAccel.setY(accelY);
                    break;
                case Z:
                    break;
            }
        }
    }

    private void updateVelocity(Vector3DDouble velocity,
                                Vector3DDouble netAccel, Axis axis, float deltaTime){
        double x, y;
        switch (axis){
            case X:
                x = velocity.getX() + (netAccel.getX() * deltaTime);
                velocity.setX(x);
                break;
            case Y:
                y = velocity.getY() + (netAccel.getY() * deltaTime);
                velocity.setY(y);
                break;
            case Z:
                break;
        }
        //InfoLog.getInstance().debugValue(className, "Velocity: " + y);
    }

    private void updateDeltaDistanceAndDirection(Vector3DInt deltaDistance, Vector3DDirection direction,
                                                 Vector3DDouble velocity, Axis axis, float deltaTime,
                                                 float ePixelPerMeter, int frameWidth,
                                                 int frameHeight){
        double x, y;
        switch (axis){
            case X:
                x = (velocity.getX() * deltaTime) * ePixelPerMeter * frameWidth;
                updateDirection(direction, axis, x);
                deltaDistance.setX((int) x);
                break;
            case Y:
                y = (velocity.getY() * deltaTime) * ePixelPerMeter * frameHeight;
                updateDirection(direction, axis, y);
                deltaDistance.setY((int) y);
                break;
            case Z:
                break;
        }

        /*
        x = (velocity.getX() * deltaTime) * ePixelPerMeter * frameWidth;
        y = (velocity.getY() * deltaTime) * ePixelPerMeter * frameHeight;
        updateDirection(direction, x, y);
        deltaDistance.setX((int) x);
        deltaDistance.setY((int) y);
        */
        //InfoLog.getInstance().debugValue(className, "Time: " + deltaTime);
        //InfoLog.getInstance().debugValue(className, "DeltaDistance: " + deltaDistance.getX());
    }

    /**
     * Update the rigid body direction in x and y axis based on the direction
     * of deltaDistance. When changing between going up or down, reset the time
     * after reaching MAX-Height.
     * @param direction
     * @param axisDouble : the value of the axis in the given direction.
     */
    private void updateDirection(Vector3DDirection direction, Axis axis, double axisDouble){
        switch (axis){
            case X:
                if(axisDouble > 0){
                    direction.setX(Direction.RIGHT);
                }else if(axisDouble < 0){
                    direction.setX(Direction.LEFT);
                }else if(axisDouble == 0){
                    direction.setX(Direction.STATIC);
                }
                break;
            case Y:
                if(axisDouble > 0){
                    direction.setY(Direction.UP);
                } else if(axisDouble < 0){
                    direction.setY(Direction.DOWN);
                }else if(axisDouble == 0 ){
                    direction.setY(Direction.STATIC);
                }
                break;
            case Z:
                break;
        }

        //InfoLog.getInstance().debugValue(className, "Direction: "+direction.getY());
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Collision Detection

    /**
     * Checks if sprite is currently colliding with background/static object.
     * @param sprite
     * @return
     */
    public boolean isSpriteColliding(Sprite sprite){
        /**
         * Check for platforms using center of player sprite, because normally, the
         * sprite position is the topLeft corner.
         */
        Vector3DInt spritePosition = new Vector3DInt();
        spritePosition.setX(sprite.getCanvasPosition().getX() + (sprite.getFrameWidth()/2));
        spritePosition.setY(sprite.getCanvasPosition().getY() + (sprite.getFrameHeight()/2));

        //Grab background sprite
        Sprite background = eMap.getEPixelFromCanvasPosition(spritePosition.getX(),
                spritePosition.getY()).getSprite();

        //Setup sprite  hitbox and platform hitBox
        SpriteHitBox spriteHitBox = sprite.getSpriteHitBox();
        SpriteHitBox backgroundHitBox = background.getSpriteHitBox();

        //check that player hitBox is within border constraints
        if(!isSpriteHitBoxWithinMapConstraints(spriteHitBox)) {
            //InfoLog.getInstance().debugValue(className, "Sprite is not within map constaints.");
            return false;
        }

        //make sure platform is visible
        if(!background.isVisible()){
            //InfoLog.getInstance().debugValue(className, "Platform is invisible");
            return false;
        }

        //Check if sprite hitBox collides with platform hitBox
        return spriteHitBox.isCollidingWithObjectHitBox(backgroundHitBox);
        /*
        return (spriteHitBox.isCollidingWithObjectHitBox(platformHitBox) ||
                spriteHitBox.isBeingIntersectedByHitBox(platformHitBox));
                */
    }

    private boolean isSpriteHitBoxWithinMapConstraints(SpriteHitBox spriteHitBox){
        return (eMap.isPositionWithinMapConstraints(spriteHitBox.getSpriteCorner(SpriteHitBox.TOPLEFT).getCorner()) &&
                eMap.isPositionWithinMapConstraints(spriteHitBox.getSpriteCorner(SpriteHitBox.TOPRIGHT).getCorner()) &&
                eMap.isPositionWithinMapConstraints(spriteHitBox.getSpriteCorner(SpriteHitBox.BOTTOMLEFT).getCorner()) &&
                eMap.isPositionWithinMapConstraints(spriteHitBox.getSpriteCorner(SpriteHitBox.BOTTOMRIGHT).getCorner()));
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Static Movement
    /**
     * Moves sprite based on a static speed. Can be used for games where sprite
     * is moving back and forth like alien shooter.
     * @param sprite
     * @param fps
     */
    public void updateSpriteLocationStaticallyX(Sprite sprite, Direction direction, long fps){
        //InfoLog.getInstance().debugValue("Is Moving",String.valueOf(sprite.isMoving()));
        /*
        switch (direction){
            case LEFT:
                deltaX = - (int) (sprite.getPixelDistancePerSecond()/fps);
                break;
            case RIGHT:
                deltaX = (int) (sprite.getPixelDistancePerSecond()/fps);
                break;
        }
        */
        if(isSpriteHitBoxWithinMapConstraints(sprite.getSpriteHitBox())){
            int deltaX = (int) (sprite.getPixelDistancePerSecond()/fps);
            switch (direction){
                case LEFT:
                    deltaX *= -1 ;
                    break;
                case RIGHT:
                    break;
            }

            //InfoLog.getInstance().debugValue(className, "DeltaX: " + deltaX);
            sprite.getRigidBody().getDeltaDistance().setX(deltaX);
            sprite.updateCanvasPosition(Axis.X);
        }



       // if(sprite.isMoving() && sprite.isWithinScreen()){
            /*
            tempX = sprite.getCanvasPosition().getX();
            switch (direction){
                case LEFT:
                    tempX -= (int) (sprite.getPixelDistancePerSecond()/fps);
                    break;
                case RIGHT:
                    tempX += (int) (sprite.getPixelDistancePerSecond()/fps);
                    break;
            }
            if(!isSpriteHitBoxWithinMapConstraints(sprite.getSpriteHitBox())){
                sprite.getCanvasPosition().setX(tempX);
            }
            */
            //InfoLog.getInstance().debugValue("PositionX", String.valueOf(tempX) + String.valueOf(tempY));

        //}
    }
}
