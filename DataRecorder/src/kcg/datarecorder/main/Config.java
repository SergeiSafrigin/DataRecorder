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
	private DeviceType mDeviceType;
	private double hAngle;
	private int previewHeight = 320;
	private int previewWidth = 240;
	private double vAngle;
	private boolean mIsContinuousScaning;
	
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

	public DeviceType getDeviceType() {
		return mDeviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		mDeviceType = deviceType;
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
		return mIsContinuousScaning;
	}

	public void setContinueosScaning(boolean continueosScaning) {
		mIsContinuousScaning = continueosScaning;
	}
}
