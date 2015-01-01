package kcg.datarecorder.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import kcg.datarecorder.main.Config;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {

	private static final String TAG = "Preview";
	private Camera camera;
	private Config config;
	private SurfaceHolder holder;
	private CameraDataUpdater listener;

	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Config config, CameraDataUpdater updater) {
		super(context);
		this.config = config;
		listener = updater;
		
		holder = getHolder(); //gets a holder pointer from parent (surfaceView)
		holder.addCallback(this); //Install a SurfaceHolder.Callback so we get notified when the underlying surface is created, changed or destroyed
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		android.hardware.Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
		Config.SCREEN_SIZES = sizes;
		Size size = sizes.get(closest(sizes, config.getPreviewWidth(), config.getPreviewHeight()));
		parameters.setPreviewSize(size.width, size.height);
		config.setPreviewHeight(size.height);
		config.setPreviewWidth(size.width);
		setCameraViewAngles();
		camera.setParameters(parameters);
		camera.setPreviewCallback(this);
		camera.startPreview();
	}

	public void surfaceCreated(SurfaceHolder surfaceholder) {
		surfaceDestroyed(surfaceholder);
		camera = Camera.open(config.getCameraId());
		try {
			camera.setDisplayOrientation(90);
			camera.setPreviewDisplay(surfaceholder);
			return;
		} catch (IOException ioexception) {
			camera.release();
		}
		camera = null;
	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		if (camera != null) {
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}
	
	private void setCameraViewAngles() {
		android.hardware.Camera.Parameters parameters = camera.getParameters();
		int i = ((Integer) parameters.getZoomRatios().get(parameters.getZoom()))
				.intValue();
		android.hardware.Camera.Size size = parameters.getPreviewSize();
		double d = (double) size.width / (double) size.height;
		double d1 = Math.toRadians(parameters.getVerticalViewAngle());
		double d2 = 2D * Math.atan(d * Math.tan(d1 / 2D));
		double d3 = 2D * Math.atan((100D * Math.tan(d1 / 2D)) / (double) i);
		double d4 = 2D * Math.atan((100D * Math.tan(d2 / 2D)) / (double) i);
		if (config.getDevice() == Config.DeviceType.PHONE) {
			config.sethAngle(Math.toDegrees(d4));
			config.setvAngle(Math.toDegrees(d3));
			return;
		} else {
			config.sethAngle(Math.toDegrees(Math.toDegrees(d4 / 3D)));
			config.setvAngle(Math.toDegrees(Math.toDegrees(d3)));
			return;
		}
	}
	
	public static int closest(List<Camera.Size> sizes, int width, int height) {
		int best = -1;
		int bestScore = Integer.MAX_VALUE;

		for (int i = 0; i < sizes.size(); i++) {
			Camera.Size s = sizes.get(i);

			int dx = s.width - width;
			int dy = s.height - height;

			int score = dx * dx + dy * dy;
			if (score < bestScore) {
				best = i;
				bestScore = score;
			}
		}
		return best;
	}
	
	public void onPreviewFrame(byte frame[], Camera camera) {
		long time = System.currentTimeMillis();
		listener.addFrame(frame, time);
	}
	
	protected void onMeasure(int width, int height) {
		setMeasuredDimension(1, 1);
	}

	public void onPause() {
		surfaceDestroyed(holder);
	}
}
