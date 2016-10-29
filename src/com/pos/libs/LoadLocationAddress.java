package com.pos.libs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.pos.interfaceapp.LocationCallBack;

public class LoadLocationAddress extends AsyncTask<LatLng, Object, Object> {

	private LocationCallBack mCallBack;
	private LatLng mPosition;	

	public LoadLocationAddress(LocationCallBack callback) {
		// TODO Auto-generated constructor stub
		mCallBack = callback;		
	}

	@Override
	protected Object doInBackground(LatLng... params) {
		// TODO Auto-generated method stub
		mPosition = params[0];
		return getLocationName(mPosition);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		mCallBack.callback(mPosition, updateLocation(String.valueOf(result)));
	}

	
	public String getLocationName(LatLng key) {
		// connect to map web service
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(LoadLocationAddress.this.makeUrl(key));
		HttpResponse response;
		try {
			response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			InputStream is = null;

			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			reader.close();
			String result = sb.toString();

			JSONObject jsonObject = new JSONObject(result);
			JSONArray routeArray = jsonObject.getJSONArray("results");
			JSONObject note = routeArray.getJSONObject(0);
			return note.getString("formatted_address");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String updateLocation(String location) {
		try {
			location = new String(location.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Throwable e) {
			location = "";
			e.printStackTrace();
		}
		return location;
	}
	
	public String makeUrl(LatLng point) {
		StringBuilder urlString = new StringBuilder();

		urlString.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
		urlString.append(point.latitude + "," + point.longitude);
		urlString.append("&sensor=false");
		Log.d("xxx", "URL=" + urlString.toString());
		return urlString.toString().trim().replace(" ", "%20");
	}
}

