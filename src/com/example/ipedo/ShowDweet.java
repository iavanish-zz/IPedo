package com.example.ipedo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowDweet extends Activity {

	TextView showDweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_dweet);
		
		showDweet = (TextView)findViewById(R.id.showDweet);
		
		SQLiteDatabase ipedoDB;
		ipedoDB = openOrCreateDatabase("IPedoDB", Context.MODE_PRIVATE, null);
		
		String name = null;
		
		ipedoDB.execSQL("CREATE TABLE IF NOT EXISTS UserName (Name VARCHAR PRIMARY KEY);");
		Cursor resultSet = ipedoDB.rawQuery("Select Name from UserName", null);
		if(resultSet.getCount() > 0) {
			resultSet.moveToFirst();
			name = resultSet.getString(0);
		}
		resultSet.close();
		
		String url = "https://dweet.io:443/get/dweets/for/" + "" + name;
		new NetworkOperation().execute(url);
	}
	
	public static String getURL(String url){
		InputStream is = null;
		String res = "";
		try {
			HttpClient hClient = new DefaultHttpClient();
			HttpResponse hResponse = hClient.execute(new HttpGet(url));
			is = hResponse.getEntity().getContent();
			if(is != null)
				res = convertInputStreamToString(is);
			else
				res = "Something went wrong!";
		}
		catch(Exception exception) {
			Log.d("InputStream", exception.getLocalizedMessage());
		}
		return res;
	}
	
    private static String convertInputStreamToString(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String l = "";
        String res = "";
        try {
        	while((l = br.readLine()) != null)
        		res += l;        
        	is.close();
        }
        catch(IOException exception) {
        	System.out.println("IO Exception occured!");
        }
        return res;
    }
	
	private class NetworkOperation extends AsyncTask <String, Void, String> {

		AndroidHttpClient newClient = AndroidHttpClient.newInstance("");
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return getURL(params[0]);
		}
		
		@Override
        protected void onPostExecute(String result) {
           	if (newClient != null)
				newClient.close();
        	try {
				JSONObject json_object = new JSONObject(result);
				JSONArray dweet = json_object.getJSONArray("with");
				int n = dweet.length();
				String tempDweet = "";
				for(int i = 0; i < n; i++) {
					JSONObject tempJSON = dweet.getJSONObject(i);
					tempDweet += ("Dweet " + (n-i) + ": \n");
					String temp = tempJSON.getString("created");
					tempDweet += ("Date: " + temp.substring(0, 10) + "\n");
					tempDweet += ("Time: " + temp.substring(11, 23) + "\n");
					tempJSON = tempJSON.getJSONObject("content");
					tempDweet += ("No. of Steps: " + tempJSON.getLong("steps") + "\n\n");
				}
	        	showDweet.setText(tempDweet);
			}
        	catch (Exception exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
       }
		
	}

}
