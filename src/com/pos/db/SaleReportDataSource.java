package com.pos.db;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.pos.PosApp;
import com.pos.common.Utilities;
import com.pos.libs.SessionManager;
import com.pos.model.ColorModel;
import com.pos.model.StockInModel;
import com.pos.ui.DialogInventoryReport;

import android.R.array;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SaleReportDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = SaleReportDataSource.class
			.getSimpleName();

	public SaleReportDataSource(Context c, Activity ac) {
		dbHelper = new MySQLiteHelper(c);
		context = ac;
		ss = new SessionManager(c);
	}

	private boolean oneTime = false;

	public ArrayList<String> loadAllItem() {
		ArrayList<String> result = new ArrayList<String>();

		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID=1 AND items.Status=1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					int colId_Name = c.getColumnIndex("Name");

					c.moveToFirst();
					do {
						try {
							String name = c.getString(colId_Name);
							result.add(name);
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
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[7]);
					int itemPrice2 = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[8]);
					int itemSpecialPrice = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[9]);
					int itemRemark = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[11]);
					int itemName = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[12]);

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

	public String UpdateStockIn2(String uprice, String qty, String id) {
		String error = "";
		String query = "";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				// query =
				// "Update items set Cost_price="+uprice+",Remarks='"+qty+"' where ItemID="+id;
				// dbHelper.getDb().execSQL(query);
				// dbHelper.getDb().
				// dbHelper.getWritableDatabase();
				ContentValues newValues = new ContentValues();
				newValues.put("Cost_price", uprice);

				int i = dbHelper.getWritableDatabase().update("items",
						newValues, "Item_code=" + id, null);
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

	public void UpdateStockIn(String uprice, String qty, String id, String Price) {
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Update items set Cost_price=" + uprice
						+ ",Remarks='" + qty + "',Selling_price_1=" + Price
						+ " where Item_code='" + id + "'";
				dbHelper.getDb().execSQL(query);

			}

		} catch (Exception e) {
			e.toString();

		}

	}

	public String CheckEndshift() {
		String s = "";
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Cash_float_end from counters order by Date DESC LIMIT 1";

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
			// s+=e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		if (s.equals("null")) {
			return "notok";
		} else {
			return "ok";
		}

	}

	// public String Endshift(String end) {
	// String s = "";
	// Cursor c = null;
	// String query = "";
	// try {
	// if (dbHelper != null && dbHelper.getDb() != null) {
	// query = "Select CountersID from counters order by Date DESC LIMIT 1";
	//
	// c = dbHelper.getDb().rawQuery(query, null);
	//
	// if (c != null && c.getCount() > 0) {
	//
	// c.moveToFirst();
	// do {
	// try {
	//
	// s += c.getString(0);
	// ;
	//
	// } catch (Exception e) {
	// }
	// } while (c.moveToNext());
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// s += e.toString();
	//
	// // TODO: handle exception
	// } finally {
	// if (c != null) {
	// c.close();
	// c = null;
	// }
	// }
	//
	// try {
	// if (dbHelper != null && dbHelper.getDb() != null) {
	// SimpleDateFormat sdf = new SimpleDateFormat(
	// "yyyy-MM-dd HH:mm:ss");
	// String currentDateandTime = sdf.format(new Date());
	// PosApp.dayEnd = currentDateandTime;
	//
	// SaleReportDataSource a = new SaleReportDataSource(context,
	// context);
	// String stockin = a.loadStockin();
	//
	// String stockin2 = a.loadStockin2();
	// String stockout = a.loadStockout();
	// String stockinhand = Double.parseDouble(stockin) + ss.getEND()
	// + "";
	//
	// int a1 = Integer.parseInt(stockinhand.replace(".0", ""));
	// int b = Integer.parseInt(stockout.replace(".0", ""));
	// double c1 = (a1 + Double.parseDouble(stockin2)) - b;
	//
	// ss.saveENDDATE(String.valueOf(currentDateandTime));
	// ss.saveEND(Integer.parseInt(String.valueOf(c1)
	// .replace(".0", "")));
	// query = "Update counters set Cash_float_end=" + end + ",Date='"
	// + currentDateandTime + "' where CountersID='" + s + "'";
	// dbHelper.getDb().execSQL(query);
	//
	// }
	//
	// } catch (Exception e) {
	// e.toString();
	// s += e.toString();
	// }
	// return query + s;
	// }
	public String Endshift(String end) {
		String s = "";
		Cursor c = null;
		String query = "";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				query = "Select CountersID from counters order by Date DESC LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);
							;

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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String currentDateandTime = sdf.format(new Date());
				PosApp.dayEnd = currentDateandTime;

				SaleReportDataSource a = new SaleReportDataSource(context,
						context);

				String stockin2 = a.loadStockin2();
				String stockout = a.loadStockout();
				String stockinhand = ss.getEND() + "";

				int a1 = Integer.parseInt(stockinhand.replace(".0", ""));
				int b = Integer.parseInt(stockout.replace(".0", ""));
				double c1 = (a1 + Double.parseDouble(stockin2)) - b;

				try {
					ss.saveEND(Integer.parseInt(String.valueOf(c1).replace(
							".0", "")));
				} catch (Exception e) {
					// TODO: handle exception
				}

				ss.saveENDDATE(String.valueOf(currentDateandTime));
				query = "Update counters set Cash_float_end=" + end + ",Date='"
						+ currentDateandTime + "' where CountersID='" + s + "'";
				dbHelper.getDb().execSQL(query);

			}

		} catch (Exception e) {
			e.toString();
			s += e.toString();
		}
		return query + s;
	}

	private static SessionManager ss;

	public String loadOpenCash() {
		String s = "$";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Cash_float_start from counters order by Date DESC LIMIT 1";

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
	
	public String loadOpenCashd(String form, String to) {
		String s = "$";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Cash_float_start from counters where Date between '"
						+ form
						+ "' AND '"
						+ to
						+ "' order by Date DESC LIMIT 1";

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

	public String loadGrossSale() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							Double temp1 = Double.parseDouble(c.getString(0));
							if (temp1 < 0) {

							} else {
								temp += Double.parseDouble(c.getString(0));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);

		return s;
	}

	public String loadGrossSaled(String from, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where Sales_date between '"
						+ from + "' AND '" + to + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							Double temp1 = Double.parseDouble(c.getString(0));
							if (temp1 < 0) {

							} else {
								temp += Double.parseDouble(c.getString(0));
							}

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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);

		return s;
	}

	public String loadItemSale() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select sale_details.Amount from sale_details inner join sales on sales.SaleID=sale_details.SaleID where sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double temp1 = Double.parseDouble(c.getString(0));
							if (temp1 < 0) {

							} else {
								temp += Double.parseDouble(c.getString(0));
							}
							// s+=c.getString(0);
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

		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}
	public String loadChange() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Sales_date,sum(change) from sales inner join sale_payments on sales.SaleID=sale_payments.SaleID where sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							
								temp += Double.parseDouble(c.getString(1));
							
							// s+=c.getString(0);
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

		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}
	public String loadChanged(String fo,String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Sales_date,sum(change) from sales inner join sale_payments on sales.SaleID=sale_payments.SaleID where sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							
								temp += Double.parseDouble(c.getString(1));
							
							// s+=c.getString(0);
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

		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadItemSaled(String form, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select sale_details.Amount from sale_details inner join sales on sales.SaleID=sale_details.SaleID where sales.Sales_date between '"
						+ form + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double temp1 = Double.parseDouble(c.getString(0));
							if (temp1 < 0) {

							} else {
								temp += Double.parseDouble(c.getString(0));
							}
							// s+=c.getString(0);
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadUserName() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select users.Username from counters inner join users on counters.UserID=users.IDUser order by counters.Date DESC LIMIT 1";
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

	public String loadUserName2(String usserID) {
		String s = "";
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select UserName from users WHERE IDUser="
						+ usserID;
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

	public String loadAmount() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Cash_float_start from counters order by Date DESC LIMIT 1";
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

	public String loadBillDisccount() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Discount_amount from sales where Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
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

	public String loadStockind(String form, String to) {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select StockInHand from Inventory_report where Date between '"
						+ form + "' AND '" + to + "'";
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

	public String loadAu() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		Double i = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Remarks,Selling_price_1 from items";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {

							temp += Double.parseDouble(c.getString(0));
							Double temp1 = Double.parseDouble(c.getString(0));
							Double temp2 = Double.parseDouble(c.getString(1));

							i += temp1 * temp2;
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(i / temp);
		return s;
	}

	public String loadAud(String form, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		Double i = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Remarks,Cost_Price from items where Date between '"
						+ form + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {

							temp += Double.parseDouble(c.getString(0));
							Double temp1 = Double.parseDouble(c.getString(0));
							Double temp2 = Double.parseDouble(c.getString(1));

							i += temp1 * temp2;
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(i / temp);
		return s;
	}

	public String loadstockvalue(String form, String to) {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		Double i = 0.0;
		String query;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (form.equals("null null") || to.equals("null null")) {
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
						query = "Select CostPrice,StockInHand,TotalIn,TotalOut from Inventory_report";
					} else {
						query = "Select CostPrice,StockInHand,TotalIn,TotalOut from Inventory_report where Date between "
								+ "'" + form + "' AND '" + to + "'";
					}

				} else {
					query = "Select CostPrice,StockInHand,TotalIn,TotalOut from Inventory_report where Date between "
							+ "'" + form + "' AND '" + to + "'";
				}
				Log.v("HAI", query);

				// String query =
				// "Select CostPrice,StockInHand,TotalIn,TotalOut from Inventory_report";
				// String query =
				// "Select StockInHand,TotalIn,Selling_price_1 from Inventory_report INNER JOIN items WHERE items .Item_code=Inventory_report.ItemCode";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double costprice = Double.parseDouble(c
									.getString(0));
							Double stockinhand = Double.parseDouble(c
									.getString(1));
							Double totalin = Double.parseDouble(c.getString(2));
							Double totalout = Double
									.parseDouble(c.getString(3));
							Log.v("HAI", costprice + "" + stockinhand + totalin
									+ "" + totalout);
							// Double temp3=Double.parseDouble(c.getString(2));
							// temp=(temp1+temp2)*temp3;
							temp = ((stockinhand + totalin) - totalout)
									* costprice;
							;
							i += temp;
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
		DecimalFormat df = new DecimalFormat("####0.00");
		// s+=df.format(i);
		return df.format(i);
	}

	public String loadstockvalued(String form, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		Double i = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select StockInHand,TotalIn,Cost_price from Inventory_report INNER JOIN items WHERE items .Item_code=Inventory_report.ItemCode AND Inventory_report.Date between '"
						+ form + "' AND '" + to + "'";
				// String query =
				// "Select Remarks,Selling_price_1 from items where Date between '"+form+"' AND '"+to+"'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {

							Double temp1 = Double.parseDouble(c.getString(0));
							Double temp2 = Double.parseDouble(c.getString(1));
							Double temp3 = Double.parseDouble(c.getString(2));
							temp = (temp1 + temp2) * temp3;
							// temp=temp1*temp2;
							i += temp;
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
		DecimalFormat df = new DecimalFormat("####0.00");
		// s+=df.format(i);
		return df.format(i);
	}

	public String loadStockin2() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query;
				String sdate = loadShiftDate();
				if (!ss.getENDDATE().equals("")) {
					query = "Select TotalIn from Inventory_report WHERE Date>"
							+ "'" + ss.getENDDATE() + "'";
				} else {
					query = "Select TotalIn from Inventory_report";
				}

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
		temp = (temp * 10) / 10;
		s += temp;
		return s;
	}

	public String loadStockin2d(String form, String to) {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select TotalIn from Inventory_report where Date between '"
						+ form + "' AND '" + to + "'";
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
		temp = (temp * 10) / 10;
		s += temp;
		return s;
	}

	public String loadStockout() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String sdate = loadShiftDate();
				String query;
				// String query =
				// "Select TotalOut from Inventory_report WHERE Date>"+ "'" +
				// sdate + "'";
				if (!ss.getENDDATE().equals("")) {
//					query = "Select TotalOut from Inventory_report  WHERE Date>"
//							+ "'" + ss.getENDDATE() + "'";
					query = "Select TotalOut from Inventory_report  WHERE Date>"
							+ "'" + ss.getENDDATE() + "' AND GroupName!=''";
				} else {
//					query = "Select TotalOut from Inventory_report ";
					query = "Select TotalOut from Inventory_report WHERE GroupName!=''";
				}

				// String query = "Select TotalOut from Inventory_report ";
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
		temp = (temp * 10) / 10;
		s += temp;
		return s;
	}

	public String loadStockoutd(String form, String to) {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
//				String query = "Select TotalOut from Inventory_report where Date between '"
//						+ form + "' AND '" + to + "'";
				String query = "Select TotalOut from Inventory_report WHERE ItemCode not like 'Custom:%' AND Date between '"
						+ form + "' AND '" + to + "'";
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
		temp = (temp * 10) / 10;
		s += temp;
		return s;
	}

	public String loadreturn() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double a = Double.parseDouble(c.getString(0));
							if (a < 0) {
								temp += Double.parseDouble(c.getString(0));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadreturnd(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double a = Double.parseDouble(c.getString(0));
							if (a < 0) {
								temp += Double.parseDouble(c.getString(0));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadBillDisccountd(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Discount_amount from sales where Sales_date between '"
						+ fo + "' AND '" + to + "'";
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadItemDisccount() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Discount from sale_details inner join sales on sales.SaleID=sale_details.SaleID where sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);

		return s;
	}

	public String loadItemDisccountd(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		int i = 0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Discount from sale_details inner join sales on sales.SaleID=sale_details.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);

		return s;
	}

	public String loadNumberOfSale() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select * from sales where Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {

							i++;

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
		s += i;
		return s;
	}

	public String loadNumberOfSaled(String fo, String to) {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select * from sales where Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {

							i++;

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
		s += i;
		return s;
	}

	public String loadGST() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		String query = "";
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				query = "Select GST from sales where Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double tempGST = Double.parseDouble(c.getString(0)
									.toString());
							temp += tempGST;
							// s+=c.getString(0).toString();
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public static String mform;
	public static String mto;
	public String loadItemSaleShiftsPrint(String form, String to) {
		String s = "";
		String query;
		Cursor c = null;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (form.equals("null null") || to.equals("null null")) {
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
						mto= myear + "-" + realmonth + "-" + realDay + " " + hour
								+ ":" + minute + ":" + second;
//						query = "Select ItemName,sum(Quantity) from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID "
//
//								+ "group by ItemName ";
						query = "Select ItemName,sum(Quantity) from sale_details inner join sales on sales.SaleID=sale_details.SaleID "

								+ "group by ItemName ";
					} else {
						query = "Select ItemName,sum(Quantity) from sale_details inner join sales on sales.SaleID=sale_details.SaleID where Sales_date between '"
								+ form
								+ "' AND '"
								+ to
								+ "' group by ItemName ";
//						query = "Select ItemName,sum(Quantity) from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID where Sales_date between '"
//								+ form
//								+ "' AND '"
//								+ to
//								+ "' group by ItemName ";
						mform=form;
						mto=to;
					}
				} else {
					query = "Select ItemName,sum(Quantity) from sale_details inner join sales on sales.SaleID=sale_details.SaleID where Sales_date between '"
							+ form + "' AND '" + to + "' group by ItemName ";
//					query = "Select ItemName,sum(Quantity) from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID where Sales_date between '"
//							+ form + "' AND '" + to + "' group by ItemName ";
					mform=form;
					mto=to;
				}
				// String query =
				// "Select ItemName,sum(Quantity) from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID where Sales_date between '"
				// + sdate
				// + "' AND '"
				// + currentDateandTime
				// + "' group by ItemName ";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += "\t\t" + c.getString(0) + "\r\n";
							s += "\t\t" + c.getString(1);
							s += "\r\n\r\n";
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

	public String loadItemSaleShifts() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select ItemName,sum(Quantity) from sale_details inner join sales on sales.SaleID=sale_details.SaleID where Sales_date between '"
						+ sdate
						+ "' AND '"
						+ currentDateandTime
						+ "' group by ItemName ";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += "\t\t" + c.getString(0) + "\r\n";
							s += "\t\t" + c.getString(1);
							s += "\r\n\r\n";
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
	public String loadItemINameFOC(String form, String to) {
		String s = "";
		String query;
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (form.equals("null null") || to.equals("null null")) {
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
						query = "Select ItemName,Sum(Quantity) as qty from sale_details inner join sales on sales.SaleID=sale_details.SaleID WHERE ItemCode like 'Custom:%' group by ItemName order by qty DESC";
						// query =
						// "Select Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID group by ItemName order by qty DESC LIMIT 10";
					} else {
						query = "Select ItemName,Sum(Quantity) as qty from sale_details inner join sales on sales.SaleID=sale_details.SaleID WHERE ItemCode like 'Custom:%' AND  sales.Sales_date BETWEEN '"
								+ form
								+ "' AND '"
								+ to
								+ "' group by ItemName order by qty DESC";
					}

				} else {
					query = "Select ItemName,Sum(Quantity) as qty from sale_details inner join sales on sales.SaleID=sale_details.SaleID WHERE ItemCode like 'Custom:%' AND sales.Sales_date BETWEEN '"
							+ form
							+ "' AND '"
							+ to
							+ "' group by ItemName order by qty DESC";
				}

				// String query =
				// "Select ItemName,Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID group by ItemName order by qty DESC LIMIT 10";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(0) + "<br> <br>";

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

	public String loadItemIName(String form, String to) {
		String s = "";
		String query;
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (form.equals("null null") || to.equals("null null")) {
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
						query = "Select ItemName,Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID WHERE items.Status=1 group by ItemName order by qty DESC";
						// query =
						// "Select Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID group by ItemName order by qty DESC LIMIT 10";
					} else {
						query = "Select ItemName,Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID WHERE items.Status=1 AND sales.Sales_date BETWEEN '"
								+ form
								+ "' AND '"
								+ to
								+ "' group by ItemName order by qty DESC";
					}

				} else {
					query = "Select ItemName,Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID WHERE items.Status=1 AND sales.Sales_date BETWEEN '"
							+ form
							+ "' AND '"
							+ to
							+ "' group by ItemName order by qty DESC";
				}

				// String query =
				// "Select ItemName,Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID group by ItemName order by qty DESC LIMIT 10";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(0) + "<br> <br>";

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
	public String loadItemIqtyFOC(String form, String to) {
		String s = "";
		Cursor c = null;
		String query;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (form.equals("null null") || to.equals("null null")) {
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
						query = "Select Sum(Quantity) as qty from sale_details inner join sales on sales.SaleID=sale_details.SaleID WHERE  ItemCode like 'Custom:%' group by ItemName order by qty DESC";
					} else {
						query = "Select Sum(Quantity) as qty from sale_details inner join sales on sales.SaleID=sale_details.SaleID WHERE ItemCode like 'Custom:%' AND sales.Sales_date BETWEEN '"
								+ form
								+ "' AND '"
								+ to
								+ "' group by ItemName order by qty DESC";
					}

				} else {
					query = "Select Sum(Quantity) as qty from sale_details inner join sales on sales.SaleID=sale_details.SaleID WHERE ItemCode like 'Custom:%' AND  sales.Sales_date BETWEEN '"
							+ form
							+ "' AND '"
							+ to
							+ "' group by ItemName order by qty DESC";
				}

				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(0) + "<br> <br>";

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
	public String loadItemIqty(String form, String to) {
		String s = "";
		Cursor c = null;
		String query;
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (form.equals("null null") || to.equals("null null")) {
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
						query = "Select Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID WHERE items.Status=1 group by ItemName order by qty DESC";
					} else {
						query = "Select Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID WHERE items.Status=1 AND sales.Sales_date BETWEEN '"
								+ form
								+ "' AND '"
								+ to
								+ "' group by ItemName order by qty DESC";
					}

				} else {
					query = "Select Sum(Quantity) as qty from sale_details inner join items on sale_details.ItemCode=items.Item_code inner join sales on sales.SaleID=sale_details.SaleID WHERE items.Status=1 AND  sales.Sales_date BETWEEN '"
							+ form
							+ "' AND '"
							+ to
							+ "' group by ItemName order by qty DESC";
				}

				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							s += c.getString(0) + "<br> <br>";

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

	public String loadUnPaid() {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount,Status from HoldSales where Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(1).equals("1")) {
								Double tempGST = Double.parseDouble(c
										.getString(0).toString());
								temp += tempGST;
							}

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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadUnPaidd(String fo, String to) {
		String s = "";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		int i = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from HoldSales where Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double tempGST = Double.parseDouble(c.getString(0)
									.toString());
							temp += tempGST;

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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSale() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where payment_mode=2 AND sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (!c.getString(0).equals("CASH")
									&& !c.getString(0).equals("")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (!c.getString(0).equals("CASH")
									&& !c.getString(0).equals("")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaled(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (!c.getString(0).equals("CASH")
									&& !c.getString(0).equals("")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (!c.getString(0).equals("CASH")
									&& !c.getString(0).equals("")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledMaster1(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where CreditType='MASTER' AND payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("MASTER")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("MASTER")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledNETS(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where CreditType='NETS' AND payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("NETS")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("NETS")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledVISA(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where CreditType='VISA' AND payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("VISA")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("VISA")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledAMEX(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where CreditType='AMEX' AND payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("AMEX")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("AMEX")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledDINERS(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where CreditType='DINERS' AND payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("DINERS")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("DINERS")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledJCB(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where CreditType='JCB' AND payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("JCB")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount-change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("JCB")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadCardSaledMaster(String fo, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where payment_mode=2 AND sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (!c.getString(0).equals("Cash")) {
								temp += Double.parseDouble(c.getString(1));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type2,Type2Amount from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fo + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (!c.getString(0).equals("Cash")) {
								temp += Double.parseDouble(c.getString(1));
							}
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
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadTotalCash() {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where payment_mode=1 AND sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double a = Double.parseDouble(c.getString(0));
							if (a < 0) {

							} else {
								temp += Double.parseDouble(c.getString(0));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change,Type1Amount,Type1 from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where Type1='CASH' AND sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(2).equals("CASH")) {																	
									temp += Double.parseDouble(c.getString(1));								
							}
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
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change,Type2Amount,Type2 from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where Type2='CASH' AND sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(2).equals("CASH")) {																	
									temp += Double.parseDouble(c.getString(1));								
							}
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
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ sdate + "' AND '" + currentDateandTime + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
																								
									temp -= Double.parseDouble(c.getString(0));								
							
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

		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadTotalCashd(String fr, String to) {
		String s = "$";
		Cursor c = null;
		Double temp = 0.0;
		String sdate = loadShiftDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Total_amount from sales where payment_mode=1 AND sales.Sales_date between '"
						+ fr + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							Double a = Double.parseDouble(c.getString(0));
							if (a < 0) {

							} else {
								temp += Double.parseDouble(c.getString(0));
							}
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

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change,Type1Amount,Type1 from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where Type1='CASH' AND sales.Sales_date between '"
						+ fr + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(2).equals("CASH")) {																	
									temp += Double.parseDouble(c.getString(1));								
							}
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
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change,Type2Amount,Type2 from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where Type2='CASH' AND sales.Sales_date between '"
						+ fr + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
							if (c.getString(2).equals("CASH")) {																	
									temp += Double.parseDouble(c.getString(1));								
							}
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
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change from sale_payments inner join sales on sales.SaleID=sale_payments.SaleID where sales.Sales_date between '"
						+ fr + "' AND '" + to + "'";
				c = dbHelper.getDb().rawQuery(query, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						try {
																								
									temp -= Double.parseDouble(c.getString(0));								
							
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

		
		DecimalFormat df = new DecimalFormat("####0.00");
		s += df.format(temp);
		return s;
	}

	public String loadItemsSearch(String s1, String s2) {

		// String [] s2 = null;
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
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[0]);
					int colId_MaGroup = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[1]);
					int itemCode = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[2]);
					int itemImage = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[3]);
					int itemBarcode = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[4]);
					int itemUom = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[5]);
					int itemCostPrice = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[6]);
					int itemPrice1 = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[7]);
					int itemPrice2 = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[8]);
					int itemSpecialPrice = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[9]);
					int itemRemark = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[11]);
					int itemName = c
							.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[12]);

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
				|| s1.equals("price2") || s1.equals("specials")) {
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "SELECT items .*,item_translations.Name FROM items   INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
							+ Utilities.getGlobalVariable(context).language_code
							+ " AND items.Status=1";
					c = dbHelper.getDb().rawQuery(query, null);

					if (c != null && c.getCount() > 0) {
						int colId_MaItem = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[0]);
						int colId_MaGroup = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[1]);
						int itemCode = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[2]);
						int itemImage = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[3]);
						int itemBarcode = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[4]);
						int itemUom = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[5]);
						int itemCostPrice = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[6]);
						int itemPrice1 = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[7]);
						int itemPrice2 = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[8]);
						int itemSpecialPrice = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[9]);
						int itemRemark = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[11]);
						int itemName = c
								.getColumnIndex(StockInModel.VATTU_FULL_PROJECTION[12]);

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

}