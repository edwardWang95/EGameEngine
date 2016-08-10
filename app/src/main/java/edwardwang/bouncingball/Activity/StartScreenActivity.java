package edwardwang.bouncingball.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edwardwang.bouncingball.Games.Game;
import edwardwang.bouncingball.Games.SkyClimberGame;
import edwardwang.bouncingball.Info.InfoLog;
import edwardwang.bouncingball.R;

/**
 * http://gamecodeschool.com/android/coding-android-sprite-sheet-animations/
 * Read this website for info on how this project is done
 */
public class StartScreenActivity extends AppCompatActivity {
    private static final String className = StartScreenActivity.class.getSimpleName();
    private RelativeLayout relativeLayout;
    private Button startGameButton;
    private TextView startScreenGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        relativeLayout = (RelativeLayout)findViewById(R.id.startScreenLayout);
        startGameButton = (Button)findViewById(R.id.startGameButton);
        startScreenGameName = (TextView)findViewById(R.id.startScreenGameName);
        //grab game from last activity and update layout to reflect game
        grabGameIntentAndUpdateLayout();
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: update & reformat this code
                Intent intent = new Intent(StartScreenActivity.this, GameScreenActivity.class);
                intent.putExtra(String.valueOf(Game.gameIntentPassString), SkyClimberGame.gameName);
                startActivity(intent);
                InfoLog.getInstance().generateLog(className,
                        InfoLog.getInstance().debug_StartGameButton);
            }
        });
    }

    /**
     * Grab intent extras and set the current game.
     * TODO:Have games have bitmaps of their backgrounds
     */
    private void grabGameIntentAndUpdateLayout(){
        Intent intent = getIntent();
        String gameName = intent.getStringExtra(Game.gameIntentPassString);
        if(gameName.equals(SkyClimberGame.gameName)){
            updateBackgroundAndName(SkyClimberGame.gameName, R.color.SkyClimberGameBackground);
            InfoLog.getInstance().generateLog(className,InfoLog.getInstance().debug_SkyClimber);
        }else{
            InfoLog.getInstance().generateLog(className,InfoLog.getInstance().error_GameNotSet);
        }
    }

    private void updateBackgroundAndName(String gameName, int color){
        startScreenGameName.setText(gameName);
        relativeLayout.setBackgroundColor(color);
    }

    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
