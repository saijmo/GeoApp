package com.example.geoapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class SendAddress extends AsyncTask<String, Integer, JSONObject>{

	
	public JSONObject doInBackground(String... urls) {
		try {
			URL addressRequest = new URL(urls[0]);
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(addressRequest.openStream(), "UTF-8"));
         StringBuilder addressStrBuilder = new StringBuilder();
         
         Boolean moreLines = true;
         while(moreLines){
         	String input = streamReader.readLine();
         	if(input == null)
         		moreLines = false;
         	else
         		addressStrBuilder.append(input);
         }
         JSONObject findData = new JSONObject(addressStrBuilder.toString());
         return findData;
		}
		catch(IOException e) {}
		catch(JSONException e) {}
		return null;
	}

	
}
