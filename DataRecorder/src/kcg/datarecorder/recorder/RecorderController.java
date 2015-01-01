package kcg.datarecorder.recorder;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import kcg.datarecorder.camera.CameraController;
import kcg.datarecorder.camera.NewCameraController;
import kcg.datarecorder.location.LocationController;
import kcg.datarecorder.main.Config;
import kcg.datarecorder.sensors.SensorsController;
import kcg.datarecorder.sensors.WifiScanner;

public class RecorderController implements OnFrameTrigger {
	//private CameraController cameraController;
	private NewCameraController mNewCameraController;
	private LocationController mLocationController;
	private WifiScanner mWifiScanner;
	private Recorder mRecorder;
	private boolean mIsRecording;
	private SensorsController mSensorsController;

	public RecorderController(Context context, Config config, int finishSoundResource,
			NewCameraController newCameraController,
			SensorsController sensorsController,
			LocationController locationController,
			WifiScanner wifiScanner) {

		mIsRecording = false;
		
		mNewCameraController = newCameraController;
		mSensorsController = sensorsController;
		mLocationController = locationController;
		mWifiScanner = wifiScanner;
		
		mNewCameraController.registerOnFrameListener(this);
		mRecorder = new Recorder(context, config, finishSoundResource);
	}

	public void onFrame() {
		if (mIsRecording) {
			mRecorder.addData(mNewCameraController.getLastFrameTime(),
					mNewCameraController.getLastFrame(),
					mLocationController.getLastLocation(),
					mWifiScanner.getLastResults(),
					mSensorsController.getYaw(),
					mSensorsController.getPitch(),
					mSensorsController.getRoll(),
					mSensorsController.getPressure());
		}
	}

	public void onResume() {
		//cameraController.onResume();
	}

	public void startRecording() {
		if (!mIsRecording) {
			mIsRecording = true;
			mNewCameraController.registerOnFrameListener(this);
			mSensorsController.startSensorScanner();
			mLocationController.startGPS();
			mWifiScanner.startWifiListener();
			mWifiScanner.startContinuousScan();
			mRecorder.start();
		}
	}

	public void stopRecording() {
		if (mIsRecording) {
			mIsRecording = false;
			mNewCameraController.unregisterOnFrameListener();
			int totalFrames = mNewCameraController.getFrameCounter();
			mSensorsController.stopSensorScanner();
			mLocationController.stopGPS();
			mWifiScanner.stopWifiListener();
			mRecorder.stop(totalFrames);
		}
	}
}
