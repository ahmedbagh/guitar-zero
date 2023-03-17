package helloandroid.ut3.myapplication.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Cord implements Element{

    private Rect shape;

    private float xPos;
    private float yPos;

    private Context context;

    private boolean isActivated;

    private int color;

    public Cord(Rect shape, float xPos, float yPos, boolean isActivated, int color, Context context) {
        this.shape = shape;
        this.xPos = xPos;
        this.yPos = yPos;
        this.context = context;
        this.isActivated = isActivated;
        this.color = color;
    }

    public Cord(float xPos, float yPos, float width, float height, Context context) {
        this(
                new Rect((int) xPos, (int) yPos, (int) (xPos + width), (int) (yPos + height)),
                xPos,
                yPos,
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
}
