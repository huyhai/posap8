package com.pos.service;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.pos.CustomFragmentActivity;
import com.pos.R;
import com.pos.SplashScreen;
import com.pos.common.Utilities;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JSONMethod extends AsyncTask<String, String, String> {
	static InputStream is = null;
	static String jObj = null;
	static String json = "";

	JSONCallBack mCallback;
	private List<NameValuePair> tmpParams;
	private Context mContext;
	private long mTime = 0;
	private Boolean mIsShowProgressBar = false;
	private String mresultNull = "{\"result\":{\"is_success\":\"false\",\"is_show_title\":\"true\",\"is_show_message\":\"true\",\"message\":\"Server connection error!\"},\"id\":\"getuser\",\"error\":null,\"jsonrpc\":\"2.0\"}";

	public JSONMethod(Context cont, List<NameValuePair> params, JSONCallBack callback, Boolean _isShowProgressBar) {
		// TODO Auto-generated constructor stub
		this.tmpParams = params;
		this.mCallback = callback;
		this.mContext = cont;
		this.mIsShowProgressBar = _isShowProgressBar;
		if (!(this.mContext instanceof SplashScreen)) {
			if (mIsShowProgressBar) {
				((CustomFragmentActivity) mContext).showProgressBar();
			}
		}
		if (!Utilities.checkInternetConnection(this.mContext)) {
			showDialogRetry(this.mContext, "No Internet connection available. Please verify your Internet connection and try again").show();
		} else {
			execute("");
		}
	}

	public String getJSONFromUrl() throws JSONException {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(mContext.getResources().getString(R.string.Config_URL_Server));
			httpPost.setEntity(new UrlEncodedFormEntity(this.tmpParams,HTTP.UTF_8));
//			httpPost.setHeader("Content-type", "application/json");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("HAI", json);
//			Toast.makeText(mContext, json, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		//			String jsonSpit = json.substring(3); 
//		jObj = json.substring(3);
//			jObj = new JSONObject(jsonSpit);

		// return JSON String
		return json;

	}

	/**
	 * call to server to get json result
	 * 
	 * @return call callback
	 */
	private String getJsonRespone() {
		String result = null;
		// connect to map web service
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(mContext.getResources().getString(R.string.Config_URL_Server));
		HttpResponse response;
		InputStream is = null;
		// Log.i("HAI", tmpParams);
		try {
			// tmpParams = URLEncoder.encode(tmpParams, "UTF-8");
//			httppost.setEntity(new StringEntity(tmpParams));// encoding
															// String
															// to
															// UTF-8
															// format
			httppost.setHeader("Content-type", "application/json");
			// ... other java code to execute the apache httpclient
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
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
			result = sb.toString();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			result = mresultNull;
			e.printStackTrace();
		} finally {
			// close stream
			closeStream(is);
		}
		return result;
	}

	void closeStream(Closeable stream) {
		try {
			stream.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String resultJson = null;
		final long t0 = System.currentTimeMillis();
		// if (Utilities.checkInternetConnection(this.mContext)) {
		// System.out.println("Calling host " +
		// mContext.getResources().getString(R.string.Config_URL_Server));
		if (tmpParams != null && mCallback != null) {
//			resultJson = getJsonRespone();
			try {
				resultJson = getJSONFromUrl();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// show dialog parameter not set
		}
		final long t1 = System.currentTimeMillis();
		mTime = t1 - t0;
		// } else {
		// showDialogRetry(this.mContext, "Check internet connection!").show();
		// }

		return  resultJson;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		String jsonObjRecv = "";
		jsonObjRecv = result; // get json object from string
								// result
		if (result == null) {
			jsonObjRecv = mresultNull; // get json object
										// from string
										// result
		}
		if (!(this.mContext instanceof SplashScreen)) {
			if (mIsShowProgressBar) {
				((CustomFragmentActivity) this.mContext).hideProgressBar();
			}
		}
		// Log.i("tinhvc", jsonObjRecv.toString());
		mCallback.callResult(mContext, jsonObjRecv, mTime);
	}

	/**
	 * Show dialog retry.
	 * 
	 * @param context
	 *            the context
	 * @param message
	 *            the message
	 * @return the alert dialog
	 */
	private static final String RETRY = "Retry";
	private static final String QUIT = "Quit";

	private AlertDialog showDialogRetry(final Context context, final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setNeutralButton(RETRY, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int id) {
				// Do nothing.
				if (!Utilities.checkInternetConnection(mContext)) {
					showDialogRetry(mContext, "No Internet connection available. Please verify your Internet connection and try again").show();
				} else {
					if (tmpParams != null) {
						execute("");
					} else {
						System.exit(0);
					}
				}
			}
		}).setNegativeButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// System.exit(0);
				System.exit(0);
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		return alert;
	}
}
