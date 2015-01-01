package kcg.datarecorder.recorder;

import java.io.Serializable;

public class WifiData implements Serializable {
	private static final long serialVersionUID = -3532334535234824913L;
	
	private String macAddress; //string representation of the access point's address;
	private String capabilities; //Describes the authentication, key management, and encryption schemes supported by the access point.
	private int frequency; //The frequency in MHz of the channel over which the client is communicating with the access point.
	private int level; //The detected signal level in dBm. At least those are the units used by the TI driver.
	private String SSID; //The network name.
	
	/**
	 * 
	 * @param macAddress The address of the access point.
	 * @param capabilities Describes the authentication, key management, and encryption schemes supported by the access point.
	 * @param frequency The frequency in MHz of the channel over which the client is communicating with the access point.
	 * @param level The detected signal level in dBm. At least those are the units used by the TI driver.
	 * @param SSID The network name.
	 */
	public WifiData(String macAddress, String capabilities, int frequency, int level, String SSID) {
		this.macAddress = macAddress;
		this.capabilities = capabilities;
		this.frequency = frequency;
		this.level = level;
		this.SSID = SSID;
	}
}
