package com.example.ipedo;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Pedometer extends Activity implements SensorEventListener, OnClickListener {

	Handler handler = new Handler();
	SensorManager sensorManager;

	TextView showStep;
	Button changeUserSettings;
	Button dweetIt;
	Button showPreviouslyDweetedSteps;
	Button timeline;
	Button showRecordedLocationsOnGoogleMaps;
	Button compareWithAFriend;
	Button recordCurrentLocation;
	Button notifyUserToStartWalking;

	double x = 0;
	double y = 0;
	double z = 0;

	double appliedAcceleration = 0;
	double currentAcceleration = 0;
	long countSteps = 0;
	Date lastUpdate;
	Date globalTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		
		SQLiteDatabase ipedoDB = openOrCreateDatabase("IPedoDB", Context.MODE_PRIVATE, null);
		ipedoDB.execSQL("CREATE TABLE IF NOT EXISTS UserName (Name VARCHAR PRIMARY KEY);");
		Cursor resultSet = ipedoDB.rawQuery("Select Name from UserName", null);
		if(resultSet.getCount() == 0) {
			ContentValues values = new ContentValues();
		    values.put("Name", "-1");
		    ipedoDB.insert("UserName", null, values);
		}
		resultSet.close();
		
		showStep = (TextView) findViewById(R.id.showStep);
		changeUserSettings = (Button) findViewById(R.id.changeUserSettings);
		dweetIt = (Button) findViewById(R.id.dweetIt);
		showPreviouslyDweetedSteps = (Button) findViewById(R.id.showPreviouslyDweetedSteps);
		timeline = (Button) findViewById(R.id.timeline);
		showRecordedLocationsOnGoogleMaps = (Button) findViewById(R.id.showRecordedLocationsOnGoogleMaps);
		compareWithAFriend = (Button) findViewById(R.id.compareWithAFriend);
		recordCurrentLocation = (Button) findViewById(R.id.recordCurrentLocation);
		notifyUserToStartWalking = (Button) findViewById(R.id.notifyUserToStartWalking);
		
		changeUserSettings.setOnClickListener(this);
		dweetIt.setOnClickListener(this);
		showPreviouslyDweetedSteps.setOnClickListener(this);
		timeline.setOnClickListener(this);
		showRecordedLocationsOnGoogleMaps.setOnClickListener(this);
		compareWithAFriend.setOnClickListener(this);
		recordCurrentLocation.setOnClickListener(this);
		notifyUserToStartWalking.setOnClickListener(this);

		lastUpdate = new Date(System.currentTimeMillis());
		globalTime = new Date(System.currentTimeMillis());

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);

		Timer updateTimer = new Timer("stepUpdate");
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				updateGUI();
			}
		}, 0, 1000);
	}

	private void updateGUI() {
		handler.post(new Runnable() {
			public void run() {
				
				Date timeNow = new Date(System.currentTimeMillis());
				double timeDelta = (double)(timeNow.getTime() - lastUpdate.getTime());
				if(timeDelta <= 220)	return;
				lastUpdate.setTime(timeNow.getTime());
				if((appliedAcceleration != 0) && (Math.abs(appliedAcceleration - currentAcceleration) >= 4))	countSteps++;
				appliedAcceleration = currentAcceleration;

				showStep.setText("No. of Steps: " + String.valueOf(countSteps));
		
				//record location
				if(countSteps == 100) {
					Pedometer obj = new Pedometer();
					Intent intent = new Intent(obj, RecordLocation.class);
					startActivity(intent);
				}
				
				if(timeNow.getTime() - globalTime.getTime() == 10800000) {
					if(countSteps < 100) {
						Pedometer obj = new Pedometer();
						Intent intent = new Intent(obj, UserNotification.class);
						startActivity(intent);
					}
					globalTime.setTime(timeNow.getTime());
				}
				//show notification
				
			}
		});
	}		

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		x = event.values[0] - 0.15 * event.values[0];
		y = event.values[1] - 0.15 * event.values[1];
		z = event.values[2] - 0.20 * event.values[2];
		
		double linearAcceleration = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		
		currentAcceleration = linearAcceleration;
		
		updateGUI();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.changeUserSettings) {
			Intent intent = new Intent(this, UserSettings.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.dweetIt) {
			Intent intent = new Intent(this, DweetSteps.class);
			intent.putExtra("Steps", countSteps);
			countSteps = 0;
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.showPreviouslyDweetedSteps) {
			Intent intent = new Intent(this, ShowDweet.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.timeline) {
			Intent intent = new Intent(this, Timeline.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.showRecordedLocationsOnGoogleMaps) {
			Intent intent = new Intent(this, ShowLocations.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.compareWithAFriend) {
			Intent intent = new Intent(this, CompareWithAFriend.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.recordCurrentLocation) {
			Intent intent = new Intent(this, RecordLocation.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.notifyUserToStartWalking) {
			Intent intent = new Intent(this, UserNotification.class);
			startActivity(intent);
		}
	}
	
}
