package edwardwang.bouncingball.Games;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.Info.PhoneInfo;
import edwardwang.bouncingball.Map.EMap;
import edwardwang.bouncingball.Map.EPixel;
import edwardwang.bouncingball.PhysicsEngine.Action;
import edwardwang.bouncingball.PhysicsEngine.PhysicsEngine;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Direction;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DInt;
import edwardwang.bouncingball.Sprite.Player1Sprite;
import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteHitBox;
import edwardwang.bouncingball.Sprite.SpriteType;
import edwardwang.bouncingball.View.GameView;

/**
 * Created by edwardwang on 7/25/16.
 */
public class SkyClimberGame extends Game{
    private static final String className = SkyClimberGame.class.getSimpleName();
    public static final String gameName = "Sky Climber";
    private Context context;

    //Constants
    private final int backgroundColor = Color.LTGRAY;
    //Generate new platforms on X axis
    private final int PLUS = 0;
    private final int MINUS = 1;
    private final int NEUTRAL = 2;
    //Use time factor to either speed up or slow down movement
    private final double timeFactor = .4;

    //Map Size in terms of ePixels
    private final int numOfEPixelsWidth = 7;
    private final int numOfEPixelsHeight = 12;
    //Sprite movement size
    private final float ePixelPerMeter = .5f;    // % ePixel = # meters --> inverse to movementSpeed
    //Player 1 Constants
    private final double movementSpeedX = 0;
    private final double movementSpeedY = 8;
    //Platform update position speed
    private final int updatePlatformSpeed = 1;

    //EPixel frame size
    private final SpriteType spriteType = SpriteType.PLATFORM_SKYCLIMBER;
    private EMap eMap;
    private Player1Sprite player1Sprite;

    //private Queue<EPixel> spriteQueue;
    private ArrayList<Sprite> playerSpriteArrayList;
    private ArrayList<EPixel> backgroundSpriteArrayList;

    //PhysicsEngine
    PhysicsEngine physicsEngine;

    //Player
    private int playerOffsetY;
    private final double playerHitBoxPercWidth = 1;
    private final double playerHitBoxPercHeight = 1;
    private SpriteHitBox playerHitBox;

    //Background
    private int startX, startY;
    private final int platformSpawnDistance = 2;
    //The % of ePixel/platform that can be interactable with player sprite
    private final double platformHitBoxPercWidth = 1;
    private final double platformHitBoxPercHeight = 1;
    private SpriteHitBox platformHitBox;
    private int screenHalfwayHeight;
    private int heightAdjustmant;

    public SkyClimberGame(Context context, GameView gameView){
        this.context = context;
        setGameName(gameName);
        setGameView(gameView);
        setSpriteType(spriteType);
        setGameBackground(backgroundColor);
        setNumOfEPixelsWidthHeight(numOfEPixelsWidth, numOfEPixelsHeight);
        setHitBoxWidthHeightPerc(platformHitBoxPercWidth, platformHitBoxPercHeight);
    }

