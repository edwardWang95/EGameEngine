package edwardwang.bouncingball.Info;

/**
 * Setup a singleton for accessible and static information regarding phone information, i.e.
 * screen dimension.
 * Created by edwardwang on 7/23/16.
 */
public class PhoneInfo {
    private static final String className = PhoneInfo.class.getSimpleName();
    private static PhoneInfo instance = null;
    public static PhoneInfo getInstance(){
        if(instance == null){
            instance = new PhoneInfo();
        }
        return instance;
    }
    ////////////////////////////////////////////////////////////////////////////////////

    private int screenWidth, screenHeight;
    private int widthBuffer = 20;
    private int heightBuffer = 150 ;

    ////////////////////////////////////////////////////////////////////////////////////
    //Getter
    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    ////////////////////////////////////////////////////////////////////////////////////
    //Setter

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_ScreenWidth + screenWidth);
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight - heightBuffer;
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_ScreenHeight + screenHeight);
    }

    public int getScreenLeft(){
        return 0;
    }

    public int getScreenRight(){
        return (screenWidth - widthBuffer);
    }

    public int getScreenTop(){
        return 0;
    }

    public int getScreenBottom(){
        return (screenHeight - heightBuffer);
    }
}
