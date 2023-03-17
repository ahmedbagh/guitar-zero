package helloandroid.ut3.myapplication.levels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import helloandroid.ut3.myapplication.R;
import helloandroid.ut3.myapplication.elements.Cord;
import helloandroid.ut3.myapplication.sensors.AccelerometerSensorActivity;
import helloandroid.ut3.myapplication.sensors.LightSensorActivity;

public class Level {
    public static int EASY_LEVEL_FREQUENCY = 2000;
    public static int MEDIUM_LEVEL_FREQUENCY = 1500;
    public static int HARD_LEVEL_FREQUENCY = 1000;

    private final Context context;
    private final LightSensorActivity lightSensorActivity;
    private final AccelerometerSensorActivity accelerometerSensorActivity;
    private final int screenWidth;
    private final int screenHeight;
    private final Handler mHandler = new Handler();
    private final float cordWidth;
    private final View view;
    private final float cordHeight;
    float xValue = 0;
    private int score = 5;
    private ArrayList<Cord> guitar;
    private Cord selectedCord;
    private int frequence = HARD_LEVEL_FREQUENCY;
    private boolean isGreenOrRed = false;
    private boolean allActivated = false;
    private final Runnable mRunnableCordActivation = new Runnable() {
        public void run() {
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
                doCycle();
            }

        }

        private void doCycle() {
            guitar.forEach(c -> c.setState(Cord.State.IS_NOT_ACTIVATED));
            Random random = new Random();
            int randomNumber = random.nextInt(5);

            if (randomNumber != 4) {
                startOneCord(randomNumber);
            } else {
                startAllCords();
            }
        }

        private void startAllCords() {
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

        private void startOneCord(int randomNumber) {
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

        initElements();

        mHandler.post(mRunnableCordActivation);
    }

    public int getScore() {
        return score;
    }

    private void initElements() {
        this.guitar = new ArrayList<>();

        int headerHeight = context.getResources().getDimensionPixelSize(R.dimen.header_height);

        int x = screenWidth / 6;

        guitar.add(new Cord(x + 0 * screenWidth / 5, headerHeight, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.a_chord)));
        guitar.add(new Cord(x + 1 * screenWidth / 5, headerHeight, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.c_chord)));
        guitar.add(new Cord(x + 2 * screenWidth / 5, headerHeight, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.d_chord)));
        guitar.add(new Cord(x + 3 * screenWidth / 5, headerHeight, cordWidth, cordHeight, context, this, MediaPlayer.create(context, R.raw.g_chord)));
    }

    public void update() {
        float currentX = accelerometerSensorActivity.getAccelerometerValue()[0];
        if (allActivated && Math.abs(currentX - xValue) > 5) {
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

    public void draggedCord(boolean draggedCord) {
        if (draggedCord) {
            selectedCord.setState(Cord.State.IS_GREEN);
            score += 1;
        } else {
            guitar.forEach(c -> c.setState(Cord.State.IS_RED));
            score -= 1;
        }
    }

    public void draw(Canvas canvas) {
        // background
        canvas.drawColor(Color.rgb(212, 168, 83));
        Paint paint = new Paint();
        paint.setColor(Color.rgb(105, 66, 7));
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (canvas.getWidth() / 3) + (canvas.getWidth() / 10), paint);

        // Cords
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
