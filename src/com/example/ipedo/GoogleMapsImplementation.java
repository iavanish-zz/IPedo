package com.example.ipedo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class GoogleMapsImplementation extends Activity {

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_maps_implementation);
		
		Intent intent = getIntent();
		String latitude_S = intent.getStringExtra("Latitude");
		String longitude_S = intent.getStringExtra("Longitude");
		
		String plotable_coordinates = latitude_S + ", " + longitude_S;
		Intent googleMap_intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&z=10&q=loc:" + plotable_coordinates));
		try {
			startActivity(googleMap_intent);
        }
		catch (Exception exception) {
			exception.printStackTrace();
        }
		finish();
	}
	
}
