package kcg.datarecorder.main;

import kcg.datarecorder.camera.CameraController;
import kcg.datarecorder.location.LocationController;
import kcg.datarecorder.recorder.RecorderController;
import kcg.datarecorder.sensors.SensorsController;
import kcg.datarecorder.sensors.WifiScanner;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	
	private CameraController cameraController;
	private Config config;
	private LocationController locationController;
	private RelativeLayout mainLayout;
	private RecorderController recorderController;
	private boolean running;
	private SensorsController sensorsController;
	private WifiScanner wifiScanner;
	private Button startButton;
	private Button stopButton;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		init(this);

		mainLayout = (RelativeLayout)(this.findViewById(R.id.mainLayout));
		mainLayout.addView(this.cameraController.getCameraPreview());
		mainLayout.addView(this.cameraController.getCameraView(), 0);
	}

	private void init(Context context) {
		this.config = new Config();
		updatePreferences();
		cameraController = new CameraController(context, config);
		sensorsController = new SensorsController(context, config);
		locationController = new LocationController(context, config);
		wifiScanner = new WifiScanner(context, config);
		recorderController = new RecorderController(context, config, R.raw.recorded, cameraController, sensorsController, locationController, wifiScanner);
		startButton = (Button)(this.findViewById(R.id.startButton));
		stopButton = (Button)(this.findViewById(R.id.stopButton));

		startButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View view) {
				recorderController.startRecording();
				running = true;
			}
		});

		this.stopButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View view) {
				recorderController.stopRecording();
				running = false;
			}
		});
	}

	private void updatePreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String device = sharedPreferences.getString("device", "phone");
		String camera = sharedPreferences.getString("camera", "back");
		String[] size = sharedPreferences.getString("screenSize", "320 x 240").split(" x ");
		boolean continueosScaning = sharedPreferences.getBoolean("continueosScaning", false);
		
		config.setPreviewWidth(Integer.parseInt(size[0]));
		config.setPreviewHeight(Integer.parseInt(size[1]));

		switch (device) {
		case "phone":
			config.setDevice(Config.Device.PHONE);
			break;

		case "glasses":
			config.setDevice(Config.Device.GLASSES);
			break;
		}

		switch (camera) {
		case "back":
			config.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
			config.setCamera(Config.Camera.BACK);
			break;

		case "front": 
			config.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
			config.setCamera(Config.Camera.FRONT);
		}
		
		config.setContinueosScaning(continueosScaning);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() != R.id.preferences)
			return false;
		Intent intent;
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			intent = new Intent(this, Preferences.class);
		else
			intent = new Intent(this, PreferencesForNewVersions.class);
		startActivity(intent);
		return false;
	}

	public void onPause() {
		super.onPause();
		if (!(this.running)) return;
		this.recorderController.stopRecording();
		this.running = false;
	}

	public void onResume() {
		super.onResume();
		this.updatePreferences();
		this.recorderController.onResume();
	}

}

