package com.skkk.ww.game2048;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by admin on 2017/5/7.
 */
/*
* 
* 描    述：卡片类
* 作    者：ksheng
* 时    间：2017/5/7$ 23:37$.
*/
public class CardImageView extends FrameLayout {
    private int num;
    private ImageView ivCard,ivBG;
    private final LayoutParams lp;

    public CardImageView(Context context) {
        super(context);


        lp = new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);



        ivCard=new ImageView(getContext());
        ivCard.setScaleType(ImageView.ScaleType.FIT_XY);
        ivCard.setBackgroundColor(Color.LTGRAY);

        addView(ivCard, lp);

        setNum(ivCard,0);
    }

    public void showBg(){
        ivBG=new ImageView(getContext());
        ivBG.setScaleType(ImageView.ScaleType.FIT_XY);
        ivBG.setBackgroundColor(Color.LTGRAY);
        setNum(ivBG,getNum());
        addView(ivBG,lp);
    }

    public void hideBg(){
        ivBG.setVisibility(INVISIBLE);
        removeView(ivBG);
    }

    public void setCardNum(int num){
        setNum(ivCard,num);
    }

    public void setNum(ImageView ivCard, int num) {
        this.num = num;
        switch (num){
            case 2:
                ivCard.setImageResource(R.drawable.c2);
                break;
            case 4:
                ivCard.setImageResource(R.drawable.c4);
                break;
            case 8:
                ivCard.setImageResource(R.drawable.c8);
                break;
            case 16:
                ivCard.setImageResource(R.drawable.c16);
                break;
            case 32:
                ivCard.setImageResource(R.drawable.c32);
                break;
            case 64:
                ivCard.setImageResource(R.drawable.c64);
                break;
            case 128:
                ivCard.setImageResource(R.drawable.c128);
                break;
            case 256:
                ivCard.setImageResource(R.drawable.c256);
                break;
            case 512:
                ivCard.setImageResource(R.drawable.c512);
                break;
            case 1024:
                ivCard.setImageResource(R.drawable.c1024);
                break;
            case 2048:
                ivCard.setImageResource(R.drawable.c2048);
                break;
            default:
                ivCard.setImageBitmap(null);
                break;
        }
    }



    public int getNum() {
        return num;
    }

    public boolean equals(CardImageView o) {
        return getNum()==o.getNum();
    }
}
