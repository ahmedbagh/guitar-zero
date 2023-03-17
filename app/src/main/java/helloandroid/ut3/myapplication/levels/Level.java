package helloandroid.ut3.myapplication.levels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import helloandroid.ut3.myapplication.R;
import helloandroid.ut3.myapplication.elements.Cord;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class Level {
    public static final int NUMBER_CORDS = 4;
    public static int EASY_LEVEL_FREQUENCY = 2000;
    public static int MEDIUM_LEVEL_FREQUENCY = 1500;
    public static int HARD_LEVEL_FREQUENCY = 1000;


    private final Context context;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;
    private final int screenWidth;
    private final int screenHeight;
    private final Handler mHandler = new Handler();
    private int score = 5;

    private ArrayList<Cord> guitar;
    private Cord selectedCord;
    private boolean isDraggedCord = false;

    private float cordHeight;
    private float cordWidth;

    private int frequence = HARD_LEVEL_FREQUENCY;

    private boolean isGreenOrRed = false;

    private boolean allActivated = false;

    private View view;

    private final Runnable mRunnableCordActivation = new Runnable() {
        public void run() {
            if (isFinished()) {
                return;
            }
            guitar.forEach(cord -> {
                if (cord.getState() == Cord.State.IS_GREEN || cord.getState() == Cord.State.IS_RED) {
                    isGreenOrRed = true;
                }
            });
            if (isGreenOrRed) {
                guitar.forEach(c -> c.setState(Cord.State.IS_NOT_ACTIVATED));
                isGreenOrRed = false;
                mHandler.postDelayed(this, frequence / 5);
            } else {
                guitar.forEach(c -> c.setState(Cord.State.IS_NOT_ACTIVATED));
                Random random = new Random();
                int randomNumber = random.nextInt(5);

                if (randomNumber != 4) {
                    selectedCord = guitar.get(randomNumber);
                    selectedCord.setState(Cord.State.IS_ACTIVATED);
                    mHandler.postDelayed(() -> {
                        if (selectedCord.getState() == Cord.State.IS_ACTIVATED) {
                            selectedCord.setState(Cord.State.IS_RED);
                            score -= 1;
                        }
                    }, frequence - frequence / 4);
                    isGreenOrRed = false;
                    mHandler.postDelayed(this, frequence);
                } else {
                    vibrateDisplay();
                    selectedCord = guitar.get(0);
                    guitar.forEach(c -> {
                        c.setState(Cord.State.IS_ACTIVATED);
                        c.setAllActivated(true);
                    });
                    allActivated = true;
                    mHandler.postDelayed(() -> {
                        if (selectedCord.getState() == Cord.State.IS_ACTIVATED) {
                            guitar.forEach(c -> {
                                c.setState(Cord.State.IS_RED);
                                c.setAllActivated(false);
                            });
                            allActivated = false;
                            score -= 1;
                        }

                    }, frequence - frequence / 4);

                    mHandler.postDelayed(this, frequence);

                }
            }

        }
    };

    public Level(Context context, View view, LightSensorActivity lightSensorActivity, AccelerometerSensorActivity accelerometerSensorActivity) {
        this.context = context;
        this.lightSensorActivity = lightSensorActivity;
        this.accelerometerSensorActivity = accelerometerSensorActivity;

        this.view = view;

        Activity activity = (Activity) context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        this.cordWidth = screenWidth / 20;
        this.cordHeight = (float) (screenHeight);

        this.guitar = new ArrayList<>();

        initElements();

        mHandler.post(mRunnableCordActivation);
    }

    public int getScore() {
        return score;
    }

    private void initElements() {
        this.guitar = new ArrayList<>();

        int headerHeight = context.getResources().getDimensionPixelSize(R.dimen.header_height);


        Point init = new Point(screenWidth / 6, headerHeight);


        guitar.add(new Cord(init.x + 0 * screenWidth / 5, init.y, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.a_chord)));
        guitar.add(new Cord(init.x + 1 * screenWidth / 5, init.y, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.c_chord)));
        guitar.add(new Cord(init.x + 2 * screenWidth / 5, init.y, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.d_chord)));
        guitar.add(new Cord(init.x + 3 * screenWidth / 5, init.y, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.g_chord)));
    }


    float xValue = 0;

    public void update() {
        float currentX = accelerometerSensorActivity.getAccelerometerValue()[0];
        if (allActivated && Math.abs(currentX - xValue) > 5) { //
            guitar.forEach(cord -> {
                cord.setState(Cord.State.IS_GREEN);
                cord.setAllActivated(false);
                cord.playSound();
            });
            allActivated = false;
            score += 1;
        }
        xValue = currentX;

        if (this.lightSensorActivity.getLuminosity() < 40) {
            this.frequence = HARD_LEVEL_FREQUENCY;
        } else if (this.lightSensorActivity.getLuminosity() >= 40 && this.lightSensorActivity.getLuminosity() < 150) {
            this.frequence = MEDIUM_LEVEL_FREQUENCY;
        } else if (this.lightSensorActivity.getLuminosity() >= 150) {
            this.frequence = EASY_LEVEL_FREQUENCY;
        }
    }

    public void vibrateDisplay() {
        Toast toast = Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 50);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.shake); // Replace "icon" with the name of your icon resource

        LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.addView(imageView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        toast.setView(toastLayout);
        toast.show();

    }

    public void draggedCord(boolean draggedCord) {
        if (draggedCord) {
            selectedCord.setState(Cord.State.IS_GREEN);
            score += 1;
        } else {
            guitar.forEach(c -> c.setState(Cord.State.IS_RED));
            score -= 1;
        }
        isDraggedCord = draggedCord;
    }


    public void setAllActivated(boolean allActivated) {
        this.allActivated = allActivated;
    }

    public void draw(Canvas canvas) {
        this.guitar.forEach(cord -> cord.draw(canvas));
    }

    public boolean isFinished() {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = saved_values.edit();
        editor.putInt("current_score", score);
        editor.commit();

        return score < 1;
    }

    public void toucheHandler(MotionEvent event) {
        this.guitar.forEach(cord -> cord.touchHandler(event));
    }

    public int getFrequency() {
        return frequence;
    }
}
