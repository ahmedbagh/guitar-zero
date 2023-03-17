package helloandroid.ut3.myapplication.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensorActivity implements SensorEventListener {
    private Sensor lightSensor;
    private SensorManager sensorManager;
    float luminosity;

    public LightSensorActivity(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Nouvelle valeur de la luminosité
        this.luminosity = event.values[0];


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onResume() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    public float getLuminosity() {
        return luminosity;
    }

    public Sensor getSensor() {
        return this.lightSensor;
    }
}
