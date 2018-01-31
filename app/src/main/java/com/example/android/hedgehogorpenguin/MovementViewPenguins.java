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

public class MovementViewPenguins extends SurfaceView implements SurfaceHolder.Callback {


    private Bitmap penguinBitmap;
    private Bitmap iceBitmap;
    private int width;
    private int height;

    private CopyOnWriteArrayList<Penguin> penguins = new CopyOnWriteArrayList<>();

    UpdateThread updateThread;

    public MovementViewPenguins(Context context, AttributeSet attrs) {

        super(context, attrs);
        getHolder().addCallback(this);

        Resources penguinImage = this.getResources();

        penguinBitmap = BitmapFactory.decodeResource(penguinImage, R.drawable.penguin);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (canvas == null) return;
        canvas.drawBitmap(iceBitmap, 0, 0, null);

        for (Penguin eachPenguin : penguins) {
            canvas.drawBitmap(penguinBitmap, eachPenguin.getxPos(), eachPenguin.getyPos(), null);
        }
    }


    public void addPenguin() {
        penguins.add(new Penguin(width, height));
    }

    public void updatePhysics() {

        for (Penguin eachPenguin : penguins) {
            eachPenguin.updatePhysics();
        }

    }

    public void restartAll() {
        if (!penguins.isEmpty()) {
            penguins.clear();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();
        Resources iceImage = this.getResources();
        iceBitmap = BitmapFactory.decodeResource(iceImage, R.drawable.ice3);

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
