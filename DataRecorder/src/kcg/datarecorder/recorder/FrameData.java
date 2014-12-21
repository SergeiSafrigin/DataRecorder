package kcg.datarecorder.recorder;

import java.io.Serializable;
import java.util.ArrayList;

import kcg.datarecorder.location.Point3d;

public class FrameData implements Serializable {
	private static final long serialVersionUID = 0xc271f1b728e89fb2L;
	private byte frame[];
	private Point3d location;
	private ArrayList<WifiData> wifiDataList;
	private float pitch;
	private float pressure;
	private float roll;
	private long time;
	private float yaw;

	public FrameData(long time, byte[] frame, Point3d location, ArrayList<WifiData> wifiDataList, float yaw, float pitch, float roll, float pressure) {
		this.time = time;
		this.frame = frame;
		this.location = location;
		this.wifiDataList = wifiDataList;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
		this.pressure = pressure;
	}

	public byte[] getFrame() {
		return frame;
	}

	public Point3d getLocation() {
		return location;
	}
	
	public ArrayList<WifiData> getWifiDataList() {
		return wifiDataList;
	}

	public float getPitch() {
		return pitch;
	}

	public float getPressure() {
		return pressure;
	}

	public float getRoll() {
		return roll;
	}

	public long getTime() {
		return time;
	}

	public float getYaw() {
		return yaw;
	}
}
