package com.ibm.bluemix.transportserviceprovider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.bluemix.transportserviceprovider.bean.SettingsBean;

public class TSPMainActivity extends Activity implements LocationListener {

	private LocationManager mLocationManager;
	private SharedPreferences prefs_cust = null;
	public static final String SERVICE_URL = "service_url";
	public static final String DEVICE_ID = "device_id";
	public static final String TIME_INTERVAL = "time_interval";
	public static final String DEVICE_NAME = "device_name";
	public static final String DIRECTION_NAME = "direction_name";

	public static final String STATUS_MSG_OFF = "Transmission is OFF";
	public static final String STATUS_MSG_ON = "Transmission is ON";

	private String mServiceUrl = "";
	private String mDeviceId = "";
	private String mDeviceName = "";
	private String mDirectionName = "";

	private String mTimeInterval = "";
	private TextView mTimeLabel = null;
	private TextView mLatLong = null;
	private TextView mInfoMsg = null;
	private TextView mStatusLine = null;

	private Button mButStart = null;
	private Button mButStop = null;

	private String mLong = "";
	private String mLat = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		prefs_cust = getSharedPreferences("prefs-myabc", Activity.MODE_PRIVATE);
		readConfig();
		mTimeLabel = (TextView) findViewById(R.id.time);
		mLatLong = (TextView) findViewById(R.id.lat_long);
		mInfoMsg = (TextView) findViewById(R.id.info_msg);
		mStatusLine = (TextView) findViewById(R.id.statusline);

		mButStart = (Button) findViewById(R.id.button_start);
		mButStop = (Button) findViewById(R.id.button_stop);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tspmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location pLocation) {

		mLat = String.valueOf(pLocation.getLatitude());
		mLong = String.valueOf(pLocation.getLongitude());

		// FIXME:: Read device name and direction from ui
		// TODO:: Device type will be enhanced later
		SettingsBean lSetting = new SettingsBean(mServiceUrl, mDeviceId,
				mDeviceName, mLat, mLong, mDirectionName, "");
		String lCompleteUrl = getCompleteUrl(lSetting).toString();
		// Toast.makeText(getBaseContext(), "lCompleteUrl:[ " + lCompleteUrl,
		// Toast.LENGTH_LONG).show();

		new Connection().execute(new String[] { lCompleteUrl,
				lSetting.getLat(), lSetting.getLong() });
	}

	@Override
	public void onProviderDisabled(String pProvider) {

		Toast.makeText(getBaseContext(), "Gps is turned off ",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String pProvider) {

		Toast.makeText(getBaseContext(), "Gps is turned on ", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		readConfig();
	}

	private void readConfig() {
		mServiceUrl = prefs_cust.getString(SERVICE_URL, null);
		mDeviceId = prefs_cust.getString(DEVICE_ID, null);
		mTimeInterval = prefs_cust.getString(TIME_INTERVAL, null);
		mDeviceName = prefs_cust.getString(DEVICE_NAME, null);
		mDirectionName = prefs_cust.getString(DIRECTION_NAME, null);
	}

	// Start the service
	public void startNewService(View pView) {

		if (null == mServiceUrl || null == mDeviceId || null == mTimeInterval
				|| null == mDeviceName || null == mDirectionName) {
			Toast.makeText(
					this,
					"One or more config (Service URL / Device Id / TimeInterval / Device Name / Direction) missing. Please set all config first.",
					Toast.LENGTH_LONG).show();

		} else if (!isNumeric(mTimeInterval)) {
			Toast.makeText(this,
					"Time interval should be seconds (as an integer)",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					this,
					"Retrieved Config: Service URL[" + mServiceUrl
							+ "], DeviceId:[" + mDeviceId
							+ "], TimeInterval :[" + mTimeInterval + "]",
					Toast.LENGTH_SHORT).show();

			setStatusLine(true);

			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					Integer.parseInt(mTimeInterval) * 1000, // convert to ms
					0, this);
		}

	}

	private void setStatusLine(boolean state) {

		if (state == true) {
			mStatusLine.setText(STATUS_MSG_ON);
			mStatusLine.setBackgroundColor(Color.GREEN);
			mButStart.setEnabled(false);
			mButStop.setEnabled(true);
		} else {
			mStatusLine.setText(STATUS_MSG_OFF);
			mStatusLine.setBackgroundColor(Color.RED);
			mButStart.setEnabled(true);
			mButStop.setEnabled(false);
		}
	}

	public boolean isNumeric(String str) {
		return str.matches("-?\\d+(.\\d+)?");
	}

	// Stop the service
	public void stopNewService(View pView) {

		// super.onDestroy();
		mLocationManager.removeUpdates(this);

		setStatusLine(false);

		Toast.makeText(this, "Stopped Transmission", Toast.LENGTH_LONG).show();
	}

	private StringBuilder getCompleteUrl(SettingsBean pSetting) {
		StringBuilder lStringBuilder = new StringBuilder(pSetting.getUrl());
		lStringBuilder.append("?");
		lStringBuilder.append("pNm=");
		lStringBuilder.append(pSetting.getDeviceId());
		lStringBuilder.append("&");
		lStringBuilder.append("pId=");
		lStringBuilder.append(pSetting.getDeviceName());
		lStringBuilder.append("&");
		lStringBuilder.append("pLngtd=");
		lStringBuilder.append(pSetting.getLong());
		lStringBuilder.append("&");
		lStringBuilder.append("pLttd=");
		lStringBuilder.append(pSetting.getLat());
		lStringBuilder.append("&");
		lStringBuilder.append("pTrp=");
		lStringBuilder.append(pSetting.getDirection());
		lStringBuilder.append("&");
		lStringBuilder.append("pTm=");
		lStringBuilder.append(pSetting.getDeviceType());
		lStringBuilder.append("Oprtn=");
		lStringBuilder.append("Update");
		return lStringBuilder;
	}

	private class Connection extends AsyncTask<String, Void, String> {

		int mStatusCode = 0;
		String mLat = "";
		String mLong = "";

		@Override
		protected String doInBackground(String... arg0) {
			HttpResponse lResponse = null;
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet lRequest = new HttpGet(arg0[0]);
				mLat = arg0[1];
				mLong = arg0[2];
				lResponse = client.execute(lRequest);
			} catch (ClientProtocolException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
			} catch (IOException e) {
				Log.d("HTTPCLIENT", e.getLocalizedMessage());
			}
			if (null != lResponse && null != lResponse.getStatusLine()) {
				mStatusCode = lResponse.getStatusLine().getStatusCode();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String pResult) {

			SimpleDateFormat lSdf = new SimpleDateFormat("HH:mm dd/MMM/yy");
			String lCurrentDateandTime = lSdf.format(new Date());

			if (200 == mStatusCode) {

				// clear the last error message
				mInfoMsg.setText("");

				// Update display
				mTimeLabel.setText(lCurrentDateandTime);
				mLatLong.setText("Latitude:" + mLat + "  Longitude:" + mLong);
				Toast.makeText(getBaseContext(),
						mLatLong.getText() + " transmitted", Toast.LENGTH_LONG)
						.show();
			} else {
				mInfoMsg.setText("Error in transmission at: "
						+ lCurrentDateandTime);
			}
		}
	}

}
