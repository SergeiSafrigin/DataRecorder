package kcg.datarecorder.sensors;


public interface SensorsDataUpdater {
    public abstract void addOrientation(float yaw, float pitch, float roll);
    public abstract void addPressure(float pressure);
}
