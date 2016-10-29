package com.pos.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
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

public class SearchBillDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = SearchBillDataSource.class
			.getSimpleName();

	public SearchBillDataSource(Context c, Activity ac) {
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
				String[] whereArgs = new String[] { re };

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

	public ArrayList<ListOrderModel> loadItemsBill(String idSale) {
		ArrayList<ListOrderModel> result = new ArrayList<ListOrderModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM HoldSales WHERE SaleID=" + idSale
						+ "";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					try {
						if (dbHelper != null && dbHelper.getDb() != null) {
							String query1 = "SELECT * FROM Holdsale_details WHERE SaleID="
									+ idSale + "";
							c = dbHelper.getDb().rawQuery(query1, null);

							if (c != null && c.getCount() > 0) {
								int colIdItemName = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[0]);
								int colId_Quantity = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[1]);
								int itemUnitPrice = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[2]);
								int itemDiscount = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[3]);
								int itemAmount = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[4]);
								int itemItemCode = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[5]);
								int itemPrice2 = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[6]);
								int itemSpecial = c
										.getColumnIndex(ListOrderModel.DETAIL_HOLD_FULL_PROJECTION[7]);

								c.moveToFirst();
								do {
									try {
										String ten = c.getString(colIdItemName);
										String qty = c
												.getString(colId_Quantity);
										String unitprice = c
												.getString(itemUnitPrice);
										String discount = c
												.getString(itemDiscount);
										String amount = c.getString(itemAmount);
										String code = c.getString(itemItemCode);
										String pri2 = c.getString(itemPrice2);
										String special = c
												.getString(itemSpecial);
										ListOrderModel vt = new ListOrderModel();
										vt.setItemName(ten);
										vt.setAmount(amount);
										vt.setDiscount(discount);
										vt.setItemCode(code);
										vt.setQualyti(qty);
										vt.setUnitPrice(unitprice);
										vt.setPrice2(pri2);
										vt.setSpecialPrice(special);
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
				String query = "SELECT SaleID,Receipt_no,Sales_date,Total_amount,Status,DiscountValue FROM HoldSales";
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
							DialogSearchBill.recepit = receptno;
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

	public int deleteItem(String Id) {
		int numRowEffect = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String whereClause = "SaleID " + " = ? ";
				String[] whereArgs = new String[] { Id };

				numRowEffect = dbHelper.getDb().delete("HoldSales",
						whereClause, whereArgs);
//				if (numRowEffect != 0) {
//					deleteHold(Id);
//				}
			}
		} catch (Exception e) {
		} finally {

		}
		return numRowEffect;
	}

	public int deleteItemAll(String Id) {
		int numRowEffect = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String whereClause = "SaleID " + " = ? ";
				String[] whereArgs = new String[] { Id };

				numRowEffect = dbHelper.getDb().delete("Holdsale_details",
						whereClause, whereArgs);
//				if (numRowEffect != 0) {
//					deleteHoldDetail(Id);
//				}
			}
		} catch (Exception e) {
		} finally {

		}
		return numRowEffect;
	}

	private void deleteHoldDetail(String SaleID) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "deleteHoldDetail"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
	}

	private void deleteHold(String SaleID) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "deleteHold"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
	}
}