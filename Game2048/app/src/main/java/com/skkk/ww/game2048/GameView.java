package com.skkk.ww.game2048;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/7.
 */
/*
* 
* 描    述：游戏View
* 作    者：ksheng
* 时    间：2017/5/7$ 23:19$.
*/
public class GameView extends GridLayout {
    float startX, startY, offsetX, offsetY;
    private CardView[][] cardsMap = new CardView[4][4];
    private List<Point> emptyPoints = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initGameView();
    }

    /**
     * 初始化GameView
     */
    private void initGameView() {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {   //水平方向滑动
                            if (offsetX < -5) {
                                Log.d("GameView", "Left");
                                swipeLeft();
                            } else if (offsetX > 5) {
                                Log.d("GameView", "Right");
                                swipeRight();
                            }
                        } else { //垂直滑动
                            if (offsetY < -5) {
                                Log.d("GameView", "Up");
                                swipeUp();
                            } else if (offsetY > 5) {
                                Log.d("GameView", "Down");
                                swipeDown();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //动态计算布局宽高
        int cardWidth = (Math.min(w, h) - 10) / 4;
        if (getChildCount()==0) {
            addCard(cardWidth, cardWidth);
        }
        startGame();
    }

    private void startGame() {

        MainActivity.getMainActivity().clearScore();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardsMap[i][j].setNum(0);
            }
        }

        addRadomNum();
        addRadomNum();
    }

    private void addRadomNum() {

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    private void addCard(int cardWidth, int cardHeight) {
        CardView c;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new CardView(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    /**
     * 左滑触发方法
     */
    private void swipeLeft() {
        boolean merge=false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {

                    if (cardsMap[x1][y].getNum() > 0) {    //如果个格子不是空的
                        if (cardsMap[x][y].getNum() <= 0) {    //这个格子的行首是空的
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());    //直接把这个格子平移过去
                            cardsMap[x1][y].setNum(0);
                            x--;    //在这个格子平移之后

                            merge=true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {  //如果这个格子和行首一样
                            cardsMap[x][y].setNum(2 * cardsMap[x1][y].getNum());  //行首翻倍
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRadomNum();
            checkComplete();
        }
    }

    /**
     * 右滑触发方法
     */
    private void swipeRight() {
        boolean merge=false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {    //如果个格子不是空的
                        if (cardsMap[x][y].getNum() <= 0) {    //这个格子的行首是空的
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());    //直接把这个格子平移过去
                            cardsMap[x1][y].setNum(0);
                            x++;    //在这个格子平移之后

                            merge=true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {  //如果这个格子和行首一样
                            cardsMap[x][y].setNum(2 * cardsMap[x1][y].getNum());  //行首翻倍
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRadomNum();
            checkComplete();
        }
    }

    /**
     * 上滑触发方法
     */
    private void swipeUp() {
        boolean merge=false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {    //如果个格子不是空的
                        if (cardsMap[x][y].getNum() <= 0) {    //这个格子的行首是空的
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());    //直接把这个格子平移过去
                            cardsMap[x][y1].setNum(0);
                            y--;    //在这个格子平移之后

                            merge=true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {  //如果这个格子和行首一样
                            cardsMap[x][y].setNum(2 * cardsMap[x][y1].getNum());  //行首翻倍
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRadomNum();
            checkComplete();
        }
    }

    /**
     * 下滑触发方法
     */
    private void swipeDown() {
        boolean merge=false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {     //如果个格子不是空的
                        Log.d("GameView", x + "," + y1 + "不是空的");
                        if (cardsMap[x][y].getNum() <= 0) {    //这个格子的行首是空的
                            Log.d("GameView", x + "," + y + "是空的");
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());    //直接把这个格子平移过去
                            cardsMap[x][y1].setNum(0);
                            y++;    //在这个格子平移之后

                            merge=true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {  //如果这个格子和行首一样
                            cardsMap[x][y].setNum(2 * cardsMap[x][y1].getNum());  //行首翻倍
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRadomNum();
            checkComplete();
        }
    }

    private void checkComplete(){

        boolean complete=true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum()==0||
                        (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
                        (x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
                        (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
                        (y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))){
                    complete=false;
                    break ALL;
                }
            }
        }
        if (complete){
            new AlertDialog.Builder(getContext())
                    .setTitle("你好")
                    .setMessage("游戏结束！")
                    .setPositiveButton("重来！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    })
                    .show();
        }
    }
}
