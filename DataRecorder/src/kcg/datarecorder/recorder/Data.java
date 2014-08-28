package kcg.datarecorder.recorder;

import java.io.Serializable;
import java.util.ArrayList;

import kcg.datarecorder.main.Config;

public class Data implements Serializable {

	private static final long serialVersionUID = 0x870f11a19c0add2cL;
	private Config config;
	private ArrayList<FrameData> data;
	private long endTime;
	private long startTime;

	public Data(Config config) {
		data = new ArrayList<FrameData>();
		this.config = config;
	}

	public void addFrame(FrameData framedata) {
		data.add(framedata);
	}

	public void clear() {
		data.clear();
	}

	public Config getConfig() {
		return config;
	}

	public ArrayList<FrameData> getData() {
		return data;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setEndTime(long time) {
		endTime = time;
	}

	public void setStartTime(long time) {
		startTime = time;
	}

	public int size() {
		return data.size();
	}
}
