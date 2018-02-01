package com.example.android.hedgehogorpenguin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Роман on 28.01.2018.
 */

public class MovementView extends SurfaceView implements SurfaceHolder.Callback {


    private Bitmap hedgehogBitmap;
    private Bitmap grassBitmap;
    private int width;
    private int height;
    private CopyOnWriteArrayList<Hedgehog> hedgehogs = new CopyOnWriteArrayList<>();

    UpdateThread updateThread;

    public MovementView(Context context, AttributeSet attrs) {

        super(context, attrs);
        getHolder().addCallback(this);

        Resources hedgehogImage = this.getResources();

        hedgehogBitmap = BitmapFactory.decodeResource(hedgehogImage, R.drawable.hedgehog);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) return;
        canvas.drawBitmap(grassBitmap, 0, 0, null);

        for (Hedgehog eachHedgehog : hedgehogs) {
            canvas.drawBitmap(hedgehogBitmap, eachHedgehog.getxPos(), eachHedgehog.getyPos(), null);
        }
    }


    public void addHedgehog() {
        hedgehogs.add(new Hedgehog(width, height));
    }

    public void restartAll() {
        if (!hedgehogs.isEmpty()) {
            hedgehogs.clear();
        }
    }

    public void updatePhysics() {

        for (Hedgehog eachHedgehog : hedgehogs) {
            eachHedgehog.updatePhysics();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        height-=50;
        Resources grassImage = this.getResources();
        grassBitmap = BitmapFactory.decodeResource(grassImage, R.drawable.grass);

        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;

        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
