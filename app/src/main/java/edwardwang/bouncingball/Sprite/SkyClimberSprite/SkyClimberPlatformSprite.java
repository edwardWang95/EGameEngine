package edwardwang.bouncingball.Sprite.SkyClimberSprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DInt;
import edwardwang.bouncingball.R;
import edwardwang.bouncingball.Sprite.Sprite;

/**
 * Created by edwardwang on 7/25/16.
 */
public class SkyClimberPlatformSprite extends Sprite {
    private static final String className = SkyClimberPlatformSprite.class.getSimpleName();
    private Bitmap image;

    private int numOfCorners = 4;
    private int numOfEdges = 4;


    public SkyClimberPlatformSprite(Context context, Vector3DInt canvasPosition, Vector3DInt eMapPosition,
                         int frameWidth, int frameHeight, double hitBoxWidthPerc, double hitBoxHeightPerc) {
        image = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.skyclimber_platform);
        setImage(image);
        setupDimensions(frameWidth, frameHeight, hitBoxWidthPerc, hitBoxHeightPerc, numOfCorners, numOfEdges);
        setupLocation(canvasPosition, eMapPosition);
        InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_SkyClimberCreated);
    }
}
