package com.pos.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.pos.common.Utilities;
import com.pos.model.BillModel;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.PayModel;
import com.pos.model.SearchBillModel;
import com.pos.model.UnpaidModel;
import com.pos.ui.DialogSearchBill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class BillDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = BillDataSource.class
			.getSimpleName();

	public BillDataSource(Context c, Activity ac) {
		dbHelper = new MySQLiteHelper(c);
		context = ac;
	}

	public boolean checkVatTuExist(String maVatTu, String maDonVi) {

		boolean existed = false;

		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				// c = dbHelper.getDb().rawQuery(
				// "select rowid from " + MAINCATE_TABLE_NAME
				// + " where MA_VATTU = " + "'" + maVatTu + "'"
				// + " and " + "MA_DVIQLY = '" + maDonVi + "'",
				// null);
				//
				// if (c != null && c.getCount() > 0) {
				// existed = true;
				// }
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return existed;
	}

	public int update(String re) {

		int numRowEffect = 0;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Active", "0");
				String whereClause = "Receipt_no" + "=?";
				String[] whereArgs = new String[] {re };

				numRowEffect = dbHelper.getDb().update("HoldSales", cv,
						whereClause, whereArgs);
			}
		} catch (Exception e) {
		} finally {
		}

		return numRowEffect;
	}

	public long insert(UnpaidModel vt) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				ContentValues cv = new ContentValues();
				cv.put("Sales_date", currentDateandTime);
				cv.put("Total_amount", vt.getTotal_amount());
				cv.put("Receipt_no", vt.getReceipt_no());
				row_id = dbHelper.getDb().insert("HoldSales", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	public long insertItems(UnpaidModel up) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();

				cv.put("SaleID", up.getSaleID());
				cv.put("ItemName", up.getItemName());
				cv.put("Quantity", up.getQuantity());
				cv.put("UnitPrice", up.getUnitPrice());
				cv.put("Discount", up.getDiscount());
				cv.put("Amount", up.getAmount());
				cv.put("ItemCode", up.getItemCode());
				row_id = dbHelper.getDb().insert("Holdsale_details", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}
	public String loadcounter(String saledate) {
		String s="";
		Cursor c = null;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select UserName from counters inner join users on counters.UserID=users.IDUser where Date between '"+saledate+"' AND '"+currentDateandTime+"' order by Date ASC LIMIT 1";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							s+=c.getString(0);
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		return s;
	}
	public String loadcash(String SaleID) {
		String s="";
		Cursor c = null;
		Double temp=0.0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type2,Type1Amount,Type2Amount from sale_payments where SaleID="+SaleID;
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							if (c.getString(0).equals("CASH"))
							{
								Double a=Double.parseDouble(c.getString(2));
								temp+=a;
							}
							if (c.getString(1).equals("CASH"))
							{
								Double a=Double.parseDouble(c.getString(3));
								temp+=a;
							}
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		s+=""+temp;
		return s;
	}
	public String loadcard(String SaleID) {
		String s="";
		Cursor c = null;
		Double temp=0.0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type2,Type1Amount,Type2Amount from sale_payments where SaleID="+SaleID;
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							if (!c.getString(0).equals("CASH"))
							{
								Double a=Double.parseDouble(c.getString(2));
								temp+=a;
							}
							if (!c.getString(1).equals("CASH"))
							{
								Double a=Double.parseDouble(c.getString(3));
								temp+=a;
							}
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		s+=""+temp;
		return s;
	}
	public String loadprinttype(String BillID) {
		String s="";
		Cursor c = null;
		Double temp=0.0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Payment_mode from sales where Receipt_no='"+BillID+"'";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							s+=c.getString(0).toString();
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
		return s;
	}
	public String loadSaleID(String BillID) {
		String s="";
		Cursor c = null;
		Double temp=0.0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select SaleID from sales where Receipt_no='"+BillID+"'";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							s+=c.getString(0).toString();
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
		return s;
	}
	public ArrayList<BillModel> loadBill() {
		ArrayList<BillModel> result = new ArrayList<BillModel>();
		SaleReportDataSource sale=new SaleReportDataSource(context, context);
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Receipt_no,Sales_date,Total_amount,GST,Payment_mode,SaleID,IDUsers FROM sales order by Sales_date DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							BillModel vt = new BillModel();
							vt.setReceipt_no(c.getString(0));
							vt.setCounter("#Cashier");
							vt.setClosesession(c.getString(1));
							vt.setTotalamount(c.getString(2));
							if (c.getString(4).equals("1"))
							{
							vt.setCash(c.getString(2));
							vt.setCard("0.00");
							}
							if (c.getString(4).equals("2"))
							{
							vt.setCash("0.00");
							vt.setCard(c.getString(2));
							}
							if (c.getString(4).equals("3"))
							{
								vt.setCash(loadcash(c.getString(5)));
								vt.setCard(loadcard(c.getString(5)));
							}
							vt.setGST(c.getString(3));
							
							vt.setUser(sale.loadUserName2(c.getString(6)));							
							result.add(vt);
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

		return result;
	}
	public ArrayList<BillModel> loadBills(String from,String to) {
		ArrayList<BillModel> result = new ArrayList<BillModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Receipt_no,Sales_date,Total_amount,GST,Payment_mode,SaleID FROM sales where Sales_date between '"+from+"' AND '"+to+"' order by Sales_date DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							
							BillModel vt = new BillModel();
							vt.setReceipt_no(c.getString(0));
							vt.setCounter("#Cashier");
							vt.setClosesession(c.getString(1));
							vt.setTotalamount(c.getString(2));
							if (c.getString(4).equals("1"))
							{
							vt.setCash(c.getString(2));
							vt.setCard("0.00");
							}
							if (c.getString(4).equals("2"))
							{
							vt.setCash("0.00");
							vt.setCard(c.getString(2));
							}
							if (c.getString(4).equals("3"))
							{
								vt.setCash(loadcash(c.getString(5)));
								vt.setCard(loadcard(c.getString(5)));
							}
							vt.setGST(c.getString(3));
							vt.setUser(loadcounter(c.getString(1)));							
							result.add(vt);
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

		return result;
	}


	public ArrayList<SearchBillModel> loadItems() {
		ArrayList<SearchBillModel> result = new ArrayList<SearchBillModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT SaleID,Receipt_no,Sales_date,Total_amount,Status,DiscountValue FROM HoldSales WHERE Active=1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c
							.getColumnIndex(SearchBillModel.HOLDBILL_FULL_PROJECTION[0]);
					int colId_Recepno = c
							.getColumnIndex(SearchBillModel.HOLDBILL_FULL_PROJECTION[1]);
					int itemSaleDay = c
							.getColumnIndex(SearchBillModel.HOLDBILL_FULL_PROJECTION[2]);
					int itemTotal = c
							.getColumnIndex(SearchBillModel.HOLDBILL_FULL_PROJECTION[3]);
					int itemStatus = c
							.getColumnIndex(SearchBillModel.HOLDBILL_FULL_PROJECTION[4]);
					int itemDiscust = c
							.getColumnIndex(SearchBillModel.HOLDBILL_FULL_PROJECTION[5]);

					c.moveToFirst();
					do {
						try {
							String maVattu = c.getString(colId_MaItem);
							String receptno = c.getString(colId_Recepno);
							String salseDay = c.getString(itemSaleDay);
							String total = c.getString(itemTotal);
							String status = c.getString(itemStatus);
							String Dis = c.getString(itemDiscust);
							SearchBillModel vt = new SearchBillModel();
							vt.setSaleID(maVattu);
							vt.setReceipt_no(receptno);
							vt.setSales_date(salseDay);
							vt.setStatus(status);
							vt.setTotal_amount(total);
							vt.setDiscountValues(Dis);
							DialogSearchBill.recepit=receptno;
							DialogSearchBill.discountUn = Dis;
							result.add(vt);
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

		return result;
	}

}