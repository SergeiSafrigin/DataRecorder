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

	private WifiManager wifi;
	private Config config;
	private Context context;
	private ArrayList<WifiData> wifiDataList;

	public WifiScanner(Context context, Config config) {
		this.context = context;
		this.config = config;
		
		wifiDataList = new ArrayList<WifiData>();
	}

	private BroadcastReceiver wifiBroadCastReceiver = new BroadcastReceiver() {
		public void onReceive(Context c, Intent i) {
			wifiDataList.clear();
			List<ScanResult> signals = wifi.getScanResults();
			
			for (ScanResult signal : signals) {
				wifiDataList.add(new WifiData(signal.BSSID, signal.capabilities, signal.frequency, signal.level, signal.SSID));
			}

			if (config.isContinueosScaning())
				wifi.startScan();
		}
	};

	public void startWifiLestener() {
		wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled() == false) {
			wifi.setWifiEnabled(true);
		}

		while (!wifi.isWifiEnabled());

		scan();

		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		context.registerReceiver(wifiBroadCastReceiver, i);

	}

	public void stopWifiListener() {
		context.unregisterReceiver(wifiBroadCastReceiver);
	}

	public void scan() {
		wifi.startScan();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<WifiData> getWifiDataList() {
		return (ArrayList<WifiData>)wifiDataList.clone();
	}
}