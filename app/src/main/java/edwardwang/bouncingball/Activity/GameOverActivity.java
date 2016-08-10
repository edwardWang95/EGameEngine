package edwardwang.bouncingball.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edwardwang.bouncingball.Games.Game;
import edwardwang.bouncingball.Games.SkyClimberGame;
import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.R;

public class GameOverActivity extends AppCompatActivity {
    private static final String className = GameOverActivity.class.getSimpleName();
    public static final String gameOverNameIntentPassString = "Game over name";
    public static final String gameOverScoreIntentPassString = "Game over score";

    private RelativeLayout relativeLayout;
    private Button playAgainButton;
    private Button backToGameListButton;
    private TextView gameOverScreenGameName;
    private TextView gameOverScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        relativeLayout = (RelativeLayout)findViewById(R.id.gameOverScreenLayout);
        playAgainButton = (Button)findViewById(R.id.playAgainButton);
        backToGameListButton = (Button)findViewById(R.id.backToGameListButton);
        gameOverScreenGameName = (TextView)findViewById(R.id.gameOverScreenGameName);
        gameOverScore = (TextView)findViewById(R.id.gameOverScoreTextView);

        //grab game from last activity and update layout to reflect game
        grabGameIntentAndUpdateLayout();
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: update & reformat this code
                Intent intent = new Intent(GameOverActivity.this, GameScreenActivity.class);
                intent.putExtra(String.valueOf(Game.gameIntentPassString), SkyClimberGame.gameName);
                startActivity(intent);
                InfoLog.getInstance().generateLog(className,
                        InfoLog.getInstance().debug_PlayAgainButton);
            }
        });
        backToGameListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
                InfoLog.getInstance().generateLog(className,
                        InfoLog.getInstance().debug_BackToGameListButton);
            }
        });
    }

    /**
     * Grab intent extras and set the current game.
     * TODO:Have games have bitmaps of their backgrounds
     */
    private void grabGameIntentAndUpdateLayout(){
        Intent intent = getIntent();
        String gameName = intent.getStringExtra(gameOverNameIntentPassString);
        String currentScore = intent.getStringExtra(gameOverScoreIntentPassString);
        if(gameName.equals(SkyClimberGame.gameName)){
            updateBackgroundAndName(SkyClimberGame.gameName, R.color.SkyClimberGameBackground);
            InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_SkyClimber);
        }else{
            InfoLog.getInstance().generateLog(className,InfoLog.getInstance().error_GameNotSet);
        }
        gameOverScore.setText(currentScore);
    }

    private void updateBackgroundAndName(String gameName, int color){
        gameOverScreenGameName.setText(gameName);
        relativeLayout.setBackgroundColor(color);
    }

}
