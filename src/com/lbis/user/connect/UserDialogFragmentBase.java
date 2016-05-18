package com.lbis.user.connect;

import net.simonvt.messagebar.MessageBar;
import android.app.ProgressDialog;

import com.lbis.model.User;
import com.lbis.model.view.DialogFragmentBase;

public class UserDialogFragmentBase extends DialogFragmentBase implements MessageBar.OnMessageClickListener {

	static User user = null;
	MessageBar toasterObject = null;
	ProgressDialog progressDialog;

	public static User getCurrentEditedUser() {
		if (user == null)
			user = new User();
		return user;
	}

	public static void resetCurrentEditedUser() {
		user = null;
	}

}
