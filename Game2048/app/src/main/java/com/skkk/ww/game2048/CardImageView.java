package com.skkk.ww.game2048;

import android.content.Context;
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
    private ImageView ivCard;

    public CardImageView(Context context) {
        super(context);

        ivCard=new ImageView(getContext());
        ivCard.setScaleType(ImageView.ScaleType.FIT_XY);
        ivCard.setBackgroundColor(0x33ffffff);

        LayoutParams lp=new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);
        addView(ivCard, lp);

        setNum(0);
    }

    public void setNum(int num) {
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
