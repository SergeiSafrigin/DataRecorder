package kcg.datarecorder.main;

import java.io.Serializable;
import java.util.List;

import android.hardware.Camera.Size;

public class Config implements Serializable {
	private static final long serialVersionUID = 0xa8b2fce2b82d3db1L;

	public static enum Camera {FRONT, BACK};
	public static enum Device {GLASSES, PHONE}

	private Camera camera;
	private int cameraId;
	private Device device;
	private double hAngle;
	private int previewHeight = 320;
	private int previewWidth = 240;
	private double vAngle;
	
	public static List<Size> SCREEN_SIZES;

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public int getCameraId() {
		return cameraId;
	}

	public void setCameraId(int cameraId) {
		this.cameraId = cameraId;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public double gethAngle() {
		return hAngle;
	}

	public void sethAngle(double hAngle) {
		this.hAngle = hAngle;
	}

	public int getPreviewHeight() {
		return previewHeight;
	}

	public void setPreviewHeight(int previewHeight) {
		this.previewHeight = previewHeight;
	}

	public int getPreviewWidth() {
		return previewWidth;
	}

	public void setPreviewWidth(int previewWidth) {
		this.previewWidth = previewWidth;
	}

	public double getvAngle() {
		return vAngle;
	}

	public void setvAngle(double vAngle) {
		this.vAngle = vAngle;
	}	
}
