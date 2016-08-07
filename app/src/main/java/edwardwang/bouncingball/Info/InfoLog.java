package edwardwang.bouncingball.Info;

import android.util.Log;

/**
 * Created by edwardwang on 7/23/16.
 */
public class InfoLog {
    private static InfoLog instance;
    public static InfoLog getInstance(){
        if(instance == null){
            instance = new InfoLog();
        }
        return instance;
    }
    ////////////////////////////////////////////////////////////////////////////////
    //StartScreenActivity
        //Error
    public final String error_GameNotSet = "Game not set.";

    ////////////////////////////////////////////////////////////////////////////////
    //GameScreenActivity
    public final String debug_StartGameButton = "Start game button pressed.";
    public final String debug_GameScreenPause = "GameScreenActivity paused.";
    public final String debug_GameResume = "GameScreenActivity resumed.";

    ////////////////////////////////////////////////////////////////////////////////
    //PhoneInfo
    public final String debug_ScreenWidth = "Screen Width: ";
    public final String debug_ScreenHeight = "Screen Height: ";

    ////////////////////////////////////////////////////////////////////////////////
    //GameView
    public final String debug_InitGameView = "Init GameView.";
    public final String debug_PauseGameThread = "Pause game thread.";
    public final String debug_ResumeGameThread = "Resume game thread.";
    public final String debug_MotionEventActionDown = "Motion event action down";
    public final String debug_MotionEventActionUp = "Motion event action up";
    public final String debug_UpdateSprites = "Update sprites.";
        //Error
    public final String error_PauseGameThread = "InterruptedException when pausing thread.";
    public final String error_SpriteQueueEmpty = "Sprite queue is empty.";

    ////////////////////////////////////////////////////////////////////////////////
    //PlayerSprite1
    public final String debug_PlayerCreated = "Playercreated";

    ////////////////////////////////////////////////////////////////////////////////
    //SkyClimberGame
    public final String debug_SkyClimber = "SkyClimber is chosen.";
    public final String debug_SkyClimberSetup = "SkyClimber game setup.";
    public final String debug_SkyClimberCreated = "SkyClimber created";

    ////////////////////////////////////////////////////////////////////////////////
    //Physics Engine
    public final String debug_ActionJump = "Action = Jump";

    ////////////////////////////////////////////////////////////////////////////////
    //Interaction Manager


    ////////////////////////////////////////////////////////////////////////////////
    //ESensorManager
    public final String debug_StartESensorManager = "Start ESensorManager";
    public final String debug_StopESensorManager = "Pause ESensorManager";
        //Error
    public final String error_SensorNotFound = "Sensor not found: ";

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    public void generateLog(String className,String statement){
        Log.d(className,statement);
    }

    // one can use String.valueOf(value)
    public void debugValue(String valueName, String value){
        Log.d(valueName, value);
    }

}
