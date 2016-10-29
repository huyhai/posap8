/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.pos.controllibs;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.Table;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions.PaxNumberModel;
import com.pos.libs.ComDDUtilities;
import com.pos.model.GetPrintModel;
import com.pos.model.IPMayinModel;
import com.pos.model.ItemsModel;
import com.pos.model.ItemsModel2;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.MayinModel;
import com.pos.model.SalesModel;
import com.pos.model.SetMenuItemModel;
import com.pos.model.SplitBillModel;
import com.pos.model.SplitOrderModel;
import com.pos.print.PrinterFunctions;
import com.pos.service.JSONCallBack;
import com.pos.service.JSONMethod;
import com.pos.ui.Items;
import com.pos.ui.SplitBillActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class UserFunctions {
	private static UserFunctions userFunctions;
	// private JSONParser jsonParser;
	private static String message;

	public String messagePush;
	private Boolean isShowMessage = false;

	public static UserFunctions getInstance() {
		if (userFunctions == null)
			userFunctions = new UserFunctions();
		return userFunctions;
	}

	public void sendMessage(Context cont, String Action, Boolean isSuccess) {
		Intent intent = new Intent(Action);
		intent.putExtra(ConstantValue.IS_SUCCESS, isSuccess);
		cont.sendBroadcast(intent);
	}

	public void callIP(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {

		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				downsoftpin123, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack downsoftpin123 = new JSONCallBack() {

		@Override
		public void callResult(final Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						getPrintModel = new GetPrintModel();
						getPrintModel.setData(resultJson
								.getJSONObject("list_data"));
						getPrintList.add(getPrintModel);
						MainActivity.count++;

						if (MainActivity.count == PosApp.listOrderData.size()) {
							for (int i = 0; i < getPrintList.size(); i++) {
								if (getPrintList.get(i).getPrint2()
										.equals("null")) {
									PrinterFunctions.PrintPosOne(
											MainActivity.activity, activity,
											"TCP:"
													+ getPrintList.get(i)
															.getPrint1(), "",
											activity.getResources(), "", i);

								} else {
									if (i % 2 == 0) {
										PrinterFunctions.PrintPosOne(
												MainActivity.activity,
												activity, "TCP:"
														+ getPrintList.get(i)
																.getPrint1(),
												"", activity.getResources(),
												"", i);
									} else {
										PrinterFunctions.PrintPosOne(
												MainActivity.activity,
												activity, "TCP:"
														+ getPrintList.get(i)
																.getPrint2(),
												"", activity.getResources(),
												"", i);
									}
								}

							}
							// Utilities.getGlobalVariable(act).isMenu = false;
							MainActivity.count = 0;
							getPrintList.clear();
							PosApp.listOrderData.clear();
							act.finish();
						}
					} else {
						MainActivity.count = 0;
						getPrintList.clear();
					}

				} else {
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	public static String dataPath = "";
	public static String DB_PATH = "";

	public static void initDataPath(String path) {
		// init value for data app path
		dataPath = path;
		DB_PATH = path + "/databases/";
	}

	public String getMessage() {
		if (message == null) {
			message = "Not Message!";
		}
		return message;
	}

	public static String LOGIN = "Login";
	public static ArrayList<GetPrintModel> getPrintList = new ArrayList<GetPrintModel>();
	private GetPrintModel getPrintModel;

	public ArrayList<GetPrintModel> getGetPrintList() {
		if (null == getPrintList) {
			getPrintList = new ArrayList<GetPrintModel>();
		}
		return getPrintList;
	}

	public void callLogin(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, userLogin,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack userLogin = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						JSONArray listPIN;
						listPIN = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listPIN.length(); i++) {
							// String jsonobjectPin = new JSONObject();
						}
						sendMessage(activity, LOGIN, true);
					} else {
						sendMessage(activity, LOGIN, false);
					}

				} else {
					sendMessage(activity, LOGIN, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, LOGIN, false);
			}

		}
	};
	public static String LISTITEMMENU = "downsoftpinss";
	private SetMenuItemModel setMenuItemModel;
	public ArrayList<SetMenuItemModel> listItemSetMenu = new ArrayList<SetMenuItemModel>();

	public ArrayList<SetMenuItemModel> getlistItemMenu() {
		if (null == listItemSetMenu) {
			listItemSetMenu = new ArrayList<SetMenuItemModel>();
		}
		return listItemSetMenu;
	}

	public static int countMenu = 0;

	public void callListSetmenuItem(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				downsoftpin1, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack downsoftpin1 = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						// listService = new ArrayList<ServiceModel>();
						JSONArray listJson;

						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							setMenuItemModel = new SetMenuItemModel();
							// setMenuItemModel.setData(jSonObFriend);
							// listItemSetMenu.add(setMenuItemModel);
							ListOrderModel md = new ListOrderModel();
							if (Utilities.getDataString(jSonObFriend,
									"language_id").equals("1")) {
								setMenuItemModel.setData(jSonObFriend);
								listItemSetMenu.add(setMenuItemModel);

								md.setQualyti("1");
								md.setItemCode(setMenuItemModel.getItem_code());
								md.setItemName(setMenuItemModel.getName());
								md.setGroupCode(setMenuItemModel
										.getItem_group_id());
								String amount;
								if (Items.num != "") {
									amount = String.valueOf(Double
											.parseDouble(setMenuItemModel
													.getSelling_price_1())
											* Double.parseDouble(Items.num));
								} else {
									amount = String.valueOf(Double
											.parseDouble(setMenuItemModel
													.getSelling_price_1()) * 1);
								}
								DecimalFormat df = new DecimalFormat("0.00");
								double amount1 = Double.valueOf(amount);
								md.setAmount(df.format(amount1) + "");
								md.setDiscount("0.00");
								md.setPrice2(setMenuItemModel
										.getSelling_price_2());
								md.setSpecialPrice(setMenuItemModel
										.getSpecial_price());
								md.setUnitPrice(setMenuItemModel
										.getSelling_price_1());
								md.setIsFOC("0");
								md.setPrice3(setMenuItemModel
										.getSelling_price_3());
								md.setPrice4(setMenuItemModel
										.getSelling_price_4());
								md.setPrice5(setMenuItemModel
										.getSelling_price_5());
								md.setIsGST(setMenuItemModel.getTax());
								md.setIsSVC(setMenuItemModel
										.getService_charge());
								// md.setGroupName(groupCode);
								md.setStandBy("0");
								md.setRemarks(setMenuItemModel.getRemarks());
								PosApp.listOrderData2.add(md);
							} else {
								setMenuItemModel.setNameTau(Utilities
										.getDataString(jSonObFriend, "name"));
								setMenuItemModel.setName(listItemSetMenu.get(
										listItemSetMenu.size() - 1).getName());
								setMenuItemModel.setDataTau(jSonObFriend);
								listItemSetMenu.set(listItemSetMenu.size() - 1,
										setMenuItemModel);

								md.setQualyti("1");
								md.setItemCode(setMenuItemModel.getItem_code());
								md.setGroupCode(setMenuItemModel
										.getItem_group_id());
								String amount;
								if (Items.num != "") {
									amount = String.valueOf(Double
											.parseDouble(setMenuItemModel
													.getSelling_price_1())
											* Double.parseDouble(Items.num));
								} else {
									amount = String.valueOf(Double
											.parseDouble(setMenuItemModel
													.getSelling_price_1()) * 1);
								}
								DecimalFormat df = new DecimalFormat("0.00");
								double amount1 = Double.valueOf(amount);
								md.setAmount(df.format(amount1) + "");
								md.setDiscount("0.00");
								md.setPrice2(setMenuItemModel
										.getSelling_price_2());
								md.setSpecialPrice(setMenuItemModel
										.getSpecial_price());
								md.setUnitPrice(setMenuItemModel
										.getSelling_price_1());
								md.setIsFOC("0");
								md.setPrice3(setMenuItemModel
										.getSelling_price_3());
								md.setPrice4(setMenuItemModel
										.getSelling_price_4());
								md.setPrice5(setMenuItemModel
										.getSelling_price_5());
								md.setIsGST(setMenuItemModel.getTax());
								md.setIsSVC(setMenuItemModel
										.getService_charge());
								// md.setGroupName(groupCode);
								md.setStandBy("0");
								md.setRemarks(setMenuItemModel.getRemarks());
								md.setItemTau(Utilities.getDataString(
										jSonObFriend, "name"));
								md.setItemName(PosApp.listOrderData2.get(
										PosApp.listOrderData2.size() - 1)
										.getItemName());
								PosApp.listOrderData2.set(
										PosApp.listOrderData2.size() - 1, md);
							}

						}

						sendMessage(activity, LISTITEMMENU, true);
						countMenu++;

					} else {
						countMenu = 0;
						listItemSetMenu.clear();
						sendMessage(activity, LISTITEMMENU, false);
					}

				} else {
					countMenu = 0;
					listItemSetMenu.clear();
					sendMessage(activity, LISTITEMMENU, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, LISTITEMMENU, false);
			}

		}
	};
	private String ip1;
	private String ip2;

	private void getIp1(int one) {
		for (int i = 0; i < listIPMayinModel.size(); i++) {
			if (one == Integer.parseInt(listIPMayinModel.get(i).getId())) {
				ip1 = listIPMayinModel.get(i).getIp_address();
				break;
			}
		}

	}

	public static String GETMAYIN = "mayin";
	public ArrayList<MayinModel> listMayin;

	public ArrayList<MayinModel> getlistMayIn() {
		if (null == listMayin) {
			listMayin = new ArrayList<MayinModel>();
		}
		return listMayin;
	}

	public MayinModel mayinModel;
	public ArrayList<IPMayinModel> listIPMayinModel;

	public ArrayList<IPMayinModel> getlistIPMayIn() {
		if (null == listIPMayinModel) {
			listIPMayinModel = new ArrayList<IPMayinModel>();
		}
		return listIPMayinModel;
	}

	public IPMayinModel iPMayinModel;

	public void callListMayin(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				downsoftpin11, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack downsoftpin11 = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listMayin = new ArrayList<MayinModel>();
						listIPMayinModel = new ArrayList<IPMayinModel>();
						JSONArray listJson;
						JSONArray listJson1;
						listJson = resultJson.getJSONArray("list_data");
						listJson1 = resultJson.getJSONArray("list_printer");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							mayinModel = new MayinModel();
							mayinModel.setData(jSonObFriend);
							listMayin.add(mayinModel);
						}
						for (int i = 0; i < listJson1.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson1.getJSONObject(i);
							iPMayinModel = new IPMayinModel();
							iPMayinModel.setData(jSonObFriend);
							listIPMayinModel.add(iPMayinModel);
						}

						sendMessage(activity, GETMAYIN, true);
					} else {
						sendMessage(activity, GETMAYIN, false);
					}

				} else {
					sendMessage(activity, GETMAYIN, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, GETMAYIN, false);
			}

		}
	};
	public static String LISTUSER = "downsoftpin";
	public ArrayList<ListUserModel> listUser = new ArrayList<ListUserModel>();

	public ArrayList<ListUserModel> getlistUser() {
		if (null == listUser) {
			listUser = new ArrayList<ListUserModel>();
		}
		return listUser;
	}

	public ListUserModel ListUserModelModel;

	public void callListUser(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, downsoftpin,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack downsoftpin = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				// message = Utilities.getDataString(resultJson, "message");
				// if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
				// "is_show_message"))) {
				// isShowMessage = true;
				// }
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						// listService = new ArrayList<ServiceModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							ListUserModelModel = new ListUserModel();
							ListUserModelModel.setData(jSonObFriend);
							listUser.add(ListUserModelModel);
						}

						sendMessage(activity, LISTUSER, true);
					} else {
						sendMessage(activity, LISTUSER, false);
					}

				} else {
					sendMessage(activity, LISTUSER, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, LISTUSER, false);
			}

		}
	};
	public static String TOPUPGAME = "tpgame";
	public static String amount;
	public String tgAcount;

	public void callTopupGame(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, tpgame,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack tpgame = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						amount = Utilities.getDataString(resultJson, "amount");
						tgAcount = Utilities.getDataString(resultJson,
								"targetAccount");
						sendMessage(activity, TOPUPGAME, true);
					} else {
						sendMessage(activity, TOPUPGAME, false);
					}

				} else {
					sendMessage(activity, TOPUPGAME, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, TOPUPGAME, false);
			}

		}
	};
	public static String TOPUPMOBILE = "tpmobile";
	public String amountMobile;
	public String tgAcountMobile;
	public boolean isAdap = false;

	public void callTopupMobile(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, tpmobile,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack tpmobile = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						amountMobile = Utilities.getDataString(resultJson,
								"amount");
						tgAcountMobile = Utilities.getDataString(resultJson,
								"phonenumber");
						sendMessage(activity, TOPUPMOBILE, true);
					} else {
						sendMessage(activity, TOPUPMOBILE, false);
					}

				} else {
					sendMessage(activity, TOPUPMOBILE, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, TOPUPMOBILE, false);
			}

		}
	};
	public static String LOGINUSER = "loginuser";
	public UserModel userModel;

	public UserModel getUserModel() {
		if (null == userModel) {
			userModel = new UserModel();
		}
		return userModel;
	}

	// public static String amount;
	// public String tgAcount;
	public void callLoginUser(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, loginuser,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack loginuser = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						userModel = new UserModel();
						userModel
								.setData(resultJson.getJSONObject("list_data"));
						sendMessage(activity, LOGINUSER, true);
					} else {
						sendMessage(activity, LOGINUSER, false);
					}

				} else {
					sendMessage(activity, LOGINUSER, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, LOGINUSER, false);
			}

		}
	};

	public void sales(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, sales,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack sales = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);

				// PosApp.listOrderData.clear();
				// PosApp.listOrderSplit.clear();
				// SplitBillActivity.listBill.clear();
				MainActivity mani = new MainActivity();
				mani.notifidataOrderList();
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
					} else {
					}

				} else {
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};
	public void getReceiptNo(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, getrc,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack getrc = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
//				Utilities.getGlobalVariable(act).receiptNo=Utilities.getDataString(resultJson, "receipt_no");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						String[] separated = Utilities.getDataString(resultJson, "receipt_no").split("-");
						String num=(Integer.parseInt(separated[1])+1)+"";
						Utilities.getGlobalVariable(act).receiptNo=separated[0]+"-"+num; 
//						PrinterFunctions.PrintPosBill(MainActivity.activity, activity,
//								"USB:", "", activity.getResources(), Utilities.getGlobalVariable(act).receiptNo);
					} else {
						Utilities.getGlobalVariable(act).receiptNo=ComDDUtilities.getDateTime()+"-1";
//						PrinterFunctions.PrintPosBill(MainActivity.activity, activity,
//								"USB:", "", activity.getResources(), Utilities.getGlobalVariable(act).receiptNo);
					}
				
				} else {
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};
	public static String GETNOTIFICATION = "getNotifi";

	// public static String amount;
	// public String tgAcount;
	public void callGetNotifir(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, getNotifi,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack getNotifi = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, GETNOTIFICATION, true);
					} else {
						sendMessage(activity, GETNOTIFICATION, false);
					}

				} else {
					sendMessage(activity, GETNOTIFICATION, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, GETNOTIFICATION, false);
			}

		}
	};

	public static String DERECTIONS = "derections";
	private DerectionsModel derectionsModel;

	public DerectionsModel getDerectionsModel() {
		if (null == derectionsModel) {
			derectionsModel = new DerectionsModel();
		}
		return derectionsModel;
	}

	public static String CONTACT = "contact";
	private ContactModel contactModel;

	public ContactModel getContactModel() {
		if (null == contactModel) {
			contactModel = new ContactModel();
		}
		return contactModel;
	}

	public void callContactModel(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, contact,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack contact = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						contactModel = new ContactModel();
						contactModel.setData(resultJson
								.getJSONObject("list_data"));
						sendMessage(activity, CONTACT, true);
					} else {
						sendMessage(activity, CONTACT, false);
					}

				} else {
					sendMessage(activity, CONTACT, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, CONTACT, false);
			}

		}
	};
	public static String MAINCATE = "listrewssards";
	public MainCateModel mainCateModel;
	private ArrayList<MainCateModel> mainCateList;

	public ArrayList<MainCateModel> getlistMainCate() {
		if (null == mainCateList) {
			mainCateList = new ArrayList<MainCateModel>();
		}
		return mainCateList;
	}

	private static Activity act;

	public void getMainCate(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		act = cont;
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, listrewards,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack listrewards = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						mainCateList = new ArrayList<MainCateModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							mainCateModel = new MainCateModel();
							
							if (Utilities.getDataString(jSonObFriend,
									"language_id").equals("1")) {
								mainCateModel.setData(jSonObFriend);
								mainCateList.add(mainCateModel);
							} else {
								mainCateModel.setNameTau(Utilities.getDataString(
										jSonObFriend, "name"));
								mainCateModel.set_Name(mainCateList.get(
										mainCateList.size() - 1).get_Name());
								mainCateModel.setDataTau(jSonObFriend);
								mainCateList.set(mainCateList.size() - 1,
										mainCateModel);
							}

							
							
//							mainCateModel.setData(jSonObFriend);
//							mainCateList.add(mainCateModel);
						}

						sendMessage(activity, MAINCATE, true);
					} else {
						sendMessage(activity, MAINCATE, false);
					}

				} else {
					sendMessage(activity, MAINCATE, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, MAINCATE, false);
			}

		}
	};
	public static String PAXNUM = "listAds";
	public PaxNumberModel paxModel;

	public PaxNumberModel getPaxModel() {
		if (null == paxModel) {
			paxModel = new PaxNumberModel();
		}
		return paxModel;
	}

	public void getPaxNumber(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, listAds,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack listAds = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						paxModel = new PaxNumberModel();
						paxModel.setData(resultJson.getJSONObject("list_data"));

						sendMessage(activity, PAXNUM, true);
					} else {
						sendMessage(activity, PAXNUM, false);
					}

				} else {
					sendMessage(activity, PAXNUM, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, PAXNUM, false);
			}

		}
	};

	public static String GETITEM1 = "product";
	public ItemsModel item1Model;
	private ArrayList<ItemsModel> listItem1Model;

	public ArrayList<ItemsModel> getLishItems1Model() {
		if (null == listItem1Model) {
			listItem1Model = new ArrayList<ItemsModel>();
		}
		return listItem1Model;
	}

	public void getItem1(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, product,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack product = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listItem1Model = new ArrayList<ItemsModel>();
						ItemsModel vta = new ItemsModel();
						listItem1Model.add(0, vta);
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							item1Model = new ItemsModel();

							if (Utilities.getDataString(jSonObFriend,
									"language_id").equals("1")) {
								item1Model.setData(jSonObFriend);
								listItem1Model.add(item1Model);
							} else {
								item1Model.setNameTau(Utilities.getDataString(
										jSonObFriend, "name"));
								item1Model.setName(listItem1Model.get(
										listItem1Model.size() - 1).getName());
								item1Model.setDataTau(jSonObFriend);
								listItem1Model.set(listItem1Model.size() - 1,
										item1Model);
							}

						}

						sendMessage(activity, GETITEM1, true);
						// Log.v("HAI", "send");
					} else {
						sendMessage(activity, GETITEM1, false);
					}

				} else {
					sendMessage(activity, GETITEM1, false);
				}
			} catch (Exception e) {
				Log.v("HAI", "send" + e.toString());
				// TODO: handle exception
				sendMessage(activity, GETITEM1, false);
			}

		}
	};
	public static String GETITEM2 = "product2";
	public ItemsModel2 item2Model;
	private ArrayList<ItemsModel2> listItem2Model;

	public ArrayList<ItemsModel2> getLishItems2Model() {
		if (null == listItem2Model) {
			listItem2Model = new ArrayList<ItemsModel2>();
		}
		return listItem2Model;
	}

	public void getItem2(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, product2,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack product2 = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listItem2Model = new ArrayList<ItemsModel2>();
						ItemsModel2 vta = new ItemsModel2();
						listItem2Model.add(0, vta);
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							item2Model = new ItemsModel2();
							// item2Model.setData(jSonObFriend);
							// listItem2Model.add(item2Model);

							if (Utilities.getDataString(jSonObFriend,
									"language_id").equals("1")) {
								item2Model.setData(jSonObFriend);
								listItem2Model.add(item2Model);
							} else {
								item2Model.setNameTau(Utilities.getDataString(
										jSonObFriend, "name"));
								item2Model.setName(listItem2Model.get(
										listItem2Model.size() - 1).getName());
								item2Model.setDataTau(jSonObFriend);
								listItem2Model.set(listItem2Model.size() - 1,
										item2Model);
							}
						}

						sendMessage(activity, GETITEM2, true);
					} else {
						sendMessage(activity, GETITEM2, false);
					}

				} else {
					sendMessage(activity, GETITEM2, false);
				}
			} catch (Exception e) {
				Log.v("HAI", "send" + e.toString());
				// TODO: handle exception
				sendMessage(activity, GETITEM2, false);
			}

		}
	};
	public static String GETMENU = "product3";
	public ItemsModel2 item3Model;
	private ArrayList<ItemsModel2> listMenuModel;

	public ArrayList<ItemsModel2> getListMenu() {
		if (null == listMenuModel) {
			listMenuModel = new ArrayList<ItemsModel2>();
		}
		return listMenuModel;
	}

	public void getMenu(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, product3,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack product3 = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listMenuModel = new ArrayList<ItemsModel2>();
						ItemsModel2 vta = new ItemsModel2();
						listMenuModel.add(0, vta);
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							item3Model = new ItemsModel2();
							item3Model.setData(jSonObFriend);
							listMenuModel.add(item3Model);
						}

						sendMessage(activity, GETMENU, true);
					} else {
						sendMessage(activity, GETMENU, false);
					}

				} else {
					sendMessage(activity, GETMENU, false);
				}
			} catch (Exception e) {
				Log.v("HAI", "send" + e.toString());
				// TODO: handle exception
				sendMessage(activity, GETMENU, false);
			}

		}
	};
	public static String TABLESTATUS = "listImagess";
	public TableModel tableModel;
	private ArrayList<TableModel> listTable;

	public ArrayList<TableModel> getListTable() {
		if (null == listTable) {
			listTable = new ArrayList<TableModel>();
		}
		return listTable;
	}

	public void getTableStatus(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, listImagess,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack listImagess = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listTable = new ArrayList<TableModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							tableModel = new TableModel();
							tableModel.setData(jSonObFriend);
							listTable.add(tableModel);
						}

						sendMessage(activity, TABLESTATUS, true);
					} else {
						sendMessage(activity, TABLESTATUS, false);
					}

				} else {
					sendMessage(activity, TABLESTATUS, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, TABLESTATUS, false);
			}

		}
	};
	public static String GETSALES = "listImages";
	public ListOrderModel salesModel;
	private ArrayList<SalesDetailModel> listSales;

	public ArrayList<SalesDetailModel> getListSales() {
		if (null == listSales) {
			listSales = new ArrayList<SalesDetailModel>();
		}
		return listSales;
	}

	public SalesModel saleMD;

	public SalesModel getSaleModel() {
		if (null == saleMD) {
			saleMD = new SalesModel();
		}
		return saleMD;
	}

	public void getSales(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		act=cont;
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, listImages,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack listImages = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_title"))) {
					Utilities.getGlobalVariable(act).isHaveSendOrder=true;
				}else{
					Utilities.getGlobalVariable(act).isHaveSendOrder=false;
				}
				saleMD = new SalesModel();
				if (null != result) {
//					if ((Boolean) resultJson.get("is_success")) {

						saleMD.setData(resultJson.getJSONObject("list_sales"));
						listSales = new ArrayList<SalesDetailModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							SplitOrderModel	salesModel = new SplitOrderModel();
							salesModel.setData(jSonObFriend);
							PosApp.listOrderSplit.add(salesModel);
							// PosApp.listOrderData2.add(salesModel);
						}
						Utilities.getGlobalVariable(Table.ac).countMutil=0;
						sendMessage(activity, GETSALES, true);
