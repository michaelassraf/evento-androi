package com.lbis.user.connect;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.mazeltov.MainActivity;
import com.lbis.mazeltov.R;
import com.lbis.mazeltov.Splash;
import com.lbis.model.Item;
import com.lbis.model.Token;
import com.lbis.model.User;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.UserActions;
import com.lbis.utils.Enums;
import com.lbis.utils.HTMLCreator;
import com.lbis.utils.Utils;
import com.lbis.utils.Enums.ContentTypes;

public class UserFacebookDialogFragment extends UserDialogFragmentBase {

	View main;
	Session session;

	static String FACEBOOK_EMAIL_PROPERTY = "email";
	static String FACEBOOK_BIRTHDAY_PROPERTY = "user_birthday";
	static String FACEBOOK_GENDER_PROPERTY = "gender";
	static boolean isAlreadyCalledOnce = false;

	static final String PROBLEM_WITH_FB_LOGIN = "Problem occur while trying to connect facebook. Please try again later.";

	public static UserFacebookDialogFragment getDialogInstance() {
		return new UserFacebookDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setRetainInstance(true);
		main = inflater.inflate(R.layout.user_facebook_fragment, null);
		return main;
	}

	@Override
	public void onResume() {
		if (!isAlreadyCalledOnce) {
			new GetFacbookData().execute();
			isAlreadyCalledOnce = true;
		}
		super.onResume();
	}

	class GetFacbookData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			getDataFromFacebook();
			return null;
		}
	}

	class ProcessFacebookConnect extends AsyncTask<Void, Void, Void> {
		User userFromServer;

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				userFromServer = new UserActions().signUp(getCurrentEditedUser(), WebReciever.getInstance().getConnection(), getDialog().getOwnerActivity());
				int numTries = 5;
				Token token = null;
				while (numTries > 0 && token == null) {
					if (getDialog() == null)
						return null;
					token = new ExecuteManagementMethods().getTokenAndUserId(getDialog().getOwnerActivity());
					Thread.sleep(1000);
					numTries--;
				}
				if (token == null) {
					log.info("Failed to sign up with user imported from facebook, going to login.");
					userFromServer = new UserActions().logIn(getCurrentEditedUser(), WebReciever.getInstance().getConnection(), getDialog().getOwnerActivity());
				}
				// new UserDbExecutors().put(getDialog().getOwnerActivity(),
				// userFromServer);
			} catch (Throwable th) {
				log.error("Problem occur while trying to create an account", th);
				getDialog().getOwnerActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getMessageBar(getDialog().getOwnerActivity()).show("There is an issue with our server. Please try again later.");
					}
				});
				userFromServer = new User(-1L);
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (getDialog() == null)
				return;
			if (userFromServer != null && userFromServer.getUserId() != null && userFromServer.getUserId().equals(-1L))
				return;
			if (userFromServer != null) {
				if (userFromServer.getUserId() == null) {
					getDialog().getOwnerActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getMessageBar(getDialog().getOwnerActivity()).show("Something went wrong.\nPlease try again.");
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

	void getDataFromFacebook() {
		session = new Session(getDialog().getOwnerActivity());
		Session.OpenRequest openRequest = new Session.OpenRequest(getDialog().getOwnerActivity());
		List<String> permissions = new ArrayList<String>();

		permissions.add(FACEBOOK_EMAIL_PROPERTY);
		permissions.add(FACEBOOK_BIRTHDAY_PROPERTY);
		openRequest.setPermissions(permissions);
		session.openForRead(openRequest);
		session.addCallback(new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session != null && session.isOpened()) {
					Request.newMeRequest(session, new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user, Response response) {
							FacebookRequestError error = response.getError();
							if (error != null) {
								log.error("Error while trying to connect facebook - " + error.toString());
								toasterObject.show(PROBLEM_WITH_FB_LOGIN);
							}
							if (user != null) {
								log.info("Data retrieved from Facebook " + user.toString());
								String genderFB = user.getProperty(FACEBOOK_GENDER_PROPERTY).toString();

								Enums.Sex enumGender = Enums.Sex.Man;

								if (genderFB.startsWith("f"))
									enumGender = Enums.Sex.Woman;
								getCurrentEditedUser().setUserProfilePicture(new Item(null, HTMLCreator.getInstance().getFacebookProfileURL(user.getId()), ContentTypes.JPG, null));

								getCurrentEditedUser().setUserBirthday(parstDate(user.getBirthday()));
								getCurrentEditedUser().setUserSex(enumGender);
								getCurrentEditedUser().setUserSexCode(enumGender.getValue());
								getCurrentEditedUser().setUserEmail(user.getProperty(FACEBOOK_EMAIL_PROPERTY).toString());
								getCurrentEditedUser().setUserPassword(user.getId().toString());
								getCurrentEditedUser().setUserJoinDate(System.currentTimeMillis() / 1000);
								getCurrentEditedUser().setUserLastName(user.getLastName());
								getCurrentEditedUser().setUserFirstName(user.getFirstName());
								log.info("User data parsed from Facebook " + getCurrentEditedUser().toString());
								new ProcessFacebookConnect().execute();
							}
						}
					}).executeAsync();
				}

			}
		});
	}

	public Long parstDate(String date) {
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			Date result = df.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(result);
			return cal.getTimeInMillis() / 1000;
		} catch (Exception ex) {
			log.error("Can't parse fucking facebook date", ex);
		}
		return 0L;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (session != null) {
				session.onActivityResult(getDialog().getOwnerActivity(), requestCode, resultCode, data);
			}

		}
	}
}
