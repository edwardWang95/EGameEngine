package edwardwang.bouncingball.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;
import edwardwang.bouncingball.R;

/**
 * Created by edwardwang on 7/23/16.
 */
public class Player1Sprite extends Sprite{
    private static final String className = Player1Sprite.class.getSimpleName();

    public Player1Sprite(Context context, Vector3DInt canvasPosition, Vector3DInt eMapPosition,
                         int frameWidth, int frameHeight,  double hitBoxWidthPerc, double hitBoxHeightPerc){
        setupSpriteSettings(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player), canvasPosition, eMapPosition, frameWidth, frameHeight,
                true, SpriteType.PLAYER, hitBoxWidthPerc, hitBoxHeightPerc);
        InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_PlayerCreated);
    }
}
