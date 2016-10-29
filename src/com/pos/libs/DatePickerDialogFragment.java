/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package com.pos.libs;


import java.util.Calendar;

import com.pos.CustomFragmentActivity;
import com.pos.interfaceapp.onDateSelectedListener;
import com.pos.interfaceapp.onTimeSelectedListener;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.TimePicker;


@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, OnTimeSetListener {
	private Fragment mFragment;
	private Boolean isDatePicker;

	public DatePickerDialogFragment(Fragment callback, Boolean isDatePicker) {
		mFragment = callback;
		this.isDatePicker = isDatePicker;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		if (isDatePicker) {
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);

		}
		else {
			int hours = c.get(Calendar.HOUR_OF_DAY);
			int minutes = c.get(Calendar.MINUTE);
			return new TimePickerDialog(getActivity(), this, hours, minutes, true);
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		((CustomFragmentActivity) mFragment.getActivity()).isShowDialog = false;
		((onDateSelectedListener) mFragment).onDateSelected(dayOfMonth, monthOfYear, year);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
		((CustomFragmentActivity) mFragment.getActivity()).isShowDialog = false;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		((CustomFragmentActivity) mFragment.getActivity()).isShowDialog = false;
		((onTimeSelectedListener) mFragment).onTimeSelected(hourOfDay, minute);
	}
}