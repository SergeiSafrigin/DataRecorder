package kcg.datarecorder.recorder;

import java.util.ArrayList;

import kcg.datarecorder.location.Point3d;

public interface RecorderDataUpdater {

	public void addData(long time, byte[] frame, Point3d location, ArrayList<WifiData> wifiDataList, float yaw, float pitch, float roll, float pressure);
}
