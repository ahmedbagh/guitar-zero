package helloandroid.ut3.myapplication;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private final GameView gameView;
    private final Handler mHandler;
    private final SurfaceHolder surfaceHolder;

    // Update data
    private final Runnable mRunnableUpdate = new Runnable() {
        public void run() {
            if (running) {
                gameView.update();
                mHandler.postDelayed(this, 1 / 100);
            }
        }
    };

    // Refresh Screen
    Canvas canvas;
    private final Runnable mRunnableDraw = new Runnable() {
        public void run() {
            if (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        gameView.draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.postDelayed(this, 1 / 60);
                }
            }
        }
    };

    boolean running;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        mHandler = new Handler();
        this.gameView = gameView;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        mHandler.post(mRunnableUpdate);
        mHandler.post(mRunnableDraw);
    }

}
