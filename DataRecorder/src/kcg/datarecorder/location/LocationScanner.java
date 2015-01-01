package kcg.datarecorder.location;

import kcg.datarecorder.main.Config;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationScanner {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1L;
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000L;
	private static final String TAG = "Location Scanner";
	private Config config;
	private Context context;
	private LocationManager locationManager;
	private LocationDataUpdater locationUpdater;

	public LocationScanner(Context context, Config config, LocationDataUpdater updater) {
		this.context = context;
		this.config = config;
		locationUpdater = updater;
	}

	public void startLocationManager() {
		locationManager = (LocationManager)context.getSystemService("location");
		if (!locationManager.isProviderEnabled("gps")) {
			Toast.makeText(context, "Please Enable GPS", Toast.LENGTH_SHORT).show();
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
		if (config.getDevice() == Config.DeviceType.PHONE) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,1, locationListener);
		}
	}
	
	private LocationListener locationListener = new LocationListener() {
		@Override public void onStatusChanged(String s, int i, Bundle b) {}
		@Override public void onProviderDisabled(String s) {}
		@Override public void onProviderEnabled(String s) {}

		@Override
		public void onLocationChanged(Location location) {
			locationUpdater.addLocation(new Point3d(location.getLatitude(), location.getLongitude(), location.getAltitude()));
		}
	};

	public void onPause() {
		locationManager.removeUpdates(locationListener);
		locationManager = null;
	}

	public void onResume() {
		startLocationManager();
	}

}
