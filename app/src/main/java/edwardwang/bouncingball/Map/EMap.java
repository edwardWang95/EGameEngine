package edwardwang.bouncingball.Map;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Queue;

import edwardwang.bouncingball.Activity.StartScreenActivity;
import edwardwang.bouncingball.Info.PhoneInfo;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;
import edwardwang.bouncingball.Sprite.SkyClimberSprite.SkyClimberPlatformSprite;
import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteType;

/**
 * EMap class used to provide more structured way of determining placement
 * of background sprites that need to be drawn on GameView. Using a 2D array,
 * the unit of measurement will be displayed with ePixels, which are the
 * equivalent size of all sprites/background items.
 *
 * There will be a separate playerSprite that users will be able to use and platform
 * on the EMap.
 * Created by edwardwang on 7/24/16.
 */
public class EMap {
    private static final String className = Sprite.class.getSimpleName();
    private Context context;
    /**
     * The map's amount of EPixels will be determined by the amount of EPixels
     * to provide a uniform display across all phone sizes/display metrics
     */
    private int numOfEPixelsWidth = 3;
    private int numOfEPixelsHeight = 3;

    //Used to center map on screen
    private int mapOffSetWidth;
    private int mapOffSetHeight;

    private EPixel[][] map;
    //The dimension of each ePixels
    private int ePixelWidth;
    private int ePixelHeight;

    //borders of eMap
    private int borderTop = 0, borderBottom = 0;    //based on y axis
    private int borderLeft = 0, borderRight = 0;    //based on x axis

    //Contains all ePixels that have a Sprite within them
    SpriteType spriteType;
    //Data sets used to display sprites. They are used according to the given game
    private ArrayList<Sprite> playerSpriteArrayList;
    private ArrayList<EPixel> backgroundSpriteArrayList;
    private Queue<EPixel> spriteQueue = null;


    public EMap(Context context, int numOfEPixelsWidth, int numOfEPixelsHeight,
                double hitBoxWidthPerc, double hitBoxHeightPerc, SpriteType spriteType){
        this.context = context;
        this.numOfEPixelsWidth = numOfEPixelsWidth;
        this.numOfEPixelsHeight = numOfEPixelsHeight;
        this.spriteType = spriteType;
        playerSpriteArrayList = new ArrayList<>();
        backgroundSpriteArrayList = new ArrayList<>();
        setMapOffSets();
        setEPixelWidthHeight();
        setupBorderConstraints();
        map = new EPixel[numOfEPixelsWidth][numOfEPixelsHeight];
        setupEMap(hitBoxWidthPerc, hitBoxHeightPerc);
    }

    /**
     * Set the EPixel width and height by dividing display width/height by the
     * number of ePixels width/height to create a uniform field.
     */
    private void setEPixelWidthHeight(){
        ePixelWidth = (PhoneInfo.getInstance().getScreenWidth() - mapOffSetWidth) /
                    numOfEPixelsWidth;
        ePixelHeight = (PhoneInfo.getInstance().getScreenHeight() - mapOffSetHeight)/
                    numOfEPixelsHeight;
    }

    private void setMapOffSets(){
        mapOffSetWidth = PhoneInfo.getInstance().getScreenWidth() %
                numOfEPixelsWidth;
        mapOffSetHeight = PhoneInfo.getInstance().getScreenHeight() %
                numOfEPixelsHeight;
        PhoneInfo.getInstance().seteMapOffSets(mapOffSetWidth, mapOffSetHeight, 0);
    }

    /**
     * Creates the 2D EMap based on the amount of EPixels for width and height
     */
    public void setupEMap(double hitBoxWidthPerc, double hitBoxHeightPerc){
        Vector3DInt canvasPosition;
        Vector3DInt eMapPosition;
        for(int i=0; i<numOfEPixelsWidth; i++){
            for(int j=0; j<numOfEPixelsHeight; j++){
                canvasPosition = new Vector3DInt();
                eMapPosition = new Vector3DInt();
                canvasPosition.setX((mapOffSetWidth/2) + (ePixelWidth * i));
                canvasPosition.setY((mapOffSetHeight / 2) + (ePixelHeight * j));
                map[i][j] = new EPixel(ePixelWidth, ePixelHeight,
                        canvasPosition.getX(), canvasPosition.getY(), i, j);
                eMapPosition.setX(i);
                eMapPosition.setY(j);
                fillEMapWithPlatformObjects(map[i][j], canvasPosition, eMapPosition,
                        hitBoxWidthPerc, hitBoxHeightPerc);
            }
        }
    }

