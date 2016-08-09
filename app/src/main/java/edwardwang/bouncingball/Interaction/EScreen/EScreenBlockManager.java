package edwardwang.bouncingball.Interaction.EScreen;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import edwardwang.bouncingball.Interaction.InteractionSetup;
import edwardwang.bouncingball.PhysicsEngine.Vector3D.Vector3DInt;

/**
 * Set the number of screen blocks before calling setup. By default
 * there will always be one screen block.
 * Created by edwardwang on 8/8/16.
 */
public class EScreenBlockManager implements InteractionSetup {

    private Context context;
    private View view;

    private ArrayList<EScreenBlock> screenBlockList;
    private int numOfScreenBlocks = 1;

    //ScreenBlock settings
    private int screenWidth;
    private int screenHeight;
    private Vector3DInt screenBlockSize;
    private Vector3DInt screenBlockPosition;

    public EScreenBlockManager(Context context, View view){
        this.context = context;
        this.view = view;
    }

    public void setNumOfScreenBlocks(int numOfScreenBlocks){
        this.numOfScreenBlocks = numOfScreenBlocks;
    }

    @Override
    public void setup() {

    }
    //////////////////////////////////////////////////////////////////
    private void calcScreenBlockWidthAndHeight(){

    }

    //////////////////////////////////////////////////////////////////
    //Getter

    public ArrayList<EScreenBlock> getScreenBlockList() {
        return screenBlockList;
    }
}
