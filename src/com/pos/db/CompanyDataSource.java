package com.pos.db;

import java.util.ArrayList;

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

public class CompanyDataSource {

	private MySQLiteHelper dbHelper;
	private Activity context;

	private final static String TAG = CompanyDataSource.class.getSimpleName();

	public CompanyDataSource(Context c, Activity ac) {
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
	public String UpdateComany(String cname,String caddress,String ctelephone,String cfacsimile,String cemail,String cwebsite, String companino,String GST,String footer, String Password) {
		String error="";
		
		try
		{
				if (dbHelper != null && dbHelper.getDb() != null) {
					
					ContentValues newValues = new ContentValues();
					newValues.put("Name", cname);
					newValues.put("Address", caddress);
					newValues.put("Telephone", ctelephone);
					newValues.put("Facsimile", cfacsimile);
					newValues.put("Email", cemail);
					newValues.put("Website", cwebsite);
					newValues.put("CompanyNo", companino);
					newValues.put("GST", GST);
					newValues.put("Receiptfooter", footer);
					newValues.put("Password",Password);

					int i=dbHelper.getWritableDatabase().update("company", newValues, "CompanyID=1", null);
					error+=i;
					dbHelper.close();
					
				}
				
		}
	 catch (Exception e) {
		
		error+=e.toString();
	} 
		return error; 

		}	
public void UpdateStockIn(String uprice,String qty,String id,String Price) {
	try
	{
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Update items set Cost_price="+uprice+",Remarks='"+qty+"',Selling_price_1="+Price+" where Item_code='"+id+"'";						
				dbHelper.getDb().execSQL(query);
				
			}
			
	}
 catch (Exception e) {
	e.toString();
	
} 

	}
	
    
    public String loadCName() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Name from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
    
    public String loadCAddress() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Address from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
    
    public String loadCPhone() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Telephone from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
    
    public String loadCFacsimile() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Facsimile from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
    
    public String loadCEmail() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Email from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
	
    public String loadCWebsite() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Website from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
    public String loadCompanyNo() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select CompanyNo from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
	public String loadFAX() {
		String s = "";
		Cursor c = null;

		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Facsimile from Company";

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
    public String loadGST() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select GST from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
	
    public String loadCReceipt() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Receiptfooter from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
			// TODO: handle exception
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		
        
		return s;
	}
    public String loadCPassword() {
		String s="";
		Cursor c = null;
        
		try {
			if (dbHelper != null && dbHelper.getDb() != null) {
				String query = "Select Password from Company";
										
				c = dbHelper.getDb().rawQuery(query, null);

				if (c != null && c.getCount() > 0) {
					
					c.moveToFirst();
					do {
						try {
							s+=c.getString(0);
							
							
						} catch (Exception e) {
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			s+=e.toString();
			
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