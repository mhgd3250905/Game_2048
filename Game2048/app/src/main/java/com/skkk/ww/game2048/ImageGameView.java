package com.skkk.ww.game2048;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/*
*
* 描    述：游戏View
* 作    者：ksheng
* 时    间：2017/5/7$ 23:19$.
*/
public class ImageGameView extends FrameLayout {
    float startX, startY, offsetX, offsetY;
    private CardImageView[][] cardsMap = new CardImageView[4][4];
    private int[][] nums = new int[4][4];
    private List<Point> emptyPoints = new ArrayList<>();
    private GridLayout gameContainer, gameContainerBG;
    private boolean merge;
    private int cardWidth;

    public ImageGameView(Context context) {
        super(context);
        initGameView();
    }

    public ImageGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public ImageGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initGameView();
    }

    /**
     * 初始化GameView
     */
    private void initGameView() {
        gameContainer = new GridLayout(getContext());
        gameContainerBG = new GridLayout(getContext());

        gameContainer.setColumnCount(4);
        gameContainerBG.setColumnCount(4);

        gameContainer.setBackgroundColor(Color.TRANSPARENT);
        gameContainerBG.setBackgroundColor(Color.GRAY);

        addView(gameContainerBG);
        addView(gameContainer);

        setOnTouchListener(new OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //动态计算布局宽高
        cardWidth = (Math.min(w, h) - 10) / 4;
        if (gameContainer.getChildCount() == 0) {
            addCard(cardWidth, cardWidth);
        }
        startGame();
    }


    private void addRadomNum() {

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (nums[x][y] <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        int randomNum = Math.random() > 0.1 ? 2 : 4;
        cardsMap[p.x][p.y].setCardNum(randomNum);
        nums[p.x][p.y] = randomNum;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addCard(int cardWidth, int cardHeight) {
        CardImageView c;
        CardImageView c2;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new CardImageView(getContext());
                c.setCardNum(0);
                c.setTranslationZ(90f);
                gameContainer.addView(c, cardWidth, cardHeight);

                c2 = new CardImageView(getContext());
                c2.setCardNum(0);
                gameContainerBG.addView(c2, cardWidth, cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    private void startGame() {

        MainActivity.getMainActivity().clearScore();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardsMap[i][j].setCardNum(0);
                nums[i][j] = 0;
            }
        }

        addRadomNum();
        addRadomNum();


    }

    /**
     * 从数组同步到卡片组
     */
    private void syncCardsFromNums() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                cardsMap[x][y].setCardNum(nums[x][y]);
            }
        }

        for (int y = 0; y < 4; y++) {
            Log.w("SKKK",nums[0][y]+" "+nums[1][y]+" "+nums[2][y]+" "+nums[3][y]);
            Log.w("SKKK","\n");
        }
    }


    /**
     * 左滑触发方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void swipeLeft() {

        merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {

                    if (nums[x1][y] > 0) {    //如果个格子不是空的
                        if (nums[x][y] <= 0) {    //这个格子的行首是空的
                            nums[x][y] = nums[x1][y];    //直接把这个格子平移过去
                            nums[x1][y] = 0;

                            final float transX = cardsMap[x][y].getLeft() - cardsMap[x1][y].getLeft();
                            final CardImageView currentCard = cardsMap[x1][y];

                            currentCard.setTranslationZ(100f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationX", 0, transX);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (!merge) {
                                        merge = true;
                                        addRadomNum();
                                        checkComplete();
                                    }
                                    currentCard.setTranslationZ(90f);
                                    syncCardsFromNums();
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationX", transX, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();


                            x--;    //在这个格子平移之后
                        } else if (nums[x][y] == nums[x1][y]) {  //如果这个格子和行首一样
                            nums[x][y] = 2 * nums[x1][y];  //行首翻倍
                            nums[x1][y] = 0;

                            final float transX = cardsMap[x][y].getLeft() - cardsMap[x1][y].getLeft();
                            final CardImageView currentCard = cardsMap[x1][y];

                            currentCard.setTranslationZ(100f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationX", 0, transX);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    syncCardsFromNums();
                                    currentCard.setTranslationZ(90f);
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationX", transX, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();
                            MainActivity.getMainActivity().addScore(nums[x][y]);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 右滑触发方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void swipeRight() {
        merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (nums[x1][y] > 0) {    //如果个格子不是空的
                        if (nums[x][y] <= 0) {    //这个格子的行首是空的
                            nums[x][y] = nums[x1][y];    //直接把这个格子平移过去
                            nums[x1][y] = 0;

                            final float transX = cardsMap[x][y].getLeft() - cardsMap[x1][y].getLeft();
                            final CardImageView currentCard = cardsMap[x1][y];

                            currentCard.setTranslationZ(100f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationX", 0, transX);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (!merge) {
                                        merge = true;
                                        addRadomNum();
                                        checkComplete();
                                    }
                                    currentCard.setTranslationZ(90f);
                                    syncCardsFromNums();
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationX", transX, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();

                            x++;    //在这个格子平移之后

                        } else if (nums[x][y]==nums[x1][y]) {  //如果这个格子和行首一样
                            nums[x][y]=2 * nums[x1][y];  //行首翻倍
                            nums[x1][y]=0;

                            final float transX = cardsMap[x][y].getLeft() - cardsMap[x1][y].getLeft();
                            final CardImageView currentCard = cardsMap[x1][y];

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationX", 0, transX);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    syncCardsFromNums();
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationX", transX, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();
                            MainActivity.getMainActivity().addScore(nums[x][y]);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 上滑触发方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void swipeUp() {
        merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (nums[x][y1] > 0) {    //如果个格子不是空的
                        if (nums[x][y]<= 0) {    //这个格子的行首是空的
                            nums[x][y]=nums[x][y1];    //直接把这个格子平移过去
                            nums[x][y1]=0;

                            final float transY = cardsMap[x][y].getTop() - cardsMap[x][y1].getTop();
                            final CardImageView currentCard = cardsMap[x][y1];

                            currentCard.setTranslationZ(100f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationY", 0, transY);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (!merge) {
                                        merge = true;
                                        addRadomNum();
                                        checkComplete();
                                    }
                                    currentCard.setTranslationZ(90f);
                                    syncCardsFromNums();
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationY", transY, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();

                            y--;    //在这个格子平移之后

                        } else if (nums[x][y]==nums[x][y1]) {  //如果这个格子和行首一样
                            nums[x][y]=2 * nums[x][y1];  //行首翻倍
                            nums[x][y1]=0;

                            final float transY = cardsMap[x][y].getTop() - cardsMap[x][y1].getTop();
                            final CardImageView currentCard = cardsMap[x][y1];

                            currentCard.setTranslationZ(90f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationY", 0, transY);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    syncCardsFromNums();
                                    currentCard.setTranslationZ(90f);
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationY", transY, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();
                            MainActivity.getMainActivity().addScore(nums[x][y]);
                        }
                        break;
                    }
                }
            }
        }

    }

    /**
     * 下滑触发方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void swipeDown() {
        merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (nums[x][y1] > 0) {     //如果个格子不是空的
                        if (nums[x][y] <= 0) {    //这个格子的行首是空的
                            nums[x][y]=nums[x][y1];    //直接把这个格子平移过去
                            nums[x][y1]=0;

                            final float transY = cardsMap[x][y].getTop() - cardsMap[x][y1].getTop();
                            final CardImageView currentCard = cardsMap[x][y1];

                            currentCard.setTranslationZ(100f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationY", 0, transY);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (!merge) {
                                        merge = true;
                                        addRadomNum();
                                        Log.d("ImageGameView", "增加随机数");
                                        checkComplete();
                                    }
                                    currentCard.setTranslationZ(90f);
                                    syncCardsFromNums();
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationY", transY, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();

                            y++;    //在这个格子平移之后

                        } else if (nums[x][y]==nums[x][y1]) {  //如果这个格子和行首一样
                            nums[x][y]=2 * nums[x][y1];  //行首翻倍
                            nums[x][y1]=0;

                            final float transY = cardsMap[x][y].getTop() - cardsMap[x][y1].getTop();
                            final CardImageView currentCard = cardsMap[x][y1];

                            currentCard.setTranslationZ(100f);

                            ObjectAnimator animTransGo = ObjectAnimator.ofFloat(currentCard, "translationY", 0, transY);
                            animTransGo.setDuration(200);
                            animTransGo.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    syncCardsFromNums();
                                    currentCard.setTranslationZ(90f);
                                    currentCard.setVisibility(INVISIBLE);
                                    ObjectAnimator animTranBack = ObjectAnimator.ofFloat(currentCard, "translationY", transY, 0);
                                    animTranBack.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            currentCard.setVisibility(VISIBLE);
                                        }
                                    });
                                    animTranBack.start();
                                }
                            });
                            animTransGo.start();
                            MainActivity.getMainActivity().addScore(nums[x][y]);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
                    complete = false;
                    break ALL;
                }
            }
        }
        if (complete) {
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
