package edwardwang.bouncingball.InteractionLayer.EScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Create a set place on the screen users can tap when interacting with application.
 * Created by edwardwang on 8/8/16.
 */
public class EScreenBlock extends View{

    private Vector3DInt position;
    private Vector3DInt size;
    private View view;


    public EScreenBlock(Context context) {
        super(context);
    }

    public EScreenBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EScreenBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EScreenBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    //Getter
    public Vector3DInt getPosition() {
        return position;
    }

    public Vector3DInt getSize() {
        return size;
    }

    public View getView() {
        return view;
    }
    ///////////////////////////////////////////////////////////////////////////
    //Setter

    public void setPosition(Vector3DInt position) {
        this.position = position;
    }

    public void setSize(Vector3DInt size) {
        this.size = size;
    }

    public void setView(View view) {
        this.view = view;
    }
}
