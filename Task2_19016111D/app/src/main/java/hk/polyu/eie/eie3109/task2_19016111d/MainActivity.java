package hk.polyu.eie.eie3109.task2_19016111d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static Panel panel;

    private SharedPreferences sharedPreferences;
    private Vibrator v;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean asAvailable;
    private double[] sensorValue_current = {0, 0, 0};
    private double[] sensorValue_previous = {0, 0, 0};
    private double[] sensorValue_diff = {0, 0, 0};
    private boolean firstTime = true;
    private static final double SHAKE_THRESHOLD = 7;
    private long shakeTime = 0;
    private long shakeTime_prev = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        sharedPreferences = getSharedPreferences("saved", Context.MODE_PRIVATE);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        panel = new Panel(this, sharedPreferences, v);
        setContentView(panel);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            asAvailable = true;
        } else {
            asAvailable = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        for (int i = 0; i < 3; i++){
            sensorValue_current[i] = sensorEvent.values[i];
        }
        boolean shaking = false;
        if (!firstTime){
            for (int i = 0; i < 3; i++){
                sensorValue_diff[i] = Math.abs(sensorValue_current[i] - sensorValue_previous[i]);
                if (sensorValue_diff[i] > SHAKE_THRESHOLD){
                    shaking = true;
                }
            }
            if (shaking){
                shakeTime = System.currentTimeMillis();
                if (shakeTime - shakeTime_prev > 50) {
                    panel.phoneIsShaking();
                    shakeTime_prev = shakeTime;
                }
            }
        } else {
            firstTime = false;
        }
        for (int i = 0; i < 3; i++){
            sensorValue_previous[i] = sensorValue_current[i];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (asAvailable){
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (asAvailable){
            sensorManager.unregisterListener(this);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}