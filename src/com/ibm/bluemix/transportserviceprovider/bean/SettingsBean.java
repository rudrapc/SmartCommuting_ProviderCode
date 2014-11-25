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
		return mUrl.trim();
	}

	public String getDeviceName() {
		return mDeviceName.trim();
	}

	public String getDeviceId() {
		return mDeviceId.trim();
	}

	public String getLat() {
		return mLat.trim();
	}

	public String getLong() {
		return mLong.trim();
	}

	public String getDirection() {
		return mDirection.trim();
	}

	public String getDeviceType() {
		return mDeviceType.trim();
	}

	public String toString() {
		String lObject = "Url:[" + mUrl + "],Device name:[ " + mDeviceName
				+ "],Device id:[ " + mDeviceId + "], Latitude:[" + mLat
				+ "], Longitude:[" + mLong + "], Direction:[" + mDirection
				+ "],Device type:[" + mDeviceType + "]";
		return lObject;
	}
}
