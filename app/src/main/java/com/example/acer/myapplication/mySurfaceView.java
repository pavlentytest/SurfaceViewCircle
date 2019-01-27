package com.example.acer.myapplication;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by admin on 27.01.2018.
 */

public class mySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private MyThread mMyThread; // наш поток прорисовки

    public mySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    // вызывается когда surfaceview появляется на экране
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mMyThread = new MyThread(getHolder());
        mMyThread.setRunning(true);
        mMyThread.start(); // запускаем в отдельном потоке
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // когда меняется surfaceview
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        mMyThread.setRunning(false);

        while(retry) {
            try {
                mMyThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
