package kcg.datarecorder.sensors;

import android.content.Context;
import kcg.datarecorder.main.Config;

public class SensorsController implements SensorsDataUpdater {
    private float pitch;
    private float pressure;
    private float roll;
    private SensorsScanner sensorsScanner;
    private float yaw;

    public SensorsController(Context context, Config config) {
        sensorsScanner = new SensorsScanner(context, config, this);
    }

    public void addOrientation(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public void addPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPressure() {
        return pressure;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setPressure(float pressure)
    {
        this.pressure = pressure;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void startSensorScanner() {
        sensorsScanner.onResume();
    }

    public void stopSensorScanner() {
        sensorsScanner.onPause();
    }
}
