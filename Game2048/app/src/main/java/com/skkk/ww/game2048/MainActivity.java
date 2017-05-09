package com.skkk.ww.game2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.tv_score)
    TextView mTvScore;
//    @BindView(R.id.gl_game_container)
//    GameView mGlGameContainer;

    int score=0;    //分数

    private static MainActivity mainActivity=null;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public MainActivity() {
        mainActivity=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvScore= (TextView) findViewById(R.id.tv_score);

    }

    public void clearScore(){
        score=0;
        showScore();
    }

    public void showScore(){
        mTvScore.setText(String.valueOf(score));
    }

    public void addScore(int s){
        score+=s;
        showScore();
    }
}
