package edwardwang.bouncingball.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.R;

/**
 * Created by edwardwang on 7/23/16.
 */
public class Player1Sprite extends Sprite{
    private static final String className = Player1Sprite.class.getSimpleName();

    public Player1Sprite(Context context, int positionX, int positionY,
                         int frameWidth, int frameHeight,  double hitBoxWidthPerc, double hitBoxHeightPerc){
        setupSpriteSettings(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player), positionX, positionY, frameWidth, frameHeight,
                true, SpriteType.PLAYER, hitBoxWidthPerc, hitBoxHeightPerc);
        InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_PlayerCreated);
    }
}
