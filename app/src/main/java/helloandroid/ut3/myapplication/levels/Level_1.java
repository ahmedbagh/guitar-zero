package helloandroid.ut3.myapplication.levels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import helloandroid.ut3.myapplication.elements.Cord;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class Level_1 {

    private final Context context;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;
    private final int screenWidth;
    private final int screenHeight;

    private Cord cord;

    public Level_1(Context context, LightSensorActivity lightSensorActivity, AccelerometerSensorActivity accelerometerSensorActivity1) {
        this.context = context;
        this.lightSensorActivity = lightSensorActivity;
        this.accelerometerSensorActivity = accelerometerSensorActivity1;

        Activity activity = (Activity) context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        initElements();
    }

    private void initElements() {
        this.cord = new Cord(200, 200, 500, 1250, context);
    }

    public void update() {
        // System.out.println("light :" + lightSensorActivity.getLuminosity());
        // System.out.println("accelerometre: " + String.valueOf(accelerometerSensorActivity.getAccelerometerValue()[0]));
    }

    public void draw(Canvas canvas) {
        cord.draw(canvas);
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
