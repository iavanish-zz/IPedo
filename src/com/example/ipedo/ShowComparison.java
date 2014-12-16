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
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowComparison extends Activity {

	TextView you;
	TextView yourFriend;
	TextView whoHasWalkedMore;
	
	String friendName;
	String yourName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_comparison);
		
		you = (TextView) findViewById(R.id.you);
		yourFriend = (TextView) findViewById(R.id.yourFriend);
		whoHasWalkedMore = (TextView) findViewById(R.id.whoHasWalkedMore);
		
		Intent intent = getIntent();
		friendName = intent.getStringExtra("FriendName");
		yourName = intent.getStringExtra("YourName");
		
		String url = "https://dweet.io:443/get/dweets/for/" + "" + yourName;
		new NetworkOperation().execute(url);
		
		url = "https://dweet.io:443/get/dweets/for/" + "" + friendName;
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
				if(json_object.getString("this").equals("failed"))	yourFriend.setText("Invalid friend. He doesn't exist");
				else {
					JSONArray dweet = json_object.getJSONArray("with");
					int n = dweet.length();
					long temp = 0;
					for(int i = 0; i < n; i++) {
						JSONObject tempJSON = dweet.getJSONObject(i);
						tempJSON = tempJSON.getJSONObject("content");
						temp += Long.parseLong(tempJSON.getString("steps"));
					}
					if(you.getText().toString() == "")	you.setText("In the last 24 hours, you have taken: " + "" + temp + "" + " steps");
					else {
						yourFriend.setText("In the last 24 hours, your friend (" + "" + friendName + "" + ") has taken: " + "" + temp + "" + " steps");
					
						long temp1 = Long.parseLong(you.getText().toString().substring(38, you.getText().toString().indexOf(" steps")));
						long difference = Math.abs(temp1 - temp); 
					
						if(temp1 < temp)	whoHasWalkedMore.setText("Your friend has taken " + "" + difference + "" + " steps more than you");
						
						else if(temp < temp1)	whoHasWalkedMore.setText("You have taken " + "" + difference + "" + " more steps than your friend");
					
						else	whoHasWalkedMore.setText("You and your friend have taken equal no. of steps");
					}
				}
        	}
        	catch (Exception exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
       }
		
	}
	
}
