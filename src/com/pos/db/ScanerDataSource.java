package com.pos.db;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.pos.MainActivity;
import com.pos.common.Utilities;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.StockInModel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class ScanerDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = ScanerDataSource.class.getSimpleName();

	public ScanerDataSource(Context c, Activity ac) {
		dbHelper = new MySQLiteHelper(c);
		context = ac;
	}

	public void UpdateItem(String name, String nameCN, String itemid,
			String itemcode, String image, String barcode, String uom,
			String cprice, String price1, String price2, String Sprice,
			String BaseStock, String idGr) {

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Item_code", itemcode);
				cv.put("Item_image", image);
				cv.put("Barcode", barcode);
				cv.put("uom", uom);
				cv.put("Cost_price", cprice);
				cv.put("Selling_price_1", price1);
				cv.put("Selling_price_2", price2);
				cv.put("Special_price", Sprice);
				cv.put("Remarks", BaseStock);
				cv.put("Item_group_ID", idGr);
				dbHelper.getDb().update("items", cv, "ItemID=" + itemid, null);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Name", name);
				dbHelper.getDb().update("item_translations", cv,
						"ItemID=" + itemid + " AND LanguageID=1", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

	}

	public void UpdateItem2(String name, String nameCN, String itemid,
			String itemcode, String image, String barcode, String uom,
			String cprice, String price1, String price2, String Sprice,
			String BaseStock) {

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				ContentValues cv = new ContentValues();
				cv.put("Name", nameCN);
				dbHelper.getDb().update("item_translations", cv,
						"ItemID=" + itemid + " AND LanguageID=2", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}

	}

	public String checkStock(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Remarks from items where ItemID = '"
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

	public String checkSPrice(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Special_price from items where ItemID = '"
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

	public String checkPrice2(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Selling_price_2 from items where ItemID = '"
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

	public String checkPrice1(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Selling_price_1 from items where ItemID = '"
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

	public String checkitemCPrice(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Cost_price from items where ItemID = '"
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

	public String checkitemBarCode(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Barcode from items where ItemID = '"
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

	public String checkitemUOM(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select uom from items where ItemID = '"
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

	public String checkitemcode(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Item_code from items where ItemID = '"
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

	public String checkname(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Name from item_translations where ItemID = '"
						+ maVatTu + "' AND LanguageID=1 LIMIT 1";
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

	public String checknameCN(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Name from item_translations where ItemID = '"
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

	public String checkSubtotal(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Sub_total from sales where SaleID = '"
						+ maVatTu + "'";
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
		Double dtemp = Double.parseDouble(s);
		s = String.format("%.2f", dtemp);
		return s;
	}

	public String checkdiscount(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Discount_amount from sales where SaleID = '"
						+ maVatTu + "'";
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

		Double dtemp = Double.parseDouble(s);
		s = String.format("%.2f", dtemp);
		return s;
	}

	public String checkGST(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Discount_amount from sales where SaleID = '"
						+ maVatTu + "'";
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

		Double dtemp = Double.parseDouble(s);
		s = String.format("%.2f", dtemp);
		return s;
	}

	// public String getitemBarcode(String barcode) {
	//
	// String s = "";
	// Cursor c = null;
	//
	// try {
	// if (dbHelper != null && dbHelper.getDb() != null) {
	// String query =
	// "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE items.Barcode="
	// + "'" + barcode + "'";
	// c = dbHelper.getDb().rawQuery(query, null);
	//
	// if (c != null && c.getCount() > 0) {
	// int colId_MaItem = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[0]);
	// int colId_MaGroup = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[1]);
	// int itemCode = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[2]);
	// int itemImage = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[3]);
	// int itemBarcode = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[4]);
	// int itemUom = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[5]);
	// int itemCostPrice = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[6]);
	// int itemPrice1 = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[7]);
	// int itemPrice2 = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[8]);
	// int itemSpecialPrice = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[9]);
	// int itemRemark = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[11]);
	// int itemName = c
	// .getColumnIndex(ItemsModel.VATTU_FULL_PROJECTION[12]);
	//
	// c.moveToFirst();
	// do {
	// try {
	// String maVattu = c.getString(colId_MaItem);
	// String maGroup = c.getString(colId_MaGroup);
	// String itemcode = c.getString(itemCode);
	// String image = c.getString(itemImage);
	// String barcode1 = c.getString(itemBarcode);
	// String uom = c.getString(itemUom);
	// String cost = c.getString(itemCostPrice);
	// String one = c.getString(itemPrice1);
	// String two = c.getString(itemPrice2);
	// String special = c.getString(itemSpecialPrice);
	// String remarks = c.getString(itemRemark);
	// String name = c.getString(itemName);
	// ItemsModel vt = new ItemsModel();
	//
	// vt.setBarcode(barcode1);
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
	// result.add(vt);
	//
	// } catch (Exception e) {
	// }
	//
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
	// Double dtemp = Double.parseDouble(s);
	// s = String.format("%.2f", dtemp);
	// return s;
	// }

	public ArrayList<ItemsModel> getitemBarcode(String barcode) {
		ArrayList<ItemsModel> result = new ArrayList<ItemsModel>();
		// ItemsModel vta = new ItemsModel();
		// result.add(0, vta);

		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE items.Barcode="
						+ "'"
						+ barcode
						+ "' AND LanguageID="
						+ Utilities.getGlobalVariable(context).language_code
						+ "";
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
							String barcode1 = c.getString(itemBarcode);
							String uom = c.getString(itemUom);
							String cost = c.getString(itemCostPrice);
							String one = c.getString(itemPrice1);
							String two = c.getString(itemPrice2);
							String special = c.getString(itemSpecialPrice);
							String remarks = c.getString(itemRemark);
							String name = c.getString(itemName);
							ItemsModel vt = new ItemsModel();

							vt.setBarcode(barcode1);
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
			Toast.makeText(context, "SQL " + e.toString(), Toast.LENGTH_SHORT)
					.show();
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		// MainActivity.searchstring = "";
		return result;
	}

	public String loadPaymentNett() {
		String s = "";
		Cursor c = null;
		int cash = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Payment_mode,Total_amount from sales order by SaleID DESC LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("1")) {
								s += "CASH      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
								cash = 1;
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

		if (cash == 0) {
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "Select CreditType,Total_amount from sales order by SaleID DESC LIMIT 1";

					c = dbHelper.getDb().rawQuery(query, null);

					if (c != null && c.getCount() > 0) {

						c.moveToFirst();
						do {
							try {
								if (!c.getString(0).equals("1")) {
									if (c.getString(0).equals("VISA")) {
										s += c.getString(0)
												+ "        : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("JCB")) {
										s += c.getString(0)
												+ "          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("MASTER")) {
										s += c.getString(0)
												+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("NETS")) {
										s += c.getString(0)
												+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("AMEX")) {
										s += c.getString(0)
												+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else {
										s += c.getString(0)
												+ "      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									}
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
		}

		return s;
	}

	public String loadPaymentNettRe(String SaleID) {
		String s = "";
		Cursor c = null;
		int cash = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Payment_mode,Total_amount from sales where SaleID='"
						+ SaleID + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("1")) {
								s += "CASH      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
								cash = 1;
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

		if (cash == 0) {
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "Select CreditType,Total_amount from sales where SaleID='"
							+ SaleID + "'";

					c = dbHelper.getDb().rawQuery(query, null);

					if (c != null && c.getCount() > 0) {

						c.moveToFirst();
						do {
							try {
								if (!c.getString(0).equals("1")) {
									if (c.getString(0).equals("VISA")) {
										s += c.getString(0)
												+ "        : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("JCB")) {
										s += c.getString(0)
												+ "          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("MASTER")) {
										s += c.getString(0)
												+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("NETS")) {
										s += c.getString(0)
												+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else if (c.getString(0).equals("AMEX")) {
										s += c.getString(0)
												+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									} else {
										s += c.getString(0)
												+ "      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
										DecimalFormat df = new DecimalFormat(
												"####0.00");
										Double temp = Double.parseDouble(c
												.getString(1));

										s += df.format(temp);
									}
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
		}

		return s;
	}

	public String loadNoteRe(String SaleID) {
		String s = "";
		Cursor c = null;
		int cash = 0;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Remarks from sales where SaleID='"
						+ SaleID + "'";

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

	public String loadShift() {
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
							s += "|";
							s += c.getString(1);
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

	public String loadPayment() {
		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount,Type2,Type2Amount,Change from sale_payments order by SaleID DESC LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("VISA")) {
								s += c.getString(0)
										+ "         : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("JCB")) {
								s += c.getString(0)
										+ "          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("MASTER")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("DINERS")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("AMEX")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("NETS")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("CASH")) {
								s += "CASH      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else {
								s += c.getString(0)
										+ "      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							}
							s += "\r\n";
							if (c.getString(2).equals("VISA")) {
								s += c.getString(2)
										+ "         : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("JCB")) {
								s += c.getString(2)
										+ "          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("MASTER")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("DINERS")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("AMEX")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("DINERS")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("CASH")) {
								s += "CASH      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("")) {

							} else {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));
								s += df.format(temp);
							}
							s += "\r\n";
							s += "Change   : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
							DecimalFormat df = new DecimalFormat("####0.00");
							Double temp = Double.parseDouble(c.getString(4));

							s += df.format(temp);

							s += "\r\n";

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

	public String loadPaymentRe(String SaleID) {
		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Type1,Type1Amount,Type2,Type2Amount,Change from sale_payments where SaleID='"
						+ SaleID + "' order by SaleID DESC LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							if (c.getString(0).equals("VISA")) {
								s += c.getString(0)
										+ "         : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("JCB")) {
								s += c.getString(0)
										+ "          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("MASTER")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("DINERS")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("AMEX")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("NETS")) {
								s += c.getString(0)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else if (c.getString(0).equals("CASH")) {
								s += "CASH      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							} else {
								s += c.getString(0)
										+ "      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(1));

								s += df.format(temp);
							}
							s += "\r\n";
							if (c.getString(2).equals("VISA")) {
								s += c.getString(2)
										+ "         : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("JCB")) {
								s += c.getString(2)
										+ "          : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("MASTER")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("DINERS")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("AMEX")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("DINERS")) {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("CASH")) {
								s += "CASH      : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));

								s += df.format(temp);
							} else if (c.getString(2).equals("")) {

							} else {
								s += c.getString(2)
										+ "    :  \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
								DecimalFormat df = new DecimalFormat("####0.00");
								Double temp = Double
										.parseDouble(c.getString(3));
								s += df.format(temp);
							}
							s += "\r\n";
							s += "Change   : \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t $";
							DecimalFormat df = new DecimalFormat("####0.00");
							Double temp = Double.parseDouble(c.getString(4));

							s += df.format(temp);

							s += "\r\n";

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

	public String Checkquantity(String quant, String id) {
		String s = "";
		Cursor c = null;
		String s1 = "";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Remarks from items where Item_code='"
						+ id + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							s1 += c.getString(0);

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
		Double old = Double.parseDouble(s1);
		Double inew = Double.parseDouble(quant);

		if (old >= inew) {
			return "ok";
		} else {
			return s1;
		}

	}

	public String loadChange() {
		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Change from sale_payments order by SaleID DESC LIMIT 1";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							s += c.getString(0);
							s += "  ";
							s += c.getString(1) + "\r\n";
							s += c.getString(2);
							s += "  ";
							s += c.getString(3) + "\r\n";

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

	private boolean oneTime = false;

	public String DelelteItem(String id) {
		String error = "";
		String query = "";
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {

				ContentValues newValues = new ContentValues();
				newValues.put("Status", "0");

				int i = dbHelper.getWritableDatabase().update("items",
						newValues, "ItemID=" + id, null);
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

	public String Updatequantity(String id, String qty) {
		String error = "";
		Double dold = 0.0;
		Double dnew = 0.0;
		Double dtemp = Double.parseDouble(qty);
		Cursor c = null;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT Remarks FROM items where Item_code='"
						+ id + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							dold += Double.parseDouble(c.getString(0));
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
		dnew = dold - dtemp;
		String strnew = "" + dnew;
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {

				ContentValues newValues = new ContentValues();
				newValues.put("Remarks", strnew);

				int i = dbHelper.getWritableDatabase().update("items",
						newValues, "Item_code='" + id + "'", null);
				error += i;
				dbHelper.close();

			}

		} catch (Exception e) {
			// e.toString();
			error += e.toString();
		}

		return error;

	}

	public void loadItemsRe(String SaleID, ArrayList<ListOrderModel> result) {

		int i = 0;
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sale_details";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							result.get(i).setItemName(c.getString(2));
							result.get(i).setQualyti(c.getString(3));
							result.get(i).setUnitPrice(c.getString(4));
							result.get(i).setDiscount(c.getString(5));
							result.get(i).setAmount(c.getString(6));
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

	}

	public String loadItemsRetest(String SaleID) {
		ArrayList<ListOrderModel> result = new ArrayList<ListOrderModel>();

		String s = "";
		int i = 0;
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "SELECT * FROM sale_details where SaleID='"
						+ SaleID + "'";

				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {
							result.get(i).setItemName(c.getString(2));
							result.get(i).setQualyti(c.getString(3));
							result.get(i).setUnitPrice(c.getString(4));
							result.get(i).setDiscount(c.getString(5));
							result.get(i).setAmount(c.getString(6));
							i++;
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

	public ArrayList<ItemsModel> loadItems() {
		ArrayList<ItemsModel> result = new ArrayList<ItemsModel>();
		ItemsModel vta = new ItemsModel();
		result.add(0, vta);

		Cursor c = null;
		if (MainActivity.searchstring.equals("")) {
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
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

		} else {
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
							+ Utilities.getGlobalVariable(context).language_code
							+ " AND items.Status=1 AND Item_code='"
							+ MainActivity.searchstring + "'";
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
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
							+ Utilities.getGlobalVariable(context).language_code
							+ " AND items.Status=1 AND Name='"
							+ MainActivity.searchstring + "'";
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
			try {
				if (dbHelper != null && dbHelper.getDb() != null) {
					String query = "SELECT items .*,item_translations.Name FROM items INNER JOIN item_translations ON items .ItemID=item_translations.ItemID WHERE item_translations.LanguageID="
							+ Utilities.getGlobalVariable(context).language_code
							+ " AND items.Status=1 AND Barcode='"
							+ MainActivity.searchstring + "'";
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

			MainActivity.searchstring = "";
		}
		return result;
	}

	public String getImage(String maVatTu) {

		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Item_image from items where ItemID = '"
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

	public String getGroup(String maVatTu) {

		String s = "";
		String name = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Item_group_ID from items where ItemID = '"
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
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "select Name from group_translations where GroupID = '"
						+ s + "' AND LanguageID=1 LIMIT 1";
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						try {

							name = c.getString(0);

						} catch (Exception e) {
							name = e.toString();
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			name = e.toString();

			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}

		return name;
	}

}