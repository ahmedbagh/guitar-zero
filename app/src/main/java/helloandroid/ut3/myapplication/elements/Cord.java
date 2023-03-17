package helloandroid.ut3.myapplication.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import helloandroid.ut3.myapplication.levels.Level;

public class Cord {

    private final Rect shape;
    private final Context context;
    private final Level level;
    MediaPlayer mediaPlayer;
    private int color;
    private float lineX;
    private boolean allActivated = false;
    private State state;

    public Cord(Rect shape, Context context, Level level, MediaPlayer mediaPlayer) {
        this.shape = shape;
        this.context = context;
        this.level = level;
        this.color = Color.WHITE;
        this.state = State.IS_NOT_ACTIVATED;
        this.mediaPlayer = mediaPlayer;
        lineX = this.shape.left;
    }

    public Cord(float xPos, float yPos, float width, float height, Context context, Level level, MediaPlayer mediaPlayer) {
        this(
                new Rect((int) xPos, (int) yPos, (int) (xPos + width), (int) (yPos + height)),
                context,
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
            case IS_RED:
                color = Color.RED;
                this.state = State.IS_RED;
                break;
            case IS_GREEN:
                color = Color.GREEN;
                this.state = State.IS_GREEN;
                break;
        }
    }

    public boolean getAllActivated() {
        return allActivated;
    }

    public void setAllActivated(boolean allActivated) {
        this.allActivated = allActivated;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        canvas.drawRect(this.shape, paint);

        // Initialize the paint object for drawing the line
        Paint linePaint = new Paint();
        linePaint.setColor(this.color);
        linePaint.setStrokeWidth(10);

        canvas.drawLine(lineX + this.shape.width() / 2, this.shape.top, lineX + this.shape.width() / 2, this.shape.bottom, linePaint);

        // Update the position of the line
        if (this.state == State.IS_GREEN) {
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
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                        level.draggedCord(true);
                        playSound();
                        System.out.println("dragged");
                    }
                }
            }
            if (state == State.IS_NOT_ACTIVATED) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                        level.draggedCord(false);
                        System.out.println("dragged");
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
        IS_GREEN,
        IS_RED
    }
}
