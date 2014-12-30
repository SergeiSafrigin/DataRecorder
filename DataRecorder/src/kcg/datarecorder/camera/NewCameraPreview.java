package kcg.datarecorder.camera;

import java.io.IOException;
import java.util.List;

import kcg.datarecorder.main.Config;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NewCameraPreview extends SurfaceView  implements SurfaceHolder.Callback, PreviewCallback {

	private Context mContext;
	
	private Camera mCamera;	
	private SurfaceHolder mHolder;
	
	private int miFrameCounter = 0;
	private long mlPrevTimeMillis = 0;
	
	public NewCameraPreview(Context context, Camera camera) {
		super(context);
	
		mContext = context;
		mCamera = camera;
		
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private void setPreviewResolution() {
	
		android.hardware.Camera.Parameters parameters = mCamera.getParameters();
		List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
		Config.SCREEN_SIZES = sizes;
		//Size size = sizes.get(closest(sizes, config.getPreviewWidth(), config.getPreviewHeight()));
		parameters.setPreviewSize(320, 240);
		//config.setPreviewHeight(size.height);
		//config.setPreviewWidth(size.width);
		//setCameraViewAngles();
		mCamera.setParameters(parameters);
		//mCamera.setPreviewCallback(this);
		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//surfaceDestroyed(mHolder);
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
    		mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            //setPreviewResolution();
            //mCamera.startPreview();
        } catch (IOException e) {
            Log.d("PRE", "Error setting camera preview: " + e.getMessage());
            mCamera.release();
        }		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        mCamera.setPreviewCallback(this);
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            setPreviewResolution();
            //mCamera.startPreview();

        } catch (Exception e){
            Log.d("PRE", "Error starting camera preview: " + e.getMessage());
        }		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if(mlPrevTimeMillis == 0) {
			mlPrevTimeMillis = System.currentTimeMillis();
			return;
		}
		
		miFrameCounter++;
		if((miFrameCounter % 100) == 0) {
			long curTime = System.currentTimeMillis();
			
			Log.d("CAMPREV", "Info: frameCounter=" + miFrameCounter + ", time=" + curTime + " FPS=" + (int)(100/((curTime - mlPrevTimeMillis) / 1000.0)));
			mlPrevTimeMillis = curTime;
		}
	}

}
