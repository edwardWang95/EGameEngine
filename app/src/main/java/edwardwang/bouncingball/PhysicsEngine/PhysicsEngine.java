package edwardwang.bouncingball.PhysicsEngine;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.Map.EMap;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Direction;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DDirection;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DFloat;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DInt;
import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteHitBox;

/**
 * Info websites
 * http://gamedev.stackexchange.com
 * http://gamedev.stackexchange.com/questions/11262/advice-needed-for-a-physics-engine
 * https://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html
 *
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
    //TODO:TESTING

    public void updateSpriteLocationTEST(Sprite sprite,
                                     float deltaTime, double timeFactor,
                                     float ePixelPerMeter, double maxHeight){
        //setup references
        RigidBody rigidBody = sprite.getRigidBody();
        Vector2DDouble netAccel = rigidBody.getNetAccel();
        Vector2DDouble velocity = rigidBody.getVelocity();
        Vector2DInt deltaDistance = rigidBody.getDeltaDistance();
        Vector2DDirection direction = rigidBody.getDirection();
        //update sprite onGround status
        if(deltaTime != 0){
            timeElapsed += (deltaTime * timeFactor);
            updateNetAccel(netAccel, sprite.isOnGround());
            //updateVelocity(velocity, netAccel, deltaTime);
            updateVelocity(velocity, netAccel, timeElapsed);
            /*
            updateDeltaDistanceAndDirection(deltaDistance, velocity, deltaTime,
                    ePixelPerMeter, sprite.getFrameWidth(), sprite.getFrameHeight());
            */
            updateDeltaDistanceAndDirection(deltaDistance, direction, velocity, timeElapsed,
                    ePixelPerMeter, eMap.getePixelWidth(), eMap.getePixelHeight());
            sprite.updatePosition();
        }
    }

    //////////////////////////////////////////////////////////////////////
    public void setSpriteAction(Action action, Sprite sprite){
        this.action = action;
        switch (action){
            case JUMP:
                sprite.setIsOnGround(false);
                sprite.getRigidBody().resetVelocity();
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
    public void updateSpriteLocation(Sprite sprite,
                                     float deltaTime, double timeFactor,
                                     float ePixelPerMeter){
        //setup references
        RigidBody rigidBody = sprite.getRigidBody();
        Vector2DDouble netAccel = rigidBody.getNetAccel();
        Vector2DDouble velocity = rigidBody.getVelocity();
        Vector2DInt deltaDistance = rigidBody.getDeltaDistance();
        //Directions
        Vector2DDirection direction = rigidBody.getDirection();
        Direction previousDirectY = direction.getY();
        //update sprite onGround status
        if(deltaTime != 0){
            timeElapsed += (deltaTime * timeFactor);
            updateNetAccel(netAccel, sprite.isOnGround());
            //updateVelocity(velocity, netAccel, deltaTime);
            updateVelocity(velocity, netAccel, timeElapsed);
            /*
            updateDeltaDistanceAndDirection(deltaDistance, velocity, deltaTime,
                    ePixelPerMeter, sprite.getFrameWidth(), sprite.getFrameHeight());
            */
            updateDeltaDistanceAndDirection(deltaDistance, direction, velocity, timeElapsed,
                    ePixelPerMeter, eMap.getePixelWidth(), eMap.getePixelHeight());

            //if direction change is updated, reset deltaTime
            if(!direction.getY().equals(previousDirectY)){
                resetTimeElapsed();
            }

            sprite.updatePosition();
        }
    }

    /**
     * Update netAccel based on the isOnGround boolean.
     * @param isOnGround
     * @return netAccelleration
     */
    private void updateNetAccel(Vector2DDouble netAccel, boolean isOnGround){
        double accelX = 0;
        double accelY = 0;
        if(isOnGround){
            accelX = 0;
            accelY += PhysicsEngine.GRAVITY + PhysicsEngine.NORMAL;
        }else{
            accelX = 0;
            accelY += PhysicsEngine.GRAVITY;
        }
        netAccel.setX(accelX);
        netAccel.setY(accelY);
    }

    private void updateVelocity(Vector2DDouble velocity,
                                Vector2DDouble netAccel, float deltaTime){
        double x, y;
        x = velocity.getX() + (netAccel.getX() * deltaTime);
        velocity.setX(x);
        y = velocity.getY() + (netAccel.getY() * deltaTime);
        velocity.setY(y);

        //InfoLog.getInstance().debugValue(className, "Velocity: " + y);
    }

    private void updateDeltaDistanceAndDirection(Vector2DInt deltaDistance, Vector2DDirection direction,
                                                 Vector2DDouble velocity, float deltaTime,
                                                 float ePixelPerMeter, int frameWidth,
                                                 int frameHeight){
        double x, y;
        x = (velocity.getX() * deltaTime) * ePixelPerMeter * frameWidth;
        y = (velocity.getY() * deltaTime) * ePixelPerMeter * frameHeight;
        updateDirection(direction, x, y);
        deltaDistance.setX((int) x);
        deltaDistance.setY((int) y);
        //InfoLog.getInstance().debugValue(className, "Time: " + deltaTime);
        //InfoLog.getInstance().debugValue(className, "DeltaDistance: " + deltaDistance.getY());
    }

    /**
     * Update the rigid body direction in x and y axis based on the direction
     * of deltaDistance. When changing between going up or down, reset the time
     * after reaching MAX-Height.
     * @param direction
     * @param x
     * @param y
     */
    private void updateDirection(Vector2DDirection direction, double x, double y){
        //X axis
        if(x > 0){
            direction.setX(Direction.RIGHT);
        }else if(x < 0){
            direction.setX(Direction.LEFT);
        }else if(x == 0){
            direction.setX(Direction.STATIC);
        }
        //Y axis
        if(y > 0){
            direction.setY(Direction.UP);
        } else if(y < 0){
            direction.setY(Direction.DOWN);
        }else if(y == 0 ){
            direction.setY(Direction.STATIC);
        }

        //InfoLog.getInstance().debugValue(className, "Direction: "+direction.getY());
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Collision Detection

    /**
     * Checks if sprite is currently colliding with background/static object
     * @param sprite
     * @return
     */
    public boolean isSpriteColliding(Sprite sprite){
        /**
         * Check for platforms using center of player sprite, because normally, the
         * sprite position is the topLeft corner.
         */
        Vector2DInt playerPosition = new Vector2DInt();
        playerPosition.setX(sprite.getPosition().getX() + (sprite.getFrameWidth()/2));
        playerPosition.setY(sprite.getPosition().getY() + (sprite.getFrameHeight()/2));

        //Grab platform
        Sprite platform = eMap.getEPixelFromCanvasPosition(playerPosition.getX(),
                playerPosition.getY()).getSprite();

        //Setup sprite  hitbox and platform hitBox
        SpriteHitBox spriteHitBox = sprite.getSpriteHitBox();
        SpriteHitBox platformHitBox = platform.getSpriteHitBox();

        //check that player hitBox is within border constraints
        if(!isSpriteHitBoxWithinMapConstraints(spriteHitBox)) {
            //InfoLog.getInstance().debugValue(className, "Sprite is not within map constaints.");
            return false;
        }

        //make sure platform is visible
        if(!platform.isVisible()){
            //InfoLog.getInstance().debugValue(className, "Platform is invisible");
            return false;
        }

        //Get corners of PlatformHitBox
        /*
        Vector2DInt platformTopLeft = new Vector2DInt();
        Vector2DInt platformTopRight = new Vector2DInt();
        Vector2DInt platformBottomLeft = new Vector2DInt();
        Vector2DInt platformBottomRight = new Vector2DInt();
        setSpriteHitBoxCorners(platformHitBox, platformTopLeft, platformTopRight,
                    platformBottomLeft, platformBottomRight);
        */

        //Check if sprite hitbox instersects with platform hitbox
        return platformHitBox.isBeingIntersectedByHitBox(spriteHitBox);
    }

    /*
    /**
     * Check if the player location on EMap has a visible platform sprite. Check
     * both bottom corners for more accuracy.
     * @return
     */
    //int counter = 0;  TODO:remove info code
    /*
    public boolean (Sprite player){
        /**
         * Check for platforms using center of player sprite, because normally, the
         * player position is the topLeft corner.
         */
    /*
        Vector2DInt playerPosition = new Vector2DInt();
        playerPosition.setX(player.getPosition().getX() + (player.getFrameWidth()/2));
        playerPosition.setY(player.getPosition().getY() + (player.getFrameHeight()/2));

        //Grab platform
        Sprite platform = eMap.getEPixel(playerPosition.getX(),
                player.getPosition().getY()).getSprite();

        //Grab hit box
        SpriteHitBox playerHitBox = player.getSpriteHitBox();
        SpriteHitBox platformHitBox = platform.getSpriteHitBox();

        //First check if the given platform sprite is visible
        if(!platform.isVisible()){
            return false;
        }

        //Corners of Player HitBox
        Vector2DInt playerTopLeft = new Vector2DInt();
        Vector2DInt playerTopRight = new Vector2DInt();
        Vector2DInt playerBottomLeft = new Vector2DInt();
        Vector2DInt playerBottomRight = new Vector2DInt();
        setSpriteHitBoxCorners(playerHitBox, playerTopLeft, playerTopRight,
                playerBottomLeft, playerBottomRight);

        //check that player hitBox is within border constraints
        if(isSpriteHitBoxWithinMapConstraints(playerTopLeft,playerTopRight,
                playerBottomLeft,playerBottomRight)){
            //Corners of PlatformHitBox
            Vector2DInt platformTopLeft = new Vector2DInt();
            Vector2DInt platformTopRight = new Vector2DInt();
            Vector2DInt platformBottomLeft = new Vector2DInt();
            Vector2DInt platformBottomRight = new Vector2DInt();
            setSpriteHitBoxCorners(platformHitBox, platformTopLeft, platformTopRight,
                    platformBottomLeft, platformBottomRight);


        }
        /*
        if(counter == 0){
            InfoLog.getInstance().debugValue(className,"EMapLeftX: " + eMapPositionLeftX);
            InfoLog.getInstance().debugValue(className,"EMapLeftY: " + eMapPositionLeftY);
            InfoLog.getInstance().debugValue(className,"EMapRightX: " + eMapPositionRightX);
            InfoLog.getInstance().debugValue(className,"EMapRightX: " + eMapPositionRightY);
            counter++;
        }
        */
/*
        //make sure player sprite is in one ePixel on eMap
        if(eMapPositionLeft.getX() == eMapPositionRight.getX() &&
                eMapPositionLeft.getY() == eMapPositionRight.getY()){
            platformHitBox.isBottomLeftWithinHitBox(playerHitBox) &&
                    platformHitBox.isBottomRightWithinHitBox(playerHitBox)




            //Check if player hitbox is within platform hitbox
            return true;
                /*
                if(platformHitBox.isBottomLeftWithinHitBox(playerHitBox) &&
                        platformHitBox.isBottomRightWithinHitBox(playerHitBox)){
                    return true;
                }
                */
   /*
        }

        return false;
    }
    */

    private boolean isSpriteHitBoxWithinMapConstraintsTest(Vector2DInt topLeft, Vector2DInt topRight,
                                                       Vector2DInt bottomLeft, Vector2DInt bottomRight){
        return (eMap.isPositionWithinMapConstraints(topLeft) &&
                eMap.isPositionWithinMapConstraints(topRight) &&
                eMap.isPositionWithinMapConstraints(bottomLeft) &&
                eMap.isPositionWithinMapConstraints(bottomRight));
    }

    private boolean isSpriteHitBoxWithinMapConstraints(SpriteHitBox spriteHitBox){
        return (eMap.isPositionWithinMapConstraints(spriteHitBox.getTopLeft()) &&
                eMap.isPositionWithinMapConstraints(spriteHitBox.getTopRight()) &&
                eMap.isPositionWithinMapConstraints(spriteHitBox.getBottomLeft()) &&
                eMap.isPositionWithinMapConstraints(spriteHitBox.getBottomRight()));
    }


    private void setSpriteHitBoxCorners(SpriteHitBox hitBox,
                                        Vector2DInt topLeft, Vector2DInt topRight,
                                        Vector2DInt bottomLeft, Vector2DInt bottomRight){
        //TopLeft
        topLeft.setX(hitBox.getTopLeft().getX());
        topLeft.setY(hitBox.getTopLeft().getY());

        //TopRight
        topRight.setX(hitBox.getTopRight().getX());
        topRight.setY(hitBox.getTopRight().getY());

        //BottomLeft
        bottomLeft.setX(hitBox.getBottomLeft().getX());
        bottomLeft.setY(hitBox.getBottomLeft().getY());

        //BottomRight
        bottomRight.setX(hitBox.getBottomRight().getX());
        bottomRight.setY(hitBox.getBottomRight().getY());
    }



    ////////////////////////////////////////////////////////////////////////////////



    //TODO:REMOVE
    //variables used to temp hold sprite info
    private int tempX, tempY;
    private int tempVelocityX, tempVelocityY;

    int counter = 5;

    /**
     * Receive action from user, add the specific force to the the sprite's
     * rigid body and return the distance traveled in meters. The conversion
     * of meters to ePixels is determined by the specific game.
     *
     * Note: Here shows a way to use enums in switch case --> setup a local variable of the
     * enum and refer to it in case without the enum class name.
     * @param action
     * @param deltaTime
     * @param sprite
     * @return deltaDistance of pixels
     */
    public void updateSpriteLocationFromActionTEST(Action action, Sprite sprite,
                                                   float deltaTime, int meterToEPixel){
        this.action = action;
        Vector2DDouble netAccel = new Vector2DDouble();
        Vector2DFloat metersTravelled = new Vector2DFloat();
        Vector2DInt newPosition = new Vector2DInt();
        RigidBody rigidBody = sprite.getRigidBody();
        switch (action){
            case JUMP:
                rigidBody.addAccelY(rigidBody.getJumpSpeed());
                break;
            case WALK:
                rigidBody.addAccelX(rigidBody.getWalkSpeed());
                break;
            case RUN:
                rigidBody.addAccelX(rigidBody.getRunSpeed());
                break;
        }
        //get netAccel
        /*
        netAccel.setX(rigidBody.getNetAccel().getX());
        netAccel.setY(rigidBody.getNetAccel().getY());
        */

        //generate meters travelled
        metersTravelled.setX(generateMetersTraveledTEST(rigidBody.getVelocityInitial().getX(),
                netAccel.getX(), deltaTime));
        metersTravelled.setY(generateMetersTraveledTEST(rigidBody.getVelocityInitial().getY(),
                netAccel.getY(), deltaTime));
        //reset net forces
        sprite.getRigidBody().resetNetForce();
        //get delta distance in pixels from meters
        newPosition.setX(convertMeterToPixel(metersTravelled.getX(), meterToEPixel,
                sprite.getFrameWidth()));
        newPosition.setY(convertMeterToPixel(metersTravelled.getY(), meterToEPixel,
                sprite.getFrameWidth()));
        //update sprite location

        InfoLog.getInstance().debugValue(className, "PositionX: " + newPosition.getX() +
                " PositionY: " + newPosition.getY());

        sprite.updatePosition();
    }



    ////////////////////////////////////////////////////////////////////////////////


    private float generateMetersTraveledTEST(double velocityInitial, double netAccel
            ,float deltaTime){
        float velocityFinal = (float)(velocityInitial + (netAccel * deltaTime));
        return (velocityFinal * deltaTime);
    }


    /**
     * @param velocityInitial
     * @param netAccel
     * @param deltaTime
     * @return distance in meters
     */
    private float generateMetersTraveled(double velocityInitial, double netAccel
                                          ,float deltaTime){


        return (float)((velocityInitial * deltaTime) + (.5*(netAccel)*(deltaTime * deltaTime)));
    }

    /**
     * Converts the meter and returns the amount of pixels the sprite moves.
     * @param meter
     * @param meterToEPixel
     * @param ePixelLength
     * @return deltaPixels
     */
    private int convertMeterToPixel(double meter,int meterToEPixel, int ePixelLength){
        int deltaPixels = (int)(meter / meterToEPixel) * ePixelLength;
        return deltaPixels;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Static Movement
    /**
     * Moves sprite based on a static speed. Can be used for games where sprite
     * is moving back and forth like alien shooter.
     * @param sprite
     * @param fps
     */
    public void updateSpriteLocationStatically(Sprite sprite, long fps){
        //InfoLog.getInstance().debugValue("Is Moving",String.valueOf(sprite.isMoving()));

        if(sprite.isMoving() && sprite.isWithinScreen()){
            tempX = sprite.getPosition().getX();
            tempY = sprite.getPosition().getY();

            tempY += (int) (sprite.getPixelDistancePerSecond()/fps);

            tempY += (int) (sprite.getPixelDistancePerSecond()/fps);

            sprite.getPosition().setY(tempY);
            tempX += (int) (sprite.getPixelDistancePerSecond()/fps);
            sprite.getPosition().setX(tempX);
            InfoLog.getInstance().debugValue("PositionXY", String.valueOf(tempX) + String.valueOf(tempY));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    //Dynamic Movement Due To Physics
    /**
     * Moves sprite dynamically based on physics.
     * Formulas used:   (i/y/x) subscript
     * y = yi + v(y)t + (1/2)at^2
     * x = xi + v(x)t + (1/2)at^2
     * v = d/t
     * a = v/t
     */
    public void updateSpriteLocationDynamically(Sprite sprite, long fps, long time){
        if(sprite.isMoving() && sprite.isWithinScreen()){
            /*
            sprite.setPositionY(getNewPositionY(sprite.getPositionCanvasY(),
                    sprite.getVelocityY(), sprite.getPixelDistancePerSecond(), fps));
            */
            InfoLog.getInstance().debugValue("Time", String.valueOf(time));
        }
    }

    private int getNewPositionY(int currentPosition, int velocity, float frameChangePerSecond,
                                long fps){
        return ((int)(currentPosition + (velocity * (frameChangePerSecond/fps)) +
                (9.81 * ((frameChangePerSecond/fps) * (frameChangePerSecond/fps)))/2))/5;
    }

    private void updatePositionX(Sprite sprite){

    }


}
