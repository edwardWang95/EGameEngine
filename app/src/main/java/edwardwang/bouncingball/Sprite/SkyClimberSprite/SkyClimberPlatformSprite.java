package edwardwang.bouncingball.Sprite.SkyClimberSprite;

import android.content.Context;
import android.graphics.BitmapFactory;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;
import edwardwang.bouncingball.R;
import edwardwang.bouncingball.Sprite.Sprite;
import edwardwang.bouncingball.Sprite.SpriteType;

/**
 * Created by edwardwang on 7/25/16.
 */
public class SkyClimberPlatformSprite extends Sprite {
    private static final String className = SkyClimberPlatformSprite.class.getSimpleName();

    public SkyClimberPlatformSprite(Context context, Vector3DInt canvasPosition, Vector3DInt eMapPosition,
                         int frameWidth, int frameHeight, double hitBoxWidthPerc, double hitBoxHeightPerc) {
        setupSpriteSettings(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.skyclimber_platform), canvasPosition, eMapPosition, frameWidth, frameHeight,
                false, SpriteType.PLATFORM_SKYCLIMBER, hitBoxWidthPerc, hitBoxHeightPerc);
        InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_SkyClimberCreated);
    }
}
