package com.pos.libs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import com.pos.model.ListOrderModel;
import com.pos.model.SplitBillModel;
import com.pos.model.SplitOrderModel;

public class CalculationUtils {

	public static double calculateSubTotal(List<ListOrderModel> vattuList) {
		double chiphiVatTu = 0;
		for (int i = 0; i < vattuList.size(); i++) {
			ListOrderModel vtct = vattuList.get(i);
//			chiphiVatTu = chiphiVatTu
//					+ (Double.parseDouble(vtct.getQualyti())
//							* Double.parseDouble(vtct.getUnitPrice()) - Double
//								.parseDouble(vtct.getDiscount()));
			chiphiVatTu = chiphiVatTu
					+ (Double.parseDouble(vtct.getAmount()));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiVatTu));
		return sub;
	}

	public static double calculateSubTotalSplit(
			List<SplitOrderModel> vattuList, String bill) {
		double chiphiVatTu = 0;
		for (int i = 0; i < vattuList.size(); i++) {
			SplitOrderModel vtct = vattuList.get(i);
			if (vtct.getBill().equals(bill)) {
//				chiphiVatTu = chiphiVatTu
//						+ (Double.parseDouble(vtct.getQualyti())
//								* Double.parseDouble(vtct.getUnitPrice()) - Double
//									.parseDouble(vtct.getDiscount()));
				chiphiVatTu = chiphiVatTu
						+ (Double.parseDouble(vtct.getAmount()));
			}

		}
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiVatTu));
		return sub;
	}

	public static double calculateGST(double sum, int percentGST) {
		double chiphiNhanCong = 0;
		chiphiNhanCong = sum * 0;
		chiphiNhanCong = round(chiphiNhanCong, 2);
		return chiphiNhanCong;
	}

	public static double calculatePercent(double sum, double percent) {
		double chiphiNhanCong = 0;
		chiphiNhanCong = sum / 100 * percent;
		chiphiNhanCong = round(chiphiNhanCong, 2);
		return chiphiNhanCong;
	}

	public static double calculateGram(double sum, double price) {
		double chiphiNhanCong = 0;
		chiphiNhanCong = sum / 100 * price;
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiNhanCong));
		return sub;
	}

	public static double calculateChange(double sum, double mustpay) {
		double chiphiNhanCong = 0;
		chiphiNhanCong = sum - mustpay;
		// chiphiNhanCong = Math.round(chiphiNhanCong / 100) * 100;
		chiphiNhanCong = round(chiphiNhanCong, 2);
		return chiphiNhanCong;
	}

	public static double lamtron(double so) {
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(so));
		return sub;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String round2(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.toString();
	}

	public static double calculateServiceCharge(List<ListOrderModel> vattuList) {

		double chiphiVatTu = 0;
		for (int i = 0; i < vattuList.size(); i++) {
			ListOrderModel vtct = vattuList.get(i);
			if (vtct.getIsSVC().equals("1")) {
				if (vtct.getTakeaway().equals("0")) {
					chiphiVatTu = chiphiVatTu
							+ (Double.parseDouble(vtct.getQualyti())
									* Double.parseDouble(vtct.getUnitPrice()) * 0.1);
				}

			}

		}
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiVatTu));
		return sub;
	}

	public static double calculateGSTCharge(List<ListOrderModel> vattuList) {

		double chiphiVatTu = 0;
		for (int i = 0; i < vattuList.size(); i++) {
			ListOrderModel vtct = vattuList.get(i);
			if (vtct.getIsGST().equals("1")) {
				double svc = 0;
				if (vtct.getIsSVC().equals("1")) {
					if (vtct.getTakeaway().equals("0")) {
					svc = (Double.parseDouble(vtct.getQualyti())
							* Double.parseDouble(vtct.getUnitPrice()) * 0.1);
					}
				}
				chiphiVatTu = chiphiVatTu
						+ ((Double.parseDouble(vtct.getQualyti())
								* Double.parseDouble(vtct.getUnitPrice()) + svc) * 0.07);
			}

		}
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiVatTu));
		return sub;
	}

	public static double calculateServiceChargeSplit(
			List<SplitOrderModel> vattuList, String bill) {

		double chiphiVatTu = 0;
		for (int i = 0; i < vattuList.size(); i++) {
			SplitOrderModel vtct = vattuList.get(i);
			if (vtct.getIsSVC().equals("1")) {
				if (vtct.getBill().equals(bill)) {
					if (vtct.getTakeaway().equals("0")) {
					chiphiVatTu = chiphiVatTu
							+ (Double.parseDouble(vtct.getQualyti())
									* Double.parseDouble(vtct.getUnitPrice()) * 0.1);
					}
				}
			}

		}
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiVatTu));
		return sub;
	}

	public static double calculateGSTChargeSplit(List<SplitOrderModel> vattuList,String bill) {

		double chiphiVatTu = 0;
		for (int i = 0; i < vattuList.size(); i++) {
			SplitOrderModel vtct = vattuList.get(i);
			if (vtct.getIsGST().equals("1")) {
				double svc = 0;
				if (vtct.getIsSVC().equals("1")) {
					if (vtct.getBill().equals(bill)) {
						svc = (Double.parseDouble(vtct.getQualyti())
								* Double.parseDouble(vtct.getUnitPrice()) * 0.1);
					}
				}
				if (vtct.getBill().equals(bill)) {
					chiphiVatTu = chiphiVatTu
							+ ((Double.parseDouble(vtct.getQualyti())
									* Double.parseDouble(vtct.getUnitPrice()) + svc) * 0.07);
				}
			}

		}
		DecimalFormat df = new DecimalFormat("0.00");
		double sub = Double.valueOf(df.format(chiphiVatTu));
		return sub;
	}
}
