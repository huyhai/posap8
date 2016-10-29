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
import com.pos.model.ColorModel;
import com.pos.model.InventoryModel;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.PayModel;
import com.pos.model.UnpaidModel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class PayDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = PayDataSource.class.getSimpleName();

	public PayDataSource(Context c, Activity ac) {
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

	public long insert(PayModel vt, String payment) {

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
				cv.put("Remarks", MainActivity.note);

				cv.put("Discount_percentage", vt.getPercentDis());
				cv.put("Discount_amount", vt.getDollarDis());
				cv.put("Payment_mode", vt.getPaymentMode());
				cv.put("CreditType", payment);
				cv.put("Sub_total", vt.getSubTotal());
				cv.put("IDUsers", vt.getIdUser());
				cv.put("GST", vt.getGST());
				row_id = dbHelper.getDb().insert("sales", null, cv);
//				if (row_id != -1) {
//					insertSale(String.valueOf(row_id), vt.getReceipt_no(),
//							currentDateandTime, vt.getSubTotal(), vt.getGST(),
//							vt.getPercentDis(), vt.getDollarDis(),
//							vt.getTotal_amount(), MainActivity.note,
//							vt.getPaymentMode(), payment, "-1", vt.getStatus(),
//							vt.getIdUser());
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	private void insertSale(String SaleID, String Receipt_no, String Sale_date,
			String Sub_total, String GST, String Discount_percentage,
			String Discount_amount, String Total_amount, String Remarks,
			String Payment_mode, String CreditType, String Counter,
			String Status, String Username) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addSale"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("Receipt_no", Receipt_no));
		params.add(new BasicNameValuePair("Sale_date", Sale_date));
		params.add(new BasicNameValuePair("Sub_total", Sub_total));
		params.add(new BasicNameValuePair("GST", GST));
		params.add(new BasicNameValuePair("Discount_percentage",
				Discount_percentage));
		params.add(new BasicNameValuePair("Discount_amount", Discount_amount));
		params.add(new BasicNameValuePair("Total_amount", Total_amount));
		params.add(new BasicNameValuePair("Remarks", Remarks));
		params.add(new BasicNameValuePair("Payment_mode", Payment_mode));
		params.add(new BasicNameValuePair("CreditType", CreditType));
		params.add(new BasicNameValuePair("Counter", Counter));
		params.add(new BasicNameValuePair("Status", Status));
		params.add(new BasicNameValuePair("UsersID", Username));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));

	}

	public long insertItems(PayModel up) {

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
				cv.put("Price2", up.getPrice2());
				if (up.getSpecialPrice().equals("")) {
					up.setSpecialPrice("0");
				}
				cv.put("SpecialPrice", up.getSpecialPrice());

				row_id = dbHelper.getDb().insert("sale_details", null, cv);
//				if (row_id != -1) {
//					insertSale_detail(up.getSaleID(), up.getItemName(),
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

	private void insertSale_detail(String SaleID, String ItemName,
			String Quantity, String UnitPrice, String Discount, String Amount,
			String ItemCode, String Price2, String SpecialPrice) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addSaleDetail"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("ItemName", ItemName));
		params.add(new BasicNameValuePair("Quantity", Quantity));
		params.add(new BasicNameValuePair("UnitPrice", UnitPrice));
		params.add(new BasicNameValuePair("Discount", Discount));
		params.add(new BasicNameValuePair("Amount", Amount));
		params.add(new BasicNameValuePair("ItemCode", ItemCode));
		params.add(new BasicNameValuePair("Price2", Price2));
		params.add(new BasicNameValuePair("SpecialPrice", SpecialPrice));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
	}

	public long insertInventoryReport(PayModel up, Activity ac) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				SaleReportDataSource a = new SaleReportDataSource(ac, ac);
				cv.put("UserName", a.loadUserName());
				cv.put("Date", currentDateandTime);
				cv.put("GroupName", getGroupNAME(up.getItemCode()));
				cv.put("ItemCode", up.getItemCode());
				cv.put("ItemName", up.getItemName());
				cv.put("StockInHand", "0");
				cv.put("TotalIn", up.getRefund());
				if (MainActivity.refund) {
					cv.put("TotalOut", "0");
				} else {
					cv.put("TotalOut", up.getQuantity());
				}

				cv.put("Balance", "0");
				// cv.put("CostPrice", getCostPrice(up.getItemCode()));
				// cv.put("SellingPrice1", up.getUnitPrice());
				cv.put("CostPrice", getCostPrice(up.getItemCode()));
				cv.put("SellingPrice1", up.getUnitPrice());
				// cv.put("uom", up.getUom());

				row_id = dbHelper.getDb().insert("Inventory_report", null, cv);