    @Override
    public void setupGame() {
        grabGameViewElements();
        setupPhysicsEngine();
        setupBackground();
        setupPlayer();
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_SkyClimberSetup);
    }


    @Override
    public void grabGameViewElements(){
        //Have twice the height to setup a buffer when setting platforms that need to be created
        eMap = getGameView().getEMap();
        //TODO:Test between queue and arrayList
        //spriteQueue = getGameView().getEMap().getSpriteQueue();
        playerSpriteArrayList = eMap.getPlayerSpriteArrayList();
        backgroundSpriteArrayList = eMap.getBackgroundSpriteArrayList();
        screenHalfwayHeight = PhoneInfo.getInstance().getScreenHeight() -
                ((numOfEPixelsHeight/2) * eMap.getePixelHeight() + (eMap.getMapOffSetHeight()/2));
    }

    @Override
    public void setupPhysicsEngine(){
        physicsEngine = getPhysicsEngine();
        physicsEngine.setEMap(eMap);
        setTimeFactor(timeFactor);
    }

    @Override
    public void setupBackground() {
        //Get the starting position for both first platform and player sprite
        startX = numOfEPixelsWidth / 2;
        //startY = getRandomNumGenerator().nextInt(numOfEPixelsHeight);
        startY = numOfEPixelsHeight - 1;
        eMap.setEPixelVisible(startX, startY);

        //TODO:Test this out
            //spriteQueue.add(eMap.getEPixel(startX, startY));
        backgroundSpriteArrayList.add(eMap.getEPixel(startX, startY));
        fillMissingPlatforms();
    }

    @Override
    public void setupPlayer() {
        playerOffsetY = eMap.getePixelHeight() / 5;
        int playerStartX = eMap.getEPixel(startX,startY).getPositionCanvasX();
        int playerStartY = eMap.getEPixel(startX,startY).getPositionCanvasY() -
                playerOffsetY;
        player1Sprite = new Player1Sprite(context, playerStartX, playerStartY,
                eMap.getePixelWidth(), eMap.getePixelHeight(),
                playerHitBoxPercWidth, playerHitBoxPercHeight);
        playerHitBox = player1Sprite.getSpriteHitBox();
        playerSpriteArrayList.add(player1Sprite);
        //playerSpeed
        player1Sprite.getRigidBody().setMovementSpeed(movementSpeedX, movementSpeedY);
        //player movement
        player1Sprite.getRigidBody().getDirection().setY(Direction.UP);
        //set isOnGround
        player1Sprite.setIsOnGround(false);
        //The distance player sprite moves
        setEPixelPerMeter(ePixelPerMeter);
        //Set the corner checklist in hitBox
        setupPlayerHitBoxCornerCheckList();
    }

    @Override
    public void updateGame() {
        handlePlayerMoving();
    }

    /**
     * Setup which corners on player sprite hitbox to check for collision
     */
    private void setupPlayerHitBoxCornerCheckList(){
        player1Sprite.getSpriteHitBox().setCornerCheckListStatus(SpriteHitBox.BOTTOMLEFT);
        player1Sprite.getSpriteHitBox().setCornerCheckListStatus(SpriteHitBox.BOTTOMRIGHT);
    }

    /**
     * Check if player is above the halfway point of screen. If so, the placements
     * of the platforms are updated to provide the illusion that user is going up.
     */
    private void handlePlayerIsAboveScreenHalfway(){
        Vector2DInt playerPosition = player1Sprite.getPosition();

        //Check if player position is above halfway point
        if(playerPosition.getY() <= screenHalfwayHeight){
            //signal that it is time to update the background
            setIsBackgroundUpdated(true);
            if(player1Sprite.getRigidBody().getDirection().getY().equals(Direction.DOWN) &&
                    isBackgroundReadyToUpdate()){
                //InfoLog.getInstance().debugValue(className, "ScreenHalfway: " + screenHalfwayHeight);
                //InfoLog.getInstance().debugValue(className, "PlayerY: " + playerPosition.getY());

                updatePreviousPlatformPositionY();
                fillMissingPlatforms();

                //reset background update flag
                setIsBackgroundUpdated(false);
            }
        }
    }

    /**
     * Use physics engine to find next canvas location of player sprite.
     * Using the returned result, check and handle if player hits a platform.
     */
    private void handlePlayerMoving(){
        //check if player is hitting a platform
        if(player1Sprite.getRigidBody().getDirection().getY().equals(Direction.DOWN) &&
                physicsEngine.isSpriteColliding(player1Sprite)){
            handlePlayerIsAboveScreenHalfway();
            physicsEngine.setSpriteAction(Action.JUMP, player1Sprite);

            //InfoLog.getInstance().debugValue(className, "COLLIDING");
        }
        physicsEngine.updateSpriteLocation(player1Sprite, getDeltaTime(),
                getTimeFactor(), ePixelPerMeter);

    }
    //////////////////////////////////////////////////////////////////////

    /**
     * TODO:Check if i want this to replace the updatePLatformQueue
     *
     * After every jump above the middle of the height, the platforms will be moved down.
     * Set current platforms to INVisible and the ones below Visible. The height can be
     * discerned as
     *
     * if user landed on platform above middle of screen (doesn't matter, just move platforms anyways)
     * heightAdjustment = (player.maxHeightBeforeGoingDown - previousPlatform.positionY)
     *
     * -update platforms
     * -adjust height
     * -if any positionY < 0, deque them
     */
    private void updatePreviousPlatformPositionY(){
        int currentX;
        int newHeight;
        EPixel ePixel;
        for(int i=0;i< backgroundSpriteArrayList.size();i++){
            ePixel = backgroundSpriteArrayList.get(i);
            //Get new platform position at currentY + 1 AND current X position
            currentX = ePixel.getPositionEMapX();
            newHeight = ePixel.getPositionEMapY() + updatePlatformSpeed;
            //Set current ePixel visibility to false
            ePixel.getSprite().setIsVisible(false);
            //if newHeight extends beyond the map, remove the item
            if(newHeight >= eMap.getNumOfEPixelsHeight()){
                backgroundSpriteArrayList.remove(i);
            }else{
                ePixel = eMap.getEPixel(currentX, newHeight);
                ePixel.getSprite().setIsVisible(true);
                backgroundSpriteArrayList.set(i, ePixel);
            }

            /*
            //Update the platform positions by the amount of height moved
            newHeight = ePixel.getPositionCanvasY() - 1;
            if(newHeight > 0){
                ePixel.getSprite().getPosition().setY(newHeight);
            }else{
                //remove this ePixel
                backgroundSpriteArrayList.remove(i);
            }
            */
        }
    }

    /**
     * After dequeing or when first setting up, add platforms to queue such that
     * its size will always be twice the height of screen
     *
     * Problem: if the y is updated by 2, then the total spots that
     */
    private void fillMissingPlatforms() {
        int newX = 0;
        int newY = 0;
        int previousX = 0;
        int previousY = 0;
        //int currentY = startY;
        int counter = 0;
        EPixel previousPlatform;

        do{
            previousPlatform = backgroundSpriteArrayList.get(backgroundSpriteArrayList.size() - 1);
            //previousPlatform = backgroundSpriteArrayList.get(counter);
            previousX = previousPlatform.getPositionEMapX();
            previousY = previousPlatform.getPositionEMapY();
            //get new position at set spawn distances away, including original positionX only
            newX = generateNewX(previousX);
            newY = generateNewY(previousY);

            //InfoLog.getInstance().debugValue("SpriteLocation",
            //        newX + ", " + newY + " = " + eMap.isEPositionWithinBounds(newX, newY));

            if(newY > -1 && newY < numOfEPixelsHeight && newX < numOfEPixelsWidth && newX > -1){
                eMap.setEPixelVisible(newX, newY);
                backgroundSpriteArrayList.add(eMap.getEPixel(newX, newY));
            }
            counter++;
        }while(backgroundSpriteArrayList.size() < numOfEPixelsHeight  &&
                eMap.isEPositionWithinBounds(newX, newY));




        /*
        while (currentY >= 0) {
            previousPlatform = backgroundSpriteArrayList.get(counter);
            previousX = previousPlatform.getPositionEMapX();
            previousY = previousPlatform.getPositionEMapY();

            //get new position at set spawn distances away, including original positionX only
            newX = generateNewX(previousX);
            newY = generateNewY(previousY);

            InfoLog.getInstance().debugValue("SpriteLocation",
                    newX + ", " + newY + " = " + eMap.isLocationWithinBounds(newX, newY));

            if (eMap.isLocationWithinBounds(newX, newY)) {
                eMap.setEPixelVisible(newX, newY);
                backgroundSpriteArrayList.add(eMap.getEPixel(newX, newY));
            }else{
                //TODO: review this
                break;
            }
            currentY = newY;
            counter++;
        }
        */

    }

        /*
        //fill in the empty spots for the sprite queue
        for(int i=0;i<startY;i++){
            /*
            //element() is like peek(), but it throws exception if queue is empty
            try{
                //previousX = spriteQueue.element().getCanvasPositionX();
                //previousY = spriteQueue.element().getCanvasPositionY();


            }catch(NoSuchElementException e){
                e.printStackTrace();
                InfoLog.getInstance().generateLog(className,InfoLog.getInstance().error_SpriteQueueEmpty);
            }
            *
            previousPlatform = backgroundSpriteArrayList.get(i);
            previousX = previousPlatform.getPositionEMapX();
            previousY = previousPlatform.getPositionEMapY();

            //get new position at set spawn distances away, including original positionX only
            newX = generateNewX(previousX);
            newY = generateNewY(previousY);

            InfoLog.getInstance().debugValue("SpriteLocation",
                    newX + ", " + newY + " = " + eMap.isLocationWithinBounds(newX, newY));

            if(eMap.isLocationWithinBounds(newX, newY)){
                eMap.setEPixelVisible(newX, newY);
                backgroundSpriteArrayList.add(eMap.getEPixel(newX,newY));
            }
        }
       }
   */

    private int generateNewX(int previousX){
        int xUpdate = getRandomNumGenerator().nextInt(platformSpawnDistance);
        int newX = 0;
        switch (generatePlusMinus()){
            case PLUS:
                newX = previousX + xUpdate;
                break;
            case MINUS:
                newX = previousX - xUpdate;
                break;
            case NEUTRAL:
                return previousX;
        }
        if(newX >= 0 && newX <= numOfEPixelsWidth){
            return newX;
        }
        generateNewX(previousX);
        return 0;
    }

    private int generatePlusMinus(){
        //TODO:check that it only generates between 0 or 1 or 2
        return getRandomNumGenerator().nextInt(3);
    }

    //TODO:Review this for better solution
    private int generateNewY(int previousY){
        int updateValue = getRandomNumGenerator().nextInt(platformSpawnDistance);
        if(updateValue == 0){
            updateValue = previousY - 1;
        }else{
            updateValue = previousY - updateValue;
        }
        return updateValue;
    }
}
