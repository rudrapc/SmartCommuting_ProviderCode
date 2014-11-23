package com.ibm.bluemix.transportserviceprovider.bean;

public class SettingsBean {

	String mUrl = "";
	String mDeviceName = "";
	String mDeviceId = "";
	String mLat = "";
	String mLong = "";
	String mDirection = "";
	String mDeviceType = "";

	public SettingsBean(String pUrl, String pDeviceName, String pDeviceId,
			String pLat, String pLong, String pDirection, String pDeviceType) {
		mUrl = pUrl;
		mDeviceName = pDeviceName;
		mDeviceId = pDeviceId;
		mLat = pLat;
		mLong = pLong;
		mDirection = pDirection;
		mDeviceType = pDeviceType;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getDeviceName() {
		return mDeviceName;
	}

	public String getDeviceId() {
		return mDeviceId;
	}

	public String getLat() {
		return mLat;
	}

	public String getLong() {
		return mLong;
	}

	public String getDirection() {
		return mDirection;
	}

	public String getDeviceType() {
		return mDeviceType;
	}

	public String toString() {
		String lObject = "Url:[" + mUrl + "],Device name:[ " + mDeviceName
				+ "],Device id:[ " + mDeviceId + "], Latitude:[" + mLat
				+ "], Longitude:[" + mLong + "], Direction:[" + mDirection
				+ "],Device type:[" + mDeviceType + "]";
		return lObject;
	}
}

