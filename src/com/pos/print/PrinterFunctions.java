package com.pos.print;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.pos.db.CompanyDataSource;
import com.pos.db.ItemsDataSource;
import com.pos.db.MySQLiteHelper;
import com.pos.db.SaleReportDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.serialport.SerialPort;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.pos.Application;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.libs.CalculationUtils;
import com.pos.libs.ComDDUtilities;
import com.pos.libs.SessionManager;
import com.pos.model.ListOrderModel;
import com.pos.model.PayModel;
import com.pos.print.RasterDocument.RasPageEndMode;
import com.pos.print.RasterDocument.RasSpeed;
import com.pos.print.RasterDocument.RasTopMargin;
import com.pos.ui.DialogPayment;
import com.pos.ui.DialogSelectQtyDelete;
import com.pos.ui.LoginDialog;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

public class PrinterFunctions extends Activity {
	private static SessionManager ss;

	public enum NarrowWide {
		_2_6, _3_9, _4_12, _2_5, _3_8, _4_10, _2_4, _3_6, _4_8
	};

	public enum BarCodeOption {
		No_Added_Characters_With_Line_Feed, Adds_Characters_With_Line_Feed, No_Added_Characters_Without_Line_Feed, Adds_Characters_Without_Line_Feed
	}

	public enum Min_Mod_Size {
		_2_dots, _3_dots, _4_dots
	};

	public enum NarrowWideV2 {
		_2_5, _4_10, _6_15, _2_4, _4_8, _6_12, _2_6, _3_9, _4_12
	};

	public enum CorrectionLevelOption {
		Low, Middle, Q, High
	};

	public enum Model {
		Model1, Model2
	};

	public enum Limit {
		USE_LIMITS, USE_FIXED
	};

	public enum CutType {
		FULL_CUT, PARTIAL_CUT, FULL_CUT_FEED, PARTIAL_CUT_FEED
	};

	public enum Alignment {
		Left, Center, Right
	};

	private Activity ac;
	private static int printableArea = 576; // for raster data
	
	public static void AddRange(ArrayList<Byte> array, Byte[] newData) {
		for (int index = 0; index < newData.length; index++) {
			array.add(newData[index]);
		}
	}

	/**
	 * This function is used to print a PDF417 barcode to standard Star POS
	 * printers
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param limit
	 *            - Selection of the Method to use specifying the barcode size.
	 *            This is either 0 or 1. 0 is Use Limit method and 1 is Use
	 *            Fixed method. See section 3-122 of the manual (Rev 1.12).
	 * @param p1
	 *            - The vertical proportion to use. The value changes with the
	 *            limit select. See section 3-122 of the manual (Rev 1.12).
	 * @param p2
	 *            - The horizontal proportion to use. The value changes with the
	 *            limit select. See section 3-122 of the manual (Rev 1.12).
	 * @param securityLevel
	 *            - This represents how well the barcode can be recovered if it
	 *            is damaged. This value should be 0 to 8.
	 * @param xDirection
	 *            - Specifies the X direction size. This value should be from 1
	 *            to 10. It is recommended that the value be 2 or less.
	 * @param aspectRatio
	 *            - Specifies the ratio of the PDF417 barcode. This values
	 *            should be from 1 to 10. It is recommended that this value be 2
	 *            or less.
	 * @param barcodeData
	 *            - Specifies the characters in the PDF417 barcode.
	 */

	public static void PrintPosOne(Activity ac, final Context context,
			final String portName, final String portSettings, Resources res,
			String recep, int itemnumber) {

		final ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// // list.addAll(Arrays
		// // .asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
		// list.addAll(Arrays.asList(tempList));

		// command = createRasterCommand("\t\t\t\ta", 13, 0);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		String textToPrint = ("");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = createRasterCommand("", 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		/*
		 * textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */
		textToPrint = ("" + "Terminal           \t:  Section 1" + "\r\n"
				+ "User                     \t:  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ "\r\n" + "Printed              \t:  "
				+ ComDDUtilities.getDateTimePrint() + "\r\n");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("              TABLE         PAX" + "\r\n"
				+ "                   "
				+ Utilities.getGlobalVariable(ac).tableNum/* tabnum */
				+ "                "
				+ Utilities.getGlobalVariable(ac).numberCustomer + "\r");
		command = createRasterCommand(textToPrint, 19, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("-----------------------------------------------------------------------\r"
				+ "     QTY            DESCRIPTION                                     \t\t\t\t\r"
				+ "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		String textin = "";
		String remark = "";
		String name = "";
		if (null == PosApp.listOrderData2.get(itemnumber).getRemarks()
				|| PosApp.listOrderData2.get(itemnumber).getRemarks()
						.equals("")) {

		} else {
			remark = "\t" + "\r\n"
					+ PosApp.listOrderData2.get(itemnumber).getRemarks();
		}

		if (PosApp.listOrderData2.get(itemnumber).getStandBy().equals("1")) {
//			name = PosApp.listOrderData2.get(itemnumber).getItemName()
//					+ " (Stand-by)";
			name = PosApp.listOrderData2.get(itemnumber).getItemTau()
					+ " (Stand-by)";
		} else {
			if(Utilities.getGlobalVariable(ac).subNow){
//				name = PosApp.listOrderData2.get(itemnumber).getItemName()+" (Subnow)";
				name = PosApp.listOrderData2.get(itemnumber).getItemTau()+" (SERVE NOW)";
				//Utilities.getGlobalVariable(ac).subNow=false;
			}else{
				name = PosApp.listOrderData2.get(itemnumber).getItemTau();
//				name = PosApp.listOrderData2.get(itemnumber).getItemName();	
			}
			
		}
		String quatity = "";
		if (Utilities.getGlobalVariable(ac).isDelete) {
		
			quatity = "-" + DialogSelectQtyDelete.numDelete;
			name+=" (CANCEL)";
		} else {
			quatity = PosApp.listOrderData2.get(itemnumber).getQualyti();
		}

		textin += "\t" + RedoPrice(quatity) + "\t\t" + name +"\n"+ remark + "\t"
				+ "\r\n";
//	Log.v("HAI", textin);
		if (MainActivity.isPrintNoSave) {
			textToPrint = (textin);
		} else {
			textToPrint = (textin);
		}

		command = createRasterCommand(textToPrint, 19, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); 
		new Thread(new Runnable() {
			public void run() {
				sendCommand(context, portName, portSettings, list);
			}
		}).start();
		Log.v("HAI1", textin);

	}

	public static void PrintPDF417Code(Context context, String portName,
			String portSettings, Limit limit, byte p1, byte p2,
			byte securityLevel, byte xDirection, byte aspectRatio,
			byte[] barcodeData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] setBarCodeSize = new Byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x30,
				0x00, 0x00, 0x00 };
		switch (limit) {
		case USE_LIMITS:
			setBarCodeSize[5] = 0;
			break;
		case USE_FIXED:
			setBarCodeSize[5] = 1;
			break;
		}

		setBarCodeSize[6] = p1;
		setBarCodeSize[7] = p2;
		AddRange(commands, setBarCodeSize);

		Byte[] setSecurityLevel = new Byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x31,
				0x00 };
		setSecurityLevel[5] = securityLevel;
		AddRange(commands, setSecurityLevel);

