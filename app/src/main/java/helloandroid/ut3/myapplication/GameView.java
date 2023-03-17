package helloandroid.ut3.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
        // Define the dimensions of the header
        int headerHeight = this.getContext().getResources().getDimensionPixelSize(R.dimen.header_height);

// Set up a clipping region that excludes the area covered by the header
        Rect clipRect = new Rect(0, 0, canvas.getWidth(), headerHeight);
        if (canvas != null) {
            super.draw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.rgb(255,255,255));
            canvas.drawColor(Color.rgb(55, 48, 107));
            canvas.drawRect(clipRect, paint);
            //
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(40);

            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((headerHeight / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
            canvas.drawText("Score : x", xPos, yPos, textPaint);
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
