package edwardwang.bouncingball.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.Map.EMap;
import edwardwang.bouncingball.Map.EPixel;
import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteType;

/**
 * Class delegated to display the player sprites/background sprites as they
 * are generated or updated by the Physics Engine.
 * Created by edwardwang on 7/23/16.
 */
public class GameView extends SurfaceView{
    private static final String className = GameView.class.getSimpleName();

    private Context context;
    //Game View Display elements
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;
    private EMap eMap;

    //Test Colors
    private final int testBackgroundColor = Color.BLACK;
    private final int testPlayerColor = Color.BLUE;

    /**
     * Required constructors when implementing SurfaceView.
     */

    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    public void setupGameView(SurfaceView surfaceView, int numOfEPixelsWidth, int numOfEPixelsHeight,
                              double hitBoxWidthPerc, double hitBoxHeightPerc, SpriteType spriteType){
        this.surfaceView = surfaceView;
        surfaceHolder = surfaceView.getHolder();
        paint = new Paint();
        eMap = new EMap(context, numOfEPixelsWidth, numOfEPixelsHeight,
                hitBoxWidthPerc, hitBoxHeightPerc, spriteType);
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_InitGameView);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    /**
     * These methods below handle all drawing all EPixels and sprites.
     */

    public void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            drawLandscape();
            drawBackground();
            drawPlayer();
            surfaceHolder.unlockCanvasAndPost(canvas); //draw everything on the screen
        }
    }

    private void drawLandscape(){
        //canvas.drawColor(Color.argb(200, 0, 0, 0));
        canvas.drawColor(Color.WHITE);
        //choose brush color for drawing
            //paint.setColor(Color.argb(255,  255, 255, 255));
            //paint.setColor(Color.WHITE);
        //make text larger
            //paint.setTextSize(45);
        //display current fps on the screen
            //canvas.drawText("FPS: " + fps, 0, 40, paint);
    }

    /**
     * Display arraylist of background sprites.
     *
     * first check what game is currently being played
     *
     */
    private void drawBackground(){
        ArrayList<EPixel> backgroundSpriteArrayList = eMap.getBackgroundSpriteArrayList();
        EPixel ePixel;
        Sprite sprite;
        paint.setColor(testBackgroundColor);
        if(backgroundSpriteArrayList != null){
            for(int i=0;i<backgroundSpriteArrayList.size();i++){
                ePixel = backgroundSpriteArrayList.get(i);
                sprite = ePixel.getSprite();

                /*
                InfoLog.getInstance().debugValue("SpriteLocationEPixel",
                        sprite.getPositionX() + ", " + sprite.getPositionY());
                */


                //drawSprite(sprite);


                //TODO: comment after testing sprites
                drawSpriteTEST(sprite, paint);

            }
        }else if(eMap.getSpriteQueue() != null){
            //draw them out TODO:finish this method
            for(int i=0;i<getEMap().getNumOfEPixelsHeight();i++){
                ePixel = eMap.getSpriteQueue().element();
            }
        }
    }

    private void drawPlayer(){
        ArrayList<Sprite> playerSpriteArrayList = eMap.getPlayerSpriteArrayList();
        Sprite sprite;
        paint.setColor(testPlayerColor);
        if(playerSpriteArrayList != null){
            for(int i=0;i<playerSpriteArrayList.size();i++){
                sprite = playerSpriteArrayList.get(i);
                //drawSprite(sprite);
                //TODO:comment after testing
                drawSpriteTEST(sprite, paint);
            }
        }
    }

    /**
     * Set the color for given sprite. Used to test sprites.
     * @param sprite
     */
    private void drawSpriteTEST(Sprite sprite, Paint paint){
        sprite.getWhereToDraw().set(sprite.getCanvasPosition().getX(),
                sprite.getCanvasPosition().getY(), sprite.getCanvasPosition().getX() + sprite.getFrameWidth(),
                sprite.getCanvasPosition().getY() + sprite.getFrameHeight());

        sprite.getCurrentFrame();
        canvas.drawBitmap(sprite.getImage(), sprite.getFrameToDraw(),
                sprite.getWhereToDraw(), paint);
    }

    private void drawSprite(Sprite sprite){
        sprite.getWhereToDraw().set(sprite.getCanvasPosition().getX(),
                sprite.getCanvasPosition().getY(), sprite.getCanvasPosition().getX() + sprite.getFrameWidth(),
                sprite.getCanvasPosition().getY() + sprite.getFrameHeight());

        sprite.getCurrentFrame();
        canvas.drawBitmap(sprite.getImage(), sprite.getFrameToDraw(),
                sprite.getWhereToDraw(), null);
    }

    /*
            for(EPixel ePixel : eMap.getBackgroundSpriteArrayList()){
            sprite = ePixel.getSprite();

            sprite.getWhereToDraw().set(sprite.getCanvasPositionX(),
                    sprite.getPositionCanvasY(), sprite.getCanvasPositionX() + sprite.getFrameWidth(),
                    sprite.getPositionCanvasY() + sprite.getFrameHeight());

            //TODO:Remove after testing is done
            //InfoLog.getInstance().debugValue("PositionX", String.valueOf(sprite.getCanvasPositionX()));
            //InfoLog.getInstance().debugValue("PositionY",String.valueOf(sprite.getCanvasPositionY()));
            //InfoLog.getInstance().debugValue("Sprite Image",String.valueOf(sprite.getImage()));

            //canvas.drawBitmap(sprite.getImage(), sprite.getPositionCanvasX(),sprite.getPositionCanvasY(), paint);
            sprite.getCurrentFrame();
            canvas.drawBitmap(sprite.getImage(), sprite.getFrameToDraw(), sprite.getWhereToDraw(), null);
        }

     */

    /*
    private void drawSprites(){
        for(Sprite sprite:spriteList){

            sprite.getWhereToDraw().set(sprite.getCanvasPositionX(),
                    sprite.getPositionCanvasY(), sprite.getCanvasPositionX() + sprite.getFrameHeight(),
                    sprite.getPositionCanvasY() + sprite.getFrameHeight());

            sprite.getCurrentFrame();
            //canvas.drawBitmap(sprite.getImage(), sprite.getPositionCanvasX(),sprite.getPositionCanvasY(), paint);
            canvas.drawBitmap(sprite.getImage(),sprite.getFrameToDraw(),sprite.getWhereToDraw(),paint);
        }
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////

    /*
    private void updateGame(){
        for(Sprite sprite: spriteList){
            //physicsEngine.updateSpriteLocationStatically(sprite, fps);
            //physicsEngine.updateSpriteLocationDynamically(sprite, fps, timeThisFrame);
        }
    }
    */

    /**
     * Handles touch events from user
     * @param
     */
    /*
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            //Player touch screen
            case MotionEvent.ACTION_DOWN:
                for(Sprite sprite: spriteList){
                    sprite.setIsMoving(true);
                    //InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_MotionEventActionDown);
                }
                InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_MotionEventActionDown);
                break;
            //Player removes finger
            case MotionEvent.ACTION_UP:
                for(Sprite sprite: spriteList){
                    sprite.setIsMoving(false);
                }
                InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_MotionEventActionUp);
                break;
        }

        return true;
    }
*/

    ////////////////////////////////////////////////////////////////////////////////////
    //Getters
    public EMap getEMap() {
        return eMap;
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //Setters


}
