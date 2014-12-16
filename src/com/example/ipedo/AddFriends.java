package com.example.ipedo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddFriends extends Activity implements OnClickListener {

	TextView friendHandle;
	EditText enterFriendHandle;
	Button submit;
	TextView error;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends);
		
		friendHandle = (TextView) findViewById(R.id.friendHandle);
		enterFriendHandle = (EditText) findViewById(R.id.enterFriendHandle);
		submit = (Button) findViewById(R.id.submit);
		error = (TextView) findViewById(R.id.error);
		
		friendHandle.setText("Enter Friend's Handle");
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.submit) {
		
			String fName = enterFriendHandle.getText().toString();
		
			SQLiteDatabase ipedoDB = openOrCreateDatabase("IPedoDB", MODE_PRIVATE, null);
			ipedoDB.execSQL("CREATE TABLE IF NOT EXISTS FriendName (Name VARCHAR PRIMARY KEY);");
			ContentValues values = new ContentValues();
			values.put("Name", fName);
			ipedoDB.insert("FriendName", null, values);
			enterFriendHandle.setText("");
			error.setText("Done!");				
			
		}
	}
}
