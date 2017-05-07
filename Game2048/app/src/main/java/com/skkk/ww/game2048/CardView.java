package com.skkk.ww.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by admin on 2017/5/7.
 */
/*
* 
* 描    述：卡片类
* 作    者：ksheng
* 时    间：2017/5/7$ 23:37$.
*/
public class CardView extends FrameLayout {
    private int num;
    private TextView tvLabel;

    public CardView(Context context) {
        super(context);

        tvLabel=new TextView(getContext());
        tvLabel.setTextSize(32);
        tvLabel.setGravity(Gravity.CENTER);
        tvLabel.setBackgroundColor(0x33ffffff);

        LayoutParams lp=new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);
        addView(tvLabel, lp);

        setNum(0);
    }

    public void setNum(int num) {
        this.num = num;
        if (num<=0){
            tvLabel.setText("");
        }else {
            tvLabel.setText(String.valueOf(num));
        }
    }

    public int getNum() {
        return num;
    }

    public boolean equals(CardView o) {
        return getNum()==o.getNum();
    }
}
