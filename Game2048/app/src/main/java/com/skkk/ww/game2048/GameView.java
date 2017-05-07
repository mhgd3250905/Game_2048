package com.skkk.ww.game2048;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.x;

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
        addCard(cardWidth, cardWidth);
        startGame();
    }

    private void startGame() {
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

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cardsMap[i][j].getNum() <= 0) {
                    emptyPoints.add(new Point(i, j));
                }
            }
        }

        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    private void addCard(int cardWidth, int cardHeight) {
        CardView c;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                c = new CardView(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);

                cardsMap[i][j] = c;
            }
        }
    }

    /**
     * 左滑触发方法
     */
    private void swipeLeft() {
        for (int y = 0; i < 4; i++) {
            for (int j = 0; x < 4; j++) {
                for (int x1 = x+1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum()>0){
                        if (cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(2*cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 右滑触发方法
     */
    private void swipeRight() {

    }

    /**
     * 上滑触发方法
     */
    private void swipeUp() {

    }

    /**
     * 下滑触发方法
     */
    private void swipeDown() {

    }
}
