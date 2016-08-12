package edwardwang.bouncingball.Games;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;

import edwardwang.bouncingball.Activity.GameScreenActivity;
import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.Info.PhoneInfo;
import edwardwang.bouncingball.Interaction.ESplitScreen.ESplitScreenManager;
import edwardwang.bouncingball.Interaction.Interaction;
import edwardwang.bouncingball.Interaction.InteractionManager;
import edwardwang.bouncingball.Map.EMap;
import edwardwang.bouncingball.Map.EPixel;
import edwardwang.bouncingball.PhysicsEngine.Action;
import edwardwang.bouncingball.PhysicsEngine.Axis;
import edwardwang.bouncingball.PhysicsEngine.PhysicsEngine;
import edwardwang.bouncingball.PhysicsEngine.Direction;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DDirection;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;
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

    //Constants
    private final int backgroundColor = Color.LTGRAY;
    //Generate new platforms on X axis
    private final int PLUS = 0;
    private final int MINUS = 1;
    private final int NEUTRAL = 2;
    //Use time factor to either speed up or slow down movement
    private final double timeFactor = .08;

    //Map Size in terms of ePixels
    private final int numOfEPixelsWidth = 12;
    private final int numOfEPixelsHeight = 12;
    //Sprite movement size
    private final float ePixelPerMeter = .5f;    // % ePixel = # meters --> inverse to movementSpeed
    //Player 1 Constants
    private final double movementSpeedX = 0;
    private final double movementSpeedY = 10;

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
    private final float playerPixelDistancePerSecond = 500;

    //Background
    private int startX, startY;
    private final int platformSpawnDistance = 2;
    //The % of ePixel/platform that can be interactable with player sprite
    private final double platformHitBoxPercWidth = 1;
    private final double platformHitBoxPercHeight = 1;
    private int screenHalfwayHeight;
    //TODO:May include this variable in Game class
    //The height
    private Vector3DInt updatePlatformSpeed = new Vector3DInt();

    //InteractionManager
    private InteractionManager interactionManager;
    private ESplitScreenManager splitScreenManager;

    //Score Keeping
    private int platformDifference = 0;
    private int currentEPlatformPosition = 0;
    private int newEPlatformPosition = 0;

    public SkyClimberGame(Context context, GameView gameView, GameScreenActivity gameScreenActivity){
        setContext(context);
        setGameName(gameName);
        setGameView(gameView);
        setSpriteType(spriteType);
        setGameBackground(backgroundColor);
        setNumOfEPixelsWidthHeight(numOfEPixelsWidth, numOfEPixelsHeight);
        setHitBoxWidthHeightPerc(platformHitBoxPercWidth, platformHitBoxPercHeight);
        setGameScreenActivity(gameScreenActivity);
    }

    ////////////////////////////////////////////////////////////////////////
    //Superclass override methods

    @Override
    public void setupGame() {
        grabGameViewElements();
        setupPhysicsEngine();
        setupBackground();
        setupPlayer();
        setupInteractionManager();
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
        EPixel playerEMapPosition = eMap.getEPixel(startX, startY);
        /*
        int playerEMapX = playerEMapPosition.getPositionEMapX();
        int playerEMapY = playerEMapPosition.getPositionEMapY();
        int playerCanvasX = playerEMapPosition.getPositionCanvasX();
        int playerCanvasY = playerEMapPosition.getPositionCanvasY() -
                playerOffsetY;
        */
        Vector3DInt playerEMap = new Vector3DInt();
        Vector3DInt playerCanvas = new Vector3DInt();
        playerEMap.setX(playerEMapPosition.getPositionEMapX());
        playerEMap.setY(playerEMapPosition.getPositionEMapY());
        playerCanvas.setX(playerEMapPosition.getPositionCanvasX());
        playerCanvas.setY(playerEMapPosition.getPositionCanvasY() -
                playerOffsetY);


        currentEPlatformPosition = playerEMap.getY();
        newEPlatformPosition = currentEPlatformPosition;
        //InfoLog.getInstance().debugValue(className, "Start Position: "+currentEPlatformPosition);


        //eMapPosition
        player1Sprite = new Player1Sprite(getContext(), playerCanvas, playerEMap,
                eMap.getePixelWidth(), eMap.getePixelHeight(),
                playerHitBoxPercWidth, playerHitBoxPercHeight);
        playerSpriteArrayList.add(player1Sprite);
        //playerSpeed
        player1Sprite.getRigidBody().setMovementSpeed(movementSpeedX, movementSpeedY);
        //player movement
        player1Sprite.getRigidBody().getDirection().setY(Direction.UP);
        //set isOnGround
        player1Sprite.setIsOnGround(false);
        //The distance player sprite moves
        setEPixelPerMeter(ePixelPerMeter);

        //Move player at static pace
        player1Sprite.setPixelDistancePerSecond(playerPixelDistancePerSecond);
        player1Sprite.setPixelDistancePerSecond(playerPixelDistancePerSecond);

        //Set the edge checklist in hitBox
        setupPlayerHitBoxEdgeCheckList();
    }

    @Override
    public void setupInteractionManager(){
        interactionManager = getInteractionManager();
        interactionManager.setupInteractionManager(getContext(),
                getGameView().getSurfaceView());
        interactionManager.addInteraction(Interaction.SplitScreen);
        interactionManager.initInteractions();
        splitScreenManager = interactionManager.getSplitScreenManager();
    }

    @Override
    public void interactionsPause(){
    }

    @Override
    public void interactionsResume(){
    }

    @Override
    public void updateCurrentScore(){
        //InfoLog.getInstance().debugValue(className, "Current: "+currentEPlatformPosition);
        //InfoLog.getInstance().debugValue(className, "New: "+newEPlatformPosition);
        platformDifference = newEPlatformPosition - currentEPlatformPosition;
        if( platformDifference != 0){
            if(newEPlatformPosition < currentEPlatformPosition){
                //above previous
                setCurrentScore((getCurrentScore() + platformDifference));

            }else if(newEPlatformPosition > currentEPlatformPosition){
                //below previous
                setCurrentScore((getCurrentScore() - platformDifference ));
            }
            updateGameScreenCurrentScore();
            InfoLog.getInstance().debugValue(className, "Current Score: "+getCurrentScore());
            currentEPlatformPosition = newEPlatformPosition;
        }
    }

    @Override
    public void updateGame(){
        try{
            handlePlayerMovingVertically();
            handlePlayerMovingHorizontally();
            updateCurrentScore();
        }catch(ArrayIndexOutOfBoundsException e){
            //Game Over
            throwGameOver();
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //Player Management

    /**
     * Unliked moving vertically, the x movement will be handled statically,
     * without incorporating any velocity related.
     */
    private void handlePlayerMovingHorizontally(){
        //InfoLog.getInstance().debugValue(className, "PreviousX: " + previousRotationX);
        //InfoLog.getInstance().debugValue(className, "OrientationX: " + x);
        Vector3DDirection direction = player1Sprite.getRigidBody().getDirection();

        if(splitScreenManager.isScreenTouched()){
            direction.setX(splitScreenManager.getDirection());
            physicsEngine.updateSpriteLocationStaticallyX(player1Sprite,
                    direction.getX(),getFPS());
        }

    }

    /**
     * Use physics engine to find next canvas location of player sprite.
     * Using the returned result, check and handle if player hits a platform.
     */
    private void handlePlayerMovingVertically(){
        //check if player is hitting a platform
        if(player1Sprite.getRigidBody().getDirection().getY().equals(Direction.DOWN) &&
                physicsEngine.isSpriteColliding(player1Sprite)){
            newEPlatformPosition = player1Sprite.geteMapPosition().getY();
            handlePlayerIsAboveScreenHalfway();
            physicsEngine.setSpriteAction(Action.JUMP, player1Sprite);
            //InfoLog.getInstance().debugValue(className, "COLLIDING");
        }
        physicsEngine.updateSpriteLocation(player1Sprite, Axis.Y, getDeltaTime(),
                getTimeFactor(), ePixelPerMeter);
    }

    /**
     * Check if player is above the halfway point of screen. If so, the placements
     * of the platforms are updated to provide the illusion that user is going up.
     */
    private void handlePlayerIsAboveScreenHalfway(){
        Vector3DInt playerPosition = player1Sprite.getCanvasPosition();

        //Check if player position is above halfway point
        if(playerPosition.getY() <= screenHalfwayHeight){
            //signal that it is time to update the background
            setUpdatePlatformSpeed(playerPosition.getY());

            //InfoLog.getInstance().debugValue(className, "ScreenHalfway: " + screenHalfwayHeight);
            //InfoLog.getInstance().debugValue(className, "PlayerY: " + playerPosition.getY());

            updatePreviousPlatformPositionY();
            fillMissingPlatforms();
            updatePlayerPositionY();
            newEPlatformPosition += updatePlatformSpeed.getY();
        }
    }

    /**
     * Setup which corners on player sprite hitbox to check for collision
     */
    private void setupPlayerHitBoxEdgeCheckList(){
        player1Sprite.getSpriteHitBox().setEdgeCheckListStatus(SpriteHitBox.BOTTOM);
    }
    //////////////////////////////////////////////////////////////////////
    //Background Management

    private void setUpdatePlatformSpeed(int playerPositionY){
        int deltaY = ((screenHalfwayHeight - playerPositionY) - (eMap.getMapOffSetHeight()/2))
                / eMap.getePixelHeight();
        updatePlatformSpeed.setY(deltaY);
    }

    /**
     * Update player position alongside the platforms
     */
    private void updatePlayerPositionY(){
        int newY = player1Sprite.getCanvasPosition().getY() +
                (updatePlatformSpeed.getY() * player1Sprite.getFrameHeight());
        player1Sprite.getCanvasPosition().setY(newY);
    }

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
        int currentY;
        int newHeight;
        EPixel ePixel;
        int counter = 0;
        //for(int i=0;i< backgroundSpriteArrayList.size();i++){
        while(counter < backgroundSpriteArrayList.size()){
            ePixel = backgroundSpriteArrayList.get(counter);
            //Get new platform position at currentY + 1 AND current X position
            currentX = ePixel.getPositionEMapX();


            //InfoLog.getInstance().debugValue(className, "UpdatePlatformSpeed: "+
            //updatePlatformSpeed.getY());

            newHeight = ePixel.getPositionEMapY() + updatePlatformSpeed.getY();
            //Set current ePixel visibility to false
            ePixel.getSprite().setIsVisible(false);
            //if newHeight extends beyond the map, remove the item
            if(newHeight >= eMap.getNumOfEPixelsHeight()){
                backgroundSpriteArrayList.remove(counter);
            }else{
                ePixel = eMap.getEPixel(currentX, newHeight);
                ePixel.getSprite().setIsVisible(true);
                backgroundSpriteArrayList.set(counter, ePixel);
                counter++;
            }
        }
    }

    /**
     * After dequeing or when first setting up, add platforms to queue such that
     * its size will always be twice the height of screen
     *
     * Problem: if the y is updated by 2, then the total spots that
     */
    private void fillMissingPlatforms() {
        Vector3DInt newPosition = new Vector3DInt();
        Vector3DInt previousPosition = new Vector3DInt();
        //int currentY = startY;
        EPixel previousPlatform;

        do {
            previousPlatform = backgroundSpriteArrayList.get(backgroundSpriteArrayList.size() - 1);
            //previousPlatform = backgroundSpriteArrayList.get(counter);
            previousPosition.setX(previousPlatform.getPositionEMapX());
            previousPosition.setY(previousPlatform.getPositionEMapY());
            //get new position at set spawn distances away, including original positionX only
            newPosition.setX(generateNewX(previousPosition.getX()));
            newPosition.setY(generateNewY(previousPosition.getY()));
            if (eMap.isEPositionWithinBounds(newPosition.getX(), newPosition.getY())) {
                eMap.setEPixelVisible(newPosition.getX(), newPosition.getY());
                backgroundSpriteArrayList.add(eMap.getEPixel(newPosition.getX(), newPosition.getY()));
            }
        } while (backgroundSpriteArrayList.size() < numOfEPixelsHeight);
    }



            //InfoLog.getInstance().debugValue("SpriteLocation",
            //        newX + ", " + newY + " = " + eMap.isEPositionWithinBounds(newX, newY));

            /*
            if(newY > -1 && newY < numOfEPixelsHeight && newX < numOfEPixelsWidth && newX > -1){
                eMap.setEPixelVisible(newX, newY);
                backgroundSpriteArrayList.add(eMap.getEPixel(newX, newY));
            }




        }while(backgroundSpriteArrayList.size() < numOfEPixelsHeight  &&
                eMap.isEPositionWithinBounds(newX, newY));

*/


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

    ////////////////////////////////////////////////////////////////////////
    //InteractionMethod

}
