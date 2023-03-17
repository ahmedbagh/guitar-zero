package helloandroid.ut3.myapplication.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Cord implements Element {

    private Rect shape;

    private Context context;

    private boolean isActivated;

    private int color;

    public Cord(Rect shape, boolean isActivated, int color, Context context) {
        this.shape = shape;
        this.context = context;
        this.isActivated = isActivated;
        this.color = color;
    }

    public Cord(float xPos, float yPos, float width, float height, Context context) {
        this(
                new Rect((int) xPos, (int) yPos, (int) (xPos + width), (int) (yPos + height)),
                false,
                Color.GRAY,
                context
        );
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
        float x = event.getX();
        float y = event.getY();

        if (isActivated) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (y >= shape.top && x >= shape.left && y <= shape.bottom && x <= shape.right) {
                    System.out.println("dragged");
                } else {
                    System.out.println("failed to drag");
                }
            }
        }
    }
}
