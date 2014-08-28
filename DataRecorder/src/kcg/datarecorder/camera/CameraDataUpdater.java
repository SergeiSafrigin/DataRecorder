package kcg.datarecorder.camera;

public interface CameraDataUpdater {
	public abstract void addFrame(byte[] frame, long time);
}