//				if (row_id != -1) {
//					insertInteryreport(currentDateandTime,
//							getGroupNAME(up.getItemCode()), up.getItemCode(),
//							up.getItemName(), "0", "0", up.getQuantity(), "0",
//							getCostPrice(up.getItemCode()), up.getUnitPrice(),
//							a.loadUserName());
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}
		return row_id;

	}

	public long insertInventoryReportStockIn(PayModel up, Activity ac,
			String costprice) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				SaleReportDataSource a = new SaleReportDataSource(ac, ac);
				cv.put("UserName", a.loadUserName());
				String currentDateandTime = sdf.format(new Date());
				cv.put("Date", currentDateandTime);
				cv.put("GroupName", getGroupNAME(up.getItemCode()));
				cv.put("ItemCode", up.getItemCode());
				cv.put("ItemName", up.getItemName());
				cv.put("StockInHand", "0");
				cv.put("TotalIn", up.getQuantity());
				cv.put("TotalOut", "0");
				cv.put("Balance", "0");
				cv.put("CostPrice", costprice);
				// cv.put("uom", up.getUom());
				cv.put("SellingPrice1", "0");

				row_id = dbHelper.getDb().insert("Inventory_report", null, cv);
//				if (row_id != -1) {
//					insertInteryreport(currentDateandTime,
//							getGroupNAME(up.getItemCode()), up.getItemCode(),
//							up.getItemName(), "0", up.getQuantity(), "0", "0",
//							costprice, "0", a.loadUserName());
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}
		return row_id;

	}

	public long insertInventoryReportAddnew(PayModel up, String num,
			Activity ac, String costprice) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				SaleReportDataSource a = new SaleReportDataSource(ac, ac);
				cv.put("UserName", a.loadUserName());
				cv.put("Date", currentDateandTime);
				cv.put("GroupName", getGroupNAME(up.getItemCode()));
				cv.put("ItemCode", up.getItemCode());
				cv.put("ItemName", up.getItemName());
				cv.put("StockInHand", num);
				cv.put("TotalIn", "0");
				cv.put("TotalOut", "0");
				cv.put("Balance", "0");
				cv.put("CostPrice", costprice);
				// cv.put("CostPrice", getCostPrice(up.getItemCode()));
				cv.put("SellingPrice1", "0");
				// cv.put("uom", up.getUom());

				row_id = dbHelper.getDb().insert("Inventory_report", null, cv);
