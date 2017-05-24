package com.skkk.ww.game2048;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by admin on 2017/5/14.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/14$ 13:30$.
*/
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
