package com.pos;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;


import android.content.SharedPreferences;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;

public class Application extends android.app.Application {

	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	//private SerialPort mSerialPort = null;
	private SerialPort mSerialPort_P = null;
	private SerialPort mSerialPort_V = null;
	private String device;

	public SerialPort getSerialPort(String device, String baud) throws SecurityException, IOException, InvalidParameterException {
		this.device=device;
		if (device.equals("Printer")) {
			if (mSerialPort_P == null) {
				/* Read serial port parameters */
				SharedPreferences sp = getSharedPreferences("com.pos.printer_preferences", MODE_PRIVATE);
				//String path = sp.getString("DEVICE", "");
				String path = sp.getString(device, "");
				//int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
				int baudrate = Integer.decode(sp.getString(baud, "-1"));
				
				int databits = Integer.decode(sp.getString("DATA", "8"));
				int parity = Integer.decode(sp.getString("PARITY", "0"));
				int stopbits = Integer.decode(sp.getString("STOP", "1"));
				int flowctl = Integer.decode(sp.getString("FLOWCTL", "0"));
	
				/* Check parameters */
				if ( (path.length() == 0) || (baudrate == -1)) {
					throw new InvalidParameterException();
				}
				
				/*
				 * Note : rename COM to absolutePath
				 * ex : COM1 to /dev/ttymxc0
				 */
				if (path.indexOf("COM")>=0) {
					int i=Integer.parseInt(path.substring(path.indexOf("COM")+"COM".length()));
					i--;
					path="/dev/ttymxc"+String.valueOf(i);
					
					/* Open the serial port */
					//mSerialPort_P = new SerialPort(new File(path), baudrate);
					mSerialPort_P = new SerialPort(new File(path), baudrate, databits, parity, stopbits, flowctl);
				}
			}
			return mSerialPort_P;
		} else {
			if (mSerialPort_V == null) {
				/* Read serial port parameters */
				SharedPreferences sp = getSharedPreferences("com.pos.printer_preferences", MODE_PRIVATE);
				//String path = sp.getString("DEVICE", "");
				String path = sp.getString(device, "");
				//int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
				int baudrate = 9600;
				
				
				int databits = 8;
				int parity = 0;
				int stopbits = 1;
				int flowctl = 0;
	
				/* Check parameters */
				if ( (path.length() == 0) || (baudrate == -1)) {
					throw new InvalidParameterException();
				}
				
				/*
				 * Note : rename COM to absolutePath
				 * ex : COM1 to /dev/ttymxc0
				 */
				
					path="/dev/ttymxc2";

					/* Open the serial port */
					//mSerialPort_V = new SerialPort(new File(path), baudrate);
					mSerialPort_V = new SerialPort(new File(path), baudrate, databits, parity, stopbits, flowctl);
				
			}
			return mSerialPort_V;
		}
	}

	public void closeSerialPort() {
		if (mSerialPort_P != null) {
			mSerialPort_P.close();
			mSerialPort_P = null;
		}

		if (mSerialPort_V != null) {
			mSerialPort_V.close();
			mSerialPort_V = null;
		}
	}
}

