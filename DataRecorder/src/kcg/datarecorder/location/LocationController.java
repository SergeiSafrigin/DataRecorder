package kcg.datarecorder.location;

import android.content.Context;
import kcg.datarecorder.main.Config;

public class LocationController implements LocationDataUpdater {
	private Point3d lastLocation;
	private LocationScanner locationScanner;

	public LocationController(Context context, Config config) {
		locationScanner = new LocationScanner(context, config, this);
	}

	public void addLocation(Point3d point3d) {
		lastLocation = point3d;
	}

	public Point3d getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(Point3d point3d) {
		lastLocation = point3d;
	}

	public void startGPS() {
		locationScanner.onResume();
	}

	public void stopGPS() {
		locationScanner.onPause();
	}
}
