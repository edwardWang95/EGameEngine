package edwardwang.bouncingball.Interaction;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import edwardwang.bouncingball.Interaction.EButton.EButtonManager;
import edwardwang.bouncingball.Interaction.EGesture.EGestureManager;
import edwardwang.bouncingball.Interaction.EScreenBlock.EScreenBlockManager;
import edwardwang.bouncingball.Interaction.ESplitScreen.ESplitScreenManager;
import edwardwang.bouncingball.Interaction.ETap.ETapManager;

/**
 * Manages all types of ways user can control/interact with sprites.
 * Created by edwardwang on 8/7/16.
 */
public class InteractionManager {
    private static final String className = InteractionManager.class.getSimpleName();

    private Context context;
    private View view;

    //Interaction methods
    private ArrayList<Interaction> interactionList = new ArrayList<>();
    private Interaction interaction;

    private EButtonManager buttonManager;
    private EGestureManager gestureManager;
    private EScreenBlockManager screenBlockManager;
    private ETapManager tapManager;
    private ESplitScreenManager splitScreenManager;

    public void setupInteractionManager(Context context, View view){
        this.context = context;
        this.view = view;
    }

    public void addInteraction(Interaction interaction){
        interactionList.add(interaction);
    }

    public void initInteractions(){
        for(Interaction interaction: interactionList){
            this.interaction = interaction;
            switch (interaction){
                case Gesture:
                    gestureManager = new EGestureManager(context);
                    break;
                case ScreenBlocks:
                    screenBlockManager = new EScreenBlockManager(context, view);
                    break;
                case TapScreen:
                    tapManager = new ETapManager(context, view);
                    break;
                case Buttons:
                    buttonManager = new EButtonManager(context);
                    break;
                case SplitScreen:
                    splitScreenManager = new ESplitScreenManager(context, view);
                    break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //Getter
    public ArrayList<Interaction> getInteractionList() {
        return interactionList;
    }

    public ETapManager getTapManager() {
        tapManager.setupInteraction();
        return tapManager;
    }

    public EScreenBlockManager getScreenBlockManager() {
        screenBlockManager.setupInteraction();
        return screenBlockManager;
    }

    public EGestureManager getGestureManager() {
        gestureManager.setupInteraction();
        return gestureManager;
    }

    public EButtonManager getButtonManager() {
        buttonManager.setupInteraction();
        return buttonManager;
    }

    public ESplitScreenManager getSplitScreenManager() {
        splitScreenManager.setupInteraction();
        return splitScreenManager;
    }
}
