package edwardwang.bouncingball.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import edwardwang.bouncingball.Games.Game;
import edwardwang.bouncingball.Games.SkyClimberGame;
import edwardwang.bouncingball.Info.PhoneInfo;
import edwardwang.bouncingball.R;

public class MainActivity extends Activity {

    private DisplayMetrics displayMetrics;
    private Intent intent;
    //Games
    private Button skyClimberButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPhoneInfoMetrics();
        initButtons();
    }

    private void setPhoneInfoMetrics(){
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        PhoneInfo.getInstance().setScreenWidth(displayMetrics.widthPixels);
        PhoneInfo.getInstance().setScreenHeight(displayMetrics.heightPixels);
    }

    private void initButtons(){
        skyClimberButton = (Button)findViewById(R.id.skyClimberButton);
        skyClimberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, StartScreenActivity.class);
                intent.putExtra(String.valueOf(Game.gameIntentPassString), SkyClimberGame.gameName);
                startActivity(intent);
            }
        });
    }

}
