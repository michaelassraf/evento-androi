package com.lbis.view.components;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class UnlimitedDatePickerFragment extends DialogFragment {

	DatePickerDialog.OnDateSetListener listener = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		listener = listener == null ? (DatePickerDialog.OnDateSetListener) getActivity() : listener;
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
		return dialog;
	}

	public void setOnDateSetListene(DatePickerDialog.OnDateSetListener listener) {
		this.listener = listener;
	}
}