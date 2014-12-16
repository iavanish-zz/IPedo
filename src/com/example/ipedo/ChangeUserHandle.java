package com.example.ipedo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeUserHandle extends Activity implements OnClickListener {

	TextView enterDesiredHandle;
	EditText desiredHandle;
	Button submit;
	TextView error;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_user_handle);
		
		enterDesiredHandle = (TextView) findViewById(R.id.enterDesiredHandle);
		desiredHandle = (EditText) findViewById(R.id.desiredHandle);
		submit = (Button) findViewById(R.id.submit);
		error = (TextView) findViewById(R.id.error);
		
		enterDesiredHandle.setText("Enter Desired Handle");
		submit.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.submit) {
			
			String newName = desiredHandle.getText().toString();
			SQLiteDatabase ipedoDB = openOrCreateDatabase("IPedoDB", MODE_PRIVATE, null);
			ipedoDB.execSQL("CREATE TABLE IF NOT EXISTS UserName (Name VARCHAR PRIMARY KEY);");
			Cursor resultSet = ipedoDB.rawQuery("Select Name from UserName", null);
			
			if(resultSet.getCount() > 0) {
				resultSet.moveToFirst();
				String name = resultSet.getString(0);
				if(name.equals(newName)) {
					error.setText("Smart huh! You already have the same handle.");
					desiredHandle.setText("");
				}
				else {
					ContentValues values = new ContentValues();
				    values.put("Name", newName);
				    ipedoDB.update("UserName", values, null, null);
					desiredHandle.setText("");
					error.setText("Done!");
				}				
			}
			
			else {
				desiredHandle.setText("");
				error.setText("Failed!");
			}
			
			resultSet.close();
		}
	}
}
