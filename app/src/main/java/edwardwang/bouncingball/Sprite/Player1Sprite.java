package edwardwang.bouncingball.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DInt;
import edwardwang.bouncingball.R;

/**
 * Created by edwardwang on 7/23/16.
 */
public class Player1Sprite extends Sprite{
    private static final String className = Player1Sprite.class.getSimpleName();
    private Bitmap image;

    //dimensions
    private int numOfCorners = 4;
    private int numOfEdges = 4;

    public Player1Sprite(Context context, Vector3DInt canvasPosition, Vector3DInt eMapPosition,
                         int frameWidth, int frameHeight,  double hitBoxWidthPerc, double hitBoxHeightPerc){
        image = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player);
        setImage(image);
        setupDimensions(frameWidth, frameHeight, hitBoxWidthPerc, hitBoxHeightPerc, numOfCorners, numOfEdges);
        setupLocation(canvasPosition, eMapPosition);
        InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_PlayerCreated);
    }
}
