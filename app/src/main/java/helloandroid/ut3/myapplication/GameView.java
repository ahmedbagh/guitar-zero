package helloandroid.ut3.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import helloandroid.ut3.myapplication.levels.Level;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {


    private final GameThread thread;
    private final Level level_;
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

        level_ = new Level(context, lightSensorActivity, accelerometerSensorActivity);
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
        level_.toucheHandler(event);

        return true;
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

            level_.draw(canvas);
        }
    }

    public void update() {
        if (level_.isFinished()) {
            stopGame();
        } else {
            level_.update();
        }
    }

    public void stopGame() {
//        this.surfaceDestroyed(this.getHolder());
//        Intent intent = new Intent(getContext(), ScoreActivity.class);
//        getContext().startActivity(intent);
    }
}
