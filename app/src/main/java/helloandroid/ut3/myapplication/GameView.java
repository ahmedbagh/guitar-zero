package helloandroid.ut3.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import helloandroid.ut3.myapplication.elements.Cord;

import helloandroid.ut3.myapplication.levels.Level_1;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {


    private final GameThread thread;
    private final Level_1 level_1;
    private final SensorManager sensorManager;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        setOnTouchListener(this);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //Light Sensor initialisation
        lightSensorActivity = new LightSensorActivity(sensorManager);
        accelerometerSensorActivity = new AccelerometerSensorActivity(sensorManager);

        level_1 = new Level_1(context, lightSensorActivity, accelerometerSensorActivity);
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
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.rgb(55, 48, 107));
            level_1.draw(canvas);
        }
    }

    public void update() {
        if (level_1.isFinished()) {
            stopGame();
        } else {
            level_1.update();
        }
    }

    public void stopGame() {
//        this.surfaceDestroyed(this.getHolder());
//        Intent intent = new Intent(getContext(), ScoreActivity.class);
//        getContext().startActivity(intent);
    }
}
