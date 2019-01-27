package com.example.acer.myapplication;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by admin on 27.01.2018.
 */

public class MyThread extends Thread {

    // частота обновления экрана
    private final int REDRAW_TIME = 100;
    // время анимаци
    private final int ANIMATION_TIME = 1500;

    // нужна для получения canvas
    private SurfaceHolder mSurfaceHolder;

    // флажок, запущен ли процесс
    private boolean mRunning;
    // время начала инимации
    private long mStartTime;
    // предыдущее время перерисовки
    private long mPrevRedrawTime;

    private Paint mPaint;
    // переменная для интерполирования
    private ArgbEvaluator mArgEvaluator;

    MyThread(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mRunning = false;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mArgEvaluator = new ArgbEvaluator();
    }

    public void setRunning(boolean running) {
        mRunning = running;
        mPrevRedrawTime = getTime();
    }

    public long getTime() {
        return System.nanoTime()/1_000;
    }

    public void run() {
        Canvas canvas;
        mStartTime = getTime();
        while(mRunning) {
            long curTime = getTime();
            long elapsedTime = curTime - mPrevRedrawTime;
            // прошло ли 10 мс.
            if(elapsedTime < REDRAW_TIME)
                continue;
            //  если прошло, то перерисовываем картинку
            canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(); // получаем canvas
                draw(canvas); // функция рисования
            } catch (NullPointerException e) { }
            finally {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
                // освобождение canvas
            }
            mPrevRedrawTime = curTime;
        }

    }

    public void draw(Canvas canvas) {
        long curTime = getTime() - mStartTime;

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawColor(Color.BLACK);


        int centerX = width/2;
        int centerY = height/2;

        float maxSize = Math.min(width,height)/2;
        float fraction = (float)(curTime%ANIMATION_TIME)/ANIMATION_TIME;

        int color = (int) mArgEvaluator.evaluate(fraction,Color.RED,Color.BLACK);
        mPaint.setColor(color);

        canvas.drawCircle(centerX,centerY,maxSize*fraction,mPaint);
    }
}
