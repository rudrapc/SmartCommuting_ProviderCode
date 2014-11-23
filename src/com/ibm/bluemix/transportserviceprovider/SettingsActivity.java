package com.ibm.bluemix.transportserviceprovider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private SharedPreferences prefs_cust = null;
	EditText mServiceUrlView = null;
	EditText mDeviceIdView = null;
	EditText mTimeIntervalView = null;
	EditText mDeviceNameView = null;
	EditText mDirectionView = null;

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		
		super.onCreate(pSavedInstanceState);
		
		setContentView(R.layout.activity_settings);
		
		prefs_cust = getSharedPreferences("prefs-myabc", Activity.MODE_PRIVATE);
		mServiceUrlView = (EditText) findViewById(R.id.service_url);
		mServiceUrlView.setText(prefs_cust.getString(
				TSPMainActivity.SERVICE_URL, null));
		mDeviceIdView = (EditText) findViewById(R.id.device_id);
		mDeviceIdView.setText(prefs_cust.getString(TSPMainActivity.DEVICE_ID,
				null));
		mTimeIntervalView = (EditText) findViewById(R.id.time_interval);
		mTimeIntervalView.setText(prefs_cust.getString(
				TSPMainActivity.TIME_INTERVAL, null));
		mDeviceNameView = (EditText) findViewById(R.id.device_name);
		mDeviceNameView.setText(prefs_cust.getString(
				TSPMainActivity.DEVICE_NAME, null));
		mDirectionView = (EditText) findViewById(R.id.direction_name);
		mDirectionView.setText(prefs_cust.getString(
				TSPMainActivity.DIRECTION_NAME, null));
	}

	// Save
	public void save(View pView) {
		String lServiceUrl = null;
		if (null != mServiceUrlView && null != mServiceUrlView.getText()) {
			lServiceUrl = mServiceUrlView.getText().toString();
		}
		String lDeviceId = null;
		if (null != mDeviceIdView && null != mDeviceIdView.getText()) {
			lDeviceId = mDeviceIdView.getText().toString();
		}
		String lTimeInterval = null;
		if (null != mTimeIntervalView && null != mTimeIntervalView.getText()) {
			lTimeInterval = mTimeIntervalView.getText().toString();
		}
		String lDeviceName = null;
		if (null != mDeviceNameView && null != mDeviceNameView.getText()) {
			lDeviceName = mDeviceNameView.getText().toString();
		}
		String lDirectionName = null;
		if (null != mDirectionView && null != mDirectionView.getText()) {
			lDirectionName = mDirectionView.getText().toString();
		}
		Toast.makeText(
				this,
				"Saved Config Data: ServiceUrl:[" + lServiceUrl + "], DeviceId:["
						+ lDeviceId + "], TimeInterval:[" + lTimeInterval
						+ "], DeviceName:[" + lDeviceName
						+ "], Direction:[" + lDirectionName + "]",
				Toast.LENGTH_SHORT).show();
		
		prefs_cust.edit().putString(TSPMainActivity.SERVICE_URL, lServiceUrl)
				.commit();
		prefs_cust.edit().putString(TSPMainActivity.DEVICE_ID, lDeviceId)
				.commit();
		prefs_cust.edit()
				.putString(TSPMainActivity.TIME_INTERVAL, lTimeInterval)
				.commit();
		prefs_cust.edit().putString(TSPMainActivity.DEVICE_NAME, lDeviceName)
				.commit();
		prefs_cust.edit()
				.putString(TSPMainActivity.DIRECTION_NAME, lDirectionName)
				.commit();
	}

}
