package edwardwang.bouncingball.Interaction.EButton;

import android.content.Context;

import java.util.ArrayList;

import edwardwang.bouncingball.Interaction.InteractionSetup;

/**
 * Created by edwardwang on 8/8/16.
 */
public class EButtonManager implements InteractionSetup{
    private static final String className = EButtonManager.class.getSimpleName();

    private Context context;

    private ArrayList<EButton> eButtonList;

    public EButtonManager(Context context){
        this.context = context;
    }

    public void createNewEButton(int x, int y, int width, int height){

    }

    @Override
    public void setupInteraction() {

    }
}
