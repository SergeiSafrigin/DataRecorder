package kcg.datarecorder.camera;

import kcg.datarecorder.main.Config;
import kcg.datarecorder.recorder.OnFrameTrigger;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

public class NewCameraController implements CameraFrameTrigger {

	private Context mContext;
	private Config mConfig;
	private Camera mCamera;
	private NewCameraPreview mPreview;
	//private PhotoHandler mPhotoHandler;
	private OnFrameTrigger mOnFrameTrigger;

	
	public NewCameraController(Context context, Config config) {
		if(!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Toast.makeText(context, "Camera not detected on this device", Toast.LENGTH_LONG)
			.show();
			return;
		}
		
		mContext = context;
		mConfig = config;

		mCamera = Camera.open(config.getCameraId());
		//mCamera.setDisplayOrientation(90);
				
		mPreview = new NewCameraPreview(mContext, mCamera, this);
		
        //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //preview.addView(mPreview);
        //mPhotoHandler = new PhotoHandler(context); for taking specific picture
    }
	
	public void newFrameTrigger() {
		if (mOnFrameTrigger != null) {
			mOnFrameTrigger.onFrame();
		}
	}
	public NewCameraPreview getCameraPreview() {
		return mPreview;
	}
	
	public void registerOnFrameListener(OnFrameTrigger trigger) {
		mPreview.reset();
		mOnFrameTrigger = trigger;
	}

	public void unregisterOnFrameListener() {
		mOnFrameTrigger = null;
	}

	public long getLastFrameTime() {
		return mPreview.getLastFrameTime();
	}

	public byte[] getLastFrame() {
		return mPreview.getLastFrame();
	}

	public int getFrameCounter() {
		return mPreview.getFrameCounter();
	}

	public int getFPS() {
		return mPreview.getFPS();
	}


}
