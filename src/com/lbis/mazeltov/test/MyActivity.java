package com.lbis.mazeltov.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.lbis.mazeltov.R;
import com.lbis.model.User;
import com.lbis.model.view.AutoCompleteAdapter;
import com.lbis.server.actions.UserActions;

public class MyActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);

		AutoCompleteAdapter<User, UserActions> myAdapter = new AutoCompleteAdapter(this, R.layout.event_list_view, new UserActions(), null);

		AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		acTextView.setAdapter(myAdapter);
	}

}