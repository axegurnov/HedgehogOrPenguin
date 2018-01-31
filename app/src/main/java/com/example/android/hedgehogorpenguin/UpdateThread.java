package com.example.android.hedgehogorpenguin;

/**
 * Created by Роман on 28.01.2018.
 */

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class UpdateThread extends Thread {

    private long time;
    private boolean isPenguin;
    private final int fps = 60;
    private boolean toRun = false;
    private MovementView movementView;
    private MovementViewPenguins movementViewPenguins;
    private SurfaceHolder surfaceHolder;

    UpdateThread(MovementView rMovementView) {
        movementView = rMovementView;
        surfaceHolder = movementView.getHolder();
        isPenguin = false;
    }

    UpdateThread(MovementViewPenguins rMovementView) {
        movementViewPenguins = rMovementView;
        surfaceHolder = movementViewPenguins.getHolder();
        isPenguin = true;
    }

    void setRunning(boolean run) {
        toRun = run;
    }

    @Override
    public void run() {
        Canvas c;
        while (toRun) {

            long cTime = System.currentTimeMillis();

            if ((cTime - time) <= (1000 / fps)) {

                c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    if (isPenguin) {
                        movementViewPenguins.updatePhysics();
                        movementViewPenguins.onDraw(c);
                    } else {
                        movementView.updatePhysics();
                        movementView.onDraw(c);
                    }

                } finally {

                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            time = cTime;
        }
    }
}