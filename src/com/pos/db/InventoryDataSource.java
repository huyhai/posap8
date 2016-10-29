package com.pos.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.pos.common.Utilities;
import com.pos.libs.SessionManager;
import com.pos.model.ExportInventoryModel;
import com.pos.model.InventoryModel;
import com.pos.model.SettingModel;

import android.R.array;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InventoryDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = InventoryDataSource.class.getSimpleName();

	public InventoryDataSource(Context c, Activity ac) {
		dbHelper = new MySQLiteHelper(c);
		context = ac;
		ss = new SessionManager(c);
	}

	private boolean oneTime = false;

	public String[] loadItemsAdapter(String[] Array) {

		Cursor c = null;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT items .*,item_translations.Name FROM items   INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
						+ Utilities.getGlobalVariable(context).language_code
						+ " AND items.Status=1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[7]);
					int itemPrice2 = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[8]);
					int itemSpecialPrice = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[9]);
					int itemRemark = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[11]);
					int itemName = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[12]);

					c.moveToFirst();
					do {
						try {
							// String maVattu = c.getString(colId_MaItem);
							// String maGroup = c.getString(colId_MaGroup);
							// String itemcode = c.getString(itemCode);
							// String image = c.getString(itemImage);
							// String barcode = c.getString(itemBarcode);
							// String uom = c.getString(itemUom);
							// String cost = c.getString(itemCostPrice);
							// String one = c.getString(itemPrice1);
							// String two = c.getString(itemPrice2);
							// String special = c.getString(itemSpecialPrice);
							// String remarks = c.getString(itemRemark);
							// String name = c.getString(itemName);
							// ItemsModel vt = new ItemsModel();

							Array[i] = c.getString(itemCode);
							i++;
							Array[i] = c.getString(itemName);
							i++;
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

		return Array;
	}

	public String loadItemsTest(String s) {

		Cursor c = null;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT items .*,item_translations.Name FROM items   INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
						+ Utilities.getGlobalVariable(context).language_code
						+ " AND items.Status=1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[7]);
					int itemPrice2 = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[8]);
					int itemSpecialPrice = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[9]);
					int itemRemark = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[11]);
					int itemName = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[12]);

					c.moveToFirst();
					do {
						try {
							// String maVattu = c.getString(colId_MaItem);
							// String maGroup = c.getString(colId_MaGroup);
							// String itemcode = c.getString(itemCode);
							// String image = c.getString(itemImage);
							// String barcode = c.getString(itemBarcode);
							// String uom = c.getString(itemUom);
							// String cost = c.getString(itemCostPrice);
							// String one = c.getString(itemPrice1);
							// String two = c.getString(itemPrice2);
							// String special = c.getString(itemSpecialPrice);
							// String remarks = c.getString(itemRemark);
							// String name = c.getString(itemName);
							// ItemsModel vt = new ItemsModel();

							// Array[i]=c.getString(itemCode).toString();
							i++;
							s += c.getString(itemCode).toString();
							s += "|";
							// Array[i]=c.getString(itemName).toString();
							// i++;
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

	public String loadItemsSearch(String s1, String s2) {

		// String [] s2 = null;
		Cursor c = null;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT items .*,item_translations.Name FROM items   INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
						+ Utilities.getGlobalVariable(context).language_code;
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_MaItem = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[7]);
					int itemPrice2 = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[8]);
					int itemSpecialPrice = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[9]);
					int itemStatus = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[10]);
					int itemRemark = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[11]);
					int itemName = c
							.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[12]);

					c.moveToFirst();
					do {
						try {
							// String maVattu = c.getString(colId_MaItem);
							// String maGroup = c.getString(colId_MaGroup);
							String itemcode = c.getString(itemCode);
							// String image = c.getString(itemImage);
							// String barcode = c.getString(itemBarcode);
							// String uom = c.getString(itemUom);
							// String cost = c.getString(itemCostPrice);
							// String one = c.getString(itemPrice1);
							// String two = c.getString(itemPrice2);
							// String special = c.getString(itemSpecialPrice);
							// String remarks = c.getString(itemRemark);
							// String name = c.getString(itemName);
							// ItemsModel vt = new ItemsModel();

							// Array[i]=c.getString(itemCode).toString();
							// s1+=s;
							if (s2.equals(itemcode.toString())) {
								if (s1.equals("itemcode")) {
									s1 = c.getString(itemCode).toString();
								}
								if (s1.equals("qty")) {
									s1 = c.getString(itemRemark).toString();
								}
								if (s1.equals("itemname")) {
									s1 = c.getString(itemName).toString();
								}
								if (s1.equals("unitprice")) {
									s1 = c.getString(itemCostPrice).toString();
								}
								if (s1.equals("price")) {
									s1 = c.getString(itemPrice1).toString();
								}
								if (s1.equals("price2")) {
									s1 = c.getString(itemPrice2).toString();
								}
								if (s1.equals("specials")) {
									s1 = c.getString(itemSpecialPrice)
											.toString();
								}
								if (s1.equals("active")) {
									s1 = c.getString(itemStatus).toString();
								}

							}
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

		if (s1.equals("itemcode") || s1.equals("qty") || s1.equals("itemname")
				|| s1.equals("unitprice") || s1.equals("price")
				|| s1.equals("price2") || s1.equals("specials")
				|| s1.equals("active")) {
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
							+ Utilities.getGlobalVariable(context).language_code;
					c = dbHelper.getDb().rawQuery(query, null);

					if (c != null && c.getCount() > 0) {
						int colId_MaItem = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[0]);
						int colId_MaGroup = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[1]);
						int itemCode = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[2]);
						int itemImage = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[3]);
						int itemBarcode = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[4]);
						int itemUom = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[5]);
						int itemCostPrice = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[6]);
						int itemPrice1 = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[7]);
						int itemPrice2 = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[8]);
						int itemSpecialPrice = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[9]);
						int itemActive = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[10]);
						int itemRemark = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[11]);
						int itemName = c
								.getColumnIndex(InventoryModel.VATTU_FULL_PROJECTION[12]);

						c.moveToFirst();
						do {
							try {
								// String maVattu = c.getString(colId_MaItem);
								// String maGroup = c.getString(colId_MaGroup);
								String itemcode = c.getString(itemCode);
								// String image = c.getString(itemImage);
								// String barcode = c.getString(itemBarcode);
								// String uom = c.getString(itemUom);
								// String cost = c.getString(itemCostPrice);
								// String one = c.getString(itemPrice1);
								// String two = c.getString(itemPrice2);
								// String special =
								// c.getString(itemSpecialPrice);
								// String remarks = c.getString(itemRemark);
								String name = c.getString(itemName);
								// ItemsModel vt = new ItemsModel();

								// Array[i]=c.getString(itemCode).toString();
								// s1+=s;
								if (s2.equals(name.toString())) {
									if (s1.equals("itemcode")) {
										s1 = c.getString(itemCode).toString();
									}
									if (s1.equals("qty")) {
										s1 = c.getString(itemRemark).toString();
									}
									if (s1.equals("itemname")) {
										s1 = c.getString(itemName).toString();
									}
									if (s1.equals("unitprice")) {
										s1 = c.getString(itemCostPrice)
												.toString();
									}
									if (s1.equals("price")) {
										s1 = c.getString(itemPrice1).toString();
									}
									if (s1.equals("price2")) {
										s1 = c.getString(itemPrice2).toString();
									}
									if (s1.equals("specials")) {
										s1 = c.getString(itemSpecialPrice)
												.toString();
									}
									if (s1.equals("active")) {
										s1 = c.getString(itemActive).toString();
									}

								}
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

		if (s1.equals("itemcode")) {
			s1 = "";
		}
		if (s1.equals("qty")) {
			s1 = "";
		}
		if (s1.equals("itemname")) {
			s1 = "";
		}
		if (s1.equals("unitprice")) {
			s1 = "";
		}
		if (s1.equals("price")) {
			s1 = "";
		}
		if (s1.equals("price2")) {
			s1 = "";
		}
		if (s1.equals("specials")) {
			s1 = "";
		}
		return s1;

	}

	public String loadStockin() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select StockInHand from Inventory_report";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {

							temp += Double.parseDouble(c.getString(0));

						} catch (Exception e) {
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

		s += temp;
		return s;
	}

	public String loadDateBegin() {
		String s = "";
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Date FROM counters ORDER BY CountersID DESC LIMIT 1";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s = c.getString(0);
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

	private static SessionManager ss;
	public static String mForm;
	public static String mTo;

	// public static int sum=0;
	public ArrayList<ExportInventoryModel> loadInventoryReport(String form,
			String to) {
		ArrayList<ExportInventoryModel> result = new ArrayList<ExportInventoryModel>();

		Cursor c = null;
		String query;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				// if (form.equals("null null") || to.equals("null null")) {
				// form = "Begin";
				// to = "Now";
				// query =
				// "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName,uom From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName,uom FROM Inventory_report  group by ItemCode order by ItemCode)";
				// } else {
				// query =
				// "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName,uom From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName,uom FROM Inventory_report  group by ItemCode order by ItemCode) where Date between '"
				// + form + "' AND '" + to + "'";
				// }
				if (form.equals("null null") || to.equals("null null")) {
					// form = ss.getENDDATE();
					final Calendar c1 = Calendar.getInstance();
					int mdate = c1.get(Calendar.DATE);
					int mmonth = c1.get(Calendar.MONTH) + 1;
					int myear = c1.get(Calendar.YEAR);
					int hour = c1.get(Calendar.HOUR_OF_DAY);
					int minute = c1.get(Calendar.MINUTE);
					int second = c1.get(Calendar.SECOND);
					// form = a.loadShiftDate();
					form = ss.getENDDATE();
					String realmonth;
					if (mmonth < 10) {
						realmonth = "0" + mmonth;
					} else {
						realmonth = mmonth + "";
					}
					String realDay;
					if (mdate < 10) {
						realDay = "0" + mdate;
					} else {
						realDay = mdate + "";
					}
					to = myear + "-" + realmonth + "-" + realDay + " " + hour
							+ ":" + minute + ":" + second;
					if (form.equals("")) {
						mForm="Begin";
						mTo=to;
						query = "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName FROM Inventory_report  group by ItemCode, ItemName order by ItemCode, ItemName)";
					} else {
						query = "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName FROM Inventory_report   where Date between "
								+ "'"
								+ form
								+ "' AND '"
								+ to
								+ "'"
								+ " group by ItemCode, ItemName order by ItemCode, ItemName ) where Date between '"
								+ form + "' AND '" + to + "'";
						mForm=form;
						mTo=to;
					}

					// query =
					// "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName,uom From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName,uom FROM Inventory_report  group by ItemCode order by ItemCode) where Date between '"
					// + form + "' AND '" + to + "'";
					// query =
					// "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName,uom From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName,uom FROM Inventory_report  group by ItemCode order by ItemCode)";
				} else {
					query = "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName FROM Inventory_report   where Date between "
							+ "'"
							+ form
							+ "' AND '"
							+ to
							+ "'"
							+ " group by ItemCode, ItemName order by ItemCode, ItemName ) where Date between '"
							+ form + "' AND '" + to + "'";
					mForm=form;
					mTo=to;
				}
				
				
				Log.v("HAI", query);
				// String query =
				// "SELECT Date,'Group',ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Sum(Balance) as d,CostPrice,SellingPrice1 FROM Inventory_report  group by ItemCode order by ItemCode";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_Date = c.getColumnIndex("Date");
					int colId_Group = c.getColumnIndex("GroupName");
					int itemItemCode = c.getColumnIndex("ItemCode");
					int itemItemName = c.getColumnIndex("ItemName");
					int itemStockInHand = c.getColumnIndex("a");
					int itemTotalIn = c.getColumnIndex("b");
					int itemTotalOut = c.getColumnIndex("c");
					int itemBalance = c.getColumnIndex("d");
					int itemCostPrice = c.getColumnIndex("CostPrice");
					int itemSellingPrice1 = c.getColumnIndex("SellingPrice1");
					int itemUserName = c.getColumnIndex("UserName");
					int itemuom = c.getColumnIndex("uom");

					c.moveToFirst();
					do {
						try {
							String date = c.getString(colId_Date);
							String gr = c.getString(colId_Group);
							String itemcode = c.getString(itemItemCode);
							String itemName = c.getString(itemItemName);
							String stockInhand = c.getString(itemStockInHand);
							String itemTotalIn3 = c.getString(itemTotalIn);
							String itemTotalOutv = c.getString(itemTotalOut);
							String itemBalancev = c.getString(itemBalance);
							String itemCostPricev = c.getString(itemCostPrice);
							String itemSellingPrice1v = c
									.getString(itemSellingPrice1);
							ExportInventoryModel vt = new ExportInventoryModel();

							vt.setBalance(itemBalancev);
							vt.setDate(date);
							vt.setGroup(gr);
							vt.setItemcode(itemcode);
							vt.setItemname(itemName);
							vt.setStockinhand(stockInhand);
							vt.setTotalin(itemTotalIn3);
							vt.setTotalout(itemTotalOutv);
							vt.setCostprice(itemCostPricev);
							vt.setSellingprice1(itemSellingPrice1v);
							vt.setUsername(c.getString(itemUserName));
							// vt.setUom(c.getString(itemuom));
							int a = Integer.parseInt(vt.getBalance());
							int b = Integer.parseInt(vt.getTotalout());
							if (a != 0 || b!=0) {
								result.add(vt);
							}
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

	public String loadStockinrp1(String Itemcode, String from, String to) {
		String s = "";
		Cursor c = null;
		String sdate = loadShiftDate();
		String query;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		String sdate1 = "";
		if (sdate.equals("")) {
			sdate1 = currentDateandTime;
		} else {
			sdate1 = sdate;
		}

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (from.equals("null null")) {
					if (ss.getENDDATE().equals("")) {
						query = "select ItemCode,ItemName, sum(b1) as b2 from (SELECT Date,ItemCode,ItemName,StockInHand,TotalIn,TotalOut,Balance,(StockInHand+TotalIn-TotalOut) as b1 FROM Inventory_report where ItemCode='"
								+ Itemcode + "')";
					} else {
						query = "select ItemCode,ItemName, sum(b1) as b2 from (SELECT Date,ItemCode,ItemName,StockInHand,TotalIn,TotalOut,Balance,(StockInHand+TotalIn-TotalOut) as b1 FROM Inventory_report where ItemCode='"
								+ Itemcode
								+ "' and date < '"
								+ ss.getENDDATE()
								+ "')";
						// query =
						// "select ItemCode,ItemName, sum(b1) as b2 from (SELECT Date,ItemCode,ItemName,StockInHand,TotalIn,TotalOut,Balance,(StockInHand+TotalIn-TotalOut) as b1 FROM Inventory_report where ItemCode='"
						// + Itemcode
						// + "' and date BETWEEN '"
						// + ss.getENDDATE()
						// + "' AND '" + currentDateandTime + "')";
					}

				} else {
					query = "select ItemCode,ItemName, sum(b1) as b2 from (SELECT Date,ItemCode,ItemName,StockInHand,TotalIn,TotalOut,Balance,(StockInHand+TotalIn-TotalOut) as b1 FROM Inventory_report where ItemCode='"
							+ Itemcode + "' and date < '" + from + "')";
				}

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(2);
						} catch (Exception e) {
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
		if (sdate.equals("")) {
			s = "0";
		}
		return s;
	}

	public String loadStockinrp(String Itemcode) {
		String s = "";
		Cursor c = null;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		String sdate1 = "";
		if (sdate.equals("")) {
			sdate1 = currentDateandTime;
		} else {
			sdate1 = sdate;
		}

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select ItemCode,ItemName, sum(b1) as b2 from (SELECT Date,ItemCode,ItemName,StockInHand,TotalIn,TotalOut,Balance,(StockInHand+TotalIn-TotalOut) as b1 FROM Inventory_report where ItemCode='"
						+ Itemcode + "' and date < '" + sdate1 + "')";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(2);
						} catch (Exception e) {
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
		if (sdate.equals("")) {
			s = "0";
		}
		return s;
	}

	public String loadStockinrpd(String Itemcode, String form, String to) {
		String s = "";
		Cursor c = null;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		String sdate1 = "";
		if (sdate.equals("")) {
			sdate1 = currentDateandTime;
		} else {
			sdate1 = sdate;
		}

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select ItemCode,ItemName, sum(b1) as b2 from (SELECT Date,ItemCode,ItemName,StockInHand,TotalIn,TotalOut,Balance,(StockInHand+TotalIn-TotalOut) as b1 FROM Inventory_report where ItemCode='"
						+ Itemcode + "' and date < '" + sdate1 + "')";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(2);
						} catch (Exception e) {
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
		if (sdate.equals("")) {
			s = "0";
		}
		return s;
	}

	public String loadShiftDate() {
		String s = "";
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Date from counters where Cash_float_end NOT NULL order by Date DESC LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(0);
						} catch (Exception e) {
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

	public static int sum = 0;

	public ArrayList<ExportInventoryModel> loadInventoryReport1() {
		ArrayList<ExportInventoryModel> result = new ArrayList<ExportInventoryModel>();

		Cursor c = null;
		String query;
		String sdate = loadShiftDate();

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				// if (form.equals("null null") || to.equals("null null")) {
				// form = "Begin";
				// to = "Now";
				query = "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName,uom From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName,uom FROM Inventory_report  group by ItemCode order by ItemCode)";
				// } else {
				// query =
				// "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName,uom From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName,uom FROM Inventory_report  group by ItemCode order by ItemCode) where Date between '"
				// + form + "' AND '" + to + "'";
				// }

				// String query =
				// "SELECT Date,'Group',ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Sum(Balance) as d,CostPrice,SellingPrice1 FROM Inventory_report  group by ItemCode order by ItemCode";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_Date = c.getColumnIndex("Date");
					int colId_Group = c.getColumnIndex("GroupName");
					int itemItemCode = c.getColumnIndex("ItemCode");
					int itemItemName = c.getColumnIndex("ItemName");
					int itemStockInHand = c.getColumnIndex("a");
					int itemTotalIn = c.getColumnIndex("b");
					int itemTotalOut = c.getColumnIndex("c");
					int itemBalance = c.getColumnIndex("d");
					int itemCostPrice = c.getColumnIndex("CostPrice");
					int itemSellingPrice1 = c.getColumnIndex("SellingPrice1");
					int itemUserName = c.getColumnIndex("UserName");
					int itemuom = c.getColumnIndex("uom");

					c.moveToFirst();
					do {
						try {
							String date = c.getString(colId_Date);
							String gr = c.getString(colId_Group);
							String itemcode = c.getString(itemItemCode);
							String itemName = c.getString(itemItemName);
							String stockInhand = c.getString(itemStockInHand);
							String itemTotalIn3 = c.getString(itemTotalIn);
							String itemTotalOutv = c.getString(itemTotalOut);
							String itemBalancev = c.getString(itemBalance);
							String itemCostPricev = c.getString(itemCostPrice);
							String itemSellingPrice1v = c
									.getString(itemSellingPrice1);
							sum += Integer.parseInt(itemBalancev);
							ExportInventoryModel vt = new ExportInventoryModel();

							vt.setBalance(itemBalancev);
							vt.setDate(date);
							vt.setGroup(gr);
							vt.setItemcode(itemcode);
							vt.setItemname(itemName);
							vt.setStockinhand(stockInhand);
							vt.setTotalin(itemTotalIn3);
							vt.setTotalout(itemTotalOutv);
							vt.setCostprice(itemCostPricev);
							vt.setSellingprice1(itemSellingPrice1v);
							vt.setUsername(c.getString(itemUserName));
							vt.setUom(c.getString(itemuom));
							int a = Integer.parseInt(vt.getBalance());
							if (a != 0) {
								result.add(vt);
							}
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

	public ArrayList<ExportInventoryModel> loadEachItem(String itemNameInput) {
		ArrayList<ExportInventoryModel> result = new ArrayList<ExportInventoryModel>();

		Cursor c = null;
		String query;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				query = "SELECT Date,GroupName,ItemCode,ItemName,a,b,c,a+b-c as d,CostPrice,SellingPrice1,UserName From (SELECT Date,GroupName,ItemCode,ItemName,Sum(StockInHand) as a,Sum(TotalIn) as b,Sum(TotalOut) as c,Balance,CostPrice,SellingPrice1,UserName FROM Inventory_report  group by ItemCode order by ItemCode ) where ItemName='"
						+ itemNameInput + "'";
				Log.v("HAI", query);
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_Date = c.getColumnIndex("Date");
					int colId_Group = c.getColumnIndex("GroupName");
					int itemItemCode = c.getColumnIndex("ItemCode");
					int itemItemName = c.getColumnIndex("ItemName");
					int itemStockInHand = c.getColumnIndex("a");
					int itemTotalIn = c.getColumnIndex("b");
					int itemTotalOut = c.getColumnIndex("c");
					int itemBalance = c.getColumnIndex("d");
					int itemCostPrice = c.getColumnIndex("CostPrice");
					int itemSellingPrice1 = c.getColumnIndex("SellingPrice1");
					int itemUserName = c.getColumnIndex("UserName");
					// int itemuom = c.getColumnIndex("uom");

					c.moveToFirst();
					do {
						try {
							String date = c.getString(colId_Date);
							String gr = c.getString(colId_Group);
							String itemcode = c.getString(itemItemCode);
							String itemName = c.getString(itemItemName);
							String stockInhand = c.getString(itemStockInHand);
							String itemTotalIn3 = c.getString(itemTotalIn);
							String itemTotalOutv = c.getString(itemTotalOut);
							String itemBalancev = c.getString(itemBalance);
							String itemCostPricev = c.getString(itemCostPrice);
							String itemSellingPrice1v = c
									.getString(itemSellingPrice1);
							// sum+=Integer.parseInt(itemBalancev);
							ExportInventoryModel vt = new ExportInventoryModel();

							vt.setBalance(itemBalancev);
							vt.setDate(date);
							vt.setGroup(gr);
							vt.setItemcode(itemcode);
							vt.setItemname(itemName);
							vt.setStockinhand(stockInhand);
							vt.setTotalin(itemTotalIn3);
							vt.setTotalout(itemTotalOutv);
							vt.setCostprice(itemCostPricev);
							vt.setSellingprice1(itemSellingPrice1v);
							vt.setUsername(c.getString(itemUserName));
							// vt.setUom(c.getString(itemuom));
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

	public int selectCount(String itemName) {
		int s = -1;
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT c as gg FROM (SELECT count(*) as c from sale_details WHERE ItemName='"
						+ itemName + "')";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					try {
						s = c.getInt(0);
					} catch (Exception e) {
						e.printStackTrace();
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

		return s;
	}

	public int selectCostPrice(String itemName) {
		int s = -1;
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Cost_price  FROM items INNER JOIN item_translations ON item_translations.ItemID=items.ItemID WHERE item_translations.Name='"
						+ itemName + "' ";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					try {
						s = c.getInt(0);
					} catch (Exception e) {
						e.printStackTrace();
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

		return s;
	}

	public int selectSumUnit(String itemName) {
		int s = -1;
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT ItemName,SumUnit From (SELECT ItemName,Sum(UnitPrice) as SumUnit FROM sale_details WHERE ItemName='"
						+ itemName + "') ";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					try {
						s = c.getInt(1);
					} catch (Exception e) {
						e.printStackTrace();
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

		return s;
	}

}