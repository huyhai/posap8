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
import com.pos.model.SettingModel;
import com.pos.model.UnpaidModel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class UnpaidDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = UnpaidDataSource.class.getSimpleName();

	public UnpaidDataSource(Context c, Activity ac) {
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

	public int updateTotal(String idSale, String total, String discount) {
		int numRowEffect = 0;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();

				cv.put("Total_amount", total);
				 cv.put("DiscountValue", discount);
				// cv.put(QUY_CACH, vt.getQuyCach());
				// cv.put(MA_DVI_QLY, vt.getMaDVIQLY());

				String whereClause = "SaleID" + "=?";
				String[] whereArgs = new String[] { idSale };

				numRowEffect = dbHelper.getDb().update("HoldSales", cv,
						whereClause, whereArgs);
//				if(numRowEffect!=0){
//					updateHoldSale(idSale, total, discount);
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return numRowEffect;
	}

	private void updateHoldSale(String SaleID, String total,
			String discount) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateSumHoldSale"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("Total", total));
		params.add(new BasicNameValuePair("Dis", discount));
		params.add(new BasicNameValuePair("Tid", String.valueOf(PosApp.teminal)));

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
				cv.put("Status", vt.getStatus());
				cv.put("Remarks", vt.getNote());
				cv.put("DiscountValue", vt.getDiscount());
				cv.put("Active", "1");
				row_id = dbHelper.getDb().insert("HoldSales", null, cv);
//				if (row_id != -1) {
//					insertHoldSale(String.valueOf(row_id), vt.getReceipt_no(),
//							currentDateandTime, vt.getDiscount(),
//							vt.getTotal_amount(), vt.getNote(),vt.getStatus());
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	private void insertHoldSale(String SaleID, String Receipt_no,
			String Sale_date, String Discount_value, String Total_amount,
			String Remarks,String stt) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addHoldSale"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("Receipt_no", Receipt_no));
		params.add(new BasicNameValuePair("Sale_date", Sale_date));
		params.add(new BasicNameValuePair("Discount_value", Discount_value));
		params.add(new BasicNameValuePair("Total_amount", Total_amount));
		params.add(new BasicNameValuePair("Remarks", Remarks));
		params.add(new BasicNameValuePair("Status", stt));
		params.add(new BasicNameValuePair("Tid", String.valueOf(PosApp.teminal)));

	}

	private void insertHoldSale_detail(String SaleID, String ItemName,
			String Quantity, String UnitPrice, String Discount, String Amount,
			String ItemCode, String Price2, String SpecialPrice) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addHoldSaleDetail"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("ItemName", ItemName));
		params.add(new BasicNameValuePair("Quantity", Quantity));
		params.add(new BasicNameValuePair("UnitPrice", UnitPrice));
		params.add(new BasicNameValuePair("Discount", Discount));
		params.add(new BasicNameValuePair("Amount", Amount));
		params.add(new BasicNameValuePair("ItemCode", ItemCode));
		params.add(new BasicNameValuePair("Price2", Price2));
		params.add(new BasicNameValuePair("SpecialPrice", SpecialPrice));
		params.add(new BasicNameValuePair("Tid", String.valueOf(PosApp.teminal)));
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

				if (up.getPrice2().equals("")) {
					up.setPrice2("0");
				}
				if (up.getSpecialPrice().equals("")) {
					up.setSpecialPrice("0");
				}
				cv.put("Price2", up.getPrice2());
				cv.put("SpecialPrice", up.getSpecialPrice());
				row_id = dbHelper.getDb().insert("Holdsale_details", null, cv);
//				if (row_id != -1) {
//					insertHoldSale_detail(up.getSaleID(), up.getItemName(),
//							up.getQuantity(), up.getUnitPrice(),
//							up.getDiscount(), up.getAmount(), up.getItemCode(),
//							up.getPrice2(), up.getSpecialPrice());
//				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	public ArrayList<ItemsModel> loadItems() {
		ArrayList<ItemsModel> result = new ArrayList<ItemsModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT items .*,item_translations.Name FROM items   INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
						+ Utilities.getGlobalVariable(context).language_code
						+ " AND items.Status=1 AND Item_group_ID="
						+ Utilities.getGlobalVariable(context).posGroup;
				// String query="select * from " +
				// MainCateModel.MAINCATE_TABLE_NAME+" ORDER BY "
				// +MainCateModel.FIELD_MAIN_ID +" DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[7]);
					int itemPrice2 = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[8]);
					int itemSpecialPrice = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[9]);
					int itemRemark = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[11]);
					int itemName = c
							.getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[12]);

					c.moveToFirst();
					do {
						try {
							String maVattu = c.getString(colId_MaItem);
							String maGroup = c.getString(colId_MaGroup);
							String itemcode = c.getString(itemCode);
							String image = c.getString(itemImage);
							String barcode = c.getString(itemBarcode);
							String uom = c.getString(itemUom);
							String cost = c.getString(itemCostPrice);
							String one = c.getString(itemPrice1);
							String two = c.getString(itemPrice2);
							String special = c.getString(itemSpecialPrice);
							String remarks = c.getString(itemRemark);
							String name = c.getString(itemName);
							ItemsModel vt = new ItemsModel();
							// if (!oneTime) {
							// result.add(vt);
							// oneTime=true;
							// }

							vt.setBarcode(barcode);
							vt.setCost_price(cost);
							vt.setItem_code(itemcode);
							vt.setItem_group_ID(maGroup);
							vt.setItem_image(image);
							vt.setItemID(maVattu);
							vt.setName(name);
							vt.setRemarks(remarks);
							vt.setSelling_price_1(one);
							vt.setSelling_price_2(two);
							vt.setSpecial_price(special);
							vt.setUom(uom);
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