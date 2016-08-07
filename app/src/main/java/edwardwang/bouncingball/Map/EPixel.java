package edwardwang.bouncingball.Map;

import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteHitBox;

/**
 * Note: eep isEmpty() --> in the future, it will allow programmers to make
 * items "dissapear" temporarily for other usages.
 * Created by edwardwang on 7/24/16.
 */
public class EPixel {
    private int width;
    private int height;
    //Position x and y on canvas
    private int positionCanvasX;
    private int positionCanvasY;
    //Position x and y on EMap
    private int positionEMapX;
    private int positionEMapY;

    //TODO:check if i still need this bool
    private boolean isEmpty;
    private Sprite sprite;

    public EPixel(int width, int height, int positionCanvasX, int positionCanvasY,
                  int positionEMapX, int positionEMapY){
        this.width = width;
        this.height = height;
        this.positionCanvasX = positionCanvasX;
        this.positionCanvasY = positionCanvasY;
        this.positionEMapX = positionEMapX;
        this.positionEMapY = positionEMapY;
        isEmpty = true;
    }

    //////////////////////////////////////////////////////////////////////
    //Getter
    public Sprite getSprite() {
        return sprite;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int getPositionCanvasX() {
        return positionCanvasX;
    }

    public int getPositionCanvasY() {
        return positionCanvasY;
    }

    public int getPositionEMapX() {
        return positionEMapX;
    }

    public int getPositionEMapY() {
        return positionEMapY;
    }

    //////////////////////////////////////////////////////////////////////
    //Setter
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;

        isEmpty = false;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setPositionEMapX(int positionEMapX) {
        this.positionEMapX = positionEMapX;
    }

    public void setPositionEMapY(int positionEMapY) {
        this.positionEMapY = positionEMapY;
    }
}
