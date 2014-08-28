package kcg.datarecorder.camera;

import kcg.datarecorder.main.Config;
import kcg.datarecorder.recorder.OnFrameTrigger;
import android.content.Context;
import android.view.View;

public class CameraController implements CameraDataUpdater {

	private CameraDataUpdater cameraDataUpdater;
	private CameraPreview cameraPreview;
	private CameraScreenView cameraScreenView;
	private Context context;
	private int height;
	private byte lastFrame[];
	private long lastFrameTime;
	private OnFrameTrigger onFrameTrigger;
	private int width;

	public CameraController(Context context, Config config) {
		this.context = context;
		cameraDataUpdater = this;
		cameraScreenView = new CameraScreenView(context, config);
		cameraPreview = new CameraPreview(context, config, cameraDataUpdater);
	}

	public void addFrame(byte[] frame, long time) {
		lastFrameTime = time;
		lastFrame = frame;
		if (onFrameTrigger != null) {
			onFrameTrigger.onFrame();
		}
		cameraScreenView.setFrame(lastFrame);
	}

	public View getCameraPreview() {
		return cameraPreview;
	}

	public View getCameraView() {
		return cameraScreenView;
	}

	public int getHeight() {
		return height;
	}

	public byte[] getLastFrame() {
		return lastFrame;
	}

	public long getLastFrameTime() {
		return lastFrameTime;
	}

	public int getWidth() {
		return width;
	}

	public void onPause() {
		cameraPreview.onPause();
	}

	public void onResume() {
		cameraScreenView.onResume();
	}

	public void registerOnFrameListener(OnFrameTrigger trigger) {
		onFrameTrigger = trigger;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setLastFrame(byte[] frame) {
		lastFrame = frame;
	}

	public void setLastFrameTime(long time) {
		lastFrameTime = time;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void unregisterOnFrameListener() {
		onFrameTrigger = null;
	}
}
