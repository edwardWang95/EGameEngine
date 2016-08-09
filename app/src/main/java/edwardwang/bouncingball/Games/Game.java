package edwardwang.bouncingball.Games;

import java.io.Serializable;
import java.util.Random;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.InteractionLayer.InteractionManager;
import edwardwang.bouncingball.PhysicsEngine.PhysicsEngine;
import edwardwang.bouncingball.Sprite.SpriteType;
import edwardwang.bouncingball.View.GameView;

/**
 * /May not need to be serializable
 * Basic framework that all other games will build their unique parts upon.
 * Each game that extends this will create/manage their own sprites as well
 * as their game logic.
 *
 * Make serializable so that game object can be passed across intents.
 * Created by edwardwang on 7/25/16.
 */
public class Game implements Serializable, Runnable{
    private static final String className = GameView.class.getSimpleName();
    public static final String intentPassString = "Game";

    private String gameName;
    //TODO:setup bitmap for background instead of color
    private int gameBackground;

    //Game View
    private GameView gameView;

    //The % of ePixel per meter
    private float ePixelPerMeter;

    //Speed of sprite movement
    private double timeFactor = 1;

    //Number of EPixels for width/height
    private int numOfEPixelsWidth;
    private int numOfEPixelsHeight;

    //Hitbox Percentage for all sprites
    private double hitBoxWidthPerc = 1;
    private double hitBoxHeightPerc = 1;

    //SpriteType
    private SpriteType spriteType;

    //Game Elements
    private Random randomNumGenerator = new Random();
    private Thread gameThread = null;
    private volatile boolean playing = false;
    private long fps = 0;
    private float deltaTime = 0f; //use to calc fps &&  dT
    private PhysicsEngine physicsEngine = new PhysicsEngine();

    //Background Update status
    private boolean isBackgroundReadyToUpdate = false;

    //InteractionManger
    private InteractionManager interactionManager = new InteractionManager();

    @Override
    public void run() {
        useMilliseconds();
        //useNanoSeconds();
    }

    //TODO:Choose between milliseconds or nanoseconds
    private void useMilliseconds(){
        //long milliseconds;
        while(isPlaying()){
            long startFrameTimeMilli = System.currentTimeMillis();
            //update and draw frame before updating fps
            updateGame();
            gameView.draw();
            //milliseconds = System.currentTimeMillis() - startFrameTimeMilli;
            deltaTime = (System.currentTimeMillis() - startFrameTimeMilli) / 1000.f;


            //InfoLog.getInstance().debugValue(className, "time= " + deltaTime);

            if(deltaTime >= 1){
                fps = (1000 / (long) deltaTime);
            }
        }
    }

    public void resetTime(){
        deltaTime = 0;
    }

    private void useNanoSeconds(){
        while(isPlaying()){
            long startFrameTimeNano = System.nanoTime();
            //update and draw frame before updating fps
            updateGame();
            gameView.draw();
            deltaTime += (System.nanoTime() - startFrameTimeNano)/1000000000.0f;

            //InfoLog.getInstance().debugValue(className, "time= " + nanoseconds);

            /*
            if(deltaTime >= 1){
                fps = 1000 / milliseconds;
            }
            */
        }
    }

    public void pause(){
        playing = false;
        try{
            gameThread.join();
            interactionsPause();
            InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_PauseGameThread);
        }catch (InterruptedException e){
            InfoLog.getInstance().generateLog(className, InfoLog.getInstance().error_PauseGameThread);
        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        interactionsResume();
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_ResumeGameThread);
    }

    ////////////////////////////////////////////////////////////////////////////
    //Override Methods

    public void setupGame() {}

    public void grabGameViewElements(){}

    public void setupPhysicsEngine(){}

    public void setupBackground() {}

    public void setupPlayer() {}

    public void setupInteractionManagement(){}

    public void interactionsPause(){}

    public void interactionsResume(){}

    public void updateGame() {}

    ////////////////////////////////////////////////////////////////////////////
    //Getter
    public String getGameName() {
        return gameName;
    }

    public int getGameBackground() {
        return gameBackground;
    }

    public GameView getGameView() {
        return gameView;
    }

    public int getNumOfEPixelsWidth() {
        return numOfEPixelsWidth;
    }

    public int getNumOfEPixelsHeight() {
        return numOfEPixelsHeight;
    }

    public SpriteType getSpriteType() {
        return spriteType;
    }

    public Random getRandomNumGenerator() {
        return randomNumGenerator;
    }

    public boolean isPlaying() {
        return playing;
    }

    public long getFPS() {
        return fps;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public double getHitBoxWidthPerc() {
        return hitBoxWidthPerc;
    }

    public double getHitBoxHeightPerc() {
        return hitBoxHeightPerc;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public float getePixelPerMeter() {
        return ePixelPerMeter;
    }

    public double getTimeFactor() {
        return timeFactor;
    }

    public boolean isBackgroundReadyToUpdate() {
        return isBackgroundReadyToUpdate;
    }

    public InteractionManager getInteractionManager() {
        return interactionManager;
    }

    ////////////////////////////////////////////////////////////////////////////
    //Setter
    public void setNumOfEPixelsWidthHeight(int numOfEPixelsWidth, int numOfEPixelsHeight){
        this.numOfEPixelsWidth = numOfEPixelsWidth;
        this.numOfEPixelsHeight = numOfEPixelsHeight;
    }

    public void setHitBoxWidthHeightPerc(double hitBoxWidthPerc, double hitBoxHeightPerc){
        this.hitBoxWidthPerc = hitBoxWidthPerc;
        this.hitBoxHeightPerc = hitBoxHeightPerc;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameBackground(int gameBackground) {
        this.gameBackground = gameBackground;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setSpriteType(SpriteType spriteType) {
        this.spriteType = spriteType;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setFPS(long fps) {
        this.fps = fps;
    }

    public void setDeltaTime(long deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void setEPixelPerMeter(float ePixelPerMeter) {
        this.ePixelPerMeter = ePixelPerMeter;
    }

    public void setTimeFactor(double timeFactor) {
        this.timeFactor = timeFactor;
    }

    public void setIsBackgroundUpdated(boolean isBackgroundUpdated) {
        this.isBackgroundReadyToUpdate = isBackgroundUpdated;
    }
}
