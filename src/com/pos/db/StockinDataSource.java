package com.pos.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pos.common.Utilities;
import com.pos.model.StockInModel;

import android.R.array;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StockinDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = StockinDataSource.class.getSimpleName();

	public StockinDataSource(Context c, Activity ac) {
		dbHelper = new MySQLiteHelper(c);
		context = ac;
	}
	
	private boolean oneTime = false;

	public String [] loadItemsAdapter(String [] Array) {
		

		Cursor c = null;
        int i=0;
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
							//String maVattu = c.getString(colId_MaItem);
							//String maGroup = c.getString(colId_MaGroup);
							//String itemcode = c.getString(itemCode);
							//String image = c.getString(itemImage);
							//String barcode = c.getString(itemBarcode);
							//String uom = c.getString(itemUom);
							//String cost = c.getString(itemCostPrice);
							//String one = c.getString(itemPrice1);
							//String two = c.getString(itemPrice2);
							//String special = c.getString(itemSpecialPrice);
							//String remarks = c.getString(itemRemark);
							//String name = c.getString(itemName);
							//ItemsModel vt = new ItemsModel();

							Array[i]=c.getString(itemCode);
							i++;
							Array[i]=c.getString(itemName);
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
	public String UpdateStockIn2(String uprice,String qty,String id) {
		String error="";
		String query="";
		try
		{
				if (dbHelper != null && dbHelper.getDb() != null) {
					//query = "Update items set Cost_price="+uprice+",Remarks='"+qty+"' where ItemID="+id;						
					//dbHelper.getDb().execSQL(query);
					//dbHelper.getDb().
					//dbHelper.getWritableDatabase();
					ContentValues newValues = new ContentValues();
					newValues.put("Cost_price", uprice);

					int i=dbHelper.getWritableDatabase().update("items", newValues, "Item_code="+id, null);
					error+=i;
					dbHelper.close();
					//dbHelper.getWritableDatabase().execSQL("UPDATE items SET Cost_price = ?, Remarks = ? WHERE ItemID = " + id,
						    //new String[] { uprice,qty});
				}
				
		}
	 catch (Exception e) {
		//e.toString();
		error+=e.toString();
	} 
		return error; 

		}	
public void UpdateStockIn(String uprice,String qty,String id,String Price,String Price2,String SpecialPrice) {
	try
	{
			if (dbHelper != null && dbHelper.getDb() != null) {
				if (!uprice.equals("null"))
				{
					String query = "Update items set Cost_price="+uprice+" where Item_code='"+id+"'";						
					dbHelper.getDb().execSQL(query);
				}
				if (!qty.equals("null"))
						{
					String query = "Update items set Remarks='"+qty+"' where Item_code='"+id+"'";						
					dbHelper.getDb().execSQL(query);
					
						}
				if (!Price.equals("null"))
				{
					String query = "Update items set Selling_price_1="+Price+" where Item_code='"+id+"'";						
					dbHelper.getDb().execSQL(query);
				}
				if (!Price2.equals("null"))
				{
					String query = "Update items set Selling_price_2="+Price2+" where Item_code='"+id+"'";						
					dbHelper.getDb().execSQL(query);
				}
				if (!SpecialPrice.equals("null"))
				{
					String query = "Update items set Special_price="+SpecialPrice+" where Item_code='"+id+"'";						
					dbHelper.getDb().execSQL(query);
				}
				//String query = "Update items set Cost_price="+uprice+",Remarks='"+qty+"',Selling_price_1="+Price+",Selling_price_2="+Price2+",Special_price="+SpecialPrice+" where Item_code='"+id+"'";						
				//dbHelper.getDb().execSQL(query);
				
				String query="insert into stock_ins values("+id+","+qty+","+uprice+")";
				dbHelper.getDb().execSQL(query);
				
			}
			
	}
 catch (Exception e) {
	e.toString();
	
} 

	}
public String UpdateStockIn3(String uprice,String qty,String id,String Price,String Price2,String SpecialPrice) {
	String s="";
	String idr="";
	Cursor c = null;
    int i=0;
	try {
		if (dbHelper != null && dbHelper.getDb() != null) {
			String query = "SELECT ItemID from items where Item_code='"+id+"'";									
			c = dbHelper.getDb().rawQuery(query, null);

			if (c != null && c.getCount() > 0) {
				
				c.moveToFirst();
				do {
					try {
						idr+=c.getString(0);
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

	try
	{			
		        double i1=Math.random();
		        String temp2=""+i1;
		        temp2=temp2.replace(".", "");
				String query="insert into stock_ins values("+temp2+","+idr+","+qty.replace(".0", "")+","+uprice+")";
				dbHelper.getDb().execSQL(query);			
	}
 catch (Exception e) {
	s+=e.toString();
	
} 
    return s;
	}
	
    public String loadItemsTest(String s) {
		

		Cursor c = null;
        int i=0;
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
							//String maVattu = c.getString(colId_MaItem);
							//String maGroup = c.getString(colId_MaGroup);
							//String itemcode = c.getString(itemCode);
							//String image = c.getString(itemImage);
							//String barcode = c.getString(itemBarcode);
							//String uom = c.getString(itemUom);
							//String cost = c.getString(itemCostPrice);
							//String one = c.getString(itemPrice1);
							//String two = c.getString(itemPrice2);
							//String special = c.getString(itemSpecialPrice);
							//String remarks = c.getString(itemRemark);
							//String name = c.getString(itemName);
							//ItemsModel vt = new ItemsModel();

							//Array[i]=c.getString(itemCode).toString();
							i++;
							s+=c.getString(itemCode).toString();
							s+="|";
							//Array[i]=c.getString(itemName).toString();
							//i++;
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
public String loadItemsSearch(String s1,String s2) {
		
        //String [] s2 = null;
		Cursor c = null;
        int i=0;
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
							//String maVattu = c.getString(colId_MaItem);
							//String maGroup = c.getString(colId_MaGroup);
							String itemcode = c.getString(itemCode);
							//String image = c.getString(itemImage);
							//String barcode = c.getString(itemBarcode);
							//String uom = c.getString(itemUom);
							//String cost = c.getString(itemCostPrice);
							//String one = c.getString(itemPrice1);
							//String two = c.getString(itemPrice2);
							//String special = c.getString(itemSpecialPrice);
							//String remarks = c.getString(itemRemark);
							//String name = c.getString(itemName);
							//ItemsModel vt = new ItemsModel();

							//Array[i]=c.getString(itemCode).toString();
							//s1+=s;
							if (s2.equals(itemcode.toString()))
									{
								         if (s1.equals("itemcode"))
								         {								        	
								        		s1=c.getString(itemCode).toString();								        	
								         }
								         if (s1.equals("qty"))
								         {								        	
								        		s1=c.getString(itemRemark).toString();								        	
								         }
								         if (s1.equals("itemname"))
								         {								        	
								        		s1=c.getString(itemName).toString();								        	
								         }
								         if (s1.equals("unitprice"))
								         {								        	
								        		s1=c.getString(itemCostPrice).toString();								        	
								         }
								         if (s1.equals("price"))
								         {								        	
								        		s1=c.getString(itemPrice1).toString();								        	
								         }
								         if (s1.equals("price2"))
								         {								        	
								        		s1=c.getString(itemPrice2).toString();								        	
								         }
								         if (s1.equals("specials"))
								         {								        	
								        		s1=c.getString(itemSpecialPrice).toString();								        	
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
		
		if (s1.equals("itemcode")||s1.equals("qty")||s1.equals("itemname")||s1.equals("unitprice")||s1.equals("price")||s1.equals("price2")||s1.equals("specials"))
        {								        	
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
								//String maVattu = c.getString(colId_MaItem);
								//String maGroup = c.getString(colId_MaGroup);
								String itemcode = c.getString(itemCode);
								//String image = c.getString(itemImage);
								//String barcode = c.getString(itemBarcode);
								//String uom = c.getString(itemUom);
								//String cost = c.getString(itemCostPrice);
								//String one = c.getString(itemPrice1);
								//String two = c.getString(itemPrice2);
								//String special = c.getString(itemSpecialPrice);
								//String remarks = c.getString(itemRemark);
								String name = c.getString(itemName);
								//ItemsModel vt = new ItemsModel();

								//Array[i]=c.getString(itemCode).toString();
								//s1+=s;
								if (s2.equals(name.toString()))
										{
									         if (s1.equals("itemcode"))
									         {								        	
									        		s1=c.getString(itemCode).toString();								        	
									         }
									         if (s1.equals("qty"))
									         {								        	
									        		s1=c.getString(itemRemark).toString();								        	
									         }
									         if (s1.equals("itemname"))
									         {								        	
									        		s1=c.getString(itemName).toString();								        	
									         }
									         if (s1.equals("unitprice"))
									         {								        	
									        		s1=c.getString(itemCostPrice).toString();								        	
									         }
									         if (s1.equals("price"))
									         {								        	
									        		s1=c.getString(itemPrice1).toString();								        	
									         }
									         if (s1.equals("price2"))
									         {								        	
									        		s1=c.getString(itemPrice2).toString();								        	
									         }
									         if (s1.equals("specials"))
									         {								        	
									        		s1=c.getString(itemSpecialPrice).toString();								        	
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
		
		
		
		
		
		
		if (s1.equals("itemcode"))
        {								        	
       		s1="";								        	
        }
        if (s1.equals("qty"))
        {								        	
        	s1="";								        	
        }
        if (s1.equals("itemname"))
        {								        	
        	s1="";								        	
        }
        if (s1.equals("unitprice"))
        {								        	
        	s1="";							        	
        }
        if (s1.equals("price"))
        {								        	
        	s1="";							        	
        }
        if (s1.equals("price2"))
        {								        	
        	s1="";								        	
        }
        if (s1.equals("specials"))
        {								        	
        	s1="";								        	
        }
     return s1;
		
	}
	
	
	
	
	
	
	
	
	

}