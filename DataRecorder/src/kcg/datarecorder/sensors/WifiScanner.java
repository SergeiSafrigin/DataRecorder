package kcg.datarecorder.sensors;

import java.util.ArrayList;
import java.util.List;

import kcg.datarecorder.main.Config;
import kcg.datarecorder.recorder.WifiData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiScanner {

	private Object mLock;
	private Config mConfig;
	private Context mContext;
	private boolean mbContinuousScan;
	private boolean mbNewResults;
	private int miNumScansCounter;
	private WifiManager mWifiManager;
	private List<ScanResult> mLastScanResults;

	public WifiScanner(Context context, Config config) {
		mContext = context;
		mConfig = config;
		
		mLock = new Object();
		mbContinuousScan = false;
		mbNewResults = false;
		miNumScansCounter = 0;

		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	private BroadcastReceiver wifiBroadCastReceiver = new BroadcastReceiver() {
		public void onReceive(Context c, Intent i) {

			List<ScanResult> results = mWifiManager.getScanResults();
			
			synchronized(mLock) {
				mbNewResults = true;
				mLastScanResults = results;
				miNumScansCounter++;
			}

			if(mbContinuousScan) {
				mWifiManager.startScan();
			}
		}
	};

	public void startWifiListener() {
		//mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled() == false) {
			mWifiManager.setWifiEnabled(true);

			//TODO - maybe need to add timer just in case
			while (!mWifiManager.isWifiEnabled())
				;
		}

		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mContext.registerReceiver(wifiBroadCastReceiver, i);
	}

	public void stopWifiListener() {
		mContext.unregisterReceiver(wifiBroadCastReceiver);
		synchronized(mLock) {
			mbContinuousScan = false;
			mbNewResults = false;
			miNumScansCounter = 0;
			mLastScanResults.clear();
		}
	}

	private void internalStartScan() {

		//TODO - need to add watchdog timer here!!!
		synchronized(mLock) {
			mbNewResults = false;
		}
		mWifiManager.startScan();
	}
	
	public void startContinuousScan() {
		mbContinuousScan = true;
		internalStartScan();
	}
	
	public void startSingleScan() {
		internalStartScan();
	}

	/*public void scan() {
		mWifiManager.startScan();
	}*/
	
	public boolean newResultsExists() {
		synchronized(mLock) {	
			return mbNewResults;
		}
	}
	
	public ArrayList<WifiData> getLastResults() {
		List<ScanResult> scanResults;
		//ArrayList<WifiData> wifiResults = new ArrayList<WifiData>();
		
		synchronized(mLock) {
			if(!mbNewResults) {
				//return wifiResults;
				return null;
			}		
			scanResults = mLastScanResults;
			mbNewResults = false;
		}
		
		ArrayList<WifiData> wifiResults = new ArrayList<WifiData>();
		for (ScanResult signal : scanResults) {
			wifiResults.add(new WifiData(signal.BSSID, signal.capabilities, signal.frequency, signal.level, signal.SSID));
		}
		
		return new ArrayList<WifiData>(wifiResults);
	}
	
	public int getNumScans() {
		return miNumScansCounter;
	}
}