//				if (row_id != -1) {
//					insertInteryreport(currentDateandTime,
//							getGroupNAME(up.getItemCode()), up.getItemCode(),
//							up.getItemName(), num, "0", "0", "0", costprice,
//							"0", a.loadUserName());
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}
		return row_id;

	}

	private void insertInteryreport(String date, String groupName,
			String itemCode, String itemName, String stockInHand,
			String totalIn, String totalOut, String balance, String costPrice,
			String sellingPrice1, String userName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addInventory"));
		params.add(new BasicNameValuePair("Date", date));
		params.add(new BasicNameValuePair("GroupName", groupName));
		params.add(new BasicNameValuePair("ItemCode", itemCode));
		params.add(new BasicNameValuePair("ItemName", itemName));
		params.add(new BasicNameValuePair("StockInHand", stockInHand));
		params.add(new BasicNameValuePair("TotalIn", totalIn));
		params.add(new BasicNameValuePair("TotalOut", totalOut));
		params.add(new BasicNameValuePair("Balance", balance));
		params.add(new BasicNameValuePair("CostPrice", costPrice));
		params.add(new BasicNameValuePair("SellingPrice1", sellingPrice1));
		params.add(new BasicNameValuePair("UserName", userName));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
	}

	public String getCostPrice(String idItem) {
		String result = null;

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Cost_price FROM items WHERE Item_code='"
						+ idItem + "'";
				// String query="select * from " +
				// MainCateModel.MAINCATE_TABLE_NAME+" ORDER BY "
				// +MainCateModel.FIELD_MAIN_ID +" DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c.getColumnIndex("Cost_price");

					c.moveToFirst();
					do {
						try {
							result = c.getString(colId_MaVatTu);
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

	public String getGroupNAME(String idItem) {
		String result = null;
		String grID = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {

				String query = "SELECT Item_group_ID FROM items WHERE Item_code='"
						+ idItem + "'";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c.getColumnIndex("Item_group_ID");

					c.moveToFirst();
					do {
						try {
							grID = c.getString(colId_MaVatTu);
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {

				String query = "SELECT group_translations.Name FROM group_translations  INNER JOIN Group_item ON Group_item.GroupID=group_translations.GroupID WHERE group_translations.LanguageID=1 AND Active=1 AND group_translations.GroupID='"
						+ grID + "'";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c.getColumnIndex("Name");

					c.moveToFirst();
					do {
						try {
							result = c.getString(colId_MaVatTu);
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

	public String loadStockInHand(String idItem) {
		String result = null;

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Remarks FROM items WHERE Item_code='"
						+ idItem + "'";
				// String query="select * from " +
				// MainCateModel.MAINCATE_TABLE_NAME+" ORDER BY "
				// +MainCateModel.FIELD_MAIN_ID +" DESC";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c.getColumnIndex("Remarks");

					c.moveToFirst();
					do {
						try {
							result = c.getString(colId_MaVatTu);
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

	public String loadUom(String Itemcode) {
		String result = null;

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT uom FROM items WHERE Item_code='"
						+ Itemcode + "'";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaVatTu = c.getColumnIndex("uom");

					c.moveToFirst();
					do {
						try {
							result = c.getString(colId_MaVatTu);
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

	public long insertPayment(PayModel up, long saleid) {

		long row_id = -1;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();

				cv.put("SaleID", saleid);
				cv.put("Payment_mode_ID", "3");
				cv.put("TotalAmount", up.getTotal_amount());
				cv.put("Type1", up.getType1());
				cv.put("Type2", up.getType2());
				cv.put("Type1Amount", up.getType1Amount());
				cv.put("Type2Amount", up.getType2Amount());
				cv.put("Change", up.getChange());
				cv.put("PrintReceipt", up.getPrintReciept());
				row_id = dbHelper.getDb().insert("sale_payments", null, cv);
//				if (row_id != -1) {
//					insertPayment(String.valueOf(saleid), "3",
//							up.getTotal_amount(), up.getType1(), up.getType2(),
//							up.getType1Amount(), up.getType2Amount(),
//							up.getChange(), up.getPrintReciept());
//				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

		return row_id;

	}

	private void insertPayment(String SaleID, String Payment_modeID,
			String TotalAmount, String Type1, String Type2, String Type1Amount,
			String Type2Amount, String Change, String PrintReceipt) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addPayment"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("Payment_modeID", Payment_modeID));
		params.add(new BasicNameValuePair("TotalAmount", TotalAmount));
		params.add(new BasicNameValuePair("Type1", Type1));
		params.add(new BasicNameValuePair("Type2", Type2));
		params.add(new BasicNameValuePair("Type1Amount", Type1Amount));
		params.add(new BasicNameValuePair("Type2Amount", Type2Amount));

		params.add(new BasicNameValuePair("Change", Change));
		params.add(new BasicNameValuePair("PrintReceipt", PrintReceipt));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
	}

}