package kcg.datarecorder.sensors;

import kcg.datarecorder.main.Config;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorsScanner {
	private static final String TAG = "Sensor Scanner";
	private float angles[];
	private Config config;
	private Context context;
	private SensorsDataUpdater listener;
	private float rotationMatrix[];
	private Sensor sensor;
	private SensorManager sensorManager;

	public SensorsScanner(Context context, Config config, SensorsDataUpdater updater){
		this.context = context;
		this.config = config;
		listener = updater;
		rotationMatrix = new float[16];
		angles = new float[3];
	}

	public void startSensorManager(){
		sensorManager = (SensorManager)context.getSystemService("sensor");
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		if (sensor != null){
			sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		if (sensor != null){
			sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	private SensorEventListener sensorListener = new SensorEventListener() {
		@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
				SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
				if (config.getDevice() == kcg.datarecorder.main.Config.Device.GLASSES){
					SensorManager.remapCoordinateSystem(rotationMatrix, 2, 129, rotationMatrix);
				}
				SensorManager.getOrientation(rotationMatrix, angles);
				listener.addOrientation(getYaw(), getPitch(), getRoll());
			}
			if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
				listener.addPressure(event.values[0]);
			}
		}
	};
	
	public float getPitch(){
		if (config.getDevice() == kcg.datarecorder.main.Config.Device.PHONE){
			return (180 / 3.14159265f) * angles[2];
		} else {
			return -((180 / 3.14159265f) * angles[2]);
		}
	}

	public float getRoll(){
		return angles[1] * (180 / 3.14159265f);
	}

	public float getYaw()
	{
		float angle;
		if (config.getDevice() == kcg.datarecorder.main.Config.Device.PHONE)
			angle = (90F + (180 / 3.14159265f) * angles[0]) % 360F;
		else
			angle= (180 / 3.14159265f) * angles[0] - 90F;
		
		if (angle < 0)
			angle += 360;
		return angle;
	}

	public void onPause() {
		if (sensorManager != null){
			sensorManager.unregisterListener(sensorListener);
			sensorManager = null;
		}
	}

	public void onResume(){
		startSensorManager();
	}
}
