package com.pos.db;

import java.util.ArrayList;

import com.pos.common.Utilities;
import com.pos.model.ItemsModel;
import com.pos.model.MainCateModel;
import com.pos.model.SettingModel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SettingDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = SettingDataSource.class.getSimpleName();

	public SettingDataSource(Context c, Activity ac) {
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

	public long insert(ItemsModel vt) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Item_group_ID", vt.getItem_group_ID());
				cv.put("Item_code", vt.getItem_code());
				cv.put("Item_image", vt.getItem_image());
				cv.put("Barcode", vt.getBarcode());
				cv.put("uom", vt.getUom());
				cv.put("Cost_price", vt.getCost_price());
				cv.put("Selling_price_1", vt.getSelling_price_1());
				cv.put("Selling_price_2", vt.getSelling_price_2());
				cv.put("Special_price", vt.getSpecial_price());
				cv.put("Status", vt.getStatus());
				cv.put("Remarks", vt.getRemarks());
				row_id = dbHelper.getDb().insert("items", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	public long insertTranslations(long idins, int lang, String name) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();

				cv.put("ItemID", idins);
				cv.put("LanguageID", lang);
				cv.put("Name", name);
				row_id = dbHelper.getDb().insert("item_translations", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	// public int updateStatus(MainCateModel vt) {
	//
	// int numRowEffect = 0;
	//
	// try {
	// if(dbHelper != null && dbHelper.getDb() != null)
	// {
	// ContentValues cv = new ContentValues();
	//
	// String suDung;
	//
	// if (vt.isSuDung()) {
	// suDung = "1";
	// }
	// else {
	// suDung = "0";
	// }
	//
	// cv.put(SU_DUNG, suDung);
	//
	// String whereClause = "MA_VATTU" + "=?" + " and " + "MA_DVIQLY" + "=?";
	// String[] whereArgs = new String[] { vt.getMaVattu(), vt.getMaDVIQLY() };
	//
	// numRowEffect = dbHelper.getDb().update(MAINCATE_TABLE_NAME, cv,
	// whereClause, whereArgs);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// finally{
	// }
	//
	// return numRowEffect;
	// }

	// public int update(MainCateModel vt) {
	// int numRowEffect = 0;
	//
	// try {
	// if(dbHelper != null && dbHelper.getDb() != null)
	// {
	// ContentValues cv = new ContentValues();
	//
	// cv.put(MA_VATTU, vt.getMaVattu());
	// cv.put(TEN_VATTU, vt.getTenVattu());
	// cv.put(DON_GIA, vt.getDonGia());
	// cv.put(QUY_CACH, vt.getQuyCach());
	// cv.put(MA_DVI_QLY, vt.getMaDVIQLY());
	//
	// String whereClause = "MA_VATTU" + "=?" + " and " + "MA_DVIQLY" + "=?";
	// String[] whereArgs = new String[] { vt.getMaVattu(), vt.getMaDVIQLY() };
	//
	// numRowEffect = dbHelper.getDb().update(MAINCATE_TABLE_NAME, cv,
	// whereClause, whereArgs);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// finally{
	// }
	//
	// return numRowEffect;
	// }


	public ArrayList<SettingModel> loadItems() {
		ArrayList<SettingModel> result = new ArrayList<SettingModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM Settings";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_name = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[0]);
					int colId_address = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[1]);
					int itemphone = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[2]);
					int itemfax = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[3]);
					int itemmail = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[4]);
					int itemgst = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[5]);
					int itemwweb = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[6]);
					int itemfooter = c
							.getColumnIndex(SettingModel.SETTING_FULL_PROJECTION[7]);

					c.moveToFirst();
					do {
						try {
							String ten = c.getString(colId_name);
							String address = c.getString(colId_address);
							String fone = c.getString(itemphone);
							String fax = c.getString(itemfax);
							String email = c.getString(itemmail);
							String gst = c.getString(itemgst);
							String web = c.getString(itemwweb);
							String footer = c.getString(itemfooter);
							SettingModel vt = new SettingModel();

							vt.setCompanyAddress(address);
							vt.setCompanyFax(fax);
							vt.setCompanyGST(gst);
							vt.setCompanyMail(email);
							vt.setCompanyName(ten);
							vt.setCompanyPhone(fone);
							vt.setCompanyWeb(web);
							vt.setReceiptFooter(footer);
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