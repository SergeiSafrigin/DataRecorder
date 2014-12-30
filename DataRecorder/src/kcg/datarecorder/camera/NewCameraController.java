package kcg.datarecorder.camera;

import kcg.datarecorder.main.Config;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

public class NewCameraController {

	private Context mContext;
	private Config mConfig;
	private Camera mCamera;
	private NewCameraPreview mPreview;
	private PhotoHandler mPhotoHandler;
	
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
				
		mPreview = new NewCameraPreview(mContext, mCamera);
		
        //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //preview.addView(mPreview);
        mPhotoHandler = new PhotoHandler(context);
    }
	
	public NewCameraPreview getCameraPreview() {
		return mPreview;
	}

}
