package com.pos.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.PosApp;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.model.BillModel;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.PayModel;
import com.pos.model.SearchBillModel;
import com.pos.model.UnpaidModel;
import com.pos.model.UpdateSalesAllModel;
import com.pos.ui.DialogSearchBill;
import com.pos.ui.LoginDialog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class UpdateAllDatabase {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = UpdateAllDatabase.class.getSimpleName();

	public UpdateAllDatabase(Context c, Activity ac) {
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

	private void insertCuonters(String date, String CastStart, String UserID,
			String UserName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addCounters"));
		params.add(new BasicNameValuePair("Date", date));
		params.add(new BasicNameValuePair("CastStart", CastStart));
		params.add(new BasicNameValuePair("CastEnd", UserID));
		params.add(new BasicNameValuePair("UserID", UserID));
		params.add(new BasicNameValuePair("UserName", UserName));
		params.add(new BasicNameValuePair("TeminalID", String
				.valueOf(PosApp.teminal)));
	}

	public ArrayList<UnpaidModel> upCounters() {
		ArrayList<UnpaidModel> result = new ArrayList<UnpaidModel>();

		Cursor c = null;
		String username="";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM counters";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_date = c.getColumnIndex("Date");
					int colId_cashStart = c.getColumnIndex("Cash_float_start");
					int colId_cashEnd = c.getColumnIndex("Cash_float_end");
					int colId_ID = c.getColumnIndex("UserID");

					c.moveToFirst();
					do {
						try {
							String date = c.getString(colId_date);
							String cashStart = c.getString(colId_cashStart);
							String cashEnd = c.getString(colId_cashEnd);
							String ID = c.getString(colId_ID);

							for (int i = 0; i < LoginDialog.list.size(); i++) {
								if (LoginDialog.list.get(i).getIDUser()
										.equals(ID)) {
									username = LoginDialog.list.get(i)
											.getUserName();
									break;
								}
							}
							if(cashEnd==null){
								cashEnd="0";
							}
							insertCuonters(date, cashStart, cashEnd, username);

						} catch (Exception e) {
							e.printStackTrace();
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

	public ArrayList<UnpaidModel> upHold_saleDetail() {
		ArrayList<UnpaidModel> result = new ArrayList<UnpaidModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM holdsale_details";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_SaleID = c.getColumnIndex("SaleID");
					int colId_ItemName = c.getColumnIndex("ItemName");
					int colId_Quantity = c.getColumnIndex("Quantity");
					int colId_UnitPrice = c.getColumnIndex("UnitPrice");
					int colId_Discount = c.getColumnIndex("Discount");
					int colId_Amount = c.getColumnIndex("Amount");

					int colId_ItemCode = c.getColumnIndex("ItemCode");
					int colId_Price2 = c.getColumnIndex("Price2");
					int colId_SpecialPrice = c.getColumnIndex("SpecialPrice");

					// int colId_Status = c.getColumnIndex("Status");

					c.moveToFirst();
					do {
						try {
							String saleID = c.getString(colId_SaleID);
							String itemName = c.getString(colId_ItemName);
							String quality = c.getString(colId_Quantity);
							String unitPrice = c.getString(colId_UnitPrice);
							String discount = c.getString(colId_Discount);
							String amount = c.getString(colId_Amount);

							String itemCode = c.getString(colId_ItemCode);
							String price2 = c.getString(colId_Price2);
							String specialPrice = c
									.getString(colId_SpecialPrice);
							// String status = c.getString(colId_Status);

							insertHoldSale_detail(saleID, itemName, quality,
									unitPrice, discount, amount, itemCode,
									price2, specialPrice);

						} catch (Exception e) {
							e.printStackTrace();
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

	public ArrayList<PayModel> upHold_sale() {
		ArrayList<PayModel> result = new ArrayList<PayModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM HoldSales";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_SaleID = c.getColumnIndex("SaleID");
					int colId_Receipt_no = c.getColumnIndex("Receipt_no");
					int colId_Sales_date = c.getColumnIndex("Sales_date");
					int colId_DiscountValue = c.getColumnIndex("DiscountValue");
					int colId_Total_amount = c.getColumnIndex("Total_amount");
					int colId_Remarks = c.getColumnIndex("Remarks");

					 int colId_Status = c.getColumnIndex("Status");

					c.moveToFirst();
					do {
						try {
							String saleID = c.getString(colId_SaleID);
							String reciepNo = c.getString(colId_Receipt_no);
							String saleDate = c.getString(colId_Sales_date);
							String disValue = c.getString(colId_DiscountValue);
							String totalAmount = c
									.getString(colId_Total_amount);
							String remarks = c.getString(colId_Remarks);
							 String status = c.getString(colId_Status);

							insertHoldSale(saleID, reciepNo, saleDate,
									disValue, totalAmount, remarks,status);

						} catch (Exception e) {
							e.printStackTrace();
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

	private void insertHoldSale(String SaleID, String Receipt_no,
			String Sale_date, String Discount_value, String Total_amount,
			String Remarks,String status) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addHoldSale"));
		params.add(new BasicNameValuePair("SaleID", SaleID));
		params.add(new BasicNameValuePair("Receipt_no", Receipt_no));
		params.add(new BasicNameValuePair("Sale_date", Sale_date));
		params.add(new BasicNameValuePair("Discount_value", Discount_value));
		params.add(new BasicNameValuePair("Total_amount", Total_amount));
		params.add(new BasicNameValuePair("Remarks", Remarks));
		params.add(new BasicNameValuePair("Status", status));
		params.add(new BasicNameValuePair("Tid", String.valueOf(PosApp.teminal)));

	}

	public ArrayList<PayModel> upInventoryReport() {
		ArrayList<PayModel> result = new ArrayList<PayModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM Inventory_report";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_Date = c.getColumnIndex("Date");
					int colId_GroupName = c.getColumnIndex("GroupName");
					int colId_ItemCode = c.getColumnIndex("ItemCode");
					int colId_ItemName = c.getColumnIndex("ItemName");
					int colId_StockInHand = c.getColumnIndex("StockInHand");
					int colId_TotalIn = c.getColumnIndex("TotalIn");
					int colId_TotalOut = c.getColumnIndex("TotalOut");
					int colId_Balance = c.getColumnIndex("Balance");
					int colId_CostPrice = c.getColumnIndex("CostPrice");

					int colId_SellingPrice1 = c.getColumnIndex("SellingPrice1");
					int colId_UserName = c.getColumnIndex("UserName");

					c.moveToFirst();
					do {
						try {
							String date = c.getString(colId_Date);
							String grName = c.getString(colId_GroupName);
							String itCode = c.getString(colId_ItemCode);
							String itName = c.getString(colId_ItemName);
							String stockInhand = c.getString(colId_StockInHand);
							String totalIn = c.getString(colId_TotalIn);
							String totalOut = c.getString(colId_TotalOut);
							String balance = c.getString(colId_Balance);
							String costPrice = c.getString(colId_CostPrice);
							String sell1 = c.getString(colId_SellingPrice1);
							String ussername = c.getString(colId_UserName);

							insertInteryreport(date, grName, itCode, itName,
									stockInhand, totalIn, totalOut, balance,
									costPrice, sell1, ussername);

						} catch (Exception e) {
							e.printStackTrace();
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

	public ArrayList<PayModel> upSalePayment() {
		ArrayList<PayModel> result = new ArrayList<PayModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sale_payments";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c.getColumnIndex("SaleID");
					int colId_Payment_mode_ID = c
							.getColumnIndex("Payment_mode_ID");
					int colId_TotalAmount = c.getColumnIndex("TotalAmount");
					int colId_Type1 = c.getColumnIndex("Type1");
					int colId_Type2 = c.getColumnIndex("Type2");
					int colId_Type1Amount = c.getColumnIndex("Type1Amount");
					int colId_Type2Amount = c.getColumnIndex("Type2Amount");
					int colId_Change = c.getColumnIndex("Change");
					int colId_PrintReceipt = c.getColumnIndex("PrintReceipt");

					c.moveToFirst();
					do {
						try {
							String saleID = c.getString(colId_MaItem);
							String paymentmode = c
									.getString(colId_Payment_mode_ID);
							String totalAmount = c.getString(colId_TotalAmount);
							String type1 = c.getString(colId_Type1);
							String type2 = c.getString(colId_Type2);
							String type1Amount = c.getString(colId_Type1Amount);
							String type2Amount = c.getString(colId_Type2Amount);
							String change = c.getString(colId_Change);
							String printReceip = c
									.getString(colId_PrintReceipt);

							insertPayment(saleID, paymentmode, totalAmount,
									type1, type2, type1Amount, type2Amount,
									change, printReceip);
							// PayModel vt = new PayModel();
							// vt.setSaleID(saleID);
							// vt.setPaymentMode(paymentmode);
							// vt.setTotal_amount(totalAmount);
							// vt.setType1(type1);
							// // String dis=Double.parseDouble(itemDis)+"";
							// vt.setType2(type2);
							// vt.setType1Amount(type1Amount);
							// vt.setType2Amount(type2Amount);
							// vt.setChange(change);
							// vt.setPrintReciept(printReceip);
							//
							// result.add(vt);

						} catch (Exception e) {
							e.printStackTrace();
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

		// for (int i = 0; i < result.size(); i++) {
		// insertSale_detail(result.get(i).getSaleID(), result.get(i)
		// .getItemName(), result.get(i).getQuantity(), result.get(i)
		// .getUnitPrice(), result.get(i).getItemDiscount(), result.get(i)
		// .getItemAmount(), result.get(i).getItemCode(), result.get(i)
		// .getPrice2(), result.get(i).getSpecialPrice());
		// }
		return result;
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

	public ArrayList<UpdateSalesAllModel> upSaleDetail() {
		ArrayList<UpdateSalesAllModel> result = new ArrayList<UpdateSalesAllModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sale_details";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c.getColumnIndex("SaleID");
					int colId_ItemName = c.getColumnIndex("ItemName");
					int itemQuantity = c.getColumnIndex("Quantity");
					int itemUnitPrice = c.getColumnIndex("UnitPrice");
					int itemDiscount = c.getColumnIndex("Discount");
					int itemAmount1 = c.getColumnIndex("Amount");
					int colId_ItemCode = c.getColumnIndex("ItemCode");
					int colId_Price2 = c.getColumnIndex("Price2");
					int itemSpecialPrice = c.getColumnIndex("SpecialPrice");

					c.moveToFirst();
					do {
						try {
							String saleID = c.getString(colId_MaItem);
							String itemName = c.getString(colId_ItemName);
							String quanlyti = c.getString(itemQuantity);
							String unitPrice = c.getString(itemUnitPrice);
							String itemDis = c.getString(itemDiscount);
							String itemAmount = c.getString(itemAmount1);
							String itemCode = c.getString(colId_ItemCode);
							String price2 = c.getString(colId_Price2);
							String specialPrice = c.getString(itemSpecialPrice);

							UpdateSalesAllModel vt = new UpdateSalesAllModel();
							vt.setSaleID(saleID);
							vt.setItemName(itemName);
							vt.setQuantity(quanlyti);
							vt.setUnitPrice(unitPrice);
							// String dis=Double.parseDouble(itemDis)+"";
							vt.setItemDiscount(itemDis);
							vt.setItemCode(itemCode);
							vt.setPrice2(price2);
							vt.setSpecialPrice(specialPrice);
							vt.setItemAmount(itemAmount);

							result.add(vt);

						} catch (Exception e) {
							e.printStackTrace();
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

		for (int i = 0; i < result.size(); i++) {
			insertSale_detail(result.get(i).getSaleID(), result.get(i)
					.getItemName(), result.get(i).getQuantity(), result.get(i)
					.getUnitPrice(), result.get(i).getItemDiscount(), result
					.get(i).getItemAmount(), result.get(i).getItemCode(),
					result.get(i).getPrice2(), result.get(i).getSpecialPrice());
		}
		return result;
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

	public ArrayList<UpdateSalesAllModel> upSale() {
		ArrayList<UpdateSalesAllModel> result = new ArrayList<UpdateSalesAllModel>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sales";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c.getColumnIndex("SaleID");
					int colId_Recepno = c.getColumnIndex("Receipt_no");
					int itemSaleDay = c.getColumnIndex("Sales_date");
					int itemSub_total = c.getColumnIndex("Sub_total");
					int itemGST = c.getColumnIndex("GST");
					int itemDiscount_percentage = c
							.getColumnIndex("Discount_percentage");
					int colId_Discount_amount = c
							.getColumnIndex("Discount_amount");
					int colId_Total_amount = c.getColumnIndex("Total_amount");
					int itemRemarks = c.getColumnIndex("Remarks");
					int itemPayment_mode = c.getColumnIndex("Payment_mode");
					int itemCreditType = c.getColumnIndex("CreditType");
					int itemCounter = c.getColumnIndex("Counter");
					int colId_Status = c.getColumnIndex("Status");
					int colIdIDUsers = c.getColumnIndex("IDUsers");

					c.moveToFirst();
					do {
						try {
							String maVattu = c.getString(colId_MaItem);
							String receptno = c.getString(colId_Recepno);
							String salseDay = c.getString(itemSaleDay);
							String subtotal = c.getString(itemSub_total);
							String gst = c.getString(itemGST);
							String Dispercent = c
									.getString(itemDiscount_percentage);
							String disAmount = c
									.getString(colId_Discount_amount);
							String totalAmount = c
									.getString(colId_Total_amount);
							String remark = c.getString(itemRemarks);
							String paymentMode = c.getString(itemPayment_mode);
							String creaditType = c.getString(itemCreditType);
							String countter = c.getString(itemCounter);
							String status = c.getString(colId_Status);
							String idUser = c.getString(colIdIDUsers);

							UpdateSalesAllModel vt = new UpdateSalesAllModel();
							vt.setSaleID(maVattu);
							vt.setReceipt_no(receptno);
							vt.setSale_day(salseDay);
							vt.setSubTotal(subtotal);
							vt.setGST(gst);
							vt.setDiscount(Dispercent);

							vt.setDollarDis(disAmount);
							vt.setTotal_amount(totalAmount);
							vt.setRemark(remark);
							vt.setPaymentMode(paymentMode);
							vt.setCreaditType(creaditType);
							if (countter == null) {
								countter = "-1";
							}
							vt.setCounters(countter);
							vt.setStatus(status);
							vt.setIdUser(idUser);

							result.add(vt);

						} catch (Exception e) {
							e.printStackTrace();
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

		for (int i = 0; i < result.size(); i++) {
			insertAllSale(result.get(i).getSaleID(), result.get(i)
					.getReceipt_no(), result.get(i).getSale_day(), result
					.get(i).getSubTotal(), result.get(i).getGST(), result
					.get(i).getDiscount(), result.get(i).getDollarDis(), result
					.get(i).getTotal_amount(), result.get(i).getRemark(),
					result.get(i).getPaymentMode(), result.get(i)
							.getCreaditType(), result.get(i).getCounters(),
					result.get(i).getStatus(), result.get(i).getIdUser());
		}
		return result;
	}

	private void insertAllSale(String SaleID, String Receipt_no,
			String Sale_date, String Sub_total, String GST,
			String Discount_percentage, String Discount_amount,
			String Total_amount, String Remarks, String Payment_mode,
			String CreditType, String Counter, String Status, String Username) {
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

}