package edwardwang.bouncingball.Games;

/**
 * Created by edwardwang on 8/16/16.
 */
public interface GameInterface {
    void setupGame();
    void grabGameViewElements();
    void setupPhysicsEngine();
    void setupBackground();
    void setupPlayer();
    void setupInteractionManager();
    void setupOrientationManager();
    void orientationPause();
    void orientationResume();
    void updateCurrentScore();
    void updateGame();
}
