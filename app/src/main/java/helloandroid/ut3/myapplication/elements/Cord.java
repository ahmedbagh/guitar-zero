package helloandroid.ut3.myapplication.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import helloandroid.ut3.myapplication.levels.Level;

public class Cord {

    private final Rect shape;
    private final Level level;
    MediaPlayer mediaPlayer;
    private int color;
    private float lineX;
    private boolean allActivated = false;
    private State state;

    public Cord(Rect shape, Level level, MediaPlayer mediaPlayer) {
        this.shape = shape;
        this.level = level;
        this.color = Color.WHITE;
        this.state = State.IS_NOT_ACTIVATED;
        this.mediaPlayer = mediaPlayer;
        lineX = this.shape.left;
    }

    public Cord(float xPos, float yPos, float width, float height, Level level, MediaPlayer mediaPlayer) {
        this(
                new Rect((int) xPos, (int) yPos, (int) (xPos + width), (int) (yPos + height)),
                level,
                mediaPlayer
        );
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        switch (state) {
            case IS_ACTIVATED:
                color = Color.rgb(208, 160, 130);
                this.state = State.IS_ACTIVATED;
                break;
            case IS_NOT_ACTIVATED:
                color = Color.WHITE;
                this.state = State.IS_NOT_ACTIVATED;
                break;
            case IS_WRONG:
                color = Color.RED;
                this.state = State.IS_WRONG;
                break;
            case IS_PLAYING:
                color = Color.GREEN;
                this.state = State.IS_PLAYING;
                break;
        }
    }

    public void setAllActivated(boolean allActivated) {
        this.allActivated = allActivated;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        // Initialize the paint object for drawing the line
        Paint linePaint = new Paint();
        linePaint.setColor(this.color);
        linePaint.setStrokeWidth(10);

        canvas.drawLine(lineX + this.shape.width() / 2, this.shape.top, lineX + this.shape.width() / 2, this.shape.bottom, linePaint);

        // Update the position of the line
        if (this.state == State.IS_PLAYING) {
            lineX = (float) (this.shape.left + Math.sin(System.currentTimeMillis() / 100.0) * 25);
        } else {
            this.lineX = this.shape.left;
        }

    }

    public void touchHandler(MotionEvent event) {
        if (!allActivated) {
            float x = event.getX();
            float y = event.getY();

            if (state == State.IS_ACTIVATED) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) { // Correct chord
                    if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                        level.draggedCord(true);
                        playSound();
                    }
                }
            }

            if (state == State.IS_NOT_ACTIVATED) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) { // Wrong chord
                    if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                        level.draggedCord(false);
                    }
                }
            }
        }

    }

    public void playSound() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        }
        mediaPlayer.start();
    }


    public enum State {
        IS_ACTIVATED,
        IS_NOT_ACTIVATED,
        IS_PLAYING,
        IS_WRONG
    }
}
