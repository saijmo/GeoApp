package com.example.geoapp;

import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
//import com.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	String part1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	String part3 = "&key=";
	String key = "AIzaSyAz1nneYfEDe2NcqSrwDL5low-Td6wAAQs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}
	
	public void submitAddress(View view) {
		EditText input = (EditText) findViewById(R.id.inputAddress);
		String address = input.getText().toString();
		address = address.replaceAll(" ", "+");
		String sendAddress = part1 + address + part3 + key;
		
		try {
			JSONObject findLocation = new SendAddress().execute(sendAddress).get();
			if(findLocation.getString("status").equals("OK")) {
				JSONArray totalResult = findLocation.getJSONArray("results");
				JSONObject geometricResult = totalResult.getJSONObject(0).getJSONObject("geometry");
				JSONObject locationResult = geometricResult.getJSONObject("location");
				double lat = locationResult.getDouble("lat");
				double lng = locationResult.getDouble("lng");
				
				String longitude = locationResult.getString("lng");
				String latitude = locationResult.getString("lat");
				
				String location_type = geometricResult.getString("location_type");
				
				String lnglat = "Latitude: " + latitude + " Longitude: " + longitude;
				GoogleMap map;
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				LatLng myLocation = new LatLng(lat, lng);
								
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
			   map.addMarker(new MarkerOptions()
			            .title(lnglat).position(myLocation));
			   Toast.makeText(this, "Location Type: " + location_type, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
			}
			
			
		}

		catch(JSONException e) {}
		catch(InterruptedException e) {}
		catch(ExecutionException e) {}
	}
	
	public void findDistance(View view){
		EditText input = (EditText) findViewById(R.id.inputAddress);
		EditText input2 = (EditText) findViewById(R.id.inputAddress2);
		String address = input.getText().toString();
		String address2 = input2.getText().toString();
		address = address.replaceAll(" ", "+");
		address2 = address2.replaceAll(" ", "+");
		String sendAddress = part1 + address + part3 + key;
		String sendAddress2 = part1 + address2 + part3 + key;
		
		try {
			JSONObject findLocation = new SendAddress().execute(sendAddress).get();
			JSONObject findLocation2 = new SendAddress().execute(sendAddress2).get();
			if(findLocation2.getString("status").equals("OK") && findLocation.getString("status").equals("OK")) {
				
				// first location finder
				JSONArray totalResult = findLocation.getJSONArray("results");
				JSONObject geometricResult = totalResult.getJSONObject(0).getJSONObject("geometry");
				JSONObject locationResult = geometricResult.getJSONObject("location");
				double lat = locationResult.getDouble("lat");
				double lng = locationResult.getDouble("lng");
				
				String longitude = locationResult.getString("lng");
				String latitude = locationResult.getString("lat");
	
				String location_type = geometricResult.getString("location_type");
				
				String lnglat = "Latitude: " + latitude + " Longitude: " + longitude;
				// end of first location finder
				
				// second location finder
				JSONArray totalResult2 = findLocation2.getJSONArray("results");
				JSONObject geometricResult2 = totalResult2.getJSONObject(0).getJSONObject("geometry");
				JSONObject locationResult2 = geometricResult2.getJSONObject("location");
				double lat2 = locationResult2.getDouble("lat");
				double lng2 = locationResult2.getDouble("lng");
				
				String longitude2 = locationResult2.getString("lng");
				String latitude2 = locationResult2.getString("lat");
				
				String location_type2 = geometricResult2.getString("location_type");
				
				String lnglat2 = "Latitude: " + latitude2 + " Longitude: " + longitude2;
				// end of second location finder
				
				// creating map fragment
				GoogleMap map;
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				LatLng myLocation = new LatLng(lat, lng);
				LatLng myLocation2 = new LatLng(lat2, lng2);
				
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation2, 15));
			   map.addMarker(new MarkerOptions()
			            .title(lnglat2).position(myLocation));
			   map.addMarker(new MarkerOptions()
            			.title(lnglat).position(myLocation2));
			   
			   map.addPolyline(new PolylineOptions().geodesic(true)
			   			.add(myLocation)
			   			.add(myLocation2));
			   			   
			  Toast.makeText(this, "Distance: " + 
					  				findDistance(lat, lng, lat2, lng2) + " km", 
					  				Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
			}
			
			
		}

		catch(JSONException e) {}
		catch(InterruptedException e) {}
		catch(ExecutionException e) {}
	}
	
	public float findDistance(double lat1, double lng1, double lat2, double lng2){
		double earthRadius = 6371;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

}