		Byte[] setXDirections = new Byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x32, 0x00 };
		setXDirections[5] = xDirection;
		AddRange(commands, setXDirections);

		Byte[] setAspectRatio = new Byte[] { 0x1b, 0x1d, 0x78, 0x53, 0x33, 0x00 };
		setAspectRatio[5] = aspectRatio;
		AddRange(commands, setAspectRatio);

		Byte[] setBarcodeData = new Byte[6 + barcodeData.length];
		setBarcodeData[0] = 0x1b;
		setBarcodeData[1] = 0x1d;
		setBarcodeData[2] = 0x78;
		setBarcodeData[3] = 0x44;
		setBarcodeData[4] = (byte) (barcodeData.length % 256);
		setBarcodeData[5] = (byte) (barcodeData.length / 256);
		for (int index = 0; index < barcodeData.length; index++) {
			setBarcodeData[index + 6] = barcodeData[index];
		}
		AddRange(commands, setBarcodeData);

		Byte[] printBarcode = new Byte[] { 0x1b, 0x1d, 0x78, 0x50 };
		AddRange(commands, printBarcode);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print a QR Code on standard Star POS printers
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param correctionLevel
	 *            - The correction level for the QR Code. The correction level
	 *            can be 7, 15, 25, or 30. See section 3-129 (Rev. 1.12).
	 * @param model
	 *            - The model to use when printing the QR Code. See section
	 *            3-129 (Rev. 1.12).
	 * @param cellSize
	 *            - The cell size of the QR Code. The value of this should be
	 *            between 1 and 8. It is recommended that this value is 2 or
	 *            less.
	 * @param barCodeData
	 *            - Specifies the characters in the QR Code.
	 */
	public static void PrintQrCode(Context context, String portName,
			String portSettings, CorrectionLevelOption correctionLevel,
			Model model, byte cellSize, byte[] barCodeData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] modelCommand = new Byte[] { 0x1b, 0x1d, 0x79, 0x53, 0x30, 0x00 };
		switch (model) {
		case Model1:
			modelCommand[5] = 1;
			break;
		case Model2:
			modelCommand[5] = 2;
			break;
		}

		AddRange(commands, modelCommand);

		Byte[] correctionLevelCommand = new Byte[] { 0x1b, 0x1d, 0x79, 0x53,
				0x31, 0x00 };
		switch (correctionLevel) {
		case Low:
			correctionLevelCommand[5] = 0;
			break;
		case Middle:
			correctionLevelCommand[5] = 1;
			break;
		case Q:
			correctionLevelCommand[5] = 2;
			break;
		case High:
			correctionLevelCommand[5] = 3;
			break;
		}
		AddRange(commands, correctionLevelCommand);

		Byte[] cellCodeSize = new Byte[] { 0x1b, 0x1d, 0x79, 0x53, 0x32,
				cellSize };
		AddRange(commands, cellCodeSize);

		// Add BarCode data
		AddRange(commands, new Byte[] { 0x1b, 0x1d, 0x79, 0x44, 0x31, 0x00 });
		commands.add((byte) (barCodeData.length % 256));
		commands.add((byte) (barCodeData.length / 256));

		for (int index = 0; index < barCodeData.length; index++) {
			commands.add(barCodeData[index]);
		}

		Byte[] printQrCodeCommand = new Byte[] { 0x1b, 0x1d, 0x79, 0x50 };
		AddRange(commands, printQrCodeCommand);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function opens the cash drawer connected to the printer This
	 * function just send the byte 0x07 to the printer which is the open
	 * cashdrawer command It is not possible that the OpenCashDraware and
	 * OpenCashDrawer2 are running at the same time.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 */
	public static void OpenCashDrawer(Context context, String portName,
			String portSettings) {
		ArrayList<Byte> commands = new ArrayList<Byte>();
		byte openCashDrawer = 0x07;

		commands.add(openCashDrawer);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function opens the cash drawer connected to the printer This
	 * function just send the byte 0x1a to the printer which is the open
	 * cashdrawer command The OpenCashDrawer2, delay time and power-on time is
	 * 200msec fixed. It is not possible that the OpenCashDraware and
	 * OpenCashDrawer2 are running at the same time.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 */
	public static void OpenCashDrawer2(Context context, String portName,
			String portSettings) {
		ArrayList<Byte> commands = new ArrayList<Byte>();
		byte openCashDrawer = 0x1a;

		commands.add(openCashDrawer);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function checks the Firmware Informatin of the printer
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 */
	public static void CheckFirmwareVersion(Context context, String portName,
			String portSettings) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version:
			 * upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port =
			 * StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			Map<String, String> firmware = port.getFirmwareInformation();

			String modelName = firmware.get("ModelName");
			String firmwareVersion = firmware.get("FirmwareVersion");

			String message = "Model Name:" + modelName;
			message += "\nFirmware Version:" + firmwareVersion;

			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Firmware Information");
			alert.setMessage(message);
			alert.setCancelable(false);
			alert.show();

		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function checks the DipSwitch Informatin of the DK-AirCash
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 */

	/**
	 * This function checks the status of the printer
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param sensorActiveHigh
	 *            - boolean variable to tell the sensor active of CashDrawer
	 *            which is High
	 */
	public static void CheckStatus(Context context, String portName,
			String portSettings, boolean sensorActiveHigh) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version:
			 * upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port =
			 * StarIOPort.getPort(portName, portSettings, 10000);
			 */

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			StarPrinterStatus status = port.retreiveStatus();

			if (status.offline == false) {
				String message = "Printer is Online";

				if (status.compulsionSwitch == false) {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Close";
					} else {
						message += "\nCash Drawer: Open";
					}
				} else {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Open";
					} else {
						message += "\nCash Drawer: Close";
					}
				}

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			} else {
				String message = "Printer is Offline";

				if (status.receiptPaperEmpty == true) {
					message += "\nPaper is Empty";
				}

				if (status.coverOpen == true) {
					message += "\nCover is Open";
				}

				if (status.compulsionSwitch == false) {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Close";
					} else {
						message += "\nCash Drawer: Open";
					}
				} else {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Open";
					} else {
						message += "\nCash Drawer: Close";
					}
				}

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			}

		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function is used to print barcodes in 39 format
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param barcodeData
	 *            - These are the characters that will be printed in the
	 *            barcode. The characters available for this bar code are listed
	 *            in section 3-43 (Rev. 1.12).
	 * @param option
	 *            - This tells the printer to put characters under the printed
	 *            barcode or not. This may also be used to line feed after the
	 *            barcode is printed.
	 * @param height
	 *            - The height of the barcode. This is measured in pixels
	 * @param width
	 *            - The width of the barcode. This value should be between 1 to
	 *            9. See section 3-42 (Rev. 1.12) for more information on the
	 *            values.
	 */
	public static void PrintCode39(Context context, String portName,
			String portSettings, byte[] barcodeData, BarCodeOption option,
			byte height, NarrowWide width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x34;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_6:
			n3 = 49;
			break;
		case _3_9:
			n3 = 50;
			break;
		case _4_12:
			n3 = 51;
			break;
		case _2_5:
			n3 = 52;
			break;
		case _3_8:
			n3 = 53;
			break;
		case _4_10:
			n3 = 54;
			break;
		case _2_4:
			n3 = 55;
			break;
		case _3_6:
			n3 = 56;
			break;
		case _4_8:
			n3 = 57;
			break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in 93 format
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param barcodeData
	 *            - These are the characters that will be printed in the
	 *            barcode. The characters available for this barcode are listed
	 *            in section 3-43 (Rev. 1.12).
	 * @param option
	 *            - This tells the printer to put characters under the printed
	 *            barcode or not. This may also be used to line feed after the
	 *            barcode is printed.
	 * @param height
	 *            - The height of the barcode. This is measured in pixels
	 * @param width
	 *            - This is the number of dots per module. This value should be
	 *            between 1 to 3. See section 3-42 (Rev. 1.12) for more
	 *            information on the values.
	 */
	public static void PrintCode93(Context context, String portName,
			String portSettings, byte[] barcodeData, BarCodeOption option,
			byte height, Min_Mod_Size width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x37;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_dots:
			n3 = 49;
			break;
		case _3_dots:
			n3 = 50;
			break;
		case _4_dots:
			n3 = 51;
			break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in ITF format
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param barcodeData
	 *            - These are the characters that will be printed in the
	 *            barcode. The characters available for this barcode are listed
	 *            in section 3-43 (Rev. 1.12).
	 * @param option
	 *            - This tell the printer to put characters under the printed
	 *            barcode or not. This may also be used to line feed after the
	 *            barcode is printed.
	 * @param height
	 *            - The height of the barcode. This is measured in pixels
	 * @param width
	 *            - The width of the barcode. This value should be between 1 to
	 *            9. See section 3-42 (Rev. 1.12) for more information on the
	 *            values.
	 */
	public static void PrintPospay(Activity ac, Context context,
			String portName, String portSettings, Resources res, String recep) {
		ss = new SessionManager(ac);
		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// // list.addAll(Arrays
		// // .asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
		// list.addAll(Arrays.asList(tempList));

		// command = createRasterCommand("\t\t\t\ta", 13, 0);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		/*
		 * String textToPrint = (""); command = createRasterCommand(textToPrint,
		 * 13, Typeface.BOLD); tempList = new Byte[command.length];
		 * CopyArray(command, tempList); list.addAll(Arrays.asList(tempList));
		 * 
		 * command = createRasterCommand("", 16, 1); tempList = new
		 * Byte[command.length]; CopyArray(command, tempList);
		 * list.addAll(Arrays.asList(tempList)); textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */

		String textToPrint = ("\t\t\t\t\t\t尚苑海鲜酒家");
		command = createRasterCommand(textToPrint, 27, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("MY DINING ROOM CHINESE CUISINE");
		command = createRasterCommand(textToPrint, 15, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (" \t\t\t 3 Temasek Boulevard #02-314/316\r\n"
				+ " \t\t\t Suntec City Mall Singapore 038983\r\n"
				+ " \t\t\t\t Tel: 6238 6898  |  Fax: 6238 8368\r\n"
				+ " \t\t\t\t Email: Address@webserver.com\r\n"
				+ " \t\t\t\t\t\t\t www.webserver.com\r\n"
				+ " \t\t\t\t\t\t\t\t GST No. 123456789");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (""
				// + address
				// + phone
				// + Email
				// + web
				// + Gst
				// + fax
				// + companyNo
				+ "\r\n"
				+ "\t             Receipt No. : "
				+ recep
				+ "\r\n"
				+ "\t\t                 Printed : "
				+ ComDDUtilities.getDateTimePrint()
				+ "\r\n"
				+ "\t\t                 Cashier : Cashier \r\n"
				+ "\t You are served by :  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ " \r\n"
				+ "\t\t                     Table : "
				+ Utilities.getGlobalVariable(ac).tableNum
				+ "\r\n"
				+ "\t\t                        Pax : "
				+ Utilities.getGlobalVariable(ac).numberCustomer
				+ "\r\n"
				+ "-----------------------------------------------------------------------\r"
				+ "\t\t          Items                    |  Qty  |  Price  |  Amount       \r" + "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		String textin = "";

		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" + ""
					+ PosApp.listOrderData.get(i).getItemTau() + "\r\n"
					+ "Discount    : "
					+ PosApp.listOrderData.get(i).getDiscount() + "\r\n"
					+ "\t\t\t\t\t\t\t\t\t\t\t"
					+ RedoPrice(PosApp.listOrderData.get(i).getQualyti())
					+ "\t" + ""
					+ RedoPrice(PosApp.listOrderData.get(i).getUnitPrice())
					+ "\t\t"
					+ ""
					// + RedoPrice(PosApp.listOrderData.get(i).getDiscount())
					// + "\t\t" + ""
					+ RedoPrice(PosApp.listOrderData.get(i).getAmount())
					+ "\r\n\n";
			/*
			 * textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" +
			 * "\t\t\t\t\t\t\t\t" +
			 * RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + "\t" + "" +
			 * RedoPrice(PosApp.listOrderData.get(i).getUnitPrice()) + "\t" + ""
			 * + RedoPrice(PosApp.listOrderData.get(i).getDiscount()) + "\t\t\t"
			 * + "" + RedoPrice(PosApp.listOrderData.get(i).getAmount()) +
			 * "\r\n";
			 */

		}
		Log.v("HAI", textin);
		DecimalFormat df = new DecimalFormat("####0.00");
		Double temp1 = Double.parseDouble(MainActivity.btnSubTotalValue
				.getText().toString());
		Double temp2 = Double.parseDouble(MainActivity.btnDisCountValue
				.getText().toString());
		Double temp3 = Double.parseDouble(MainActivity.btnGSTValue.getText()
				.toString());
		Double temp5 = Double.parseDouble(MainActivity.btnSVCValue.getText()
				.toString());
		Double temp4 = Double.parseDouble(MainActivity.tvTotalAmount.getText()
				.toString());
		if (MainActivity.isPrintNoSave) {
			textToPrint = (textin
					+ "-----------------------------------------------------------------------\r\n"
					+ "Subtotal         : \t\t\t\t\t" + "$" + ""
					+ df.format(temp1) + "\r\n"
					+ "Service Charge :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
					+ "$" + "" + df.format(temp5) + "\r\n"
					+ "GST         :  \t\t\t\t\t" + "$" + "" + df.format(temp3)
					+ "\r\n" + "Bill Discount  :  \t\t\t\t\t\t\t" + "$" + ""
					+ df.format(temp2) + "\r\n"
					+ "TOTAL           : \t\t\t\t\t" + "$" + "" + df
					.format(temp4));
		} else {
			textToPrint = (textin
					+ "-----------------------------------------------------------------------\r\n"
					+ "Subtotal             : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp1)
					+ "\r\n"
					+ "Service Charge : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp5)
					+ "\r\n"
					+ "GST                     : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp3)
					+ "\r\n"
					+ "Bill Discount     :  \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp2)
					+ "\r\n"
					+ "TOTAL                : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp4)
					+ "\r\n"
					+ "-----------------------------------------------------------------------\r\n"
					+ "                           Payment Detail   \t                   \r\n"
					+ "-----------------------------------------------------------------------\r\n\r\n"
					+ MainActivity.creaditType + "\t\t\t\t\t:\t\t\t\t$"
					+ df.format(temp4) + "  \r\n" + "Change               :\t\t\t\t$0.00\r\n");
		}

		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		textToPrint = "\r\n NOTE : \r\n" + MainActivity.note
				+ "\r\n\r\n"
				+ "";

		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = "\t\t\t\t\t 感谢您对尚苑海鲜酒家的惠顾，\r\n\t\t\t\t\t\t\t\t\t 欢迎您再次光临，\r\n\t Thank You For Dining At My Dining Room,\r\n\t\t\t\t\t\t\t\t Please Come Again.";
		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
//		int ab = 1;
//		try {
//			ab = Integer.parseInt(ss.getNumPrint());
//		} catch (Exception e) {
//			ab = 1;
//		}
//		for (int i = 0; i < ab; i++) {
			sendCommand(context, portName, portSettings, list);
//		}

	}
	public static void PrintPosPay(Activity ac, Context context,
			String portName, String portSettings, Resources res, String recep,
			String cash, String masterCash, String cashValue, String masterCashValue, String redeemValue) {

		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// // list.addAll(Arrays
		// // .asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
		// list.addAll(Arrays.asList(tempList));

		// command = createRasterCommand("\t\t\t\ta", 13, 0);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		/*
		 * String textToPrint = (""); command = createRasterCommand(textToPrint,
		 * 13, Typeface.BOLD); tempList = new Byte[command.length];
		 * CopyArray(command, tempList); list.addAll(Arrays.asList(tempList));
		 * 
		 * command = createRasterCommand("", 16, 1); tempList = new
		 * Byte[command.length]; CopyArray(command, tempList);
		 * list.addAll(Arrays.asList(tempList)); textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */

		String textToPrint = ("\t\t\t\t\t\t尚苑海鲜酒家");
		command = createRasterCommand(textToPrint, 27, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("MY DINING ROOM CHINESE CUISINE");
		command = createRasterCommand(textToPrint, 15, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (" \t\t\t 3 Temasek Boulevard #02-314/316\r\n"
				+ " \t\t\t Suntec City Mall Singapore 038983\r\n"
				+ " \t\t\t\t Tel: 6238 6898  |  Fax: 6238 8368\r\n"
				+ " \t\t\t\t Email: Address@webserver.com\r\n"
				+ " \t\t\t\t\t\t\t www.webserver.com\r\n"
				+ " \t\t\t\t\t\t\t\t GST No. 123456789");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (""
				// + address
				// + phone
				// + Email
				// + web
				// + Gst
				// + fax
				// + companyNo
				+ "\r\n"
				+ "\t             Receipt No. : "
				+ recep
				+ "\r\n"
				+ "\t\t                 Printed : "
				+ ComDDUtilities.getDateTimePrint()
				+ "\r\n"
				+ "\t\t                 Cashier : Cashier \r\n"
				+ "\t You are served by :  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ " \r\n"
				+ "\t\t                     Table : "
				+ Utilities.getGlobalVariable(ac).tableNum
				+ "\r\n"
				+ "\t\t                        Pax : "
				+ Utilities.getGlobalVariable(ac).numberCustomer
				+ "\r\n"
				+ "-----------------------------------------------------------------------\r"
				+ "\t\t          Items                    |  Qty  |  Price  |  Amount       \r" + "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		String textin = "";

		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" + ""
					+ PosApp.listOrderData.get(i).getItemTau() + "\r\n"
					+ "Discount    : "
					+ PosApp.listOrderData.get(i).getDiscount() + "\r\n"
					+ "\t\t\t\t\t\t\t\t\t\t\t"
					+ RedoPrice(PosApp.listOrderData.get(i).getQualyti())
					+ "\t" + ""
					+ RedoPrice(PosApp.listOrderData.get(i).getUnitPrice())
					+ "\t\t"
					+ ""
					// + RedoPrice(PosApp.listOrderData.get(i).getDiscount())
					// + "\t\t" + ""
					+ RedoPrice(PosApp.listOrderData.get(i).getAmount())
					+ "\r\n\n";
			/*
			 * textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" +
			 * "\t\t\t\t\t\t\t\t" +
			 * RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + "\t" + "" +
			 * RedoPrice(PosApp.listOrderData.get(i).getUnitPrice()) + "\t" + ""
			 * + RedoPrice(PosApp.listOrderData.get(i).getDiscount()) + "\t\t\t"
			 * + "" + RedoPrice(PosApp.listOrderData.get(i).getAmount()) +
			 * "\r\n";
			 */

		}
		Log.v("HAI", textin);
		DecimalFormat df = new DecimalFormat("####0.00");
		Double temp1 = Double.parseDouble(MainActivity.btnSubTotalValue
				.getText().toString());
		Double temp2 = Double.parseDouble(MainActivity.btnDisCountValue
				.getText().toString());
		Double temp3 = Double.parseDouble(MainActivity.btnGSTValue.getText()
				.toString());
		Double temp5 = Double.parseDouble(MainActivity.btnSVCValue.getText()
				.toString());
		Double temp4 = Double.parseDouble(MainActivity.tvTotalAmount.getText()
				.toString());
		double temp6 = DialogPayment.change;
		
			textToPrint = (textin
					+ "-----------------------------------------------------------------------\r\n"
					+ "Subtotal             : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp1)
					+ "\r\n"
					+ "Service Charge : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp5)
					+ "\r\n"
					+ "GST                     : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp3)
					+ "\r\n"
					+ "Bill Discount     :  \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp2)
					+ "\r\n"
					+ "TOTAL                : \t\t\t\t\t\t\t\t\t\t\t"
					+ "$"
					+ ""
					+ df.format(temp4)
					+ "\r\n"
					+ "-----------------------------------------------------------------------\r\n"
					+ "                           Payment Detail   \t                   \r\n"
					+ "-----------------------------------------------------------------------\r\n\r\n");
			if(!redeemValue.equals("0.00")){
				textToPrint += ("Redeem \t\t\t\t\t:\t\t\t\t$"
						+ redeemValue
						+ "  \r\n");
			}
		
			if(!cashValue.equals("0.00")){
				if(cash.equals("VISA")){
					cash = "VISA       ";;
				}else if (cash.equals("MASTER")){
					cash = "MASTER ";;
				}else if (cash.equals("AMEX")){
					cash = "AMEX    ";
				}else if (cash.equals("UNION")){
					cash = "UNION  ";
				}else if (cash.equals("JCB")){
					cash = "JCB         ";
				}else if (cash.equals("DINERS")){
					cash = "DINERS ";
				}
				textToPrint += (cash + "\t\t\t\t\t:\t\t\t\t$"
						+ cashValue
						+ "  \r\n");
			}
			if(!masterCashValue.equals("0.00")){
				if(masterCash.equals("VISA")){
					masterCash = "VISA       ";;
				}else if (cash.equals("MASTER")){
					masterCash = "MASTER ";;
				}else if (cash.equals("AMEX")){
					masterCash = "AMEX    ";
				}else if (cash.equals("UNION")){
					masterCash = "UNION  ";
				}else if (cash.equals("JCB")){
					masterCash = "JCB         ";
				}else if (cash.equals("DINERS")){
					masterCash = "DINERS ";
				}
				textToPrint += (masterCash + "\t\t\t\t\t:\t\t\t\t$"
						+ masterCashValue
						+ "  \r\n");
			}
			textToPrint += ("Change               :\t\t\t\t$"
					+ df.format(temp6)
					+ "\r\n");

		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		textToPrint = "\r\n NOTE : \r\n" + MainActivity.note
				+ "\r\n\r\n"
				+ "";

		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = "\t\t\t\t\t 感谢您对尚苑海鲜酒家的惠顾，\r\n\t\t\t\t\t\t\t\t\t 欢迎您再次光临，\r\n\t Thank You For Dining At My Dining Room,\r\n\t\t\t\t\t\t\t\t Please Come Again.";
		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
		sendCommand(context, portName, portSettings, list);
	}

	public static void PrintPosBill(Activity ac, Context context,
			String portName, String portSettings, Resources res, String recep) {

		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// // list.addAll(Arrays
		// // .asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
		// list.addAll(Arrays.asList(tempList));

		// command = createRasterCommand("\t\t\t\ta", 13, 0);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		/*
		 * String textToPrint = (""); command = createRasterCommand(textToPrint,
		 * 13, Typeface.BOLD); tempList = new Byte[command.length];
		 * CopyArray(command, tempList); list.addAll(Arrays.asList(tempList));
		 * 
		 * command = createRasterCommand("", 16, 1); tempList = new
		 * Byte[command.length]; CopyArray(command, tempList);
		 * list.addAll(Arrays.asList(tempList)); textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */

		String textToPrint = ("\t\t\t\t\t\t尚苑海鲜酒家");
		command = createRasterCommand(textToPrint, 27, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("MY DINING ROOM CHINESE CUISINE");
		command = createRasterCommand(textToPrint, 15, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (" \t\t\t 3 Temasek Boulevard #02-314/316\r\n"
				+ " \t\t\t Suntec City Mall Singapore 038983\r\n"
				+ " \t\t\t\t Tel: 6238 6898  |  Fax: 6238 8368\r\n"
				+ " \t\t\t\t Email: Address@webserver.com\r\n"
				+ " \t\t\t\t\t\t\t www.webserver.com\r\n"
				+ " \t\t\t\t\t\t\t\t GST No. 123456789");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (""
				// + address
				// + phone
				// + Email
				// + web
				// + Gst
				// + fax
				// + companyNo
				+ "\r\n"
				+ "\t             Receipt No. : "
				+ recep
				+ "\r\n"
				+ "\t\t                 Printed : "
				+ ComDDUtilities.getDateTimePrint()
				+ "\r\n"
				+ "\t\t                 Cashier : Cashier \r\n"
				+ "\t You are served by :  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ " \r\n"
				+ "\t\t                     Table : "
				+ Utilities.getGlobalVariable(ac).tableNum
				+ "\r\n"
				+ "\t\t                        Pax : "
				+ Utilities.getGlobalVariable(ac).numberCustomer
				+ "\r\n"
				+ "-----------------------------------------------------------------------\r"
				+ "\t\t          Items                    |  Qty  |  Price  |  Amount       \r" + "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		String textin = "";

		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" + ""
					+ PosApp.listOrderData.get(i).getItemTau() + "\r\n"
					+ "Discount    : "
					+ PosApp.listOrderData.get(i).getDiscount() + "\r\n"
					+ "\t\t\t\t\t\t\t\t\t\t\t"
					+ RedoPrice(PosApp.listOrderData.get(i).getQualyti())
					+ "\t" + ""
					+ RedoPrice(PosApp.listOrderData.get(i).getUnitPrice())
					+ "\t\t"
					+ ""
					// + RedoPrice(PosApp.listOrderData.get(i).getDiscount())
					// + "\t\t" + ""
					+ RedoPrice(PosApp.listOrderData.get(i).getAmount())
					+ "\r\n\n";
			/*
			 * textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" +
			 * "\t\t\t\t\t\t\t\t" +
			 * RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + "\t" + "" +
			 * RedoPrice(PosApp.listOrderData.get(i).getUnitPrice()) + "\t" + ""
			 * + RedoPrice(PosApp.listOrderData.get(i).getDiscount()) + "\t\t\t"
			 * + "" + RedoPrice(PosApp.listOrderData.get(i).getAmount()) +
			 * "\r\n";
			 */

		}
		Log.v("HAI", textin);
		DecimalFormat df = new DecimalFormat("####0.00");
		Double temp1 = Double.parseDouble(MainActivity.btnSubTotalValue
				.getText().toString());
		Double temp2 = Double.parseDouble(MainActivity.btnDisCountValue
				.getText().toString());
		Double temp3 = Double.parseDouble(MainActivity.btnGSTValue.getText()
				.toString());
		Double temp5 = Double.parseDouble(MainActivity.btnSVCValue.getText()
				.toString());
		Double temp4 = Double.parseDouble(MainActivity.tvTotalAmount.getText()
				.toString());
		if (MainActivity.isPrintNoSave) {
			textToPrint = (textin
					+ "-----------------------------------------------------------------------\r\n"
					+ "Subtotal         : \t\t\t\t\t" + "$" + ""
					+ df.format(temp1) + "\r\n"
					+ "Service Charge :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
					+ "$" + "" + df.format(temp5) + "\r\n"
					+ "GST         :  \t\t\t\t\t" + "$" + "" + df.format(temp3)
					+ "\r\n" + "Bill Discount  :  \t\t\t\t\t\t\t" + "$" + ""
					+ df.format(temp2) + "\r\n"
					+ "TOTAL           : \t\t\t\t\t" + "$" + "" + df
					.format(temp4));
		} else {
			textToPrint = (textin
					+ "-----------------------------------------------------------------------\r\n"
					+ "Subtotal             : \t\t\t\t\t\t\t\t\t\t\t" + "$"
					+ "" + df.format(temp1) + "\r\n"
					+ "Service Charge : \t\t\t\t\t\t\t\t\t\t\t" + "$" + ""
					+ df.format(temp5) + "\r\n"
					+ "GST                     : \t\t\t\t\t\t\t\t\t\t\t" + "$"
					+ "" + df.format(temp3) + "\r\n"
					+ "Bill Discount     :  \t\t\t\t\t\t\t\t\t\t\t" + "$" + ""
					+ df.format(temp2) + "\r\n"
					+ "TOTAL                : \t\t\t\t\t\t\t\t\t\t\t" + "$"
					+ "" + df.format(temp4) + "\r\n"
			// +
			// "-----------------------------------------------------------------------\r\n"
			// +
			// "                           Payment Detail   \t                   \r\n"
			// +
			// "-----------------------------------------------------------------------\r\n\r\n"
			// + "d.loadPaymentNett()" + "\r\n" +
			// "Change               : \t\t\t $0.00 \r\n"
			);
		}

		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		/*
		 * textToPrint =
		 * "\t\t\t\t\t 感谢您对尚苑海鲜酒家的惠顾，\r\n\t\t\t\t\t\t\t\t\t 欢饮您再次光临，\r\n\t Thank You For Dining At My Dining Room,\r\n\t\t\t\t\t\t\t\t Please Come Again."
		 * ; command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		 * tempList = new Byte[command.length]; CopyArray(command, tempList);
		 * list.addAll(Arrays.asList(tempList));
		 */

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
		sendCommand(context, portName, portSettings, list);
	}

	public static void PrintCodeITF(Context context, String portName,
			String portSettings, byte[] barcodeData, BarCodeOption option,
			byte height, NarrowWideV2 width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x35;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_5:
			n3 = 49;
			break;
		case _4_10:
			n3 = 50;
			break;
		case _6_15:
			n3 = 51;
			break;
		case _2_4:
			n3 = 52;
			break;
		case _4_8:
			n3 = 53;
			break;
		case _6_12:
			n3 = 54;
			break;
		case _2_6:
			n3 = 55;
			break;
		case _3_9:
			n3 = 56;
			break;
		case _4_12:
			n3 = 57;
			break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in the 128 format
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param barcodeData
	 *            - These are the characters that will be printed in the
	 *            barcode. The characters available for this barcode are listed
	 *            in section 3-43 (Rev. 1.12).
	 * @param option
	 *            - This tell the printer to put characters under the printed
	 *            barcode or not. This may also be used to line feed after the
	 *            barcode is printed.
	 * @param height
	 *            - The height of the barcode. This is measured in pixels
	 * @param width
	 *            - This is the number of dots per module. This value should be
	 *            between 1 to 3. See section 3-42 (Rev. 1.12) for more
	 *            information on the values.
	 */
	public static void PrintCode128(Context context, String portName,
			String portSettings, byte[] barcodeData, BarCodeOption option,
			byte height, Min_Mod_Size width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x36;
		byte n2 = 0;
		switch (option) {
		case No_Added_Characters_With_Line_Feed:
			n2 = 49;
			break;
		case Adds_Characters_With_Line_Feed:
			n2 = 50;
			break;
		case No_Added_Characters_Without_Line_Feed:
			n2 = 51;
			break;
		case Adds_Characters_Without_Line_Feed:
			n2 = 52;
			break;
		}
		byte n3 = 0;
		switch (width) {
		case _2_dots:
			n3 = 49;
			break;
		case _3_dots:
			n3 = 50;
			break;
		case _4_dots:
			n3 = 51;
			break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function shows different cut patterns for Star POS printers.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param cuttype
	 *            - The cut type to perform. The cut types are full cut, full
	 *            cut with feed, partial cut, and partial cut with feed
	 */
	public static void performCut(Context context, String portName,
			String portSettings, CutType cuttype) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] autocutCommand = new Byte[] { 0x1b, 0x64, 0x00 };
		switch (cuttype) {
		case FULL_CUT:
			autocutCommand[2] = 48;
			break;
		case PARTIAL_CUT:
			autocutCommand[2] = 49;
			break;
		case FULL_CUT_FEED:
			autocutCommand[2] = 50;
			break;
		case PARTIAL_CUT_FEED:
			autocutCommand[2] = 51;
			break;
		}

		AddRange(commands, autocutCommand);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw text to the printer, showing how the text can be
	 * formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param slashedZero
	 *            - boolean variable to tell the printer to slash zeroes
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * @param invertColor
	 *            - boolean variable that tells the printer to should invert
	 *            text. All white space will become black but the characters
	 *            will be left white.
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * @param heightExpansion
	 *            - This integer tells the printer what the character height
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param widthExpansion
	 *            - This integer tell the printer what the character width
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintText(Context context, String portName,
			String portSettings, boolean slashedZero, boolean underline,
			boolean invertColor, boolean emphasized, boolean upperline,
			boolean upsideDown, int heightExpansion, int widthExpansion,
			byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		Byte[] slashedZeroCommand = new Byte[] { 0x1b, 0x2f, 0x00 };
		if (slashedZero) {
			slashedZeroCommand[2] = 49;
		} else {
			slashedZeroCommand[2] = 48;
		}
		AddRange(commands, slashedZeroCommand);

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] invertColorCommand = new Byte[] { 0x1b, 0x00 };
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		AddRange(commands, invertColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterExpansion = new Byte[] { 0x1b, 0x69, 0x00, 0x00 };
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		AddRange(commands, characterExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		for (int i = 0; i < textData.length; i++) {
			commands.add(textData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw text to the printer, showing how the text can be
	 * formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param slashedZero
	 *            - boolean variable to tell the printer to slash zeroes
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * 
	 * 
	 * @param twoColor
	 *            - boolean variable that tells the printer to should print red
	 *            or black text.
	 * 
	 * 
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * @param heightExpansion
	 *            - boolean variable that tells the printer to should expand
	 *            double-tall printing.
	 * 
	 * @param widthExpansion
	 *            - boolean variable that tells the printer to should expand
	 *            double-wide printing.
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextbyDotPrinter(Context context, String portName,
			String portSettings, boolean slashedZero, boolean underline,
			boolean twoColor, boolean emphasized, boolean upperline,
			boolean upsideDown, boolean heightExpansion,
			boolean widthExpansion, byte leftMargin, Alignment alignment,
			byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		Byte[] slashedZeroCommand = new Byte[] { 0x1b, 0x2f, 0x00 };
		if (slashedZero) {
			slashedZeroCommand[2] = 49;
		} else {
			slashedZeroCommand[2] = 48;
		}
		AddRange(commands, slashedZeroCommand);

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] twoColorCommand = new Byte[] { 0x1b, 0x00 };
		if (twoColor) {
			twoColorCommand[1] = 0x34;
		} else {
			twoColorCommand[1] = 0x35;
		}
		AddRange(commands, twoColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterheightExpansion = new Byte[] { 0x1b, 0x68, 0x00 };
		if (heightExpansion) {
			characterheightExpansion[2] = 49;
		} else {
			characterheightExpansion[2] = 48;
		}
		AddRange(commands, characterheightExpansion);

		Byte[] characterwidthExpansion = new Byte[] { 0x1b, 0x57, 0x00 };
		if (widthExpansion) {
			characterwidthExpansion[2] = 49;
		} else {
			characterwidthExpansion[2] = 48;
		}
		AddRange(commands, characterwidthExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		for (int i = 0; i < textData.length; i++) {
			commands.add(textData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw JP-Kanji text to the printer, showing how the
	 * text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param shiftJIS
	 *            - boolean variable to tell the printer to Shift-JIS/JIS
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * @param invertColor
	 *            - boolean variable that tells the printer to should invert
	 *            text. All white space will become black but the characters
	 *            will be left white.
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * @param heightExpansion
	 *            - This integer tells the printer what the character height
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param widthExpansion
	 *            - This integer tell the printer what the character width
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextKanji(Context context, String portName,
			String portSettings, boolean shiftJIS, boolean underline,
			boolean invertColor, boolean emphasized, boolean upperline,
			boolean upsideDown, int heightExpansion, int widthExpansion,
			byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		if (shiftJIS) {
			Byte[] kanjiModeCommand = new Byte[] { 0x1b, 0x71, 0x1b, 0x24, 0x31 }; // Shift-JIS
																					// Kanji
																					// Mode(Disable
																					// JIS(ESC
																					// q)
																					// +
																					// Enable
																					// Shift-JIS(ESC
																					// $
																					// n))
			AddRange(commands, kanjiModeCommand);
		} else {
			Byte[] kanjiModeCommand = new Byte[] { 0x1b, 0x24, 0x30, 0x1b, 0x70 }; // JIS
																					// Kanji
																					// Mode(Disable
																					// Shift-JIS(ESC
																					// $
																					// n)
																					// +
																					// Enable
																					// JIS(ESC
																					// p))
			AddRange(commands, kanjiModeCommand);
		}

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] invertColorCommand = new Byte[] { 0x1b, 0x00 };
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		AddRange(commands, invertColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterExpansion = new Byte[] { 0x1b, 0x69, 0x00, 0x00 };
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		AddRange(commands, characterExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			if (shiftJIS) {
				rawData = strData.getBytes("Shift_JIS"); // Shift JIS code
			} else {
				rawData = strData.getBytes("ISO2022JP"); // JIS code
			}
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw JP-Kanji text to the printer, showing how the
	 * text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param shiftJIS
	 *            - boolean variable to tell the printer to Shift-JIS/JIS
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * 
	 * @param twoColor
	 *            - boolean variable that tells the printer to should print red
	 *            or black text.
	 * 
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * 
	 * @param widthExpansion
	 *            - boolean variable that tells the printer to should expand
	 *            double-wide printing.
	 * 
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextKanjibyDotPrinter(Context context,
			String portName, String portSettings, boolean shiftJIS,
			boolean underline, boolean twoColor, boolean emphasized,
			boolean upperline, boolean upsideDown, boolean widthExpansion,
			byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		if (shiftJIS) {
			Byte[] kanjiModeCommand = new Byte[] { 0x1b, 0x71, 0x1b, 0x24, 0x31 }; // Shift-JIS
																					// Kanji
																					// Mode(Disable
																					// JIS(ESC
																					// q)
																					// +
																					// Enable
																					// Shift-JIS(ESC
																					// $
																					// n))
			AddRange(commands, kanjiModeCommand);
		} else {
			Byte[] kanjiModeCommand = new Byte[] { 0x1b, 0x24, 0x30, 0x1b, 0x70 }; // JIS
																					// Kanji
																					// Mode(Disable
																					// Shift-JIS(ESC
																					// $
																					// n)
																					// +
																					// Enable
																					// JIS(ESC
																					// p))
			AddRange(commands, kanjiModeCommand);
		}

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] twoColorCommand = new Byte[] { 0x1b, 0x00 };
		if (twoColor) {
			twoColorCommand[1] = 0x34;
		} else {
			twoColorCommand[1] = 0x35;
		}
		AddRange(commands, twoColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterwidthExpansion = new Byte[] { 0x1b, 0x57, 0x00 };
		if (widthExpansion) {
			characterwidthExpansion[2] = 49;
		} else {
			characterwidthExpansion[2] = 48;
		}
		AddRange(commands, characterwidthExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			if (shiftJIS) {
				rawData = strData.getBytes("Shift_JIS"); // Shift JIS code
			} else {
				rawData = strData.getBytes("ISO2022JP"); // JIS code
			}
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw Traditional Chinese text to the printer, showing
	 * how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * @param invertColor
	 *            - boolean variable that tells the printer to should invert
	 *            text. All white space will become black but the characters
	 *            will be left white.
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * @param heightExpansion
	 *            - This integer tells the printer what the character height
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param widthExpansion
	 *            - This integer tell the printer what the character width
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextTraditionalChinese(Context context,
			String portName, String portSettings, boolean underline,
			boolean invertColor, boolean emphasized, boolean upperline,
			boolean upsideDown, int heightExpansion, int widthExpansion,
			byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		// if(shiftJIS)
		// {
		// Byte[] kanjiModeCommand = new Byte[] {0x1b, 0x71, 0x1b, 0x24, 0x31};
		// // Shift-JIS Kanji Mode(Disable JIS(ESC q) + Enable Shift-JIS(ESC $
		// n))
		// AddRange(commands, kanjiModeCommand);
		// }
		// else
		// {
		// Byte[] kanjiModeCommand = new Byte[] {0x1b, 0x24, 0x30, 0x1b, 0x70};
		// // JIS Kanji Mode(Disable Shift-JIS(ESC $ n) + Enable JIS(ESC p))
		// AddRange(commands, kanjiModeCommand);
		// }

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] invertColorCommand = new Byte[] { 0x1b, 0x00 };
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		AddRange(commands, invertColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterExpansion = new Byte[] { 0x1b, 0x69, 0x00, 0x00 };
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		AddRange(commands, characterExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			rawData = strData.getBytes("Big5");
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw Simplified Chinese text to the printer, showing
	 * how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * @param invertColor
	 *            - boolean variable that tells the printer to should invert
	 *            text. All white space will become black but the characters
	 *            will be left white.
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * @param heightExpansion
	 *            - This integer tells the printer what the character height
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param widthExpansion
	 *            - This integer tell the printer what the character width
	 *            should be, ranging from 0 to 5 and representing multiples from
	 *            1 to 6.
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextSimplifiedChinese(Context context,
			String portName, String portSettings, boolean underline,
			boolean invertColor, boolean emphasized, boolean upperline,
			boolean upsideDown, int heightExpansion, int widthExpansion,
			byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		// if(shiftJIS)
		// {
		// Byte[] kanjiModeCommand = new Byte[] {0x1b, 0x71, 0x1b, 0x24, 0x31};
		// // Shift-JIS Kanji Mode(Disable JIS(ESC q) + Enable Shift-JIS(ESC $
		// n))
		// AddRange(commands, kanjiModeCommand);
		// }
		// else
		// {
		// Byte[] kanjiModeCommand = new Byte[] {0x1b, 0x24, 0x30, 0x1b, 0x70};
		// // JIS Kanji Mode(Disable Shift-JIS(ESC $ n) + Enable JIS(ESC p))
		// AddRange(commands, kanjiModeCommand);
		// }

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] invertColorCommand = new Byte[] { 0x1b, 0x00 };
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		AddRange(commands, invertColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterExpansion = new Byte[] { 0x1b, 0x69, 0x00, 0x00 };
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		AddRange(commands, characterExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			rawData = strData.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw Simplified Chinese text to the printer, showing
	 * how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * 
	 * 
	 * @param twoColor
	 *            - boolean variable that tells the printer to should print red
	 *            or black text.
	 * 
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * 
	 * @param widthExpansion
	 *            - boolean variable that tells the printer to should expand
	 *            double-wide printing.
	 * 
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextSimplifiedChinesebyDotPrinter(Context context,
			String portName, String portSettings, boolean underline,
			boolean twoColor, boolean emphasized, boolean upperline,
			boolean upsideDown, boolean widthExpansion, byte leftMargin,
			Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] twoColorCommand = new Byte[] { 0x1b, 0x00 };
		if (twoColor) {
			twoColorCommand[1] = 0x34;
		} else {
			twoColorCommand[1] = 0x35;
		}
		AddRange(commands, twoColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterwidthExpansion = new Byte[] { 0x1b, 0x57, 0x00 };
		if (widthExpansion) {
			characterwidthExpansion[2] = 49;
		} else {
			characterwidthExpansion[2] = 48;
		}
		AddRange(commands, characterwidthExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			rawData = strData.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw Traditional Chinese text to the printer, showing
	 * how the text can be formated. Ex: Changing size
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param underline
	 *            - boolean variable that tells the printer to underline the
	 *            text
	 * 
	 * 
	 * @param twoColor
	 *            - boolean variable that tells the printer to should print red
	 *            or black text.
	 * 
	 * @param emphasized
	 *            - boolean variable that tells the printer to should emphasize
	 *            the printed text. This is somewhat like bold. It isn't as
	 *            dark, but darker than regular characters.
	 * @param upperline
	 *            - boolean variable that tells the printer to place a line
	 *            above the text. This is only supported by newest printers.
	 * @param upsideDown
	 *            - boolean variable that tells the printer to print text upside
	 *            down.
	 * 
	 * @param widthExpansion
	 *            - boolean variable that tells the printer to should expand
	 *            double-wide printing.
	 * 
	 * @param leftMargin
	 *            - Defines the left margin for text on Star portable printers.
	 *            This number can be from 0 to 65536. However, remember how much
	 *            space is available as the text can be pushed off the page.
	 * @param alignment
	 *            - Defines the alignment of the text. The printers support
	 *            left, right, and center justification.
	 * @param textData
	 *            - The text to send to the printer.
	 */
	public static void PrintTextTraditionalChinesebyDotPrinter(Context context,
			String portName, String portSettings, boolean underline,
			boolean twoColor, boolean emphasized, boolean upperline,
			boolean upsideDown, boolean widthExpansion, byte leftMargin,
			Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[] { 0x1b, 0x40 }; // Initialization
		AddRange(commands, initCommand);

		Byte[] underlineCommand = new Byte[] { 0x1b, 0x2d, 0x00 };
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] twoColorCommand = new Byte[] { 0x1b, 0x00 };
		if (twoColor) {
			twoColorCommand[1] = 0x34;
		} else {
			twoColorCommand[1] = 0x35;
		}
		AddRange(commands, twoColorCommand);

		Byte[] emphasizedPrinting = new Byte[] { 0x1b, 0x00 };
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[] { 0x1b, 0x5f, 0x00 };
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterwidthExpansion = new Byte[] { 0x1b, 0x57, 0x00 };
		if (widthExpansion) {
			characterwidthExpansion[2] = 49;
		} else {
			characterwidthExpansion[2] = 48;
		}
		AddRange(commands, characterwidthExpansion);

		Byte[] leftMarginCommand = new Byte[] { 0x1b, 0x6c, 0x00 };
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[] { 0x1b, 0x1d, 0x61, 0x00 };
		switch (alignment) {
		case Left:
			alignmentCommand[3] = 48;
			break;
		case Center:
			alignmentCommand[3] = 49;
			break;
		case Right:
			alignmentCommand[3] = 50;
			break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			rawData = strData.getBytes("Big5");
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print a Java bitmap directly to the printer.
	 * There are 2 ways a printer can print images: through raster commands or
	 * line mode commands This function uses raster commands to print an image.
	 * Raster is supported on the TSP100 and all Star Thermal POS printers. Line
	 * mode printing is not supported by the TSP100. There is no example of
	 * using this method in this sample.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param source
	 *            - The bitmap to convert to Star Raster data
	 * @param maxWidth
	 *            - The maximum width of the image to print. This is usually the
	 *            page width of the printer. If the image exceeds the maximum
	 *            width then the image is scaled down. The ratio is maintained.
	 */
	public static void PrintBitmap(Context context, String portName,
			String portSettings, Bitmap source, int maxWidth,
			boolean compressionEnable) {
		try {
			ArrayList<Byte> commands = new ArrayList<Byte>();
			Byte[] tempList;

			RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
					RasPageEndMode.FeedAndFullCut,
					RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0,
					0);
			StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);

			byte[] command = rasterDoc.BeginDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			commands.addAll(Arrays.asList(tempList));

			command = starbitmap
					.getImageRasterDataForPrinting(compressionEnable);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			commands.addAll(Arrays.asList(tempList));

			command = rasterDoc.EndDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			commands.addAll(Arrays.asList(tempList));

			sendCommand(context, portName, portSettings, commands);
		} catch (OutOfMemoryError e) {
			throw e;
		}

	}

	/**
	 * This function is used to print a Java bitmap directly to the printer.
	 * There are 2 ways a printer can print images: through raster commands or
	 * line mode commands This function uses raster commands to print an image.
	 * Raster is supported on the TSP100 and all Star Thermal POS printers. Line
	 * mode printing is not supported by the TSP100. There is no example of
	 * using this method in this sample.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param res
	 *            - The resources object containing the image data
	 * @param source
	 *            - The resource id of the image data
	 * @param maxWidth
	 *            - The maximum width of the image to print. This is usually the
	 *            page width of the printer. If the image exceeds the maximum
	 *            width then the image is scaled down. The ratio is maintained.
	 */
	public static void PrintBitmapImage(Context context, String portName,
			String portSettings, Resources res, int source, int maxWidth,
			boolean compressionEnable) {
		ArrayList<Byte> commands = new ArrayList<Byte>();
		Byte[] tempList;

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);

		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		command = starbitmap.getImageRasterDataForPrinting(compressionEnable);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * MSR functionality is supported on Star portable printers only.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user that this
	 *            function is not supported
	 */
	public static void MCRStart(Context context) {
		Builder dialog = new AlertDialog.Builder(context);
		dialog.setNegativeButton("Ok", null);
		AlertDialog alert = dialog.create();
		alert.setTitle("Feature Not Available");
		alert.setMessage("MSR functionality is supported only on portable printer models");
		alert.setCancelable(false);
		alert.show();
	}

	/**
	 * This function shows how to print the receipt data of a Impact Dot Matrix
	 * printer.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param res
	 *            - The resources object containing the image data. ( e.g.)
	 *            getResources())
	 */
	public static void PrintSampleReceiptbyDotPrinter(Context context,
			String portName, String portSettings) {
		byte[] data;
		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 })); // Alignment
																			// (center)

		// data = "[If loaded.. Logo1 goes here]\r\n".getBytes();
		// tempList = new Byte[data.length];
		// CopyArray(data, tempList);
		// list.addAll(Arrays.asList(tempList));
		//
		// list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00,
		// '\r', '\n'})); //Stored Logo Printing <ESC> <FC> <p> n m

		data = "\nStar Clothing Boutique\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "123 Star Road\r\nCity, State 12345\r\n\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 })); // Set horizontal tab <ESC> <D> n1 n2 ...nk NUL

		data = "Date: MM/DD/YYYY".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "             Time:HH:MM PM\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "------------------------------------------\r\n\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x45 })); // bold

		data = "SALE \r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x46 })); // bolf off

		data = "SKU ".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		list.addAll(Arrays.asList(new Byte[] { 0x09 }));

		// Notice that we use a unicode representation because that is how Java
		// expresses these bytes as double byte unicode
		// This will TAB to the next horizontal position
		data = "Description \u0009 Total\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "300678566 \u0009PLAIN T-SHIRT\u0009  10.99\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "300692003 \u0009BLACK DENIM\u0009  29.99\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "300651148 \u0009BLUE DENIM\u0009  29.99\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "300642980 \u0009STRIPED DRESS\u0009  49.99\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "300638471 \u0009BLACK BOOTS\u0009  35.99\r\n\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "Subtotal \u0009\u0009 156.95\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "Tax \u0009\u0009   0.00\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "------------------------------------------\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "Total".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		// Character expansion
		list.addAll(Arrays.asList(new Byte[] { 0x06, 0x09, 0x1b, 0x69, 0x01,
				0x01 }));

		data = "                  $156.95\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x69, 0x00, 0x00 })); // Cancel
																			// Character
																			// Expansion

		data = "------------------------------------------\r\n\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "Charge\r\n159.95\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "Visa XXXX-XXXX-XXXX-0123\r\n\r\n".getBytes();
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = "\u001b\u0034Refunds and Exchanges\u001b\u0035\r\n".getBytes(); // Specify/Cancel
																				// White/Black
																				// Invert
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		data = ("Within " + "\u001b\u002d\u0001" + "30 days\u001b\u002d\u0000"
				+ " with receipt\r\n").getBytes(); // Specify/Cancel Underline
													// Printing
		tempList = new Byte[data.length];
		CopyArray(data, tempList);
		list.addAll(Arrays.asList(tempList));

		// data = ("And tags attached\r\n\r\n").getBytes();
		// tempList = new Byte[data.length];
		// CopyArray(data, tempList);
		// list.addAll(Arrays.asList(tempList));

		// 1D barcode example
		// list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x01}));
		// list.addAll(Arrays.asList(new Byte[]{0x1b, 0x62, 0x06, 0x02, 0x02}));
		//
		// data = (" 12ab34cd56\u001e\r\n").getBytes();
		// tempList = new Byte[data.length];
		// CopyArray(data, tempList);
		// list.addAll(Arrays.asList(tempList));

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x64, 0x02 })); // Cut
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash drawer

		sendCommand(context, portName, portSettings, list);
	}

	/**
	 * This function shows how to print the receipt data of a thermal POS
	 * printer.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param commandType
	 *            - Command type to use for printing. This should be ("Line" or
	 *            "Raster")
	 * @param res
	 *            - The resources object containing the image data. ( e.g.)
	 *            getResources())
	 * @param strPrintArea
	 *            - Printable area size, This should be ("3inch (78mm)" or
	 *            "4inch (112mm)")
	 */
	public static String RedoPrice(String s) {
		int leng = s.length();
		int lengvar = 4 - leng;
		for (int i = 0; i < lengvar; i++) {
			s += "\t";
		}
		return s;
	}

	public static String Redo(String s) {
		String s1 = "";
		int leng = s.length();
		int lengvar = 27 - leng;
		for (int i = 0; i < lengvar; i++) {
			s1 += "\t";
		}
		s1 += s;
		return s1;
	}

	public static String RedoAddress(String s) {
		String s1 = "";
		int leng = s.length();
		int lengvar = 15 - leng;
		int cc = lengvar + (leng / 2);
		for (int i = 0; i < cc; i++) {
			s1 += "\t";
		}
		// s1 += s;
		return s1 + s;
		// return s;
	}
	public static void PrintPosCOPY(Activity ac, final Context context, final String portName,
			final String portSettings, Resources res, String recep) {

		final ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));


		String textToPrint = ("");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = createRasterCommand("", 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		/*
		 * textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */
		textToPrint = ("" + "Terminal           \t:  Section 1" + "\r\n"
				+ "User                     \t:  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ "\r\n" + "Printed              \t:  "
				+ ComDDUtilities.getDateTimePrint() + "\r\n");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("              TABLE         PAX" + "\r\n"
				+ "                   "
				+ Utilities.getGlobalVariable(ac).tableNum/* tabnum */
				+ "                 "
				+ Utilities.getGlobalVariable(ac).numberCustomer + "\r");
		command = createRasterCommand(textToPrint, 19, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("-----------------------------------------------------------------------\r"
				+ "     QTY            DESCRIPTION                                     \t\t\t\t\r"
				+ "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		String textin = "";
		String name="";

		for (int i = 0; i < PosApp.listOrderData2.size(); i++) {
			String remark = "";
			if (null == PosApp.listOrderData2.get(i).getRemarks()
					|| PosApp.listOrderData2.get(i).getRemarks()
							.equals("")) {

			} else {
				remark = "\t" + "\r\n"
						+ PosApp.listOrderData2.get(i).getRemarks();
			}
			if (PosApp.listOrderData2.get(i).getStandBy().equals("1")) {
//				name = PosApp.listOrderData2.get(itemnumber).getItemName()
//						+ " (Stand-by)";
				name = PosApp.listOrderData2.get(i).getItemTau()
						+ " (Stand-by)";
			} else {
				if(Utilities.getGlobalVariable(ac).subNow){
//					name = PosApp.listOrderData2.get(itemnumber).getItemName()+" (Subnow)";
					name = PosApp.listOrderData2.get(i).getItemTau()+" (SERVE NOW)";
					//Utilities.getGlobalVariable(ac).subNow=false;
				}else{
					name = PosApp.listOrderData2.get(i).getItemTau();
//					name = PosApp.listOrderData2.get(itemnumber).getItemName();	
				}
				
			}
			textin += "\t"
					+ RedoPrice(PosApp.listOrderData2.get(i).getQualyti())
					+ "\t\t" + name+"\n"+ remark + "\t"
					+ "\r\n";
			/*
			 * textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" +
			 * "\t\t\t\t\t\t\t\t" +
			 * RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + "\t" + "" +
			 * RedoPrice(PosApp.listOrderData.get(i).getUnitPrice()) + "\t" + ""
			 * + RedoPrice(PosApp.listOrderData.get(i).getDiscount()) + "\t\t\t"
			 * + "" + RedoPrice(PosApp.listOrderData.get(i).getAmount()) +
			 * "\r\n";
			 */

		}
		Log.v("HAI1", "Master "+textin);
		DecimalFormat df = new DecimalFormat("####0.00");
		Double temp1 = Double.parseDouble(MainActivity.btnSubTotalValue
				.getText().toString());
		Double temp2 = Double.parseDouble(MainActivity.btnDisCountValue
				.getText().toString());
		Double temp3 = Double.parseDouble(MainActivity.btnGSTValue.getText()
				.toString());
		Double temp4 = Double.parseDouble(MainActivity.tvTotalAmount.getText()
				.toString());
		if (MainActivity.isPrintNoSave) {
			textToPrint = (textin
			/*
			 * +
			 * "-----------------------------------------------------------------------\r\n"
			 * + "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp1) + "\r\n" +
			 * "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp2) + "\r\n" +
			 * "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp3) + "\r\n" +
			 * "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp4)
			 */);
		} else {
			textToPrint = (textin
			/*
			 * +
			 * "-----------------------------------------------------------------------\r\n"
			 * + "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp1) + "\r\n" +
			 * "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp2) + "\r\n" +
			 * "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp3) + "\r\n" +
			 * "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp4) + "\r\n" +
			 * "-----------------------------------------------------------------------\r\n"
			 * +
			 * "                           Payment Detail   \t                   \r\n"
			 * +
			 * "-----------------------------------------------------------------------\r\n\r\n"
			 * + "\r\n" + "Change   : \t\t\t $0.00 \r\n"
			 */);
		}

		command = createRasterCommand(textToPrint, 17, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
															// drawer
		for(int i=0;i<PosApp.soluongIn;i++){
			sendCommand(context, portName, portSettings, list);
		}
			
//		sendCommand(context, portName, portSettings, list);

	}
	public static void PrintPos(Activity ac, final Context context, final String portName,
			final String portSettings, Resources res, String recep) {

		final ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// // list.addAll(Arrays
		// // .asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
		// list.addAll(Arrays.asList(tempList));

		// command = createRasterCommand("\t\t\t\ta", 13, 0);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		String textToPrint = ("");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = createRasterCommand("", 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		/*
		 * textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */
		textToPrint = ("" + "Terminal           \t:  Section 1" + "\r\n"
				+ "User                     \t:  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ "\r\n" + "Printed              \t:  "
				+ ComDDUtilities.getDateTimePrint() + "\r\n");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("              TABLE         PAX" + "\r\n"
				+ "                   "
				+ Utilities.getGlobalVariable(ac).tableNum/* tabnum */
				+ "                 "
				+ Utilities.getGlobalVariable(ac).numberCustomer + "\r");
		command = createRasterCommand(textToPrint, 19, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("-----------------------------------------------------------------------\r"
				+ "     QTY            DESCRIPTION                                     \t\t\t\t\r"
				+ "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		String textin = "";
		String name="";

		for (int i = 0; i < PosApp.listOrderData2.size(); i++) {
			String remark = "";
			if (null == PosApp.listOrderData2.get(i).getRemarks()
					|| PosApp.listOrderData2.get(i).getRemarks()
							.equals("")) {

			} else {
				remark = "\t" + "\r\n"
						+ PosApp.listOrderData2.get(i).getRemarks();
			}
			if (PosApp.listOrderData2.get(i).getStandBy().equals("1")) {
//				name = PosApp.listOrderData2.get(itemnumber).getItemName()
//						+ " (Stand-by)";
				name = PosApp.listOrderData2.get(i).getItemTau()
						+ " (Stand-by)";
			} else {
				if(Utilities.getGlobalVariable(ac).subNow){
//					name = PosApp.listOrderData2.get(itemnumber).getItemName()+" (Subnow)";
					name = PosApp.listOrderData2.get(i).getItemTau()+" (SERVE NOW)";
					//Utilities.getGlobalVariable(ac).subNow=false;
				}else{
					name = PosApp.listOrderData2.get(i).getItemTau();
//					name = PosApp.listOrderData2.get(itemnumber).getItemName();	
				}
				
			}
			textin += "\t"
					+ RedoPrice(PosApp.listOrderData2.get(i).getQualyti())
					+ "\t\t" + name+"\n"+ remark + "\t"
					+ "\r\n";
			/*
			 * textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" +
			 * "\t\t\t\t\t\t\t\t" +
			 * RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + "\t" + "" +
			 * RedoPrice(PosApp.listOrderData.get(i).getUnitPrice()) + "\t" + ""
			 * + RedoPrice(PosApp.listOrderData.get(i).getDiscount()) + "\t\t\t"
			 * + "" + RedoPrice(PosApp.listOrderData.get(i).getAmount()) +
			 * "\r\n";
			 */

		}
		Log.v("HAI1", "Master "+textin);
		DecimalFormat df = new DecimalFormat("####0.00");
		Double temp1 = Double.parseDouble(MainActivity.btnSubTotalValue
				.getText().toString());
		Double temp2 = Double.parseDouble(MainActivity.btnDisCountValue
				.getText().toString());
		Double temp3 = Double.parseDouble(MainActivity.btnGSTValue.getText()
				.toString());
		Double temp4 = Double.parseDouble(MainActivity.tvTotalAmount.getText()
				.toString());
		if (MainActivity.isPrintNoSave) {
			textToPrint = (textin
			/*
			 * +
			 * "-----------------------------------------------------------------------\r\n"
			 * + "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp1) + "\r\n" +
			 * "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp2) + "\r\n" +
			 * "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp3) + "\r\n" +
			 * "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp4)
			 */);
		} else {
			textToPrint = (textin
			/*
			 * +
			 * "-----------------------------------------------------------------------\r\n"
			 * + "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp1) + "\r\n" +
			 * "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp2) + "\r\n" +
			 * "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp3) + "\r\n" +
			 * "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp4) + "\r\n" +
			 * "-----------------------------------------------------------------------\r\n"
			 * +
			 * "                           Payment Detail   \t                   \r\n"
			 * +
			 * "-----------------------------------------------------------------------\r\n\r\n"
			 * + "\r\n" + "Change   : \t\t\t $0.00 \r\n"
			 */);
		}

		command = createRasterCommand(textToPrint, 17, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
															// drawer
		new Thread(new Runnable() {
			public void run() {
				sendCommand(context, portName, portSettings, list);
			}
		}).start();
//		sendCommand(context, portName, portSettings, list);

	}

	public static void PrintSampleReceiptRE(Activity ac, Context context,
			String portName, String portSettings, String commandType,
			String strPrintArea, String recep, String recep2) {
		if (commandType == "Line") {
			if (strPrintArea.equals("3inch (78mm)")) {

			} else if (strPrintArea.equals("4inch (112mm)")) {

			}
		} else if (commandType == "Raster") {
			if (strPrintArea.equals("3inch (78mm)")) {
				CompanyDataSource b = new CompanyDataSource(ac, ac);

				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 576; // Printable area in paper is 832(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
						RasPageEndMode.FeedAndFullCut,
						RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard,
						0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				String textToPrint = ("");
				// command = createRasterCommand(textToPrint, 13,
				// Typeface.BOLD);
				// tempList = new Byte[command.length];
				// CopyArray(command, tempList);
				// list.addAll(Arrays.asList(tempList));
				SaleReportDataSource a = new SaleReportDataSource(ac, ac);
				ItemsDataSource d = new ItemsDataSource(ac, ac);
				String address;
				String companyName;
				String phone;
				String Email;
				String web;
				String Gst;
				String fax;
				String companyNo;
				if (b.loadCName().equals("")) {
					companyName = "";
				} else {
					companyName = b.loadCName() + "\r\n";
				}
				if (b.loadCAddress().equals("")) {
					address = "";
				} else {
					address = b.loadCAddress() + "\r\n";
				}
				if (b.loadCPhone().equals("")) {
					phone = "";
				} else {
					phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
				}
				if (b.loadCEmail().equals("")) {
					Email = "";
				} else {
					Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
				}
				if (b.loadCWebsite().equals("")) {
					web = "";
				} else {
					web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
				}
				if (b.loadGST().equals("")) {
					Gst = "";
				} else {
					Gst = "\t\t\t\t\t\t\t\t" + b.loadGST() + "\r\n";
				}
				if (b.loadFAX().equals("")) {
					fax = "";
				} else {
					fax = "\t\t\t\t\t\t\t\t" + b.loadFAX() + "\r\n";
				}
				if (b.loadCompanyNo().equals("")) {
					companyNo = "";
				} else {
					companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo()
							+ "\r\n";
				}
				command = createRasterCommand(RedoAddress(companyName), 16, 1);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				textToPrint = (""
						+ address
						+ phone
						+ Email
						+ web
						+ Gst
						+ fax
						+ companyNo
						+ " \r\n\n"
						+ "Receipt No.             \t: "
						+ recep2
						+ " \r\n"
						+ "Printed                    \t: "
						+ ComDDUtilities.getDateTimePrint()
						+ "  \r\n"
						+ "Cashier                   \t: Cashier \r\n"
						+ "You are served by \t:  "
						+ a.loadUserName()
						+ "\r\n"
						+ "-----------------------------------------------------------------------\r\n"
						+ "                           Reprint Receipt   \t                   \r\n"
						+ "-----------------------------------------------------------------------\r"
						+ "     Items          |  Qty  |  Price  |  Dis  |  Amount \t\t\t\t\r" + "----------------------------------------------------------------------\r");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				String textin = "";
				int i = 0;
				Cursor c = null;
				MySQLiteHelper dbHelper = new MySQLiteHelper(context);
				try {
					if (dbHelper != null && dbHelper.getDb() != null) {
						String query = "SELECT * FROM sale_details where SaleID='"
								+ recep + "'";

						c = dbHelper.getDb().rawQuery(query, null);

						if (c != null && c.getCount() > 0) {

							c.moveToFirst();
							do {
								try {

									textin += c.getString(2).toString()
											+ "\r\n"
											+ "\t\t\t\t\t\t\t\t"
											+ RedoPrice(c.getString(3)
													.toString())
											+ "\t"
											+ "$"
											+ RedoPrice(c.getString(4)
													.toString())
											+ "\t"
											+ "$"
											+ RedoPrice(c.getString(5)
													.toString())
											+ "\t\t\t"
											+ "$"
											+ RedoPrice(c.getString(6)
													.toString()) + "\r\n";
									i++;
								} catch (Exception e) {
								}
							} while (c.moveToNext());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				} finally {
					if (c != null) {
						c.close();
						c = null;
					}

				}
				Log.v("HAI", textin);
				textToPrint = (textin
						+ "-----------------------------------------------------------------------\r\n"
						+ "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						+ "$"
						+ d.checkSubtotal(recep)
						+ "\r\n"
						+ "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						+ "$"
						+ d.checkdiscount(recep)
						+ "\r\n"
						+ "GST          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						+ "$"
						+ d.checkGST(recep)
						+ "\r\n"
						+ "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						+ "$"
						+ d.checkTotalamount(recep)
						+ "\r\n"
						+ "-----------------------------------------------------------------------\r\n"
						+ "                           Payment Detail   \t                   \r\n"
						+ "-----------------------------------------------------------------------\r\n\r\n"
						+ d.loadPaymentRe(recep) + "\r\n");

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				PayModel up = new PayModel();

				textToPrint = "\r\n NOTE : \r\n" + MainActivity.note
						+ "\r\n\r\n" + b.loadCReceipt() + "\r\n\r\n"
						+ "***Presettlement Bill";

				command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				// Bitmap bm = BitmapFactory.decodeResource(res,
				// R.drawable.qrcode);
				// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
				// command = starbitmap.getImageRasterDataForPrinting(true);
				// tempList = new Byte[command.length];

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																	// drawer

				for (int i1 = 0; i1 < PosApp.soluongIn; i1++) {
					sendCommand(context, portName, portSettings, list);
				}
			} else if (strPrintArea.equals("4inch (112mm)")) {

			}
		}
	}

	public static void RePrintPos(Activity ac, Context context,
			String portName, String portSettings, String recep, String recep2,
			Resources res) {
		CompanyDataSource b = new CompanyDataSource(ac, ac);

		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		String textToPrint = ("");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		SaleReportDataSource a = new SaleReportDataSource(ac, ac);
		ItemsDataSource d = new ItemsDataSource(ac, ac);
		String address;
		String companyName;
		String phone;
		String Email;
		String web;
		String Gst;
		String fax;
		String companyNo;
		if (b.loadCName().equals("")) {
			companyName = "";
		} else {
			companyName = b.loadCName() + "\r\n";
		}
		if (b.loadCAddress().equals("")) {
			address = "";
		} else {
			address = b.loadCAddress() + "\r\n";
		}
		if (b.loadCPhone().equals("")) {
			phone = "";
		} else {
			phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
		}
		if (b.loadCEmail().equals("")) {
			Email = "";
		} else {
			Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
		}
		if (b.loadCWebsite().equals("")) {
			web = "";
		} else {
			web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
		}
		if (b.loadGST().equals("")) {
			Gst = "";
		} else {
			Gst = "\t\t\t\t\t\t\t\t" + b.loadGST() + "\r\n";
		}
		if (b.loadFAX().equals("")) {
			fax = "";
		} else {
			fax = "\t\t\t\t\t\t\t\t" + b.loadFAX() + "\r\n";
		}
		if (b.loadCompanyNo().equals("")) {
			companyNo = "";
		} else {
			companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo() + "\r\n";
		}
		command = createRasterCommand(RedoAddress(companyName), 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		textToPrint = (""
				+ address
				+ phone
				+ Email
				+ web
				+ Gst
				+ fax
				+ companyNo
				+ " \r\n\n"
				+ "Receipt No.             \t: "
				+ recep2
				+ " \r\n"
				+ "Printed                    \t: "
				+ ComDDUtilities.getDateTimePrint()
				+ "  \r\n"
				+ "Cashier                   \t: Cashier \r\n"
				+ "You are served by \t:  "
				+ a.loadUserName()
				+ "\r\n"
				+ "-----------------------------------------------------------------------\r\n"
				+ "                           Reprint Receipt   \t                   \r\n"
				+ "-----------------------------------------------------------------------\r"
				+ "     Items          | Qty | Price | Dis | Amount \t\t\t\t\r" + "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		String textin = "";

		int i = 0;
		Cursor c = null;
		MySQLiteHelper dbHelper = new MySQLiteHelper(context);
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sale_details where SaleID='"
						+ recep + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							Double a1 = Double.parseDouble(c.getString(5)
									.toString());
							Double b1 = Double.parseDouble(c.getString(6)
									.toString());

							String stra = String.format("%.2f", a1);
							String strb = String.format("%.2f", b1);

							textin += c.getString(2).toString() + "\r\n"
									+ "\t\t\t\t\t\t\t\t"
									+ RedoPrice(c.getString(3).toString())
									+ "\t" + "$"
									+ RedoPrice(c.getString(4).toString())
									+ "\t" + "$" + RedoPrice(stra) + "\t\t\t"
									+ "$" + RedoPrice(strb) + "\r\n";
							i++;
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		Log.v("HAI", textin);
		textToPrint = (textin
				+ "-----------------------------------------------------------------------\r\n"
				+ "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				+ "$"
				+ d.checkSubtotal(recep)
				+ "\r\n"
				+ "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				+ "$"
				+ d.checkdiscount(recep)
				+ "\r\n"
				+ "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				+ "$"
				+ d.checkGST(recep)
				+ "\r\n"
				+ "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				+ "$"
				+ d.checkTotalamount(recep)
				+ "\r\n"
				+ "-----------------------------------------------------------------------\r\n"
				+ "                           Payment Detail   \t                   \r\n"
				+ "-----------------------------------------------------------------------\r\n\r\n"
				+ d.loadPaymentNettRe(recep) + "\r\n" + "Change   : \t\t\t $0.00 \r\n");

		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		PayModel up = new PayModel();

		textToPrint = "\r\n NOTE : \r\n" + d.loadNoteRe(recep) + "\r\n\r\n"
				+ b.loadCReceipt();

		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res,
		// R.drawable.qrcode);
		// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
															// drawer

		for (int i1 = 0; i1 < PosApp.soluongIn; i1++) {
			sendCommand(context, portName, portSettings, list);
		}

	}

	public static void PrintPosReturn(Activity ac, Context context,
			String portName, String portSettings, Resources res, String recep) {
		CompanyDataSource b = new CompanyDataSource(ac, ac);

		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		String textToPrint = ("");
		// String textToPrint = ("                    "+b.loadCName() + "\r\n");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		SaleReportDataSource a = new SaleReportDataSource(ac, ac);
		ItemsDataSource d = new ItemsDataSource(ac, ac);
		String recepstr = "";
		String address;
		String companyName;
		String phone;
		String Email;
		String web;
		String Gst;
		String fax;
		String companyNo;
		if (MainActivity.isPrintNoSave) {
			recepstr = "";
		} else {
			recepstr = "Receipt No.             \t: " + recep;
		}
		if (b.loadCName().equals("")) {
			companyName = "";
		} else {
			companyName = b.loadCName() + "\r\n";
		}
		if (b.loadCAddress().equals("")) {
			address = "";
		} else {
			address = b.loadCAddress() + "\r\n";
		}
		if (b.loadCPhone().equals("")) {
			phone = "";
		} else {
			phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
		}
		if (b.loadCEmail().equals("")) {
			Email = "";
		} else {
			Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
		}
		if (b.loadCWebsite().equals("")) {
			web = "";
		} else {
			web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
		}
		if (b.loadGST().equals("")) {
			Gst = "";
		} else {
			Gst = "\t\t\t\t\t\t\t\t" + b.loadGST() + "\r\n";
		}
		if (b.loadFAX().equals("")) {
			fax = "";
		} else {
			fax = "\t\t\t\t\t\t\t\t" + b.loadFAX() + "\r\n";
		}
		if (b.loadCompanyNo().equals("")) {
			companyNo = "";
		} else {
			companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo() + "\r\n";
		}
		command = createRasterCommand(RedoAddress(companyName), 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = (""
				+ address
				+ phone
				+ Email
				+ web
				+ Gst
				+ fax
				+ companyNo
				+ " \r\n\n"
				+ "Receipt No.             \t: "
				+ recep
				+ " \r\n"
				+ "Printed                    \t: "
				+ ComDDUtilities.getDateTimePrint()
				+ "  \r\n"
				+ "Cashier                   \t: Cashier \r\n"
				+ "You are served by \t:  "
				+ a.loadUserName()
				+ "\r\n"
				+ "-----------------------------------------------------------------------\r\n"
				+ "                           REFUND RECEIPT   \t                   \r\n"
				+ "-----------------------------------------------------------------------\r"
				+ "     Items                          | Qty | Price | Amount  \t\t\t\t\r" + "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		String textin = "";

		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			double tempsub = Double.parseDouble(PosApp.listOrderData.get(i)
					.getAmount());
			String tempsubstr = String.format("%.2f", tempsub);
			textin += PosApp.listOrderData.get(i).getItemName() + "\r\n"
					+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
					+ RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + ""
					+ "$"
					+ RedoPrice(PosApp.listOrderData.get(i).getUnitPrice())
					+ "\t\t"

					+ "$" + RedoPrice(tempsubstr) + "\r\n";

		}
		Log.v("HAI", textin);
		textToPrint = (textin
				+ "-----------------------------------------------------------------------\r\n"
				+ "TOTAL  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
				+ "$"
				+ MainActivity.btnSubTotalValue.getText().toString()
				+ "      "
				+ "-----------------------------------------------------------------------\r\n"
				+ "                           Payment Detail   \t                   \r\n"
				+ "-----------------------------------------------------------------------\r\n\r\n"
				+ "CASH   : \t\t\t $"
				+ MainActivity.btnSubTotalValue.getText().toString() + "      \r\n");

		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		PayModel up = new PayModel();

		textToPrint = "\r\n NOTE : \r\n" + MainActivity.note + "\r\n\r\n"
				+ b.loadCReceipt();

		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res,
		// R.drawable.qrcode);
		// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
															// drawer

		for (int i = 0; i < PosApp.soluongIn; i++) {
			sendCommand(context, portName, portSettings, list);
		}
		ComDDUtilities.showWelcome();
	}

	public static void PrintPosSaleEndShift(Activity ac, Context context,
			String portName, String portSettings, String mode, Resources res) {
		if (mode.equals("1")) {
			CompanyDataSource b = new CompanyDataSource(ac, ac);

			ArrayList<Byte> list = new ArrayList<Byte>();

			Byte[] tempList;
			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10,
					0x22, 0x00 }));
			printableArea = 576; // Printable area in paper is 832(dot)

			RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
					RasPageEndMode.FeedAndFullCut,
					RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0,
					0);
			byte[] command = rasterDoc.BeginDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory.decodeResource(res,
			// R.drawable.logo_print);
			// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];
			// CopyArray(command, tempList);
			// list.addAll(Arrays.asList(tempList));

			String textToPrint = ("");
			command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource a = new SaleReportDataSource(ac, ac);
			ItemsDataSource d = new ItemsDataSource(ac, ac);
			String address;
			String companyName;
			String phone;
			String Email;
			String web;
			String Gst;
			String fax;
			String companyNo;
			if (b.loadCName().equals("")) {
				companyName = "";
			} else {
				companyName = b.loadCName() + "\r\n";
			}
			if (b.loadCAddress().equals("")) {
				address = "";
			} else {
				address = b.loadCAddress() + "\r\n";
			}
			if (b.loadCPhone().equals("")) {
				phone = "";
			} else {
				phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
			}
			if (b.loadCEmail().equals("")) {
				Email = "";
			} else {
				Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
			}
			if (b.loadCWebsite().equals("")) {
				web = "";
			} else {
				web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
			}
			if (b.loadGST().equals("")) {
				Gst = "";
			} else {
				Gst = "\t\t\t\t\t\t\t\t" + "GST: " + b.loadGST() + "\r\n";
			}
			if (b.loadFAX().equals("")) {
				fax = "";
			} else {
				fax = "\t\t\t\t\t\t\t\t" + "FAX: " + b.loadFAX() + "\r\n";
			}
			if (b.loadCompanyNo().equals("")) {
				companyNo = "";
			} else {
				companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo()
						+ "\r\n";
			}
			command = createRasterCommand(RedoAddress(companyName), 16, 1);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			textToPrint = (""
					+ address
					+ phone
					+ Email
					+ web
					+ Gst
					+ fax
					+ companyNo

					+ "\r\nPrinted      \t: "
					+ ComDDUtilities.getDateTimePrint()
					+ "  \r\n"
					+ "Cashier      \t: Cashier \r\n"
					+ "Printed by \t:  "
					+ a.loadUserName()
					+ "\r\n"
					+ "-----------------------------------------------------------------------\r\n"
					+ "                           GROSS SALE REPORT   \t                   " + "-----------------------------------------------------------------------");
			command = createRasterCommand(textToPrint, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource ard = new SaleReportDataSource(ac, ac);
			String textin = "";
			textin += "\t\t\t Descriptions \t\t\t\t\t\t | Amount \r\n\r\n";
			textin += "\t\t\t CASH FLOAT  \t\t\t\t\t\t\t\t"
					+ ard.loadOpenCash() + ".00\r\n";
			textin += "\t\t\t GROSS SALES  \t\t\t\t\t\t\t"
					+ ard.loadGrossSale() + "\r\n";
			textin += "\t\t\t ITEM DISCOUNT  \t\t\t\t\t"
					+ ard.loadItemDisccount() + "\r\n";
			textin += "\t\t\t BILL DISCOUNT  \t\t\t\t\t\t"
					+ ard.loadBillDisccount() + "\r\n";
			textin += "\t\t\t GST(IF ANY)  \t\t\t\t\t\t\t\t$" + ard.loadGST()
					+ "\r\n";
			textin += "\t\t\t CASH SALES  \t\t\t\t\t\t\t\t"
					+ ard.loadTotalCash() + "\r\n";
			textin += "\t\t\t CREDIT CARD SALES  \t\t\t" + ard.loadCardSale()
					+ "\r\n";
			textin += "\t\t\t UNPAID SALES  \t\t\t\t\t\t$" + ard.loadUnPaid()
					+ "\r\n";
			textin += "\t\t\t NUMBER OF SALE  \t\t\t\t"
					+ ard.loadNumberOfSale() + "\r\n";
			textin += "\t\t\t SALE REFUND  \t\t\t\t\t\t\t" + ard.loadreturn()
					+ "\r\n";

			Double tempCashf = Double.parseDouble((ard.loadOpenCash()).replace(
					"$", ""));
			Double tempNet = Double.parseDouble((ard.loadTotalCash()).replace(
					"$", ""));
			Double totalcash = tempCashf + tempNet;
			Log.v("HAI", textin);

			command = createRasterCommand(textin, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			PayModel up = new PayModel();
			textToPrint = "";
			command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory.decodeResource(res,
			// R.drawable.qrcode);
			// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];

			command = rasterDoc.EndDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																// drawer

			// for (int i = 0; i < PosApp.soluongIn; i++) {
			sendCommand(context, portName, portSettings, list);
			// }
		} else {
			CompanyDataSource b = new CompanyDataSource(ac, ac);

			ArrayList<Byte> list = new ArrayList<Byte>();

			Byte[] tempList;
			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10,
					0x22, 0x00 }));
			printableArea = 576; // Printable area in paper is 832(dot)

			RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
					RasPageEndMode.FeedAndFullCut,
					RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0,
					0);
			byte[] command = rasterDoc.BeginDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory.decodeResource(res,
			// R.drawable.logo_print);
			// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];
			// CopyArray(command, tempList);
			// list.addAll(Arrays.asList(tempList));

			String textToPrint = ("");
			command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource a = new SaleReportDataSource(ac, ac);
			ItemsDataSource d = new ItemsDataSource(ac, ac);
			String address;
			String companyName;
			String phone;
			String Email;
			String web;
			String Gst;
			String fax;
			String companyNo;
			if (b.loadCName().equals("")) {
				companyName = "";
			} else {
				companyName = b.loadCName() + "\r\n";
			}
			if (b.loadCAddress().equals("")) {
				address = "";
			} else {
				address = b.loadCAddress() + "\r\n";
			}
			if (b.loadCPhone().equals("")) {
				phone = "";
			} else {
				phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
			}
			if (b.loadCEmail().equals("")) {
				Email = "";
			} else {
				Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
			}
			if (b.loadCWebsite().equals("")) {
				web = "";
			} else {
				web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
			}
			if (b.loadGST().equals("")) {
				Gst = "";
			} else {
				Gst = "\t\t\t\t\t\t\t\t" + "GST: " + b.loadGST() + "\r\n";
			}
			if (b.loadFAX().equals("")) {
				fax = "";
			} else {
				fax = "\t\t\t\t\t\t\t\t" + "FAX: " + b.loadFAX() + "\r\n";
			}
			if (b.loadCompanyNo().equals("")) {
				companyNo = "";
			} else {
				companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo()
						+ "\r\n";
			}
			command = createRasterCommand(RedoAddress(companyName), 16, 1);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			textToPrint = (""
					+ address
					+ phone
					+ Email
					+ web
					+ Gst
					+ fax
					+ companyNo

					+ "\r\nPrinted      \t: "
					+ ComDDUtilities.getDateTimePrint()
					+ "  \r\n"
					+ "Cashier      \t: Cashier \r\n"
					+ "Printed by \t:  "
					+ a.loadUserName()
					+ "\r\n"
					+ "-----------------------------------------------------------------------\r\n"
					+ "                           END SHIFT REPORT   \t                   " + "-----------------------------------------------------------------------");
			command = createRasterCommand(textToPrint, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource ard = new SaleReportDataSource(ac, ac);
			String textin = "";
			textin += "\t\t\t Descriptions \t\t\t\t\t\t | Amount \r\n\r\n";
			textin += "\t\t\t CASH FLOAT  \t\t\t\t\t\t\t\t"
					+ ard.loadOpenCash() + ".00\r\n";
			textin += "\t\t\t GROSS SALES  \t\t\t\t\t\t\t"
					+ ard.loadGrossSale() + "\r\n";
			textin += "\t\t\t ITEM DISCOUNT  \t\t\t\t\t"
					+ ard.loadItemDisccount() + "\r\n";
			textin += "\t\t\t BILL DISCOUNT  \t\t\t\t\t\t"
					+ ard.loadBillDisccount() + "\r\n";
			textin += "\t\t\t GST(IF ANY)  \t\t\t\t\t\t\t\t$" + ard.loadGST()
					+ "\r\n";
			textin += "\t\t\t CASH SALES  \t\t\t\t\t\t\t\t"
					+ ard.loadTotalCash() + "\r\n";
			textin += "\t\t\t CREDIT CARD SALES  \t\t\t" + ard.loadCardSale()
					+ "\r\n";
			textin += "\t\t\t UNPAID SALES  \t\t\t\t\t\t$" + ard.loadUnPaid()
					+ "\r\n";

			Double tempCashf = Double.parseDouble((ard.loadOpenCash()).replace(
					"$", ""));
			Double tempNet = Double.parseDouble((ard.loadTotalCash()).replace(
					"$", ""));
			Double totalcash = tempCashf + tempNet;
			Log.v("HAI", textin);

			command = createRasterCommand(textin, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			PayModel up = new PayModel();
			textToPrint = "";
			command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory.decodeResource(res,
			// R.drawable.qrcode);
			// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];

			command = rasterDoc.EndDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																// drawer
																// for (int i =
																// 0; i <
																// PosApp.soluongIn;
																// i++) {
			sendCommand(context, portName, portSettings, list);
			// }
		}
	}

	public static void PrintItemSaleEndShift(Activity ac, Context context,
			String portName, String portSettings, String mode, Resources res,
			String form, String to) {
		if (mode.equals("2")) {
			CompanyDataSource b = new CompanyDataSource(ac, ac);

			ArrayList<Byte> list = new ArrayList<Byte>();

			Byte[] tempList;
			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10,
					0x22, 0x00 }));
			printableArea = 576; // Printable area in paper is 832(dot)

			RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
					RasPageEndMode.FeedAndFullCut,
					RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0,
					0);
			byte[] command = rasterDoc.BeginDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory
			// .decodeResource(res, R.drawable.logo_print);
			// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];
			// CopyArray(command, tempList);
			// list.addAll(Arrays.asList(tempList));

			String textToPrint = ("");
			command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource a = new SaleReportDataSource(ac, ac);
			ItemsDataSource d = new ItemsDataSource(ac, ac);
			String address;
			String companyName;
			String phone;
			String Email;
			String web;
			String Gst;
			String fax;
			String companyNo;
			if (b.loadCName().equals("")) {
				companyName = "";
			} else {
				companyName = b.loadCName() + "\r\n";
			}
			if (b.loadCAddress().equals("")) {
				address = "";
			} else {
				address = b.loadCAddress() + "\r\n";
			}
			if (b.loadCPhone().equals("")) {
				phone = "";
			} else {
				phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
			}
			if (b.loadCEmail().equals("")) {
				Email = "";
			} else {
				Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
			}
			if (b.loadCWebsite().equals("")) {
				web = "";
			} else {
				web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
			}
			if (b.loadGST().equals("")) {
				Gst = "";
			} else {
				Gst = "\t\t\t\t\t\t\t\t" + "GST: " + b.loadGST() + "\r\n";
			}
			if (b.loadFAX().equals("")) {
				fax = "";
			} else {
				fax = "\t\t\t\t\t\t\t\t" + "FAX: " + b.loadFAX() + "\r\n";
			}
			if (b.loadCompanyNo().equals("")) {
				companyNo = "";
			} else {
				companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo()
						+ "\r\n";
			}
			command = createRasterCommand(RedoAddress(companyName), 16, 1);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			textToPrint = (""
					+ address
					+ phone
					+ Email
					+ web
					+ Gst
					+ fax
					+ companyNo

					+ "\r\nPrinted      \t: "
					+ ComDDUtilities.getDateTimePrint()
					+ "  \r\n"
					+ "Cashier      \t: Cashier \r\n"
					+ "Printed by \t:  "
					+ a.loadUserName()
					+ "\r\n"
					+ "-----------------------------------------------------------------------\r\n"
					+ "               END SHIFT ITEM SALE REPORT   \t                   " + "-----------------------------------------------------------------------");
			command = createRasterCommand(textToPrint, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource ard = new SaleReportDataSource(ac, ac);
			String textin = "";
			textin += "\t\t Item Name \r\n \t\tQty \r\n\r\n";
			textin += ard.loadItemSaleShifts();

			Log.v("HAI", textin);

			command = createRasterCommand(textin, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			PayModel up = new PayModel();
			textToPrint = "";
			command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory.decodeResource(res,
			// R.drawable.qrcode);
			// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];

			command = rasterDoc.EndDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																// drawer
																// for (int i =
																// 0; i <
																// PosApp.soluongIn;
																// i++) {
			sendCommand(context, portName, portSettings, list);
			// }
		} else {
			CompanyDataSource b = new CompanyDataSource(ac, ac);

			ArrayList<Byte> list = new ArrayList<Byte>();

			Byte[] tempList;
			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

			list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10,
					0x22, 0x00 }));
			printableArea = 576; // Printable area in paper is 832(dot)

			RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
					RasPageEndMode.FeedAndFullCut,
					RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0,
					0);
			byte[] command = rasterDoc.BeginDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory
			// .decodeResource(res, R.drawable.logo_print);
			// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];
			// CopyArray(command, tempList);
			// list.addAll(Arrays.asList(tempList));

			String textToPrint = ("");
			command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			SaleReportDataSource a = new SaleReportDataSource(ac, ac);
			ItemsDataSource d = new ItemsDataSource(ac, ac);
			String address;
			String companyName;
			String phone;
			String Email;
			String web;
			String Gst;
			String fax;
			String companyNo;
			if (b.loadCName().equals("")) {
				companyName = "";
			} else {
				companyName = b.loadCName() + "\r\n";
			}
			if (b.loadCAddress().equals("")) {
				address = "";
			} else {
				address = b.loadCAddress() + "\r\n";
			}
			if (b.loadCPhone().equals("")) {
				phone = "";
			} else {
				phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
			}
			if (b.loadCEmail().equals("")) {
				Email = "";
			} else {
				Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
			}
			if (b.loadCWebsite().equals("")) {
				web = "";
			} else {
				web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
			}
			if (b.loadGST().equals("")) {
				Gst = "";
			} else {
				Gst = "\t\t\t\t\t\t\t\t" + "GST: " + b.loadGST() + "\r\n";
			}
			if (b.loadFAX().equals("")) {
				fax = "";
			} else {
				fax = "\t\t\t\t\t\t\t\t" + "FAX: " + b.loadFAX() + "\r\n";
			}
			if (b.loadCompanyNo().equals("")) {
				companyNo = "";
			} else {
				companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo()
						+ "\r\n";
			}
			String textin1 = "";
			textin1 = "";
			textin1 += "\t\t Item Name " + "\r\n \t\t Qty \r\n\r\n";
			SaleReportDataSource ard = new SaleReportDataSource(ac, ac);
			textin1 += ard.loadItemSaleShiftsPrint(form, to);

			command = createRasterCommand(RedoAddress(companyName), 16, 1);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			textToPrint = (""
					+ address
					+ phone
					+ Email
					+ web
					+ Gst
					+ fax
					+ companyNo

					+ "\r\nPrinted      \t: "
					+ ComDDUtilities.getDateTimePrint()
					+ "\r\nTime         \t: "
					+ SaleReportDataSource.mform
					+ " - "
					+ SaleReportDataSource.mto

					+ "  \r\n"
					+ "Cashier      \t: Cashier \r\n"
					+ "Printed by \t:  "
					+ a.loadUserName()
					+ "\r\n"
					+ "-----------------------------------------------------------------------\r\n"
					+ "               GROSS SALE ITEM SALE REPORT   \t                   " + "-----------------------------------------------------------------------");
			command = createRasterCommand(textToPrint, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			String textin = "";
			command = createRasterCommand(textin, 13, Typeface.BOLD);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			Log.v("HAI", textin1);

			command = createRasterCommand(textin1, 13, 0);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			PayModel up = new PayModel();
			textToPrint = "";
			command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));

			// Bitmap bm = BitmapFactory.decodeResource(res,
			// R.drawable.qrcode);
			// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
			// command = starbitmap.getImageRasterDataForPrinting(true);
			// tempList = new Byte[command.length];

			command = rasterDoc.EndDocumentCommandData();
			tempList = new Byte[command.length];
			CopyArray(command, tempList);
			list.addAll(Arrays.asList(tempList));
			list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																// drawer
																// for (int i =
																// 0; i <
																// PosApp.soluongIn;
																// i++) {
			sendCommand(context, portName, portSettings, list);
			// }

		}
	}

	public static void PrintSampleReceipt(Activity ac, Context context,
			String portName, String portSettings, String commandType,
			Resources res, String strPrintArea, String recep) {
		if (commandType == "Line") {
			if (strPrintArea.equals("3inch (78mm)")) {
				byte[] data;
				ArrayList<Byte> list = new ArrayList<Byte>();

				Byte[] tempList;

				list.addAll(Arrays
						.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 })); // Alignment
																			// (center)

				// data = "[If loaded.. Logo1 goes here]\r\n".getBytes();
				// tempList = new Byte[data.length];
				// CopyArray(data, tempList);
				// list.addAll(Arrays.asList(tempList));
				//
				// list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1c, 0x70, 0x01,
				// 0x00, '\r', '\n'})); //Stored Logo Printing

				data = "\nStar Clothing Boutique\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "123 Star Road\r\nCity, State 12345\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays
						.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

				list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10,
						0x22, 0x00 })); // Set horizontal tab

				data = "Date: MM/DD/YYYY".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[] { ' ', 0x09, ' ' })); // Moving
																			// Horizontal
																			// Tab

				data = "Time:HH:MM PM\r\n------------------------------------------------\r\n\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x45 })); // bold

				data = "SALE \r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x46 })); // bolf
																		// off

				data = "SKU ".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[] { 0x09 }));

				// Notice that we use a unicode representation because that is
				// how Java expresses these bytes as double byte unicode
				// This will TAB to the next horizontal position
				data = "  Description   \u0009         Total\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300678566 \u0009  PLAIN T-SHIRT\u0009         10.99\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300692003 \u0009  BLACK DENIM\u0009         29.99\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300651148 \u0009  BLUE DENIM\u0009         29.99\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300642980 \u0009  STRIPED DRESS\u0009         49.99\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300638471 \u0009  BLACK BOOTS\u0009         35.99\r\n\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Subtotal \u0009\u0009        156.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Tax \u0009\u0009          0.00\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "------------------------------------------------\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Total".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				// Character expansion
				list.addAll(Arrays.asList(new Byte[] { 0x06, 0x09, 0x1b, 0x69,
						0x01, 0x01 }));

				data = "        $156.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays
						.asList(new Byte[] { 0x1b, 0x69, 0x00, 0x00 })); // Cancel
																			// Character
																			// Expansion

				data = "------------------------------------------------\r\n\r\n"
						.getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Charge\r\n159.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Visa XXXX-XXXX-XXXX-0123\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "\u001b\u0034Refunds and Exchanges\u001b\u0035\r\n"
						.getBytes(); // Specify/Cancel White/Black Invert
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = ("Within " + "\u001b\u002d\u0001"
						+ "30 days\u001b\u002d\u0000" + " with receipt\r\n")
						.getBytes(); // Specify/Cancel Underline Printing
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = ("And tags attached\r\n\r\n").getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				// 1D barcode example
				list.addAll(Arrays
						.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
				list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x62, 0x06, 0x02,
						0x02 }));

				data = (" 12ab34cd56\u001e\r\n").getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x64, 0x02 })); // Cut
				list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																	// drawer

				sendCommand(context, portName, portSettings, list);
			} else if (strPrintArea.equals("4inch (112mm)")) {

			}
		} else if (commandType == "Raster") {
			if (strPrintArea.equals("3inch (78mm)")) {
				CompanyDataSource b = new CompanyDataSource(ac, ac);

				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 576; // Printable area in paper is 832(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
						RasPageEndMode.FeedAndFullCut,
						RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard,
						0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				// Bitmap bm = BitmapFactory.decodeResource(res,
				// R.drawable.logo_print);
				// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
				// command = starbitmap.getImageRasterDataForPrinting(true);
				// tempList = new Byte[command.length];
				// CopyArray(command, tempList);
				// list.addAll(Arrays.asList(tempList));

				String textToPrint = ("");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				SaleReportDataSource a = new SaleReportDataSource(ac, ac);
				ItemsDataSource d = new ItemsDataSource(ac, ac);
				String recep1 = "";
				if (MainActivity.isPrintNoSave) {
					recep1 = "";
				} else {
					recep1 = recep;
				}
				String address;
				String companyName;
				String phone;
				String Email;
				String web;
				String Gst;
				String fax;
				String companyNo;
				if (b.loadCName().equals("")) {
					companyName = "";
				} else {
					companyName = b.loadCName() + "\r\n";
				}
				if (b.loadCAddress().equals("")) {
					address = "";
				} else {
					address = b.loadCAddress() + "\r\n";
				}
				if (b.loadCPhone().equals("")) {
					phone = "";
				} else {
					phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
				}
				if (b.loadCEmail().equals("")) {
					Email = "";
				} else {
					Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
				}
				if (b.loadCWebsite().equals("")) {
					web = "";
				} else {
					web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
				}
				if (b.loadGST().equals("")) {
					Gst = "";
				} else {
					Gst = "\t\t\t\t\t\t\t\t" + b.loadGST() + "\r\n";
				}
				if (b.loadFAX().equals("")) {
					fax = "";
				} else {
					fax = "\t\t\t\t\t\t\t\t" + b.loadFAX() + "\r\n";
				}
				if (b.loadCompanyNo().equals("")) {
					companyNo = "";
				} else {
					companyNo = "\t\t COMPANY NO:" + b.loadCompanyNo() + "\r\n";
				}
				command = createRasterCommand(RedoAddress(companyName), 16, 1);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				textToPrint = (""
						+ address
						+ phone
						+ Email
						+ web
						+ Gst
						+ fax
						+ companyNo
						+ " \r\n\n"
						+ "Receipt No.             \t: "

						+ recep1
						+ " \r\n"
						+ "Printed                    \t: "
						+ ComDDUtilities.getDateTimePrint()
						+ "  \r\n"
						+ "Cashier                   \t: Cashier \r\n"
						+ "You are served by \t:  "
						+ a.loadUserName()
						+ "\r\n"
						+ "-----------------------------------------------------------------------\r"
						+ "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" + "----------------------------------------------------------------------\r");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				String textin = "";
				for (int i = 0; i < PosApp.listOrderData.size(); i++) {
					textin += PosApp.listOrderData.get(i).getItemName()
							+ "\r\n"
							+ "\t\t\t\t\t\t\t\t"
							+ RedoPrice(PosApp.listOrderData.get(i)
									.getQualyti())
							+ "\t"
							+ ""
							+ RedoPrice(PosApp.listOrderData.get(i)
									.getUnitPrice())
							+ "\t"
							+ ""
							+ RedoPrice(PosApp.listOrderData.get(i)
									.getDiscount())
							+ "\t\t\t"
							+ ""
							+ RedoPrice(PosApp.listOrderData.get(i).getAmount())
							+ "\r\n";

				}
				Log.v("HAI", textin);
				if (MainActivity.isPrintNoSave) {
					textToPrint = (textin
							+ "-----------------------------------------------------------------------\r\n"
							+ "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.btnSubTotalValue.getText()
									.toString()
							+ "\r\n"
							+ "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.btnDisCountValue.getText()
									.toString()
							+ "\r\n"
							+ "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.btnGSTValue.getText().toString()
							+ "\r\n"
							+ "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$" + MainActivity.tvTotalAmount.getText()
							.toString());
				} else {
					textToPrint = (textin
							+ "-----------------------------------------------------------------------\r\n"
							+ "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.btnSubTotalValue.getText()
									.toString()
							+ "\r\n"
							+ "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.btnDisCountValue.getText()
									.toString()
							+ "\r\n"
							+ "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.btnGSTValue.getText().toString()
							+ "\r\n"
							+ "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
							+ "$"
							+ MainActivity.tvTotalAmount.getText().toString()
							+ "\r\n"
							+ "-----------------------------------------------------------------------\r\n"
							+ "                           Payment Detail   \t                   \r\n"
							+ "-----------------------------------------------------------------------\r\n\r\n"
							+ d.loadPayment() + "\r\n");
				}

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				PayModel up = new PayModel();

				if (MainActivity.isPrintNoSave) {
					textToPrint = "\r\n NOTE : \r\n" + MainActivity.note
							+ "\r\n\r\n" + b.loadCReceipt() + "\r\n\r\n"
							+ "\t\t\t\t\t\t***Presettlement Bill***";
				} else {
					textToPrint = "\r\n NOTE : \r\n" + MainActivity.note
							+ "\r\n\r\n" + b.loadCReceipt();
				}

				command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				// Bitmap bm = BitmapFactory.decodeResource(res,
				// R.drawable.qrcode);
				// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
				// command = starbitmap.getImageRasterDataForPrinting(true);
				// tempList = new Byte[command.length];

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																	// drawer
				for (int i = 0; i < PosApp.soluongIn; i++) {
					sendCommand(context, portName, portSettings, list);
				}
			} else if (strPrintArea.equals("4inch (112mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 832; // Printable area in paper is 832(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
						RasPageEndMode.FeedAndFullCut,
						RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard,
						0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				// Bitmap bm = BitmapFactory.decodeResource(res,
				// R.drawable.logo_print);
				// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
				// command = starbitmap.getImageRasterDataForPrinting(true);
				// tempList = new Byte[command.length];
				// CopyArray(command, tempList);
				// list.addAll(Arrays.asList(tempList));

				String textToPrint = ("                                          Star Clothing Boutique\r\n"
						+ "                                                123 Star Road\r\n"
						+ "                                              City, State 12345\r\n\r\n"
						+ "Date: MM/DD/YYYY                                                      Time:HH:MM PM\r\n"
						+ "-------------------------------------------------------------------------------------------------------\r");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("SALE");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("SKU \t\t\t                                   Description \t\t                                  Total\r\n"
						+ "300678566 \t\t\t                        PLAIN T-SHIRT		\t\t                      10.99\n"
						+ "300692003 \t\t\t                        BLACK DENIM		\t\t                      29.99\n"
						+ "300651148 \t\t\t                        BLUE DENIM		\t\t                         29.99\n"
						+ "300642980 \t\t\t                        STRIPED DRESS		\t                         49.99\n"
						+ "300638471 \t\t\t                        BLACK BOOTS		\t\t                      35.99\n\n");

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Subtotal\t\t\t\t                                                                                  156.95\r\n"
						+ "Tax		\t\t\t\t                                                                                         0.00\r\n"
						+ "-------------------------------------------------------------------------------------------------------\r"
						+ "Total   \t                                                                                       $156.95\r\n"
						+ "-------------------------------------------------------------------------------------------------------\r\n\r\n"
						+ "Charge\r\n159.95\r\n"
						+ "Visa XXXX-XXXX-XXXX-0123\r\n");

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Refunds and Exchanges");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Within 30 days with receipt\r\n"
						+ "And tags attached");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				// Bitmap bm = BitmapFactory.decodeResource(res,
				// R.drawable.qrcode);
				// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
				// command = starbitmap.getImageRasterDataForPrinting(true);
				// tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
																	// drawer

				sendCommand(context, portName, portSettings, list);
			}
		}
	}

	/**
	 * This function shows how to print the receipt data of a thermal POS
	 * printer.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param commandType
	 *            - Command type to use for printing. This should be ("Line" or
	 *            "Raster")
	 * @param strPrintArea
	 *            - Printable area size, This should be ("3inch (78mm)" or
	 *            "4inch (112mm)")
	 */

	/**
	 * This function shows how to print the receipt data of a thermal POS
	 * printer.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param commandType
	 *            - Command type to use for printing. This should be ("Line" or
	 *            "Raster")
	 * @param strPrintArea
	 *            - Printable area size, This should be ("3inch (78mm)" or
	 *            "4inch (112mm)")
	 */

	/**
	 * This function shows how to print the receipt data of a thermal POS
	 * printer.
	 * 
	 * @param context
	 *            - Activity for displaying messages to the user
	 * @param portName
	 *            - Port name to use for communication. This should be
	 *            (TCP:<IPAddress>)
	 * @param portSettings
	 *            - Should be blank
	 * @param commandType
	 *            - Command type to use for printing. This should be ("Line" or
	 *            "Raster")
	 * @param res
	 *            - The resources object containing the image data. ( e.g.)
	 *            getResources())
	 * @param strPrintArea
	 *            - Printable area size, This should be ("3inch (78mm)" or
	 *            "4inch (112mm)")
	 */

	private static byte[] createShiftJIS(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Shift_JIS");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] createGB2312(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static byte[] createBIG5(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Big5");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
		for (int index = 0; index < cpyArray.length; index++) {
			cpyArray[index] = srcArray[index];
		}
	}

	private static byte[] createRasterCommand(String printText, int textSize,
			int bold) {
		byte[] command;

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);

		Typeface typeface;

		try {
			typeface = Typeface.create(Typeface.SERIF, bold);
		} catch (Exception e) {
			typeface = Typeface.create(Typeface.DEFAULT, bold);
		}

		paint.setTypeface(typeface);
		paint.setTextSize(textSize * 2);
		paint.setLinearText(true);

		TextPaint textpaint = new TextPaint(paint);
		textpaint.setLinearText(true);
		android.text.StaticLayout staticLayout = new StaticLayout(printText,
				textpaint, printableArea, Layout.Alignment.ALIGN_NORMAL, 1, 0,
				false);
		int height = staticLayout.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(staticLayout.getWidth(), height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bitmap);
		c.drawColor(Color.WHITE);
		c.translate(0, 0);
		staticLayout.draw(c);

		StarBitmap starbitmap = new StarBitmap(bitmap, false, printableArea);

		command = starbitmap.getImageRasterDataForPrinting(true);

		return command;
	}

	private static byte[] convertFromListByteArrayTobyteArray(
			List<Byte> ByteArray) {
		byte[] byteArray = new byte[ByteArray.size()];
		for (int index = 0; index < byteArray.length; index++) {
			byteArray[index] = ByteArray.get(index);
		}

		return byteArray;
	}

	private static void sendCommand(Context context, String portName,
			String portSettings, ArrayList<Byte> byteList) {
		StarIOPort port = null;
		try {
			/*
			 * using StarIOPort3.1.jar (support USB Port) Android OS Version:
			 * upper 2.2
			 */
			port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port =
			 * StarIOPort.getPort(portName, portSettings, 10000);
			 */
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			/*
			 * Using Begin / End Checked Block method When sending large amounts
			 * of raster data, adjust the value in the timeout in the
			 * "StarIOPort.getPort" in order to prevent "timeout" of the
			 * "endCheckedBlock method" while a printing.
			 * 
			 * If receipt print is success but timeout error occurs(Show message
			 * which is
			 * "There was no response of the printer within the timeout period."
			 * ), need to change value of timeout more longer in
			 * "StarIOPort.getPort" method. (e.g.) 10000 -> 30000
			 */
			StarPrinterStatus status = port.beginCheckedBlock();

			if (true == status.offline) {
				throw new StarIOPortException("A printer is offline");
			}

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
			port.writePort(commandToSendToPrinter, 0,
					commandToSendToPrinter.length);

			port.setEndCheckedBlockTimeoutMillis(30000);// Change the timeout
														// time of
														// endCheckedBlock
														// method.
			status = port.endCheckedBlock();

			if (true == status.coverOpen) {
				throw new StarIOPortException("Printer cover is open");
			} else if (true == status.receiptPaperEmpty) {
				throw new StarIOPortException("Receipt paper is empty");
			} else if (true == status.offline) {
				throw new StarIOPortException("Printer is offline");
			}
		} catch (StarIOPortException e) {
			try {
				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Failure");
				alert.setMessage(e.getMessage());
				alert.setCancelable(false);
				alert.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	public static void PrintPosSaleChoice(Activity ac, Context context,
			String portName, String portSettings, Resources res,
			String grossSale, String itemSale, String totalCash,
			String numofSale, String itemDis, String billDis,
			String saleRefund, String cash, String cards, String unpaid,
			String amex, String visa, String master, String nets,
			String dinners, String jcb, String Open, String mode, String form,
			String to) {
		CompanyDataSource b = new CompanyDataSource(ac, ac);

		ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		String textToPrint = ("");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		SaleReportDataSource a = new SaleReportDataSource(ac, ac);
		ItemsDataSource d = new ItemsDataSource(ac, ac);
		String address;
		String companyName;
		String phone;
		String Email;
		String web;
		String Gst;
		String fax;
		String companyNo;
		if (b.loadCName().equals("")) {
			companyName = "";
		} else {
			companyName = b.loadCName() + "\r\n";
		}
		if (b.loadCAddress().equals("")) {
			address = "";
		} else {
			address = b.loadCAddress() + "\r\n";
		}
		if (b.loadCPhone().equals("")) {
			phone = "";
		} else {
			phone = "\t\t\t\t\t\t\t\t TEL: " + b.loadCPhone() + "\r\n";
		}
		if (b.loadCEmail().equals("")) {
			Email = "";
		} else {
			Email = "\t\t\t\t\t" + b.loadCEmail() + "\r\n";
		}
		if (b.loadCWebsite().equals("")) {
			web = "";
		} else {
			web = "\t\t\t\t\t" + b.loadCWebsite() + "\r\n";
		}
		if (b.loadGST().equals("")) {
			Gst = "";
		} else {
			Gst = "\t\t\t\t\t\t\t\t" + "GST: " + b.loadGST() + "\r\n";
		}
		if (b.loadFAX().equals("")) {
			fax = "";
		} else {
			fax = "\t\t\t\t\t\t\t\t" + "FAX: " + b.loadFAX() + "\r\n";
		}
		if (b.loadCompanyNo().equals("")) {
			companyNo = "";
		} else {
			companyNo = "\t\t" + "COMPANY NO: " + b.loadCompanyNo() + "\r\n";
		}
		command = createRasterCommand(RedoAddress(companyName), 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		textToPrint = ("" + address + phone + Email + web + Gst + fax
				+ companyNo

				+ "\r\nPrinted      \t: " + ComDDUtilities.getDateTimePrint()
				+ "  \r\n" + "Cashier      \t: Cashier \r\n"
				+ "Printed by \t:  " + a.loadUserName() + "\r\n");
		if (mode.equals("2")) {
			textToPrint += " From " + form + " to " + to + "\r\n";
			textToPrint += "-----------------------------------------------------------------------\r\n";
			textToPrint += "                           GROSS SALE REPORT   \t                   ";
		} else {
			textToPrint += "-----------------------------------------------------------------------\r\n";
			textToPrint += "                           END SHIFT SALE REPORT   \t                   ";
		}
		textToPrint += "-----------------------------------------------------------------------";

		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		SaleReportDataSource ard = new SaleReportDataSource(ac, ac);
		String textin = "";
		textin += "\t\t\t Descriptions \t\t\t\t\t\t | Amount \r\n\r\n";
		String sOpen = Open.replace("$", "");
		Double dOpen = Double.parseDouble(sOpen);
		if (dOpen > 0) {
			textin += "\t\t\t CASH FLOAT  \t\t\t\t\t\t\t\t" + Open + "\r\n";
		}
		String SGross = grossSale.replace("$", "");
		Double dGross = Double.parseDouble(SGross);
		if (dGross > 0) {
			textin += "\t\t\t GROSS SALES  \t\t\t\t\t\t\t" + grossSale + "\r\n";
		}
		String SItemdis = itemDis.replace("$", "");
		Double dItemdis = Double.parseDouble(SItemdis);
		if (dItemdis > 0) {
			textin += "\t\t\t ITEM DISCOUNT  \t\t\t\t\t" + itemDis + "\r\n";
		}
		String SBillDis = billDis.replace("$", "");
		Double dBillDis = Double.parseDouble(SBillDis);
		if (dBillDis > 0) {
			textin += "\t\t\t BILL DISCOUNT  \t\t\t\t\t\t" + billDis + "\r\n";
		}

		// textin += "\t\t\t GST(IF ANY)  \t\t\t\t\t\t\t\t$" + ard.loadGST()
		// + "\r\n";
		String Scash = cash.replace("$", "");
		Double dcash = Double.parseDouble(Scash);
		if (dcash > 0) {
			textin += "\t\t\t CASH SALES  \t\t\t\t\t\t\t\t" + cash + "\r\n";
		}
		String Scards = cards.replace("$", "");
		Double dcards = Double.parseDouble(Scards);
		if (dcards > 0) {
			textin += "\t\t\t CREDIT CARD SALES  \t\t\t" + cards + "\r\n";
		}
		String SMaster = master.replace("$", "");
		Double dMaster = Double.parseDouble(SMaster);
		if (dMaster > 0) {
			textin += "\t\t\t MASTER  \t\t\t$" + master + "\r\n";
		}
		String snets = nets.replace("$", "");
		Double dnets = Double.parseDouble(snets);
		if (dnets > 0) {
			textin += "\t\t\t NETS  \t\t\t\t\t$" + nets + "\r\n";
		}
		String svisa = visa.replace("$", "");
		Double dvisa = Double.parseDouble(svisa);
		if (dvisa > 0) {
			textin += "\t\t\t VISA  \t\t\t\t\t\t$" + visa + "\r\n";
		}
		String samex = amex.replace("$", "");
		Double damex = Double.parseDouble(samex);
		if (damex > 0) {
			textin += "\t\t\t AMEX  \t\t\t\t\t$" + amex + "\r\n";
		}
		String sjcb = jcb.replace("$", "");
		Double djcb = Double.parseDouble(sjcb);
		if (djcb > 0) {
			textin += "\t\t\t JCB  \t\t\t\t\t\t$" + jcb + "\r\n";
		}
		String sdinners = dinners.replace("$", "");
		Double ddinners = Double.parseDouble(sdinners);
		if (ddinners > 0) {
			textin += "\t\t\t DINERS  \t\t\t$" + dinners + "\r\n";
		}
		String sunpaid = unpaid.replace("$", "");
		Double dunpaid = Double.parseDouble(sunpaid);
		if (dunpaid > 0) {
			textin += "\t\t\t UNPAID SALES  \t\t\t\t\t\t" + unpaid + "\r\n";
		}
		String snumofsale = numofSale.replace("$", "");
		Double dnumofsale = Double.parseDouble(snumofsale);
		if (dnumofsale > 0) {
			textin += "\t\t\t NUMBER OF SALE  \t\t\t\t" + numofSale + "\r\n";
		}
		String srefund = saleRefund.replace("$", "");
		Double drefund = Double.parseDouble(srefund);
		if (drefund > 0) {
			textin += "\t\t\t SALE REFUND  \t\t\t\t\t\t\t$-" + saleRefund
					+ "\r\n";
		}
		Double tempCashf = Double.parseDouble((ard.loadOpenCash()).replace("$",
				""));
		Double tempNet = Double.parseDouble((ard.loadTotalCash()).replace("$",
				""));
		Double totalcash = tempCashf + tempNet;
		Log.v("HAI", textin);

		command = createRasterCommand(textin, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		PayModel up = new PayModel();
		textToPrint = "";
		command = createRasterCommand(textToPrint, 13, Typeface.ITALIC);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res,
		// R.drawable.qrcode);
		// StarBitmap starbitmap = new StarBitmap(bm, false, 146);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
															// drawer

		// for (int i = 0; i < PosApp.soluongIn; i++) {
		sendCommand(context, portName, portSettings, list);
		// }
	}
	public static void PrintPos1(Activity ac, final Context context, final String portName,
			final String portSettings, Resources res, String recep) {

		final ArrayList<Byte> list = new ArrayList<Byte>();

		Byte[] tempList;
		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x00 })); // Alignment

		list.addAll(Arrays.asList(new Byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22,
				0x00 }));
		printableArea = 576; // Printable area in paper is 832(dot)

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium,
				RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut,
				RasTopMargin.Standard, 0, 0, 0);
		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		// Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.logo_print);
		// StarBitmap starbitmap = new StarBitmap(bm, true, 367);
		// command = starbitmap.getImageRasterDataForPrinting(true);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// // list.addAll(Arrays
		// // .asList(new Byte[] { 0x1b, 0x1d, 0x61, 0x01 }));
		// list.addAll(Arrays.asList(tempList));

		// command = createRasterCommand("\t\t\t\ta", 13, 0);
		// tempList = new Byte[command.length];
		// CopyArray(command, tempList);
		// list.addAll(Arrays.asList(tempList));

		String textToPrint = ("");
		command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = createRasterCommand("", 16, 1);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		/*
		 * textToPrint = (""
		 * 
		 * + " \r\n\n"
		 * 
		 * + " \r\n" + "Printed                    \t: "
		 * 
		 * + "  \r\n" + "Cashier                   \t: Cashier \r\n" +
		 * "You are served by \t:  "
		 * 
		 * + "\r\n" +
		 * "-----------------------------------------------------------------------\r"
		 * + "     Items          | Qty | Price | Dis |  Amount \t\t\t\t\r" +
		 * "----------------------------------------------------------------------\r"
		 * );
		 */
		textToPrint = ("" + "Terminal           \t:  Section 1" + "\r\n"
				+ "User                     \t:  "
				+ UserFunctions.getInstance().getUserModel().getUsername()
				+ "\r\n" + "Printed              \t:  "
				+ ComDDUtilities.getDateTimePrint() + "\r\n");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("              TABLE         PAX" + "\r\n"
				+ "                   "
				+ Utilities.getGlobalVariable(ac).tableNum/* tabnum */
				+ "                 "
				+ Utilities.getGlobalVariable(ac).numberCustomer + "\r");
		command = createRasterCommand(textToPrint, 19, Typeface.BOLD);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		textToPrint = ("-----------------------------------------------------------------------\r"
				+ "     QTY            DESCRIPTION                                     \t\t\t\t\r"
				+ "----------------------------------------------------------------------\r");
		command = createRasterCommand(textToPrint, 13, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		String textin = "";
		String name="";

		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			String remark = "";
			if (null == PosApp.listOrderData.get(i).getRemarks()
					|| PosApp.listOrderData.get(i).getRemarks()
							.equals("")) {

			} else {
				remark = "\t" + "\r\n"
						+ PosApp.listOrderData.get(i).getRemarks();
			}
			if (PosApp.listOrderData.get(i).getStandBy().equals("1")) {
//				name = PosApp.listOrderData2.get(itemnumber).getItemName()
//						+ " (Stand-by)";
				name = PosApp.listOrderData.get(i).getItemTau()
						+ " (Stand-by)";
			} else {
				if(Utilities.getGlobalVariable(ac).subNow){
//					name = PosApp.listOrderData2.get(itemnumber).getItemName()+" (Subnow)";
					name = PosApp.listOrderData.get(i).getItemTau()+" (SERVE NOW)";
					//Utilities.getGlobalVariable(ac).subNow=false;
				}else{
					name = PosApp.listOrderData.get(i).getItemTau();
//					name = PosApp.listOrderData2.get(itemnumber).getItemName();	
				}
				
			}
			textin += "\t"
					+ RedoPrice(PosApp.listOrderData.get(i).getQualyti())
					+ "\t\t" + name+"\n"+ remark + "\t"
					+ "\r\n";
			/*
			 * textin += PosApp.listOrderData.get(i).getItemName() + "\r\n" +
			 * "\t\t\t\t\t\t\t\t" +
			 * RedoPrice(PosApp.listOrderData.get(i).getQualyti()) + "\t" + "" +
			 * RedoPrice(PosApp.listOrderData.get(i).getUnitPrice()) + "\t" + ""
			 * + RedoPrice(PosApp.listOrderData.get(i).getDiscount()) + "\t\t\t"
			 * + "" + RedoPrice(PosApp.listOrderData.get(i).getAmount()) +
			 * "\r\n";
			 */

		}
		Log.v("HAI1", "Master "+textin);
		if (MainActivity.isPrintNoSave) {
			textToPrint = (textin
			/*
			 * +
			 * "-----------------------------------------------------------------------\r\n"
			 * + "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp1) + "\r\n" +
			 * "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp2) + "\r\n" +
			 * "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp3) + "\r\n" +
			 * "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp4)
			 */);
		} else {
			textToPrint = (textin
			/*
			 * +
			 * "-----------------------------------------------------------------------\r\n"
			 * + "Subtotal  : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp1) + "\r\n" +
			 * "Discount :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp2) + "\r\n" +
			 * "GST          :\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp3) + "\r\n" +
			 * "TOTAL     : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "$" + "" +
			 * df.format(temp4) + "\r\n" +
			 * "-----------------------------------------------------------------------\r\n"
			 * +
			 * "                           Payment Detail   \t                   \r\n"
			 * +
			 * "-----------------------------------------------------------------------\r\n\r\n"
			 * + "\r\n" + "Change   : \t\t\t $0.00 \r\n"
			 */);
		}

		command = createRasterCommand(textToPrint, 17, 0);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		list.addAll(Arrays.asList(tempList));
		list.addAll(Arrays.asList(new Byte[] { 0x07 })); // Kick cash
															// drawer
//		new Thread(new Runnable() {
//			public void run() {
				for(int i=0;i<PosApp.soluongIn;i++){
					sendCommand(context, portName, portSettings, list);
					Log.v("HAI1", "sendOrder");
				}
//		
//			}
//		}).start();
//		sendCommand(context, portName, portSettings, list);

	}
}
