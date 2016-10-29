package com.pos.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.libs.ComDDUtilities;
import com.pos.libs.DatePickerDialogFragment;
import com.pos.libs.SessionManager;
import com.pos.libs.ViewUtils;
import com.pos.model.ExportInventoryModel;
import com.pos.model.ListOrderModel;
import com.pos.print.PrinterFunctions;
import com.pos.db.CompanyDataSource;
import com.pos.db.InventoryDataSource;
import com.pos.db.SaleReportDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

public class DialogInventoryReport extends Activity implements OnClickListener,
		OnTimeSetListener, OnDateSetListener, OnItemClickListener {

	private Button btnStockin;
	private Button btnStockin2;
	private Button btnStockout;
	public static Button btnbalance;
	private Button btnAu;
	private Button btnStockValue;
	private EditText btnmoving1;
	private EditText btnmoving2;

	private Button edFromday;
	private Button edFromTime;
	private Button edToday;
	private Button edToTime;
	private boolean checkpick = true;
	private Button btnSearch;
	private String todate;
	private String fromdate;
	private String totime;
	private String formdtime;
	private Button btnPrintA4;
	private Button btnPrint;
	private ImageView imgBack;
	CompanyDataSource company;
	private EditText edInput;
	private ListView lvDataItem;
	// public static int blance=0;
	private static SessionManager ss;
	private ArrayList<ExportInventoryModel> list = new ArrayList<ExportInventoryModel>();
	// private ArrayList<ExportInventoryModel> list2 = new
	// ArrayList<ExportInventoryModel>();
	private ArrayList<ExportInventoryModel> listEachItem = new ArrayList<ExportInventoryModel>();
	InventoryDataSource da;
	private boolean oneTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_report);
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		oneTime = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private ArrayList<String> listSearch = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	private int mdate;
	private int mmonth;
	private int myear;
	int hour;
	int minute;

	@Override
	public void onClick(View v) {
		if (v == btnSearch) {
			if (TextUtils.isEmpty(edInput.getText().toString())) {
				if (edFromday.getText().toString().equals("")
						|| edToday.getText().toString().equals("")
						|| edToday.getText().toString().equals("")
						|| edToTime.getText().toString().equals("")) {
					Toast.makeText(this, "Please input date and time !!!",
							Toast.LENGTH_LONG).show();

				} else if (!edFromday.getText().toString().equals("")
						&& !edToday.getText().toString().equals("")
						&& !edFromTime.getText().toString().equals("")
						&& !edToTime.getText().toString().equals("")) {
					// SaleReportDataSource a = new SaleReportDataSource(this,
					// this);
					String form = fromdate + " " + formdtime;
					String to = todate + " " + totime;

					SaleReportDataSource a = new SaleReportDataSource(this,
							this);
					String stockin = a.loadStockind(form, to);
					String stockin2 = a.loadStockin2d(form, to);
					String stockout = a.loadStockoutd(form, to);
					String stockinhand = Double.parseDouble(stockin) + "";
					// String stockinhand = ((Double.parseDouble(stockin) +
					// Double
					// .parseDouble(stockin2)) - Double.parseDouble(stockout)) +
					// "";
					// String au = a.loadAu();
					String stockvalue = a.loadstockvalued(form, to);
					String moving1 = a.loadItemIName(form, to);
//					String moving1FOC = a.loadItemINameFOC(form, to);
//					String moving2FOC = a.loadItemIqtyFOC(form, to);
					String moving2 = a.loadItemIqty(form, to);

					btnStockin.setText(stockinhand.replace(".0", ""));
					btnStockin2.setText(stockin2.replace(".0", ""));
					btnStockout.setText(stockout.replace(".0", ""));

					int a1 = Integer.parseInt(stockinhand.replace(".0", ""));
					int b = Integer.parseInt(stockout.replace(".0", ""));
					double c = (a1 + Double.parseDouble(stockin2)) - b;

					btnbalance
							.setText("" + String.valueOf(c).replace(".0", ""));

					btnStockValue.setText(stockvalue);
					DecimalFormat df = new DecimalFormat("0.00");
					double num = Double.parseDouble(stockvalue) / c;
					String avg = (df.format(num) + "");
					btnAu.setText(avg);
					if (!avg.equals("NaN")) {
						// btnAu.setText(avg);
						btnAu.setText(avg);
						// btnAu.setText(df.format(Double.parseDouble(stockvalue)
						// /
						// c));
					} else {
						btnAu.setText("0");
					}

					Spanned s = Html.fromHtml(moving1);

					btnmoving1.setText(s);

					Spanned s1 = Html.fromHtml(moving2);

					btnmoving2.setText(s1);
					// String stockin = a.loadStockind(form, to);
					// String stockin2 = a.loadStockin2d(form, to);
					// String stockout = a.loadStockoutd(form, to);
					// String au = a.loadAud(form, to);
					// String stockvalue = a.loadstockvalued(form, to);
					// String moving1 = a.loadItemIName();
					// String moving2 = a.loadItemIqty();
					//
					// btnStockin.setText(stockin.replace(".0", ""));
					// btnStockin2.setText(stockin2.replace(".0", ""));
					// btnStockout.setText(stockout.replace(".0", ""));
					//
					// int a1 = Integer.parseInt(stockin.replace(".0", ""));
					// int b = Integer.parseInt(stockout.replace(".0", ""));
					// int c = a1 - b;
					//
					// btnbalance.setText("" + c);
					// btnAu.setText(au);
					// btnStockValue.setText(stockvalue);
					//
					// Spanned s = Html.fromHtml(moving1);
					//
					// btnmoving1.setText(s);
					//
					// Spanned s1 = Html.fromHtml(moving2);
					//
					// btnmoving2.setText(s1);
				}
			} else {
				DecimalFormat df = new DecimalFormat("0.00");
				btnStockin.setText("");
				btnStockin2.setText("");
				btnStockout.setText("");
				listEachItem = da.loadEachItem(edInput.getText().toString());
				try {
					btnbalance.setText(listEachItem.get(0).getBalance());
				} catch (Exception e) {
					// TODO: handle exception
				}

				double giatriStock = da.selectCostPrice(edInput.getText()
						.toString());
				double stockVl = 0;
				try {
					stockVl = Integer
							.parseInt(listEachItem.get(0).getBalance())
							* giatriStock;
				} catch (Exception e) {
					// TODO: handle exception
				}

				btnStockValue.setText(df.format(stockVl) + "");
				double totalItem = da.selectCount(edInput.getText().toString());
				double sumUnit = da.selectSumUnit(edInput.getText().toString());
				double ketqua;
				if (!String.valueOf(sumUnit / totalItem).equals("NaN")) {
					ketqua = sumUnit / totalItem;
					btnAu.setText(df.format(ketqua) + "");
				} else {
					btnAu.setText("0");
				}

			}

		}

		if (v == btnPrint) {
			String form = fromdate + " " + formdtime;
			String to = todate + " " + totime;
			
			PrinterFunctions.PrintItemSaleEndShift(this, this, "USB:", "", "1",
					this.getResources(), form, to);
		}
		if (v == btnPrintA4) {

			String form = fromdate + " " + formdtime;
			String to = todate + " " + totime;

			list = da.loadInventoryReport(form, to);
			if (form.equals("null null") || to.equals("null null")) {
				SaleReportDataSource a = new SaleReportDataSource(
						DialogInventoryReport.this, DialogInventoryReport.this);

				final Calendar c = Calendar.getInstance();
				int mdate = c.get(Calendar.DATE);
				int mmonth = c.get(Calendar.MONTH);
				int myear = c.get(Calendar.YEAR);
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				// form = a.loadShiftDate();
				form = ss.getENDDATE();
				to = myear + "-" + mmonth + "-" + mdate + " " + hour + ":"
						+ minute + ":" + "00";
			}
			String form1 = fromdate + " " + formdtime;
			String to1 = todate + " " + totime;
			generatePDF(form1, to1);
		}
		if (v == edFromday) {
			edInput.setText("");
			checkpick = true;
			final Calendar c = Calendar.getInstance();
			mdate = c.get(Calendar.DATE);
			mmonth = c.get(Calendar.MONTH);
			myear = c.get(Calendar.YEAR);
			DatePickerDialog d = new DatePickerDialog(this, this, myear,
					mmonth, mdate);
			d.show();
		}
		if (v == edFromTime) {
			edInput.setText("");
			checkpick = true;
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			mdate = c.get(Calendar.DATE);
			mmonth = c.get(Calendar.MONTH);
			myear = c.get(Calendar.YEAR);
			TimePickerDialog tm = new TimePickerDialog(this, this, hour,
					minute, true);
			tm.show();
		}
		if (v == edToday) {
			edInput.setText("");
			checkpick = false;
			final Calendar c1 = Calendar.getInstance();
			mdate = c1.get(Calendar.DATE);
			mmonth = c1.get(Calendar.MONTH);
			myear = c1.get(Calendar.YEAR);
			DatePickerDialog d1 = new DatePickerDialog(this, this, myear,
					mmonth, mdate);
			d1.show();
		}
		if (v == edToTime) {
			edInput.setText("");
			checkpick = false;
			final Calendar c1 = Calendar.getInstance();
			hour = c1.get(Calendar.HOUR_OF_DAY);
			minute = c1.get(Calendar.MINUTE);
			mdate = c1.get(Calendar.DATE);
			mmonth = c1.get(Calendar.MONTH);
			myear = c1.get(Calendar.YEAR);
			TimePickerDialog tm1 = new TimePickerDialog(this, this, hour,
					minute, true);
			tm1.show();
		}
		if (v == imgBack) {
			DialogInventoryReport.this.finish();
		}

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		String month;
		String date;
		if ((monthOfYear + 1) < 10) {
			month = "0" + (monthOfYear + 1);
		} else {
			month = String.valueOf(monthOfYear + 1);
		}
		if (dayOfMonth < 10) {
			date = "0" + dayOfMonth;
		} else {
			date = "" + dayOfMonth;
		}
		if (checkpick) {
			edFromday.setText(date + "/" + month + "/" + year);
			fromdate = year + "-" + month + "-" + date;
		} else {
			edToday.setText(date + "/" + month + "/" + year);
			todate = year + "-" + month + "-" + date;
		}

	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (checkpick) {
			formdtime = "";
			if (hourOfDay < 10) {

				formdtime += "0" + hourOfDay + ":";
			} else {

				formdtime += hourOfDay + ":";
			}
			if (minute < 10) {

				formdtime += "0" + minute + ":00";
			} else {

				formdtime += minute + ":00";
			}
			edFromTime.setText(formdtime);
		} else {
			totime = "";
			if (hourOfDay < 10) {

				totime += "0" + hourOfDay + ":";
			} else {

				totime += hourOfDay + ":";
			}
			if (minute < 10) {

				totime += "0" + minute + ":00";
			} else {

				totime += minute + ":00";
			}
			edFromTime.setText(formdtime);

			edToTime.setText(totime);

		}

	}

	PdfPTable table;
	private int num1 = 0;

	private BaseFont bfBold;

	private void initializeFonts() {

		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isOne = false;
	private String stockInhand = "0";

	private void generatePDF(String form, String to) {
		num1 = 0;
		initializeFonts();
		final String APPLICATION_PACKAGE_NAME = this.getBaseContext()
				.getPackageName();
		File path = new File(Environment.getExternalStorageDirectory(),
				APPLICATION_PACKAGE_NAME);
		if (!path.exists()) {
			path.mkdir();
		}
		File file = new File(path, "InventoryReport.pdf");
		// create a new document
		Document document = new Document(PageSize.A4.rotate());
		SaleReportDataSource a = new SaleReportDataSource(this, this);
		try {

			PdfWriter docWriter = PdfWriter.getInstance(document,
					new FileOutputStream(file));
			document.open();

			PdfContentByte cb = docWriter.getDirectContent();
			// initialize fonts for text printing
			initializeFonts();

			// the company logo is stored in the assets which is read only
			// get the logo and print on the document
			// InputStream inputStream = getAssets().open("a.jpg");
			// Bitmap bmp = BitmapFactory.decodeStream(inputStream);
			// ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			// Image companyLogo = Image.getInstance(stream.toByteArray());
			// companyLogo.setAbsolutePosition(25, 200);
			// companyLogo.scalePercent(25);
			// document.add(companyLogo);

			// creating a sample invoice with some customer data
			createTitle(cb, 350, 550, company.loadCName());

			createHeadings(cb, 30, 535, "ADD: " + company.loadCAddress());
			createHeadings(cb, 30, 520, "TEL: " + company.loadCPhone());
			createHeadings(cb, 30, 505, "WEB: " + company.loadCWebsite());
			createHeadings(cb, 30, 490, "GENERAL INVENTORY REPORT");
			createHeadings(cb, 30, 475, "FROM " + InventoryDataSource.mForm + " TO " + InventoryDataSource.mTo);
			createHeadings(cb, 500, 520, "");
			createHeadings(cb, 500, 520, ComDDUtilities.getDateTimePrint());
			createHeadings(cb, 500, 505, a.loadUserName());

			// list all the products sold to the customer
			float[] columnWidths = { 1.5f, 1.5f, 2f, 4f, 1.5f, 1.5f, 1.5f,
					1.5f, 1.5f, 1.5f };
			// create PDF table with the given widths

			table = new PdfPTable(columnWidths);
			// set table width a percentage of the page width
			table.setTotalWidth(700f);

			PdfPCell cell = new PdfPCell(new Phrase("DATE"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("GROUP"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("ITEM CODE"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("ITEM NAME"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("STOCK IN HAND"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("TOTAL IN"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("TOTAL OUT"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("BALANCE"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("USERS"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("COST PRICE"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			// cell = new PdfPCell(new Phrase("UOM"));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// table.addCell(cell);
			table.setHeaderRows(1);
			InventoryDataSource id = new InventoryDataSource(this, this);
			// DecimalFormat df = new DecimalFormat("0.00");
			for (int i = 0; i < list.size(); i++) {
				table.addCell(list.get(i).getDate());
				table.addCell(list.get(i).getGroup());
				if (list.get(i).getGroup() == null) {
					table.addCell(list.get(i).getItemcode());
					table.addCell(list.get(i).getItemname());
					table.addCell("0");
					table.addCell("0");
					table.addCell(list.get(i).getTotalout());
					table.addCell("0");
					table.addCell(list.get(i).getUsername());
					table.addCell(list.get(i).getCostprice());

				} else {
					table.addCell(list.get(i).getItemcode());
					table.addCell(list.get(i).getItemname());

					stockInhand = id.loadStockinrp1(list.get(i).getItemcode(),
							form, to);
					if (stockInhand.equals("null")) {
						stockInhand = "0";
					}

					table.addCell(stockInhand);
					table.addCell(list.get(i).getTotalin());
					table.addCell(list.get(i).getTotalout());
					int t1 = -1;
					try {
						t1 = Integer.parseInt(stockInhand);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Double t2 = -1.0;
					try {
						t2 = Double.parseDouble(list.get(i).getTotalin());
					} catch (Exception e) {
						e.printStackTrace();
					}
					Double t3 = -1.0;
					try {
						t3 = Double.parseDouble(list.get(i).getTotalout());
					} catch (Exception e) {
						e.printStackTrace();
					}
					Double t4 = -1.0;
					try {
						t4 = (t1 + t2) - t3;
					} catch (Exception e) {
						e.printStackTrace();
					}
					table.addCell("" + t4);
					// table.addCell(list.get(i).getBalance());
					table.addCell(list.get(i).getUsername());
					table.addCell(list.get(i).getCostprice());

				}

				num1++;
				if (num1 == 9) {

					num1 = 0;
					if (oneTime) {
						document.newPage();
						table.writeSelectedRows(0, -1, document.leftMargin(),
								550, docWriter.getDirectContent());
						table.deleteBodyRows();
					} else {
						table.writeSelectedRows(0, -1, document.leftMargin(),
								450, docWriter.getDirectContent());
						table.deleteBodyRows();

					}

					oneTime = true;
				}
				if (i == list.size() - 1) {
					if (i < 10) {
						table.writeSelectedRows(0, -1, document.leftMargin(),
								450, docWriter.getDirectContent());
						table.deleteBodyRows();
					} else {
						document.newPage();
						table.writeSelectedRows(0, -1, document.leftMargin(),
								550, docWriter.getDirectContent());
						table.deleteBodyRows();
					}

				}
			}

			// absolute location to print the PDF table from
			// document.newPage();
			// table.writeSelectedRows(0, -1, document.leftMargin(), 450,
			// docWriter.getDirectContent());

			// print the signature image along with the persons name
			// inputStream = getAssets().open("b.jpg");
			// bmp = BitmapFactory.decodeStream(inputStream);
			// stream = new ByteArrayOutputStream();
			// bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			// Image signature = Image.getInstance(stream.toByteArray());
			// signature.setAbsolutePosition(400f, 150f);
			// signature.scalePercent(25f);
			// document.add(signature);

			// createHeadings(cb, 450, 135, personName);
			// document.setPageSize(PageSize.A4.rotate());

			Intent target = new Intent(Intent.ACTION_VIEW);
			target.setDataAndType(Uri.fromFile(file), "application/pdf");
			target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

			Intent intent = Intent.createChooser(target, "Open File");
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				// Instruct the user to install a PDF reader here, or something
			}
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTitle(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 15);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();

	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 10);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == lvDataItem) {
			String str = lvDataItem.getItemAtPosition(position).toString();
			edInput.setText(str);
			lvDataItem.setVisibility(View.GONE);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edInput.getWindowToken(), 0);
		}

	}

}
