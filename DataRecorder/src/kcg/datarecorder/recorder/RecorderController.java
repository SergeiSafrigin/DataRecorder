package kcg.datarecorder.recorder;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import kcg.datarecorder.camera.CameraController;
import kcg.datarecorder.location.LocationController;
import kcg.datarecorder.main.Config;
import kcg.datarecorder.sensors.SensorsController;
import kcg.datarecorder.sensors.WifiScanner;

public class RecorderController implements OnFrameTrigger {
	private CameraController cameraController;
	private LocationController locationController;
	private WifiScanner wifiScanner;
	private Recorder recorder;
	private boolean recording;
	private SensorsController sensorsController;

	public RecorderController(Context context, Config config, int finishSoundResource,
			CameraController cameraController,
			SensorsController sensorsController,
			LocationController locationController,
			WifiScanner wifiScanner) {
		
		this.cameraController = cameraController;
		this.sensorsController = sensorsController;
		this.locationController = locationController;
		this.wifiScanner = wifiScanner;
		
		cameraController.registerOnFrameListener(this);
		recorder = new Recorder(context, config, finishSoundResource);
	}

	public void onFrame() {
		if (recording) {
			recorder.addData(cameraController.getLastFrameTime(),
					cameraController.getLastFrame(),
					locationController.getLastLocation(),
					wifiScanner.getWifiDataList(),
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
			wifiScanner.startWifiLestener();
			recorder.start();
		}
	}

	public void stopRecording() {
		if (recording) {
			recording = false;
			cameraController.unregisterOnFrameListener();
			sensorsController.stopSensorScanner();
			locationController.stopGPS();
			wifiScanner.stopWifiListener();
			recorder.stop();
		}
	}
}
