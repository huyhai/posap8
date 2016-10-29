package com.pos.db;

import java.util.ArrayList;

import com.pos.model.ColorModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ColorDataSource {

	private MySQLiteHelper dbHelper;

	private final static String TAG = ColorDataSource.class.getSimpleName();

	public ColorDataSource(Context c) {
		dbHelper = new MySQLiteHelper(c);
	}



	public long insertTranslations(String name, String des, long idins, int lang) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();

				cv.put("GroupID", idins);
				cv.put("LanguageID", lang);
				cv.put("Name", name);
				cv.put("Description", des);
				row_id = dbHelper.getDb()
						.insert("group_translations", null, cv);
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

	public ArrayList<ColorModel> loadMainCate() {
		ArrayList<ColorModel> result = new ArrayList<ColorModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM color";
				// String query="select * from " +
				// MainCateModel.MAINCATE_TABLE_NAME+" ORDER BY "
				// +MainCateModel.FIELD_MAIN_ID +" DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c
							.getColumnIndex(ColorModel.VATTU_FULL_PROJECTION[0]);
					int colId_TenVatTu = c
							.getColumnIndex(ColorModel.VATTU_FULL_PROJECTION[1]);
					int colId_mota = c
							.getColumnIndex(ColorModel.VATTU_FULL_PROJECTION[2]);
					int colId_Image = c
							.getColumnIndex(ColorModel.VATTU_FULL_PROJECTION[3]);

					c.moveToFirst();
					do {
						try {
							String maVattu = c.getString(colId_MaVatTu);
							String tenVattu = c.getString(colId_mota);
							String quyCach = c.getString(colId_Image);
							String donGia = c.getString(colId_TenVatTu);

							ColorModel vt = new ColorModel();

							vt.set_ItemGr_ID(maVattu);
							vt.set_Name(tenVattu);
							vt.set_Description(quyCach);
							vt.set_Image(donGia);

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