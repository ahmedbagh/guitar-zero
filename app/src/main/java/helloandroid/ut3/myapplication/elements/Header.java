package helloandroid.ut3.myapplication.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import helloandroid.ut3.myapplication.R;
import helloandroid.ut3.myapplication.levels.Level;

public class Header {


    private final Context context;
    private final Level level;

    public Header(Context context, Level level) {
        this.context = context;
        this.level = level;
    }

    public void draw(Canvas canvas) {
        // Get Header height & Width
        int headerHeight = context.getResources().getDimensionPixelSize(R.dimen.header_height);
        Rect headerWidth = new Rect(0, 0, canvas.getWidth(), headerHeight);

        // Header background
        Paint paint = new Paint();
        paint.setColor(Color.rgb(54, 41, 4));
        canvas.drawRect(headerWidth, paint);

        // Score settings
        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);


        int xPos = (canvas.getWidth() - (canvas.getWidth() / 8));
        int yPos = (int) ((headerHeight / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText("Score: " + level.getScore(), xPos, yPos, textPaint);

        // Level difficulty settings
        textPaint.setTextAlign(Paint.Align.LEFT);
        xPos = canvas.getWidth() / 8;
        yPos = (int) ((headerHeight / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        if (level.getFrequency() == Level.EASY_LEVEL_FREQUENCY) {
            canvas.drawText("Level: EASY", xPos, yPos, textPaint);
        }
        if (level.getFrequency() == Level.MEDIUM_LEVEL_FREQUENCY) {
            canvas.drawText("Level: MEDIUM", xPos, yPos, textPaint);
        }
        if (level.getFrequency() == Level.HARD_LEVEL_FREQUENCY) {
            canvas.drawText("Level: HARD", xPos, yPos, textPaint);
        }

    }
}
