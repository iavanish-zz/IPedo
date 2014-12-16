package com.example.ipedo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UserSettings extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
		Button changeUserHandle = (Button) findViewById(R.id.changeUserHandle);
		Button addFriends = (Button) findViewById(R.id.addFriends);
		Button deleteFriends = (Button) findViewById(R.id.deleteFriends);
		
		changeUserHandle.setOnClickListener(this);
		addFriends.setOnClickListener(this);
		deleteFriends.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.changeUserHandle) {
			Intent intent = new Intent(this, ChangeUserHandle.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.addFriends) {
			Intent intent = new Intent(this, AddFriends.class);
			startActivity(intent);
		}
		
		else if(v.getId() == R.id.deleteFriends) {
			Intent intent = new Intent(this, DeleteFriends.class);
			startActivity(intent);
		}
	}
}
