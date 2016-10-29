package com.pos.db;

import java.util.ArrayList;

import com.pos.common.Utilities;
import com.pos.model.MainCateModel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MainCateDataSource {

	private MySQLiteHelper dbHelper;

	public static final String MAINCATE_TABLE_NAME = "Group_item";

	public static final String FIELD_MAIN_ID = "GroupID";
	// public static final String FIELD_NAME = "Name";
	// public static final String FIELD_DESCRIPTION = "Description";
	public static final String FIELD_IMAGE = "Image";

	private final static String TAG = MainCateDataSource.class.getSimpleName();
	private Activity ac;

	public MainCateDataSource(Activity c) {
		dbHelper = new MySQLiteHelper(c);
		ac = c;
	}

	public boolean checkVatTuExist(String maVatTu, String maDonVi) {

		boolean existed = false;

		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				c = dbHelper.getDb().rawQuery(
						"select rowid from " + MAINCATE_TABLE_NAME
								+ " where MA_VATTU = " + "'" + maVatTu + "'"
								+ " and " + "MA_DVIQLY = '" + maDonVi + "'",
						null);

				if (c != null && c.getCount() > 0) {
					existed = true;
				}
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

	public boolean checkGroupCode(String groupCode) {

		int s = -1;
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Group_Code from Group_item where Group_Code = '"
						+ groupCode + "'";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s = c.getInt(0);

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

		if (s != -1) {
			return true;
		} else {
			return false;
		}
	}

	public long insert(MainCateModel vt) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();

				cv.put(FIELD_IMAGE, vt.get_Image());
				cv.put("Active", vt.getActive());
				cv.put("Group_Code", vt.get_group_code());
				cv.put("TextSize", vt.getTextSize());
				row_id = dbHelper.getDb().insert(MAINCATE_TABLE_NAME, null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	public String checkgroupid(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select GroupID from group_translations where Name = '"
						+ maVatTu + "' LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);

						} catch (Exception e) {
							s += e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s += e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return s;
	}

	public String checkgroupcode(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Group_Code from Group_item where GroupID = '"
						+ maVatTu + "' LIMIT 1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);

						} catch (Exception e) {
							s += e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s += e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return s;
	}

	public String checkgroupCN(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Name from group_translations where GroupID = '"
						+ maVatTu + "' AND LanguageID=2 LIMIT 1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);

						} catch (Exception e) {
							s += e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s += e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return s;
	}

	public String getTs(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select TextSize from Group_item where GroupID = '"
						+ maVatTu + "' LIMIT 1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);

						} catch (Exception e) {
							s += e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s += e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return s;
	}

	public String getImage(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Image from Group_item where GroupID = '"
						+ maVatTu + "' LIMIT 1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);

						} catch (Exception e) {
							s += e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s += e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return s;
	}

	public void update(String image, String groupcode, String groupid,
			String name, String nameCN, String ts) {

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Image", image);
				cv.put("Group_Code", groupcode);
				cv.put("TextSize", ts);
				dbHelper.getDb().update(MAINCATE_TABLE_NAME, cv,
						"GroupID=" + groupid, null);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Name", name);
				dbHelper.getDb().update("group_translations", cv,
						"GroupID=" + groupid + " AND LanguageID=1", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

	}

	public void update2(String image, String groupcode, String groupid,
			String name, String nameCN) {

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Name", nameCN);
				dbHelper.getDb().update("group_translations", cv,
						"GroupID=" + groupid + " AND LanguageID=2", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
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
	public String DelelteMainCate(String id) {
		String error = "";
		String query = "";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {

				ContentValues newValues = new ContentValues();
				newValues.put("Active", "0");

				int i = dbHelper.getWritableDatabase().update("Group_item",
						newValues, "GroupID=" + id, null);
				error += i;
				dbHelper.close();
				// dbHelper.getWritableDatabase().execSQL("UPDATE items SET Cost_price = ?, Remarks = ? WHERE ItemID = "
				// + id,
				// new String[] { uprice,qty});
			}

		} catch (Exception e) {
			// e.toString();
			error += e.toString();
		}
		return error;

	}

	public ArrayList<MainCateModel> loadMainCate() {
		ArrayList<MainCateModel> result = new ArrayList<MainCateModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Group_item .*, group_translations.Name,group_translations.Description FROM Group_item  INNER JOIN group_translations ON Group_item.GroupID=group_translations.GroupID WHERE group_translations.LanguageID="
						+ Utilities.getGlobalVariable(ac).language_code
						+ " AND Active=1";
				// String query="select * from " +
				// MainCateModel.MAINCATE_TABLE_NAME+" ORDER BY "
				// +MainCateModel.FIELD_MAIN_ID +" DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c
							.getColumnIndex(MainCateModel.VATTU_FULL_PROJECTION[0]);
					int colId_TenVatTu = c
							.getColumnIndex(MainCateModel.VATTU_FULL_PROJECTION[1]);
					int colId_mota = c
							.getColumnIndex(MainCateModel.VATTU_FULL_PROJECTION[2]);
					int colId_Image = c
							.getColumnIndex(MainCateModel.VATTU_FULL_PROJECTION[3]);
					int colId_Ts = c.getColumnIndex("TextSize");

					c.moveToFirst();
					do {
						try {
							String maVattu = c.getString(colId_MaVatTu);
							String tenVattu = c.getString(colId_mota);
							String quyCach = c.getString(colId_Image);
							String donGia = c.getString(colId_TenVatTu);

							MainCateModel vt = new MainCateModel();

							vt.set_ItemGr_ID(maVattu);
							if(tenVattu.equals("")){
								vt.set_Name(getEnglishIfDontHaveChinese(maVattu));
							}else{
								vt.set_Name(tenVattu);
							}
							
							// vt.set_Description(quyCach);
							vt.set_Image(donGia);
							vt.setTextSize(c.getString(colId_Ts));
							// Log.v("HAI", donGia);

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

	public String getEnglishIfDontHaveChinese(String grCode) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT  group_translations.Name FROM Group_item  INNER JOIN group_translations ON Group_item.GroupID=group_translations.GroupID WHERE group_translations.LanguageID=1 AND Group_item.GroupID="+grCode;
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s = c.getString(0);

						} catch (Exception e) {
							s = e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s = e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return s;
	}
}