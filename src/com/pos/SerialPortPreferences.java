package com.pos;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pos.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SerialPortPreferences extends PreferenceActivity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;
	
	private SerialPort mSerialPort_P;
	private SerialPort mSerialPort_V;
	
	private OutputStream mOutputStream_P;
	private OutputStream mOutputStream_V;
	
	private EditText fldTestString;
	
	
	
	private Thread thread;
	private boolean bRunning;
	private Double dCounter=0.0;
	private DecimalFormat df=new DecimalFormat("#");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Hide the Title Bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);

		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;

		
		
	}
	
	// bar code
    
	
    
    
    public void btnTestPage2(View v) {
    	try {

			mSerialPort_V = mApplication.getSerialPort("VFD", "BAUD_VFD");
			mOutputStream_V = mSerialPort_V.getOutputStream();
			
			serial_V();
    		
    	} catch(Exception e) {
    		
    	}
    }
    
    
    
    
    
    
    
    
    
	
	
	private void serial_V() {
		try {
			
			
			mOutputStream_V.write(0x1b);
			mOutputStream_V.write(0x40);
    		
			
			mOutputStream_V.write(new String(fldTestString.getText().toString()).getBytes());
			mOutputStream_V.write('\r');
			mOutputStream_V.write('\n');

			mApplication.closeSerialPort();
			
		} catch (IOException e) {
			DisplayError(e.toString());
		}
	}
	
	//private void DisplayError(int resourceId) {
	private void DisplayError(String msg) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		//b.setMessage(resourceId);
		b.setMessage(msg);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//SerialPortPreferences.this.finish();
			}
		});
		b.show();
	}
	
	//Max add
	/*private int center(String s, int pageWidth) {
		int len;
		int retVal=0;
		
		try {
			len=s.length();
			retVal=((pageWidth-len)/2)*fontSize;
			return retVal;
			
		} catch (Exception e) {
			DisplayError(e.toString());
			return retVal;
			
		}
	}*/
	
	
	
	public void onStop() {
		super.onStop();
		
		if (thread!=null) {
			if (thread.isAlive()) {
				bRunning=false;
			}
		}
		
		mApplication.closeSerialPort();
	}
	
	static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int desiredWidth, int desiredHeight) throws WriterException {
		final int WHITE = 0xFFFFFFFF; //allow other color for colorful two-dimensional bar code
		final int BLACK = 0xFF000000;
		
		HashMap<EncodeHintType, String> hints = null;
		String encoding = guessAppropriateEncoding(contents);
		if (encoding != null) {
			hints = new HashMap<EncodeHintType, String>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = writer.encode(contents, format, desiredWidth,
				desiredHeight, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}
		
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
}
