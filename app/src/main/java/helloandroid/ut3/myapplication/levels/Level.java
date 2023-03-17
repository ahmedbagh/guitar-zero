package helloandroid.ut3.myapplication.levels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import helloandroid.ut3.myapplication.elements.Cord;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class Level {

    public static final int NUMBER_CORDS = 4;

    private final Context context;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;
    private final int screenWidth;
    private final int screenHeight;
    private final Handler mHandler = new Handler();

    private ArrayList<Cord> guitar;
    private Cord selectedCord;
    private boolean isDraggedCord = false;

    private float cordHeight;
    private float cordWidth;

    private int frequence = 1500;

    private boolean isGreenOrRed = false;

    private final Runnable mRunnableCordActivation = new Runnable() {
        public void run() {
//            if (!isDraggedCord) {
//                guitar.forEach(c -> c.setState(Cord.State.IS_RED));
//            } else {
            guitar.forEach(cord -> {
                if(cord.getState() == Cord.State.IS_GREEN || cord.getState() == Cord.State.IS_RED){
                    isGreenOrRed = true;
                }
            });
            if(isGreenOrRed){
                guitar.forEach(c -> c.setState(Cord.State.IS_NOT_ACTIVATED));
                isGreenOrRed = false;
                mHandler.postDelayed(this, frequence/5);
            }else {
                guitar.forEach(c -> c.setState(Cord.State.IS_NOT_ACTIVATED));
                Random random = new Random();
                int randomNumber = random.nextInt(4);
                selectedCord = guitar.get(randomNumber);
                selectedCord.setState(Cord.State.IS_ACTIVATED);
                mHandler.postDelayed(()-> {
                    if(selectedCord.getState() == Cord.State.IS_ACTIVATED){
                        selectedCord.setState(Cord.State.IS_RED);
                    }
                },frequence - frequence/4);
                isGreenOrRed = false;
                mHandler.postDelayed(this, frequence);
            }

        }
    };

    public Level(Context context, LightSensorActivity lightSensorActivity, AccelerometerSensorActivity accelerometerSensorActivity1) {
        this.context = context;
        this.lightSensorActivity = lightSensorActivity;
        this.accelerometerSensorActivity = accelerometerSensorActivity1;

        Activity activity = (Activity) context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        this.cordWidth = screenWidth / 30;
        this.cordHeight = (float) (screenHeight * 0.75);

        this.guitar = new ArrayList<>();

        initElements();

        mHandler.post(mRunnableCordActivation);
    }

    private void initElements() {
        this.guitar = new ArrayList<>();

        Point init = new Point(screenWidth / 6, screenHeight / 6);

        for (int i = 0; i < NUMBER_CORDS; i++) {
            guitar.add(new Cord(init.x + i * screenWidth / 5, init.y, cordWidth, cordHeight, context, this));
        }
    }


    long lastActivation = 0;

    public void update() {
        // System.out.println("light :" + lightSensorActivity.getLuminosity());
        // System.out.println("accelerometre: " + String.valueOf(accelerometerSensorActivity.getAccelerometerValue()[0]));

        long currentTime = System.currentTimeMillis();


    }

    public void draggedCord(boolean draggedCord) {
        if (draggedCord) {
            selectedCord.setState(Cord.State.IS_GREEN);
        } else {
            guitar.forEach(c -> c.setState(Cord.State.IS_RED));
        }
        isDraggedCord = draggedCord;
    }

    public void draw(Canvas canvas) {
        this.guitar.forEach(cord -> cord.draw(canvas));
    }

    public boolean isFinished() {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = saved_values.edit();
        editor.putInt("score", 0);
        editor.commit();

        return false;
    }

    public void toucheHandler(MotionEvent event) {
        this.guitar.forEach(cord -> cord.touchHandler(event));
    }
}
