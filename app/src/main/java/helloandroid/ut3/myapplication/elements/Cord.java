package helloandroid.ut3.myapplication.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import helloandroid.ut3.myapplication.R;
import helloandroid.ut3.myapplication.levels.Level;

public class Cord implements Element {

    private Rect shape;

    private Context context;

    private Level level;

    private int color;

    private boolean allActivated = false;

    MediaPlayer mediaPlayer;

    public enum State {
        IS_ACTIVATED,
        IS_NOT_ACTIVATED,
        IS_GREEN,
        IS_RED
    }

    private State state;

    public Cord(Rect shape, int color, Context context, Level level, MediaPlayer mediaPlayer) {
        this.shape = shape;
        this.context = context;
        this.level = level;
        this.color = color;
        this.state = State.IS_NOT_ACTIVATED;
        this.mediaPlayer = mediaPlayer;
    }

    public State getState() {
        return state;
    }

    public Cord(float xPos, float yPos, float width, float height, Context context, Level level, MediaPlayer mediaPlayer) {
        this(
                new Rect((int) xPos, (int) yPos, (int) (xPos + width), (int) (yPos + height)),
                Color.WHITE,
                context,
                level,
                mediaPlayer
        );
    }

    public boolean getAllActivated() {
        return allActivated;
    }

    public void setAllActivated(boolean allActivated) {
        this.allActivated = allActivated;
    }

    public void setState(State state) {
        switch (state) {
            case IS_ACTIVATED:
                color = Color.rgb(152, 108, 80);
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

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.color);
        canvas.drawRect(this.shape, paint);
    }

    public void touchHandler(MotionEvent event) {
        if (!allActivated) {
            float x = event.getX();
            float y = event.getY();

            if (state == State.IS_ACTIVATED) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                        level.draggedCord(true);
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.reset();
                        } else {
                            mediaPlayer.start();
                        }
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
}
