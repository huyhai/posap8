package com.pos.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.PosApp;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.model.ItemsModel;
import com.pos.model.MainCateModel;
import com.pos.model.SettingModel;
import com.pos.model.UsersModel;
import com.pos.ui.LoginDialog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UsersDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = UsersDataSource.class.getSimpleName();

	public UsersDataSource(Context c, Activity ac) {
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

	public long insert(UsersModel vt) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("UserName", vt.getUserName());
				cv.put("Image", vt.getImage());
				cv.put("Status", vt.getStatus());
				row_id = dbHelper.getDb().insert("users", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	public long insertCounters(String idins, String name) {

		long row_id = -1;
		String username = "";

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				cv.put("Date", currentDateandTime);
				cv.put("Cash_float_start", idins);
				cv.put("UserID", name);
				row_id = dbHelper.getDb().insert("counters", null, cv);
//				if (row_id != -1) {
//
//					for (int i = 0; i < LoginDialog.list.size(); i++) {
//						if (LoginDialog.list.get(i).getIDUser().equals(name)) {
//							username = LoginDialog.list.get(i).getUserName();
//							break;
//						}
//					}
//					insertCuonters(currentDateandTime, idins, name, username);
//				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	private void insertCuonters(String date, String CastStart, String UserID,
			String UserName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addCounters"));
		params.add(new BasicNameValuePair("Date", date));
		params.add(new BasicNameValuePair("CastStart", CastStart));
		params.add(new BasicNameValuePair("CastEnd", "0"));
		params.add(new BasicNameValuePair("UserID", UserID));
		params.add(new BasicNameValuePair("UserName", UserName));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
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
	public String DelelteUser(String id) {
		String error = "";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {

				ContentValues newValues = new ContentValues();
				newValues.put("Status", "0");
				int i = dbHelper.getWritableDatabase().update("users",
						newValues, "IDUser=" + id, null);
				error += i;
				dbHelper.close();
			}
		} catch (Exception e) {
			// e.toString();
			error += e.toString();
		}
		return error;
	}

	public ArrayList<UsersModel> loadItems() {
		ArrayList<UsersModel> result = new ArrayList<UsersModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM users where Status=1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_id = c
							.getColumnIndex(UsersModel.VATTU_FULL_PROJECTION[0]);
					int colId_username = c
							.getColumnIndex(UsersModel.VATTU_FULL_PROJECTION[1]);
					int itemimage = c
							.getColumnIndex(UsersModel.VATTU_FULL_PROJECTION[2]);
					int itemstatus = c
							.getColumnIndex(UsersModel.VATTU_FULL_PROJECTION[3]);

					c.moveToFirst();
					do {
						try {
							String id = c.getString(colId_id);
							String ten = c.getString(colId_username);
							String image = c.getString(itemimage);
							String stt = c.getString(itemstatus);
							UsersModel vt = new UsersModel();
							vt.setIDUser(id);
							vt.setImage(image);
							vt.setStatus(stt);
							vt.setUserName(ten);
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