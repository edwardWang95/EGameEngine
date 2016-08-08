package edwardwang.bouncingball.InteractionLayer.ETap;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import edwardwang.bouncingball.InteractionLayer.InteractionSetup;

/**
 * Created by edwardwang on 8/8/16.
 */
public class ETapManager implements InteractionSetup{

    private Context context;
    private View view;

    public ETapManager(Context context, View view){
        this.context = context;
        this.view = view;
        setup();
    }

    @Override
    public void setup(){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
}