//					} else {
//						
//						sendMessage(activity, GETSALES, false);
//					}

				} else {
//					Utils.getReceiptNo(activity);
					sendMessage(activity, GETSALES, false);
				}
			} catch (Exception e) {
				Utils.getReceiptNo(Table.ac);
				sendMessage(activity, GETSALES, false);
			}

		}
	};
	public static String SALEREMARKS = "resservation";
	public SaleRemarkModel saleRemarkModel;
	private ArrayList<SaleRemarkModel> listsaleRemarkModel;

	// public SVModel getListService() {
	// if (null == listSV) {
	// listSV = new SVModel();
	// }
	// return listSV;
	// }

	public ArrayList<SaleRemarkModel> getlistSaleRemarks() {
		if (null == listsaleRemarkModel) {
			listsaleRemarkModel = new ArrayList<SaleRemarkModel>();
		}
		return listsaleRemarkModel;
	}

	private String price1;
	private String price2;
	private String price3;
	private String price4;
	private String price5;
	/**
	 * @return the price1
	 */
	public String getPrice1() {
		return price1;
	}

	/**
	 * @param price1 the price1 to set
	 */
	public void setPrice1(String price1) {
		this.price1 = price1;
	}
	/**
	 * @return the price2
	 */
	public String getPrice2() {
		return price2;
	}

	/**
	 * @param price2 the price2 to set
	 */
	public void setPrice2(String price2) {
		this.price2 = price2;
	}

	/**
	 * @return the price3
	 */
	public String getPrice3() {
		return price3;
	}

	/**
	 * @param price3 the price3 to set
	 */
	public void setPrice3(String price3) {
		this.price3 = price3;
	}

	/**
	 * @return the price4
	 */
	public String getPrice4() {
		return price4;
	}

	/**
	 * @param price4 the price4 to set
	 */
	public void setPrice4(String price4) {
		this.price4 = price4;
	}

	/**
	 * @return the price5
	 */
	public String getPrice5() {
		return price5;
	}

	/**
	 * @param price5 the price5 to set
	 */
	public void setPrice5(String price5) {
		this.price5 = price5;
	}

	public void getSalesRemarks(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				resservation, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack resservation = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				setPrice1(Utilities.getDataString(resultJson, "price1"));
				setPrice2(Utilities.getDataString(resultJson, "price2"));
				setPrice3(Utilities.getDataString(resultJson, "price3"));
				setPrice4(Utilities.getDataString(resultJson, "price4"));
				setPrice5(Utilities.getDataString(resultJson, "price5"));
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listsaleRemarkModel = new ArrayList<SaleRemarkModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							saleRemarkModel = new SaleRemarkModel();
							saleRemarkModel.setData(jSonObFriend);
							listsaleRemarkModel.add(saleRemarkModel);
						}

						sendMessage(activity, SALEREMARKS, true);
					} else {
						sendMessage(activity, SALEREMARKS, false);
					}

				} else {
					sendMessage(activity, SALEREMARKS, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, SALEREMARKS, false);
			}

		}
	};

	public static String CHECKEND = "bookInternet";
	public BookedModel bookedModel;

	public BookedModel getBookedModel() {
		if (null == bookedModel) {
			bookedModel = new BookedModel();
		}
		return bookedModel;
	}

	public void checkEndShift(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				bookInternet, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack bookInternet = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, CHECKEND, true);
					} else {
						sendMessage(activity, CHECKEND, false);
					}

				} else {
					sendMessage(activity, CHECKEND, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, CHECKEND, false);
			}

		}
	};

	public static String PRODUCDEFAULT = "cossntssact";
	private ProducDefaultModel roducDefaultModel;

	public ProducDefaultModel producDefaultModel() {
		if (null == roducDefaultModel) {
			roducDefaultModel = new ProducDefaultModel();
		}
		return roducDefaultModel;
	}

	public void callroducDefaultModel(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				rodsucDefaultModel, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack rodsucDefaultModel = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						roducDefaultModel = new ProducDefaultModel();
						roducDefaultModel.setData(resultJson
								.getJSONObject("list_data"));
						sendMessage(activity, PRODUCDEFAULT, true);
					} else {
						sendMessage(activity, PRODUCDEFAULT, false);
					}

				} else {
					sendMessage(activity, PRODUCDEFAULT, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, PRODUCDEFAULT, false);
			}

		}
	};
	public static String ENDSHIFT = "upPoint";

	public void endShift(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, upPoint,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack upPoint = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, ENDSHIFT, true);
					} else {
						sendMessage(activity, ENDSHIFT, false);
					}

				} else {
					sendMessage(activity, ENDSHIFT, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, ENDSHIFT, false);
			}

		}
	};
	public static String BOOKREWARDS = "bookRewards";

	public void callBookRewards(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, bookRewards,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack bookRewards = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, BOOKREWARDS, true);
					} else {
						sendMessage(activity, BOOKREWARDS, false);
					}

				} else {
					sendMessage(activity, BOOKREWARDS, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, BOOKREWARDS, false);
			}

		}
	};
	public static String PUSH = "pppppp";

	public void updatePush(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, pppppp,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack pppppp = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, PUSH, true);
					} else {
						sendMessage(activity, PUSH, false);
					}

				} else {

					sendMessage(activity, PUSH, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, PUSH, false);
			}

		}
	};
	public static String SIGNUPUSER = "signupuser";

	public void callSignup(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, signupuser,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack signupuser = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, SIGNUPUSER, true);
					} else {
						sendMessage(activity, SIGNUPUSER, false);
					}

				} else {
					sendMessage(activity, SIGNUPUSER, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, SIGNUPUSER, false);
			}

		}
	};
	public static String UPDATEUSER = "upuser";
	private UpdateUserModel updateModel;

	public UpdateUserModel getUpdateModel() {
		if (null == updateModel) {
			updateModel = new UpdateUserModel();
		}
		return updateModel;
	}

	public void up(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, upuser,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack upuser = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, UPDATEUSER, true);
					} else {
						sendMessage(activity, UPDATEUSER, false);
					}

				} else {
					sendMessage(activity, UPDATEUSER, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, UPDATEUSER, false);
			}

		}
	};
	public static String CHECk = "check";

	public void check(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, check,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack check = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, CHECk, true);
					} else {
						sendMessage(activity, CHECk, false);
					}

				} else {
					sendMessage(activity, CHECk, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, CHECk, false);
			}

		}
	};
	public static String ADV = "cossntact";
	private ADVModel ADVModel;
	private ArrayList<ADVModel> ADVModelList;

	public ADVModel ADVModel() {
		if (null == ADVModel) {
			ADVModel = new ADVModel();
		}
		return ADVModel;
	}

	public ArrayList<ADVModel> getListADV() {
		if (null == ADVModelList) {
			ADVModelList = new ArrayList<ADVModel>();
		}
		return ADVModelList;
	}

	public void callADVModel(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, cosntact,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack cosntact = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					// if ((Boolean) resultJson.get("is_success")) {
					// ADVModel = new ADVModel();
					// ADVModel.setData(resultJson
					// .getJSONObject("list_data"));
					// sendMessage(activity, ADV, true);
					// } else {
					// sendMessage(activity, ADV, false);
					// }
					if ((Boolean) resultJson.get("is_success")) {
						ADVModelList = new ArrayList<ADVModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							ADVModel = new ADVModel();
							ADVModel.setData(jSonObFriend);
							ADVModelList.add(ADVModel);
						}

						sendMessage(activity, ADV, true);
					} else {
						sendMessage(activity, ADV, false);
					}

				} else {
					sendMessage(activity, ADV, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, ADV, false);
			}

		}
	};
	public static String MAILLINGLIST = "percent1";

	public void updateMailling(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, percent1,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack percent1 = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						// getUserModel().setVisit(Utilities.getDataString(resultJson,
						// "stamps"));
						sendMessage(activity, MAILLINGLIST, true);
					} else {
						// getUserModel().setVisit(Utilities.getDataString(resultJson,
						// "stamps"));
						sendMessage(activity, MAILLINGLIST, false);
					}

				} else {

					sendMessage(activity, MAILLINGLIST, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, MAILLINGLIST, false);
			}

		}
	};
	public static String GETALLCOUCHERREAL = "allvoucherreal";
	public static String allVoucherreal;
	public String vouAvail;

	public void allVoucherReal(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				allvoucherreal, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack allvoucherreal = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						// getUserModel().setVisit(
						// Utilities.getDataString(resultJson, "stamps"));
						allVoucherreal = Utilities.getDataString(resultJson,
								"user_info");
						vouAvail = Utilities.getDataString(resultJson,
								"vou_avail");
						sendMessage(activity, GETALLCOUCHERREAL, true);
					} else {
						// getUserModel().setVisit(Utilities.getDataString(resultJson,
						// "stamps"));
						sendMessage(activity, GETALLCOUCHERREAL, false);
					}

				} else {

					sendMessage(activity, GETALLCOUCHERREAL, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, GETALLCOUCHERREAL, false);
			}

		}
	};
	public static String GETALLCOUCHER = "allvoucher";
	public static String allVoucher;

	public void allVoucher(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, allvoucher,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack allvoucher = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						// getUserModel().setVisit(
						// Utilities.getDataString(resultJson, "stamps"));
						allVoucher = Utilities.getDataString(resultJson,
								"user_info");
						sendMessage(activity, GETALLCOUCHER, true);
					} else {
						// getUserModel().setVisit(Utilities.getDataString(resultJson,
						// "stamps"));
						sendMessage(activity, GETALLCOUCHER, false);
					}

				} else {

					sendMessage(activity, GETALLCOUCHER, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, GETALLCOUCHER, false);
			}

		}
	};
	public static String UPDATEVOU = "percentvou";

	public void updateVou(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, percentvou,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack percentvou = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, UPDATEVOU, true);
					} else {
						// getUserModel().setVisit(Utilities.getDataString(resultJson,
						// "stamps"));
						sendMessage(activity, UPDATEVOU, false);
					}

				} else {

					sendMessage(activity, UPDATEVOU, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, UPDATEVOU, false);
			}

		}
	};
	public static String UPDATESTAMPS = "percent";

	public void updateStamps(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, percent,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack percent = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, UPDATESTAMPS, true);
					} else {
						// getUserModel().setVisit(Utilities.getDataString(resultJson,
						// "stamps"));
						sendMessage(activity, UPDATESTAMPS, false);
					}

				} else {

					sendMessage(activity, UPDATESTAMPS, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, UPDATESTAMPS, false);
			}

		}
	};
	public static String ADDDEVICE = "adddive";

	public void add(Context cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, adddive1,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack adddive1 = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				// String message = Utilities.getDataString(resultJson,
				// "message");
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, ADDDEVICE, true);
					} else {
						sendMessage(activity, ADDDEVICE, false);
					}

				} else {
					sendMessage(activity, ADDDEVICE, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, ADDDEVICE, false);
			}
			// Log.e("JSON", json);
		}
	};

	public ArrayList<String> listPhone = new ArrayList<String>();

	// public ArrayList<String> listName =new ArrayList<String>();
	public class ListUserModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String userId;
		private String nameUser;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setUserId(Utilities.getDataString(jSonInfo, "id"));
			this.setNameUser(Utilities.getDataString(jSonInfo, "UserName"));

		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getNameUser() {
			return nameUser;
		}

		public void setNameUser(String nameUser) {
			this.nameUser = nameUser;
		}

	}

	public class UpdateUserModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private String nameUp;
		private String fullUp;
		private String phoneUp;
		private String passUp;
		private String emailUp;
		private String IdUser;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setUsernameUp(Utilities.getDataString(jSonInfo, "UserName"));
			this.setfullUp(Utilities.getDataString(jSonInfo, "FullName"));
			this.setEmailUp(Utilities.getDataString(jSonInfo, "Email"));
			this.setPhoneUp(Utilities.getDataString(jSonInfo, "Phone"));
			this.setPassUp(Utilities.getDataString(jSonInfo, "Password"));
			this.setIdUser(Utilities.getDataString(jSonInfo, "Iduser"));

		}

		public String getUsernameUp() {
			return nameUp;
		}

		public void setUsernameUp(String _usernameup) {
			nameUp = _usernameup;
		}

		public String getFullUp() {
			return fullUp;
		}

		public void setfullUp(String _fullup) {
			fullUp = _fullup;
		}

		public String getPhoneUp() {
			return phoneUp;
		}

		public void setPhoneUp(String _phoneup) {
			phoneUp = _phoneup;
		}

		public String getPassUp() {
			return passUp;
		}

		public void setPassUp(String _passup) {
			passUp = _passup;
		}

		public String getEmailUp() {
			return emailUp;
		}

		public void setEmailUp(String _emailup) {
			emailUp = _emailup;
		}

		public String getIdUser() {
			return IdUser;
		}

		public void setIdUser(String idUser) {
			IdUser = idUser;
		}
	}

	public class BookedModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String token;
		private String DateOrder;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setToken(Utilities.getDataString(jSonInfo, "token"));
			this.setDateOrder(Utilities.getDataString(jSonInfo, "TimeOrder"));
		}

		public String getToken() {
			return token;
		}

		public void setToken(String _token) {
			token = _token;
		}

		public String getDateOrder() {
			return DateOrder;
		}

		public void setDateOrder(String dateOrder) {
			DateOrder = dateOrder;
		}

	}

	public class UserModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String id;
		private String username;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setUsername(Utilities.getDataString(jSonInfo, "UserName"));
			this.setId(Utilities.getDataString(jSonInfo, "id"));

		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String _username) {
			username = _username;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	public class LogGameModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String loaigiaodich;
		private String nhamang;
		private String sdtPin;
		private String menhgia;
		private String trangthai;
		private String buyerreal;

		private String date;
		private String timezone;
		private String timezone_type;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setLoaigiaodich(Utilities
					.getDataString(jSonInfo, "trans_type"));
			this.setNhamang(Utilities.getDataString(jSonInfo, "provider"));
			this.setSdtPin(Utilities.getDataString(jSonInfo, "buyer"));
			this.setDate(jSonInfo.getJSONObject("time_create"));
			this.setMenhgia(Utilities.getDataString(jSonInfo, "trans_price"));
			this.setTrangthai(Utilities.getDataString(jSonInfo, "status"));
			this.setBuyerreal(Utilities.getDataString(jSonInfo, "buyerreal"));

		}

		public void setDate(JSONObject jSonInfo) throws JSONException {
			this.setDate(Utilities.getDataString(jSonInfo, "date"));
			this.setTimezone(Utilities.getDataString(jSonInfo, "timezone"));
			this.setTimezone_type(Utilities.getDataString(jSonInfo,
					"timezone_type"));
		}

		public String getLoaigiaodich() {
			return loaigiaodich;
		}

		public void setLoaigiaodich(String loaigiaodich) {
			this.loaigiaodich = loaigiaodich;
		}

		public String getNhamang() {
			return nhamang;
		}

		public void setNhamang(String nhamang) {
			this.nhamang = nhamang;
		}

		public String getSdtPin() {
			return sdtPin;
		}

		public void setSdtPin(String sdtPin) {
			this.sdtPin = sdtPin;
		}

		public String getMenhgia() {
			return menhgia;
		}

		public void setMenhgia(String menhgia) {
			this.menhgia = menhgia;
		}

		public String getTrangthai() {
			return trangthai;
		}

		public void setTrangthai(String trangthai) {
			this.trangthai = trangthai;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTimezone() {
			return timezone;
		}

		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		public String getTimezone_type() {
			return timezone_type;
		}

		public void setTimezone_type(String timezone_type) {
			this.timezone_type = timezone_type;
		}

		public String getBuyerreal() {
			return buyerreal;
		}

		public void setBuyerreal(String buyerreal) {
			this.buyerreal = buyerreal;
		}

	}

	public class SaleRemarkModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String category_id;
		private String option;

		public void SaleRemarkModel() {

		}

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setCategory_id(Utilities
					.getDataString(jSonInfo, "category_id"));
			this.setOption(Utilities.getDataString(jSonInfo, "option"));

		}

		/**
		 * @return the category_id
		 */
		public String getCategory_id() {
			return category_id;
		}

		/**
		 * @param category_id
		 *            the category_id to set
		 */
		public void setCategory_id(String category_id) {
			this.category_id = category_id;
		}

		/**
		 * @return the option
		 */
		public String getOption() {
			return option;
		}

		/**
		 * @param option
		 *            the option to set
		 */
		public void setOption(String option) {
			this.option = option;
		}

	}

	public class DerectionsModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String longt;
		private String lat;
		private String address;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setLongt(Utilities.getDataString(jSonInfo, "Longitude"));
			this.setLat(Utilities.getDataString(jSonInfo, "Latitude"));
			this.setAddress(Utilities.getDataString(jSonInfo, "CompanyAddress"));

		}

		public String getLongt() {
			return longt;
		}

		public void setLongt(String longt) {
			this.longt = longt;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

	}

	public class ContactModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String address;
		private String mobile;
		private String email;
		private String image;
		private String Latitude;
		private String Longitude;

		private String web;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setName(Utilities.getDataString(jSonInfo, "CompanyName"));
			this.setAddress(Utilities.getDataString(jSonInfo, "CompanyAddress"));
			this.setMobile(Utilities.getDataString(jSonInfo, "CompanyPhone"));
			this.setEmail(Utilities.getDataString(jSonInfo, "CompanyEmail"));
			this.setImage(Utilities.getDataString(jSonInfo, "images"));
			this.setWeb(Utilities.getDataString(jSonInfo, "Website"));
			this.setLatitude(Utilities.getDataString(jSonInfo, "Latitude"));
			this.setLongitude(Utilities.getDataString(jSonInfo, "Longitude"));

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getWeb() {
			return web;
		}

		public void setWeb(String web) {
			this.web = web;
		}

		public String getLatitude() {
			return Latitude;
		}

		public void setLatitude(String latitude) {
			Latitude = latitude;
		}

		public String getLongitude() {
			return Longitude;
		}

		public void setLongitude(String longitude) {
			Longitude = longitude;
		}

	}

	public class ADVModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private String image;
		private String nameAd;
		private String contentAD;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setImage(Utilities.getDataString(jSonInfo, "Image"));
			this.setNameAd(Utilities.getDataString(jSonInfo, "Name"));
			this.setContentAD(Utilities.getDataString(jSonInfo, "Description"));

		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getNameAd() {
			return nameAd;
		}

		public void setNameAd(String nameAd) {
			this.nameAd = nameAd;
		}

		public String getContentAD() {
			return contentAD;
		}

		public void setContentAD(String contentAD) {
			this.contentAD = contentAD;
		}

	}

	public class ProducDefaultModel implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private String content;
		private String images;
		private String oldPrice;
		private String newPrice;

		public void setData(JSONObject jSonInfo) throws JSONException {

			this.setName(Utilities.getDataString(jSonInfo, "Name"));
			this.setContent(Utilities.getDataString(jSonInfo, "Content"));
			this.setImages(Utilities.getDataString(jSonInfo, "Images"));
			this.setOldPrice(Utilities.getDataString(jSonInfo, "OldPrice"));
			this.setNewPrice(Utilities.getDataString(jSonInfo, "NewPrice"));

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setImages(String images) {
			this.images = images;
		}

		public String getImage() {
			return images;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getOldPrice() {
			return oldPrice;
		}

		public void setOldPrice(String oldPrice) {
			this.oldPrice = oldPrice;
		}

		public String getNewPrice() {
			return newPrice;
		}

		public void setNewPrice(String newPrice) {
			this.newPrice = newPrice;
		}

	}

	public class ServiceModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String images;

		public void ListReservationModel() {

		}

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setImages(Utilities.getDataString(jSonInfo, "Image"));

		}

		public String getImages() {
			return images;
		}

		public void setImages(String images) {
			this.images = images;
		}

	}

	public class PaxNumberModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String number_of_customers;

		public void ListReservationModel() {

		}

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setNumber_of_customers(Utilities.getDataString(jSonInfo,
					"number_of_customers"));

		}

		public String getNumber_of_customers() {
			return number_of_customers;
		}

		public void setNumber_of_customers(String number_of_customers) {
			this.number_of_customers = number_of_customers;
		}

	}

	public class EventModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String des;
		private String title;
		private String images;

		public void setData(JSONObject jSonInfo) throws JSONException {

			this.setDes(Utilities.getDataString(jSonInfo, "Name"));
			this.setTit(Utilities.getDataString(jSonInfo, "ContentAds"));
			this.setImages(Utilities.getDataString(jSonInfo, "Image"));
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public void setImages(String images) {
			this.images = images;
		}

		public String getImage() {
			return images;
		}

		public String getTit() {
			return title;
		}

		public void setTit(String title) {
			this.title = title;
		}
	}

	public class ProductModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private String content;
		private String images;
		private String oldPrice;
		private String newPrice;

		public void setData(JSONObject jSonInfo) throws JSONException {

			this.setName(Utilities.getDataString(jSonInfo, "Name"));
			this.setContent(Utilities.getDataString(jSonInfo, "Content"));
			this.setImages(Utilities.getDataString(jSonInfo, "Images"));
			this.setOldPrice(Utilities.getDataString(jSonInfo, "OldPrice"));
			this.setNewPrice(Utilities.getDataString(jSonInfo, "NewPrice"));

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setImages(String images) {
			this.images = images;
		}

		public String getImage() {
			return images;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getOldPrice() {
			return oldPrice;
		}

		public void setOldPrice(String oldPrice) {
			this.oldPrice = oldPrice;
		}

		public String getNewPrice() {
			return newPrice;
		}

		public void setNewPrice(String newPrice) {
			this.newPrice = newPrice;
		}
	}

	public class TableModel implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private String status;
		private String join;

		public void TableModel() {

		}

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setName(Utilities.getDataString(jSonInfo, "name"));
			this.setJoin(Utilities.getDataString(jSonInfo, "joined_to"));
			this.setStatus(Utilities.getDataString(jSonInfo, "statuses_id"));

		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getJoin() {
			return join;
		}

		public void setJoin(String join) {
			this.join = join;
		}

	}

	public class SalesDetailModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String item;
		private String item_Code;
		private String discount_amount;
		private String subtotal;
		private String selling_price;
		private String quantity;

		public void ListReservationModel() {

		}

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setItem(Utilities.getDataString(jSonInfo, "item"));
			this.setItem_code(Utilities.getDataString(jSonInfo, "item_id"));
			this.setDiscount_amount(Utilities.getDataString(jSonInfo,
					"discount_amount"));

			this.setSubtotal(Utilities.getDataString(jSonInfo, "subtotal"));
			this.setSelling_price(Utilities.getDataString(jSonInfo,
					"selling_price"));
			this.setQuantity(Utilities.getDataString(jSonInfo, "quantity"));

		}

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}

		public String getItem_code() {
			return item_Code;
		}

		public void setItem_code(String setItem_code) {
			this.item_Code = setItem_code;
		}

		public String getDiscount_amount() {
			return discount_amount;
		}

		public void setDiscount_amount(String discount_amount) {
			this.discount_amount = discount_amount;
		}

		public String getSubtotal() {
			return subtotal;
		}

		public void setSubtotal(String subtotal) {
			this.subtotal = subtotal;
		}

		public String getSelling_price() {
			return selling_price;
		}

		public void setSelling_price(String selling_price) {
			this.selling_price = selling_price;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	/*
	 * public JSONObject loginUser(String email, String password) { // Building
	 * Parameters List<NameValuePair> params = new ArrayList<NameValuePair>();
	 * params.add(new BasicNameValuePair("tag", login_tag)); params.add(new
	 * BasicNameValuePair("email", email)); params.add(new
	 * BasicNameValuePair("password", password)); JSONObject json =
	 * jsonParser.getJSONFromUrl(loginURL, params); // return json //
	 * Log.e("JSON", json.toString()); return json; }
	 */

	/**
	 * function make Login Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	/*
	 * public JSONObject registerUser(String name, String email, String
	 * password) { // Building Parameters List<NameValuePair> params = new
	 * ArrayList<NameValuePair>(); params.add(new BasicNameValuePair("tag",
	 * register_tag)); params.add(new BasicNameValuePair("name", name));
	 * params.add(new BasicNameValuePair("email", email)); params.add(new
	 * BasicNameValuePair("password", password));
	 * 
	 * // getting JSON Object JSONObject json =
	 * jsonParser.getJSONFromUrl(registerURL, params); // return json return
	 * json; }
	 * 
	 * public JSONObject callEp() { // Building Parameters List<NameValuePair>
	 * params = new ArrayList<NameValuePair>(); params.add(new
	 * BasicNameValuePair("tag", "epay")); // JSONObject json =
	 * jsonParser.getJSONFromUrl(loginURL, params); JSONObject json =
	 * jsonParser.getJSONFromUrl(loginURL, params); // return json //
	 * Log.e("JSON", json.toString()); return json; }
	 *//**
	 * Function get Login status
	 * */
	/*
	 * public boolean isUserLoggedIn(Context context) { DatabaseHandler db = new
	 * DatabaseHandler(context); int count = db.getRowCount(); if (count > 0) {
	 * // user logged in return true; } return false; }
	 *//**
	 * Function to logout user Reset Database
	 * */
	/*
	 * public boolean logoutUser(Context context) { DatabaseHandler db = new
	 * DatabaseHandler(context); db.resetTables(); return true; }
	 */
	public static String LISTUSERRESERVATION = "userresservation";

	public ListUserReservationModel userReservationModel;
	private ArrayList<ListUserReservationModel> listUserReservationModel;

	// public ListReservationModel getUserModel() {
	// if (null == userModel) {
	// userModel = new ListReservationModel();
	// }
	// return userModel;
	// }
	public ArrayList<ListUserReservationModel> getListUserReservation() {
		if (null == listUserReservationModel) {
			listUserReservationModel = new ArrayList<ListUserReservationModel>();
		}
		return listUserReservationModel;
	}

	public void getListUserReservation(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams,
				userresservation, _isShowProgressBar);
		// method.execute();
	}

	JSONCallBack userresservation = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						listUserReservationModel = new ArrayList<ListUserReservationModel>();
						JSONArray listJson;
						listJson = resultJson.getJSONArray("list_data");
						for (int i = 0; i < listJson.length(); i++) {
							JSONObject jSonObFriend = new JSONObject();
							jSonObFriend = listJson.getJSONObject(i);
							userReservationModel = new ListUserReservationModel();
							userReservationModel.setData(jSonObFriend);
							listUserReservationModel.add(userReservationModel);
						}

						sendMessage(activity, LISTUSERRESERVATION, true);
					} else {
						sendMessage(activity, LISTUSERRESERVATION, false);
					}

				} else {
					sendMessage(activity, LISTUSERRESERVATION, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, LISTUSERRESERVATION, false);
			}

		}
	};

	public class ListUserReservationModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String daytime;

		public void setData(JSONObject jSonInfo) throws JSONException {
			this.setDate(jSonInfo.getJSONObject("SetDate"));

		}

		public void setDate(JSONObject jSonInfo) throws JSONException {
			this.setdaytime(Utilities.getDataString(jSonInfo, "date"));
		}

		public String getdaytime() {
			return daytime;
		}

		public void setdaytime(String resId) {
			this.daytime = resId;
		}

	}

	public static String FORGOTPASS = "forgotPass";

	public void callForgotPass(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, forgotPass,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack forgotPass = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);
				// JSONObject objectResult = resultJson.getJSONObject("result");
				message = Utilities.getDataString(resultJson, "message");
				if (Boolean.parseBoolean(Utilities.getDataString(resultJson,
						"is_show_message"))) {
					isShowMessage = true;
				}
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {
						sendMessage(activity, FORGOTPASS, true);
					} else {
						sendMessage(activity, FORGOTPASS, false);
					}

				} else {
					sendMessage(activity, FORGOTPASS, false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				sendMessage(activity, FORGOTPASS, false);
			}

		}
	};
	public void deleteItem(Activity cont, List<NameValuePair> JSONObjectParams,
			Boolean _isShowProgressBar) {
		message = "Not Message!";
		// Log.v("HAI", "param: " + JSONObjectParams);
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, forgotPass,
				_isShowProgressBar);
		// method.execute();
	}



}
