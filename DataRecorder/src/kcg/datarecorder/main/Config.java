package kcg.datarecorder.main;

import java.io.Serializable;
import java.util.List;

import android.hardware.Camera.Size;

public class Config implements Serializable {
	private static final long serialVersionUID = 0xa8b2fce2b82d3db1L;

	public static enum CameraType {FRONT, BACK};
	public static enum DeviceType {GLASSES, PHONE}

	private CameraType mCameraType;
	private int cameraId;
	private DeviceType device;
	private double hAngle;
	private int previewHeight = 320;
	private int previewWidth = 240;
	private double vAngle;
	private boolean continueosScaning;
	
	public static List<Size> SCREEN_SIZES;

	public CameraType getCameraType() {
		return mCameraType;
	}

	public void setCameraType(CameraType cameraType) {
		mCameraType = cameraType;
	}

	public int getCameraId() {
		return cameraId;
	}

	public void setCameraId(int cameraId) {
		this.cameraId = cameraId;
	}

	public DeviceType getDevice() {
		return device;
	}

	public void setDevice(DeviceType device) {
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

	public boolean isContinueosScaning() {
		return continueosScaning;
	}

	public void setContinueosScaning(boolean continueosScaning) {
		this.continueosScaning = continueosScaning;
	}
}
