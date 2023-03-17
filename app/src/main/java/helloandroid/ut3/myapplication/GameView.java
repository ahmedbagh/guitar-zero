package helloandroid.ut3.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import helloandroid.ut3.myapplication.elements.Header;
import helloandroid.ut3.myapplication.levels.Level;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {


    private final GameThread thread;
    private final Level level;
    private final Header header;
    private final SensorManager sensorManager;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;
    private final long GAMEPLAY_TIME_MS = 2 * 60 * 1000;
    long startGame = System.currentTimeMillis();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        setOnTouchListener(this);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Sensors initialisation
        lightSensorActivity = new LightSensorActivity(sensorManager);
        accelerometerSensorActivity = new AccelerometerSensorActivity(sensorManager);

        level = new Level(context, this, lightSensorActivity, accelerometerSensorActivity);
        header = new Header(context, level);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        lightSensorActivity.onResume();
        accelerometerSensorActivity.onResume();
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
        lightSensorActivity.onPause();
        accelerometerSensorActivity.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        level.toucheHandler(event);

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            level.draw(canvas);
            header.draw(canvas);
        }
    }

    public void update() {
        if (level.isFinished() || (System.currentTimeMillis() - startGame) >= GAMEPLAY_TIME_MS) {
            stopGame();
        } else {
            level.update();
        }
    }

    public void stopGame() {
        this.surfaceDestroyed(this.getHolder());
        Intent intent = new Intent(getContext(), ScoreActivity.class);
        getContext().startActivity(intent);
    }
}
