package helloandroid.ut3.myapplication.levels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.ArrayList;

import helloandroid.ut3.myapplication.elements.Cord;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class Level_1 {

    public static final int NUMBER_CORDS = 4;

    private final Context context;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;
    private final int screenWidth;
    private final int screenHeight;

    private ArrayList<Cord> guitar;

    private float cordHeight;
    private float cordWidth;

    public Level_1(Context context, LightSensorActivity lightSensorActivity, AccelerometerSensorActivity accelerometerSensorActivity1) {
        this.context = context;
        this.lightSensorActivity = lightSensorActivity;
        this.accelerometerSensorActivity = accelerometerSensorActivity1;

        Activity activity = (Activity) context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        this.cordWidth = screenWidth/30;
        this.cordHeight = (float) (screenHeight*0.75);

        this.guitar = new ArrayList<>();

        initElements();
    }

    private void initElements() {
        this.guitar = new ArrayList<>();

        Point init = new Point(screenWidth/6, screenHeight/6);

        for (int i = 0; i < NUMBER_CORDS; i++) {
            guitar.add(new Cord(init.x +  i * screenWidth/5, init.y, cordWidth, cordHeight, context));
        }
    }

    public void update() {
        // System.out.println("light :" + lightSensorActivity.getLuminosity());
        // System.out.println("accelerometre: " + String.valueOf(accelerometerSensorActivity.getAccelerometerValue()[0]));
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

    public void toucheHandler(MotionEvent event){
        cord.touchHandler(event);
    }
}
