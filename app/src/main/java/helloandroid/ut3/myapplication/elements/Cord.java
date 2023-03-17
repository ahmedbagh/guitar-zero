package helloandroid.ut3.myapplication.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import helloandroid.ut3.myapplication.levels.Level;

public class Cord implements Element {

    private Rect shape;

    private Context context;

    private Level level;

    private int color;
    private float lineX;

    public enum State {
        IS_ACTIVATED,
        IS_NOT_ACTIVATED,
        IS_GREEN,
        IS_RED
    }

    private State state;

    public Cord(Rect shape, int color, Context context, Level level) {
        this.shape = shape;
        this.context = context;
        this.level = level;
        this.color = color;
        this.state = State.IS_NOT_ACTIVATED;
        lineX = this.shape.left;
    }

    public State getState() {
        return state;
    }

    public Cord(float xPos, float yPos, float width, float height, Context context, Level level) {
        this(
                new Rect((int) xPos, (int) yPos, (int) (xPos + width), (int) (yPos + height)),
                Color.GRAY,
                context,
                level
        );
    }

    public void setState(State state) {
        switch (state) {
            case IS_ACTIVATED:
                color = Color.rgb(255, 165, 0);
                this.state = State.IS_ACTIVATED;
                break;
            case IS_NOT_ACTIVATED:
                color = Color.GRAY;
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

        // Initialize the paint object for drawing the line
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(4);

        canvas.drawLine(lineX, this.shape.top, lineX, this.shape.bottom, linePaint);

        // Update the position of the line
        if (this.state == State.IS_GREEN) {
            lineX = (float) (this.shape.left + Math.sin(System.currentTimeMillis() / 100.0) * 35);
            if (lineX < this.shape.left) {
                this.lineX = this.shape.left;
            }
            if (lineX > this.shape.right) {
                this.lineX = this.shape.right;
            }
        } else {
            this.lineX = this.shape.left + this.shape.width() / 2;
        }

    }

    public void touchHandler(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (state == State.IS_ACTIVATED) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                    level.draggedCord(true);
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
