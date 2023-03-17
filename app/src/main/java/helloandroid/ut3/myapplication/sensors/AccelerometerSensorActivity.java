package helloandroid.ut3.myapplication.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerSensorActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private float[] values;

    public AccelerometerSensorActivity(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.values = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    public float[] getAccelerometerValue() {
        return values;
    }

    public Sensor getSensor() {
        return this.accelerometerSensor;
    }
}
