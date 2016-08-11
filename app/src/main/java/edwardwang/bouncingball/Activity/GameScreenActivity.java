package edwardwang.bouncingball.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.logging.LogRecord;

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
    private TextView currentScoreTextView;

    private Game game;
    private Intent gameOverIntent;

    //Score keeping
    String currentScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        surfaceView = (SurfaceView)findViewById(R.id.gameScreen);
        currentScoreTextView = (TextView)findViewById(R.id.gameViewCurrentScoreTextView);
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
        String gameName = intent.getStringExtra(Game.gameIntentPassString);
        if(gameName.equals(SkyClimberGame.gameName)){
            game = new SkyClimberGame(getApplicationContext(), gameView, this);
            gameOverIntent.putExtra(GameOverActivity.gameOverNameIntentPassString, SkyClimberGame.gameName);
        }else{
            //no other game added yet
        }
        game.setGameOverIntent(gameOverIntent);
    }

    public void updateCurrentScoreTextView(double gameScore){
        currentScore = "Current Score: " + gameScore;
        currentScoreTextView.setText(currentScore);
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
