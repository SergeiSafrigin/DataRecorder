package kcg.datarecorder.main;

import java.util.Timer;
import java.util.TimerTask;

import kcg.datarecorder.camera.CameraController;
import kcg.datarecorder.camera.NewCameraController;
import kcg.datarecorder.location.LocationController;
import kcg.datarecorder.recorder.RecorderController;
import kcg.datarecorder.sensors.SensorsController;
import kcg.datarecorder.sensors.WifiScanner;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private final int TIME_1_SECOND = 1000;//1000 millis
	
	private NewCameraController mNewCameraController;
	private Config config;
	private LocationController locationController;
	private RelativeLayout mainLayout;
	private RecorderController recorderController;
	private boolean running;
	private SensorsController sensorsController;
	private WifiScanner mWifiScanner;
	private Button startButton;
	private Button stopButton;
	private TextView mInfo;
	private Handler mTimerHandler;
	private Timer mTimer;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		init(this);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mNewCameraController.getCameraPreview());
        mInfo = (TextView)(this.findViewById(R.id.basicInfo));
        mInfo.setTextColor(Color.RED);
		//mainLayout = (RelativeLayout)(this.findViewById(R.id.mainLayout));
		//mainLayout.addView(mNewCameraController.getCameraPreview());		
		//mainLayout.addView(this.cameraController.getCameraPreview());
		//mainLayout.addView(this.cameraController.getCameraView(), 0);
	}

	private void init(Context context) {
		this.config = new Config();
		updatePreferences();
		
    	mTimerHandler = new Handler();

		mNewCameraController = new NewCameraController(context, config);
		sensorsController = new SensorsController(context, config);
		locationController = new LocationController(context, config);
		mWifiScanner = new WifiScanner(context, config);

		recorderController = new RecorderController(context, config, R.raw.recorded, mNewCameraController, sensorsController, locationController, mWifiScanner);
		
		startButton = (Button)(this.findViewById(R.id.startButton));
		stopButton = (Button)(this.findViewById(R.id.stopButton));
		
		stopButton.setEnabled(false);

		startButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View view) {
				stopButton.setEnabled(true);
				startButton.setEnabled(false);

				mTimer = new Timer();		
				mTimer.scheduleAtFixedRate(new timerExpiredTask(), TIME_1_SECOND, TIME_1_SECOND);

				recorderController.startRecording();
				running = true;
			}
		});

		this.stopButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View view) {
				stopButton.setEnabled(false);
				startButton.setEnabled(true);

				mTimer.cancel();
				mTimer.purge();

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
			config.setDeviceType(Config.DeviceType.PHONE);
			break;

		case "glasses":
			config.setDeviceType(Config.DeviceType.GLASSES);
			break;
		}

		switch (camera) {
		case "back":
			config.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
			config.setCameraType(Config.CameraType.BACK);
			break;

		case "front": 
			config.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
			config.setCameraType(Config.CameraType.FRONT);
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
		
		if (!running) 
			return;
		recorderController.stopRecording();
		running = false;
	}

	public void onResume() {
		super.onResume();
		
		updatePreferences();
		recorderController.onResume();
	}

	class timerExpiredTask extends TimerTask {
        public void run() {
        	mTimerHandler.post(new Runnable() {
        		public void run() {
        			int frames = mNewCameraController.getFrameCounter();
        			int fps = mNewCameraController.getFPS();
        			int wifiScans = mWifiScanner.getNumScans();
        			mInfo.setText("FPS=" + fps + " [" + frames + "]" + " w=" + wifiScans);
        		}
        	});
        }
	}

}

