package com.lbis.model.view;

import net.simonvt.messagebar.MessageBar;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.lbis.mazeltov.R;

public class DialogFragmentBase extends DialogFragment implements OnDateSetListener, TimePickerDialog.OnTimeSetListener, MessageBar.OnMessageClickListener {

	static MessageBar toasterObject = null;
	protected final Logger log = Logger.getLogger(getClass().getSimpleName());

	public static MessageBar getMessageBar(Activity activity) {
		if (toasterObject == null)
			toasterObject = new MessageBar(activity);
		return toasterObject;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Fragmanet_Dialog);
		setCancelable(true);
	}

	@Override
	public void onMessageClick(Parcelable token) {

	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub

	}
}
