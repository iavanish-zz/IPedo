package com.example.ipedo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

public class RecordLocation extends Activity {

	private TextView success;
	private TextView latitude;
	private TextView longitude;
	      
	private LocationManager locationManager;
	private String provider;
	private MyLocationListener mylistener;
	private Criteria criteria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_location);
		
		success = (TextView) findViewById(R.id.success);
		latitude = (TextView) findViewById(R.id.lat);
		longitude = (TextView) findViewById(R.id.lon);

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

		criteria.setCostAllowed(false);
		// get the best provider depending on the criteria
		
		provider = locationManager.getBestProvider(criteria, false);


		// the last known location of this provider
		Location location = locationManager.getLastKnownLocation(provider);

		mylistener = new MyLocationListener();

		if (location != null) {
			mylistener.onLocationChanged(location);
		} else {
			// leads to the settings because there is no last known location
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		finish();
	}
	
	private class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			// Initialize the location fields
			
			
			String longitude_S = String.valueOf(location.getLongitude());
			String latitude_S = String.valueOf(location.getLatitude());
			
			SQLiteDatabase ipedoDB = openOrCreateDatabase("IPedoDB", MODE_PRIVATE, null);
			ipedoDB.execSQL("CREATE TABLE IF NOT EXISTS Location (Latitude VARCHAR PRIMARY KEY, Longitude VARCHAR);");
			ContentValues values = new ContentValues();
			values.put("Latitude", latitude_S);
			values.put("Longitude", longitude_S);
			ipedoDB.insert("Location", null, values);	
			
			success.setText("Success: Current location recorded");
			latitude.setText("Latitude: "+ latitude_S);
			longitude.setText("Longitude: "+ longitude_S);
			
			Toast.makeText(getApplicationContext(), "Location is Recorded", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
	}
	
}
