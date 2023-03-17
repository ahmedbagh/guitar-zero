package helloandroid.ut3.myapplication.elements;

import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

public interface Element {

    public  void update();

    public  void draw(Canvas canvas);

}
