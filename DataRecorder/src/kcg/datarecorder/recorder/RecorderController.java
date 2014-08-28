package kcg.datarecorder.recorder;

import android.content.Context;
import kcg.datarecorder.camera.CameraController;
import kcg.datarecorder.location.LocationController;
import kcg.datarecorder.main.Config;
import kcg.datarecorder.sensors.SensorsController;

public class RecorderController implements OnFrameTrigger {
	private CameraController cameraController;
	private LocationController locationController;
	private Recorder recorder;
	private boolean recording;
	private SensorsController sensorsController;

	public RecorderController(Context context, Config config, int finishSoundResource,
			CameraController cameracontroller,
			SensorsController sensorscontroller,
			LocationController locationcontroller) {
		
		cameraController = cameracontroller;
		sensorsController = sensorscontroller;
		locationController = locationcontroller;
		cameracontroller.registerOnFrameListener(this);
		recorder = new Recorder(context, config, finishSoundResource);
	}

	public void onFrame() {
		if (recording) {
			recorder.addData(cameraController.getLastFrameTime(),
					cameraController.getLastFrame(),
					locationController.getLastLocation(),
					sensorsController.getYaw(),
					sensorsController.getPitch(),
					sensorsController.getRoll(),
					sensorsController.getPressure());
		}
	}

	public void onResume() {
		cameraController.onResume();
	}

	public void startRecording() {
		if (!recording) {
			recording = true;
			cameraController.registerOnFrameListener(this);
			sensorsController.startSensorScanner();
			locationController.startGPS();
			recorder.start();
		}
	}

	public void stopRecording() {
		if (recording) {
			recording = false;
			cameraController.unregisterOnFrameListener();
			sensorsController.stopSensorScanner();
			locationController.stopGPS();
			recorder.stop();
		}
	}
}
