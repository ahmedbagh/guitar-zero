package helloandroid.ut3.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {


    private final GameThread thread;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.rgb(55, 48, 107));
        }
    }

    public void update() {
//        if (currentLevel.isFinished()) {
//            stopGame();
//        } else {
//            currentLevel.update();
//        }
    }

    public void stopGame() {
//        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getContext());
//        SharedPreferences.Editor editor = saved_values.edit();
//        editor.putInt("current_score", (int) (System.currentTimeMillis() - startTime));
//        editor.commit();
//
//        this.surfaceDestroyed(this.getHolder());
//        Intent intent = new Intent(getContext(), ScoreActivity.class); // replace AnotherActivity with the name of your desired activity
//        getContext().startActivity(intent);
    }
}
