package com.lbis.user.connect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.mazeltov.MainActivity;
import com.lbis.mazeltov.R;
import com.lbis.mazeltov.Splash;
import com.lbis.model.Item;
import com.lbis.model.Token;
import com.lbis.model.User;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.ItemActions;
import com.lbis.server.actions.UserActions;
import com.lbis.utils.Enums;
import com.lbis.utils.PictureDownloader;
import com.lbis.utils.Utils;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.view.components.BeforeDatePickerFragment;

public class UserSignUpDialogFragment extends UserDialogFragmentBase implements OnDateSetListener {

	static final int SELECT_PICTURE = 1;
	static final String SELECT_PICTURE_TITLE = "Select your profile picture";

	View main;
	LinearLayout userSignUpHolderView;
	EditText userSignUpFirstNameView;
	EditText userSignUpLastNameView;
	EditText userSignUpEmailView;
	EditText userSignUpPasswordView;
	TextView userSignUpBirthDateView;
	ImageView userSignUpProfilePictureView;
	Spinner userSignUpGenderView;
	Button userSignUpResetButton;
	Button userSignUpCommitButton;
	FrameLayout userSignUpProfilePictureHolderView;

	static UserSignUpDialogFragment userSignUpDialogFragment;

	public static UserSignUpDialogFragment getDialogInstance() {
		return new UserSignUpDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		main = inflater.inflate(R.layout.user_sign_up_fragment, null);

		userSignUpHolderView = (LinearLayout) main.findViewById(R.id.user_sign_up_holder);
		userSignUpFirstNameView = (EditText) main.findViewById(R.id.user_signup_first_name);
		userSignUpLastNameView = (EditText) main.findViewById(R.id.user_signup_last_name);
		userSignUpEmailView = (EditText) main.findViewById(R.id.user_signup_email);
		userSignUpPasswordView = (EditText) main.findViewById(R.id.user_signup_password);
		userSignUpBirthDateView = (TextView) main.findViewById(R.id.user_signup_birthday);
		userSignUpProfilePictureView = (ImageView) main.findViewById(R.id.user_signup_profile_picture);
		userSignUpResetButton = (Button) main.findViewById(R.id.user_signup_reset);
		userSignUpCommitButton = (Button) main.findViewById(R.id.user_signup_commit);
		userSignUpGenderView = (Spinner) main.findViewById(R.id.user_signup_gender);
		userSignUpProfilePictureHolderView = (FrameLayout) main.findViewById(R.id.user_signup_profile_picture_holder);

		userSignUpGenderView.setPopupBackgroundResource(R.drawable.spinner_bkg);

		userSignUpProfilePictureHolderView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, Utils.getInstance().getScreenSizes(getActivity())[1] / 5));

		userSignUpResetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resetCurrentEditedUser();
				initializeAllViews();
			}
		});

		userSignUpCommitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getCurrentEditedUser().setUserEmail(userSignUpEmailView.getText().toString());
				getCurrentEditedUser().setUserFirstName(userSignUpFirstNameView.getText().toString());
				getCurrentEditedUser().setUserJoinDate(System.currentTimeMillis() / 1000);
				getCurrentEditedUser().setUserLastName(userSignUpLastNameView.getText().toString());
				getCurrentEditedUser().setUserSex(Enums.Sex.values()[userSignUpGenderView.getSelectedItemPosition()]);
				getCurrentEditedUser().setUserSexCode(userSignUpGenderView.getSelectedItemPosition() + 1);
				getCurrentEditedUser().setUserPassword(userSignUpPasswordView.getText().toString());
				String fullValidation = getCurrentEditedUser().fullValidation(true);
				if (fullValidation != null) {
					getMessageBar(getDialog().getOwnerActivity()).show(fullValidation);
					return;
				}
				new ProcessUserLogInRequest().execute();
			}
		});

		initializeAllViews();
		userSignUpDialogFragment = this;
		return main;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				try {
					Uri selectedImageUri = data.getData();
					getCurrentEditedUser().setUserProfilePicture(new Item(null, PictureDownloader.getPath(getDialog().getOwnerActivity(), selectedImageUri), ContentTypes.PNG, null));
					Utils.getInstance().loadDesignedImageView(userSignUpProfilePictureView, getCurrentEditedUser().getUserProfilePicture().getItemUrl(), getDialog().getOwnerActivity(), 1, 2, true, false, true);
				} catch (Exception e) {
					log.error("Bad content recieved from the file picker");
				}
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}

	void initializeAllViews() {
		List<String> genders = new ArrayList<String>();
		String[] gendersValues = getResources().getStringArray(R.array.genders);

		for (String value : gendersValues)
			genders.add(value);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, genders);

		userSignUpGenderView.setAdapter(adapter);
		userSignUpBirthDateView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BeforeDatePickerFragment newFragment = new BeforeDatePickerFragment();
				newFragment.setOnDateSetListener((OnDateSetListener) userSignUpDialogFragment);
				newFragment.show(((SherlockFragmentActivity) getActivity()).getSupportFragmentManager(), "datePicker");
			}
		});
		userSignUpBirthDateView.setText(Utils.getInstance().getInitialDateAsString(getActivity()));
		userSignUpProfilePictureView.setBackgroundColor(Color.parseColor("#FF777777"));
		userSignUpProfilePictureView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Utils.getInstance().launchImageChoser(getActivity(), SELECT_PICTURE_TITLE, SELECT_PICTURE);
				return false;
			}
		});

	}

	class ProcessUserLogInRequest extends AsyncTask<Void, Void, Void> {

		User userFromServer;

		@Override
		protected void onPreExecute() {
			progressDialog = Utils.getInstance().showProgressDialog(getDialog().getOwnerActivity());
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				userFromServer = new UserActions().signUp(getCurrentEditedUser(), WebReciever.getInstance().getConnection(), getDialog().getOwnerActivity());
				int numTries = 5;
				Token token = null;
				while (numTries > 0 && token == null) {
					token = new ExecuteManagementMethods().getTokenAndUserId(getDialog().getOwnerActivity());
					Thread.sleep(1000);
					numTries--;
				}

				if (token == null) {
					userFromServer = null;
					return null;
				}
				new UserDbExecutors().put(getDialog().getOwnerActivity(), userFromServer);
				new ItemActions().putFile(getDialog().getOwnerActivity(), userFromServer.getUserId(), ContentTypes.PNG, getCurrentEditedUser().getUserProfilePicture().getItemUrl());
			} catch (Throwable th) {
				userFromServer = null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			if (userFromServer != null && userFromServer.getUserId() != null && userFromServer.getUserId().equals(-1L))
				return;
			if (userFromServer != null) {
				if (userFromServer.getUserId() == null) {
					getDialog().getOwnerActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getMessageBar(getDialog().getOwnerActivity()).show("Problem sigining up.\nPlease try again.");
						}
					});
					log.error("No User Id recivied");
					return;
				}
				getMessageBar(getDialog().getOwnerActivity()).show(userFromServer.welcome());
				Thread thread = new Thread() {
					public void run() {
						Thread.currentThread().setName("App Launcher Thread from Log in");
						getDialog().getOwnerActivity().finish();
						log.info("Got user object from server ! Lauching up !!");
						Intent myIntent = new Intent(getDialog().getOwnerActivity(), Splash.class);
						myIntent.putExtra(MainActivity.JUST_LOGGED_IN, true);
						startActivity(myIntent);
						new UserDbExecutors().put(getDialog().getOwnerActivity(), userFromServer);
					}
				};
				thread.start();
			} else {
				getDialog().getOwnerActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getMessageBar(getDialog().getOwnerActivity()).show("We have a problem process your request.\nPlease try again later.");
					}
				});
				userFromServer.setUserId(-1L);
				log.error("Error occured while trying to get user from server. Returned object was null.");
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		UserDialogFragmentBase.getCurrentEditedUser().setUserBirthday(Utils.getInstance().setChosenDate(getDialog().getContext(), userSignUpBirthDateView, year, monthOfYear, dayOfMonth) / 1000);
	}

}
