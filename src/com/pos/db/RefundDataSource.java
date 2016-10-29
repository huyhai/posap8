package com.pos.db;

import java.util.ArrayList;

import com.pos.common.Utilities;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.RefundModel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RefundDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = RefundDataSource.class.getSimpleName();

	public RefundDataSource(Context c, Activity ac) {
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

	public ArrayList<ListOrderModel> loadItemsBill(String idSale) {
		ArrayList<ListOrderModel> result = new ArrayList<ListOrderModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sale_details WHERE SaleID="
						+ idSale + "";
				c = dbHelper.getDb().rawQuery(query, null);

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
							String qty = c.getString(colId_Quantity);
							String unitprice = "-"+c.getString(itemUnitPrice);
							String discount = c.getString(itemDiscount);
							String amount = c.getString(itemAmount);
							String code = c.getString(itemItemCode);
							String pri2 = c.getString(itemPrice2);
							String special = c.getString(itemSpecial);
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

		return result;
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
	public int getSaleID(String rece) {
		int result = -1;

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT SaleID FROM sales WHERE Receipt_no="
						+ "'" + rece + "'";
				// String query="select * from " +
				// MainCateModel.MAINCATE_TABLE_NAME+" ORDER BY "
				// +MainCateModel.FIELD_MAIN_ID +" DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c.getColumnIndex("SaleID");

					c.moveToFirst();
					do {
						try {
							result = Integer
									.parseInt(c.getString(0));

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
	public String getSale(String rece) {
		int result = -1;
        String s="";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT SaleID,Receipt_no FROM sales";
				
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					

					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							s+="|";
							s+=c.getString(1);

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

		return s;
	}

	public ArrayList<RefundModel> seachItems() {
		ArrayList<RefundModel> result = new ArrayList<RefundModel>();

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
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(RefundModel.VATTU_FULL_PROJECTION[7]);

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
							RefundModel vt = new RefundModel();

							// vt.setBarcode(barcode);
							// vt.setCost_price(cost);
							// vt.setItem_code(itemcode);
							// vt.setItem_group_ID(maGroup);
							// vt.setItem_image(image);
							// vt.setItemID(maVattu);
							// vt.setName(name);
							// vt.setRemarks(remarks);
							// vt.setSelling_price_1(one);
							// vt.setSelling_price_2(two);
							// vt.setSpecial_price(special);
							// vt.setUom(uom);
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