    /**
     * Find borders of the eMap position wise on canvas.
     */
    private void setupBorderConstraints(){
        //borderTop
        borderTop = mapOffSetHeight/2;
        //borderBottom
        borderBottom = (mapOffSetHeight/2) + (ePixelHeight * numOfEPixelsHeight);
        //borderLeft
        borderLeft = mapOffSetWidth/2;
        //borderRight
        borderRight = (mapOffSetWidth/2) + (ePixelWidth * numOfEPixelsWidth);
    }

    public boolean isPositionWithinMapConstraints(Vector3DInt position){
        int x = position.getX();
        int y = position.getY();
        return (x >= borderLeft && x <= borderRight && y >= borderTop && y <= borderBottom);
    }

    /**
     * Fills ePixel with a background platform pixels
     */
    private void fillEMapWithPlatformObjects(EPixel ePixel, Vector3DInt canvasPosition, Vector3DInt eMapPosition,
                                             double hitBoxWidthPerc, double hitBoxHeightPerc){
        Sprite sprite = null;
        switch (spriteType){
            case PLATFORM_SKYCLIMBER:
                sprite= new SkyClimberPlatformSprite(context,
                        canvasPosition, eMapPosition, ePixelWidth, ePixelHeight,
                        hitBoxWidthPerc, hitBoxHeightPerc);
            break;
        }
        ePixel.setSprite(sprite);
    }

    public EPixel getEPixelFromCanvasPosition(int positionX, int positionY){
        EPixel ePixel = new EPixel();
        try{


        }catch(ArrayIndexOutOfBoundsException e){
            //TODO:Setup the game over
            //Intent intent = new Intent(context, StartScreenActivity.class);
            //context.getApplicationContext().startActivity(intent);
        }

        int x = (positionX + (mapOffSetWidth/2)) / ePixelWidth;
        int y = (positionY + (mapOffSetHeight/2)) / ePixelHeight;
        if(x >= numOfEPixelsWidth){
            x -= 1;
        }else if(x < 0){
            x = 0;
        }
        if(y >= numOfEPixelsHeight){
            y -= 1;
        }else if(y < 0){
            //TODO: GAME OVER
            y = 0;
        }
        //InfoLog.getInstance().debugValue(className, "PlatformX: "+x + " PlatformY: "+y);
        ePixel = map[x][y];

        return ePixel;
    }

    //////////////////////////////////////////////////////////////////////
    //Setter
    /**
     * Used to move and display player sprite.
     * @param x
     * @param y
     * @param sprite
     */
    public void setPlayerSprite(int x, int y, Sprite sprite){
        map[x][y].setSprite(sprite);
        backgroundSpriteArrayList.add(map[x][y]);
    }


    public void setEPixelVisible(int x, int y){
        map[x][y].getSprite().setIsVisible(true);
    }

    public void setEPixelINVisible(int x, int y){
        map[x][y].getSprite().setIsVisible(false);
    }

    public boolean isEPositionWithinBounds(int x, int y){
        return(x <= numOfEPixelsWidth && x >= 0
                && y <= numOfEPixelsHeight && y >= 0);
    }

    public void setNumOfEPixelsHeight(int numOfEPixelsHeight) {
        this.numOfEPixelsHeight = numOfEPixelsHeight;
    }

    public void setNumOfEPixelsWidth(int numOfEPixelsWidth) {
        this.numOfEPixelsWidth = numOfEPixelsWidth;
    }

    //////////////////////////////////////////////////////////////////////
    //Getter
    public EPixel[][] getMap(){
        return map;
    }

    public EPixel getEPixel(int x, int y){
        return map[x][y];
    }


    public ArrayList<EPixel> getBackgroundSpriteArrayList() {
        return backgroundSpriteArrayList;
    }

    public Queue<EPixel> getSpriteQueue() {
        return spriteQueue;
    }

    public int getNumOfEPixelsWidth() {
        return numOfEPixelsWidth;
    }

    public int getNumOfEPixelsHeight() {
        return numOfEPixelsHeight;
    }

    public int getMapOffSetHeight() {
        return mapOffSetHeight;
    }

    public int getMapOffSetWidth() {
        return mapOffSetWidth;
    }

    public ArrayList<Sprite> getPlayerSpriteArrayList() {
        return playerSpriteArrayList;
    }

    public int getePixelWidth() {
        return ePixelWidth;
    }

    public int getePixelHeight() {
        return ePixelHeight;
    }

    /**
     * Used to find where point is in conjunction to eMap
     * @param canvasPositionX
     * @return eMap position X
     */
    public int getEMapPositionX(int canvasPositionX){
        return canvasPositionX / ePixelWidth;
    }
    public int getEMapPositionY(int canvasPositionY){
        return canvasPositionY / ePixelHeight;
    }
}
