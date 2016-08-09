package edwardwang.bouncingball.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import edwardwang.bouncingball.Games.Game;
import edwardwang.bouncingball.Games.SkyClimberGame;
import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.R;
import edwardwang.bouncingball.View.GameView;

/**
 * Created by edwardwang on 7/23/16.
 */
public class GameScreenActivity extends AppCompatActivity{
    private static final String className = GameScreenActivity.class.getSimpleName();
    private GameView gameView;
    private SurfaceView surfaceView;
    private Game game;
    private Intent gameOverIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        surfaceView = (SurfaceView)findViewById(R.id.gameScreen);
        gameView = new GameView(this.getApplicationContext());
        setupGameOverIntent();
        grabIntentExtra(gameView);
        gameView.setupGameView(surfaceView, game.getNumOfEPixelsWidth(), game.getNumOfEPixelsHeight(),
                game.getHitBoxWidthPerc(), game.getHitBoxHeightPerc(), game.getSpriteType());
        game.setupGame();
    }

    private void setupGameOverIntent(){
        gameOverIntent = new Intent(this, GameOverActivity.class);
        gameOverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void grabIntentExtra(GameView gameView){
        Intent intent = getIntent();
        String gameName = intent.getStringExtra(Game.intentPassString);
        if(gameName.equals(SkyClimberGame.gameName)){
            game = new SkyClimberGame(getApplicationContext(), gameView);
            gameOverIntent.putExtra(String.valueOf(Game.intentPassString), SkyClimberGame.gameName);
        }else{
            //no other game added yet
        }
        game.setGameOverIntent(gameOverIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_GameResume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
        InfoLog.getInstance().generateLog(className, InfoLog.getInstance().debug_GameScreenPause);
    }
    //////////////////////////////////////////////////////////////////////

}
