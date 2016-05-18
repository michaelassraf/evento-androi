package com.lbis.mazeltov;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.simonvt.messagebar.MessageBar;
import net.simonvt.messagebar.MessageBar.OnMessageClickListener;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.capricorn.RayMenu;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.model.Item;
import com.lbis.model.Token;
import com.lbis.model.User;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.ItemActions;
import com.lbis.server.actions.UserActions;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.utils.Enums.Sex;
import com.lbis.utils.HTMLCreator;
import com.lbis.utils.PictureDownloader;
import com.lbis.utils.Utils;
import com.lbis.view.components.BeforeDatePickerFragment;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class SignUpActivity extends BaseActivity implements OnDateSetListener, MessageBar.OnMessageClickListener {

	final Logger log = Logger.getLogger(getClass().getSimpleName());
	Calendar cal;

	ScrollView userScrollView;
	EditText userFirstNameView;
	EditText userLastNameView;
	EditText userEmailView;
	EditText userPasswordView;
	TextView userBirthDateView;
	ImageView userProfilePictureView;
	Button userLoginSignupButton;
	Button userFacebookLoginButton;
	Spinner userGenderView;

	RayMenu rayMenu;
	ProgressDialog progressDialog;
	MessageBar toasterObject = null;

	int[] commitButton = new int[] { R.drawable.composer_camera, COMMIT };
	int[] cancelButton = new int[] { R.drawable.composer_thought, CANCEL };

	Session session;

	public static final int COMMIT = 8;
	public static final int CANCEL = 9;

	final int LOGIN_MODE = 1;
	final int SIGNUP_MODE = 2;
	static Integer CURRENT_MODE;
	static boolean IS_FACEBOOK = false;
	static final int SELECT_PICTURE = 1;
	static final String SELECT_PICTURE_TITLE = "Select your profile picture";
	static final String PROBLEM_WITH_FB_LOGIN = "Problem occur while trying to connect facebook. Please try again later.";
	static String FACEBOOK_EMAIL_PROPERTY = "email";
	static String FACEBOOK_BIRTHDAY_PROPERTY = "user_birthday";
	static String FACEBOOK_GENDER_PROPERTY = "gender";
	final boolean ARROW_STATE = true;
	boolean ARROW_STATE_1 = ARROW_STATE;
	static String ARROW_ROTATION = "rotation";
	static String ARROW_SCALE_X = "rotation";
	static String ARROW_SCALE_Y = "rotation";

	static final long SLEEP_TIME_BEFORE_LAUNCH = 2000;

	User activeUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_from_top, R.anim.push_left_out);
		Utils.getInstance().designActionBar(this, true);
		setContentView(R.layout.sign_up_fragment);

		userScrollView = (ScrollView) findViewById(R.id.signup_fieds_scroll);

		rayMenu = (RayMenu) findViewById(R.id.ray_menu);

		addButtonToRayMenu(commitButton, this);
		addButtonToRayMenu(cancelButton, this);

		FrameLayout pictureHolder = (FrameLayout) findViewById(R.id.profile_picture_holder);

		RelativeLayout.LayoutParams paramas = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, Utils.getInstance().getScreenSizes(this)[1] / 2);

		pictureHolder.setLayoutParams(paramas);

		userFirstNameView = (EditText) findViewById(R.id.first_name);
		userLastNameView = (EditText) findViewById(R.id.last_name);
		userEmailView = (EditText) findViewById(R.id.email);
		userPasswordView = (EditText) findViewById(R.id.password);
		userProfilePictureView = (ImageView) findViewById(R.id.profile_picture);
		userGenderView = (Spinner) findViewById(R.id.gender);
		userLoginSignupButton = (Button) findViewById(R.id.login);
		userFacebookLoginButton = (Button) findViewById(R.id.facebook_login);
		userBirthDateView = (TextView) findViewById(R.id.birthday);

		toasterObject = new MessageBar(this);
		toasterObject.setOnClickListener((OnMessageClickListener) this);

		initializeAllViews();

		if (activeUser != null) {
			displayUser(activeUser);
			return;
		}

		activeUser = new User();
	}

	void initializeAllViews() {
		List<String> genders = new ArrayList<String>();
		String[] gendersValues = getResources().getStringArray(R.array.genders);

		for (String value : gendersValues)
			genders.add(value);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, genders);

		userGenderView.setAdapter(adapter);
		userBirthDateView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new BeforeDatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		userBirthDateView.setText(Utils.getInstance().getInitialDateAsString(getApplicationContext()));
		final Activity act = this;
		userProfilePictureView.setBackgroundColor(Color.parseColor("#FF777777"));
		userProfilePictureView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Utils.getInstance().launchImageChoser(act, SELECT_PICTURE_TITLE, SELECT_PICTURE);
				return false;
			}
		});
		userFacebookLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDataFromFacebook(act);
				CURRENT_MODE = SIGNUP_MODE;
			}
		});

		userLoginSignupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchConnectionMethod();
			}
		});
	}

	public void displayUser(User user) {
		userFirstNameView.setText(user.getUserFirstName());
		userLastNameView.setText(user.getUserLastName());
		userEmailView.setText(user.getUserEmail());
		userPasswordView.setText(user.getUserPassword());
		userBirthDateView.setText(Utils.getInstance().getDateAsString(getApplicationContext(), user.getUserBirthday() * 1000));

		if (user.getUserSex() == null)
			user.setUserSex(Enums.Sex.Man);

		switch (user.getUserSex()) {
		case Man:
			userGenderView.setSelection(0);
			break;
		case Woman:
			userGenderView.setSelection(1);
			break;
		default:
			userGenderView.setSelection(0);
			break;
		}

		if (IS_FACEBOOK) {
			changeFieldsState(false);
			Utils.getInstance().loadDesignedImageView(userProfilePictureView, user.getUserProfilePicture().getItemUrl(), getApplicationContext(), 1, 2, true, false, false);
			return;
		}

		changeFieldsState(true);
		Utils.getInstance().loadDesignedImageView(userProfilePictureView, user.getUserProfilePicture().getItemUrl(), getApplicationContext(), 1, 2, true, false, true);

	}

	private void changeFieldsState(boolean state) {
		userGenderView.setEnabled(state);
		userFirstNameView.setEnabled(state);
		userLastNameView.setEnabled(state);
		userEmailView.setEnabled(state);
		userPasswordView.setEnabled(state);
		userBirthDateView.setEnabled(state);
	}

	public void switchConnectionMethod() {
		switch (CURRENT_MODE) {
		case LOGIN_MODE:
			CURRENT_MODE = SIGNUP_MODE;
			break;
		case SIGNUP_MODE:
			CURRENT_MODE = LOGIN_MODE;
			break;
		default:
			CURRENT_MODE = SIGNUP_MODE;
			break;
		}
		setConnectionMethod();
	}

	void setConnectionMethod() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (CURRENT_MODE == null) {
					CURRENT_MODE = SIGNUP_MODE;
				}
				switch (CURRENT_MODE) {
				case LOGIN_MODE:
					userLoginSignupButton.setText("Sign Up");
					userGenderView.setVisibility(View.INVISIBLE);
					userFirstNameView.setVisibility(View.INVISIBLE);
					userLastNameView.setVisibility(View.INVISIBLE);
					userBirthDateView.setVisibility(View.INVISIBLE);
					Utils.getInstance().scorllToBottom(userScrollView);
					Utils.getInstance().scorllToTop(userScrollView);
					break;
				case SIGNUP_MODE:
					userLoginSignupButton.setText("Log In");
					userGenderView.setVisibility(View.VISIBLE);
					userFirstNameView.setVisibility(View.VISIBLE);
					userLastNameView.setVisibility(View.VISIBLE);
					userBirthDateView.setVisibility(View.VISIBLE);
					Utils.getInstance().scorllToBottom(userScrollView);
					Utils.getInstance().scorllToTop(userScrollView);
					break;
				}

			}
		});
	}

	@Override
	public void onMessageClick(Parcelable token) {

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		activeUser.setUserBirthday(Utils.getInstance().setChosenDate(getApplicationContext(), userBirthDateView, year, monthOfYear, dayOfMonth) / 1000);
	}

	void getDataFromFacebook(final Activity activity) {
		session = new Session(activity.getApplicationContext());
		Session.OpenRequest openRequest = new Session.OpenRequest(activity);
		List<String> permissions = new ArrayList<String>();

		permissions.add(FACEBOOK_EMAIL_PROPERTY);
		permissions.add(FACEBOOK_BIRTHDAY_PROPERTY);
		openRequest.setPermissions(permissions);
		session.openForRead(openRequest);
		session.addCallback(new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				progressDialog = Utils.getInstance().showProgressDialog(activity);
				if (session != null && session.isOpened()) {
					Request.newMeRequest(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user, Response response) {
							FacebookRequestError error = response.getError();
							if (error != null) {
								log.error("Error while trying to connect facebook - " + error.toString());
								toasterObject.show(PROBLEM_WITH_FB_LOGIN);
								finish();
							}
							if (user != null) {
								log.info("Data retrieved from Facebook " + user.toString());
								String genderFB = user.getProperty(FACEBOOK_GENDER_PROPERTY).toString();

								Enums.Sex enumGender = Enums.Sex.Man;

								if (genderFB.startsWith("f"))
									enumGender = Enums.Sex.Woman;
								activeUser.setUserProfilePicture(new Item(null, HTMLCreator.getInstance().getFacebookProfileURL(user.getId()), ContentTypes.JPG, null));

								activeUser.setUserBirthday(parstDate(user.getBirthday()));
								activeUser.setUserSex(enumGender);
								activeUser.setUserSexCode(enumGender.getValue());
								activeUser.setUserEmail(user.getProperty(FACEBOOK_EMAIL_PROPERTY).toString());
								activeUser.setUserPassword(user.getId().toString());
								activeUser.setUserJoinDate(System.currentTimeMillis() / 1000);
								activeUser.setUserLastName(user.getLastName());
								activeUser.setUserFirstName(user.getFirstName());
								log.info("User data parsed from Facebook " + activeUser.toString());
								if (progressDialog != null)
									progressDialog.dismiss();
								animateArrow();
								IS_FACEBOOK = true;
								displayUser(activeUser);
							}
						}

					}).executeAsync();
				} else {
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					// toasterObject
					// .show("Problem occur while connecting to facebook.");
				}
			}
		});
	}

	void animateArrow() {
		ImageView image = (ImageView) findViewById(R.id.instruct);
		image.setVisibility(View.VISIBLE);
		ObjectAnimator scaleXOut = ObjectAnimator.ofFloat(image, ARROW_SCALE_X, 1f, 0f);
		ObjectAnimator scaleXIn = ObjectAnimator.ofFloat(image, ARROW_SCALE_X, 0f, 1f);
		ObjectAnimator scaleYOut = ObjectAnimator.ofFloat(image, ARROW_SCALE_Y, 1f, 0f);
		ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(image, ARROW_SCALE_Y, 0f, 1f);
		ObjectAnimator rotateClockWise = ObjectAnimator.ofFloat(image, ARROW_ROTATION, 0f, 360f);
		ObjectAnimator rotateCounterClockWise = ObjectAnimator.ofFloat(image, ARROW_ROTATION, 0f, -360f);
		AnimatorSet set = new AnimatorSet();
		if (ARROW_STATE_1 == ARROW_STATE) {
			set.play(scaleXIn).with(rotateClockWise).with(scaleYIn);
		} else {
			set.play(scaleXOut).with(rotateCounterClockWise).with(scaleYOut);
		}
		ARROW_STATE_1 = !ARROW_STATE_1;
		set.setDuration(2000);
		set.start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				try {
					Uri selectedImageUri = data.getData();
					activeUser.setUserProfilePicture(new Item(null, PictureDownloader.getPath(getApplicationContext(), selectedImageUri), ContentTypes.PNG, null));
					Utils.getInstance().loadDesignedImageView(userProfilePictureView, activeUser.getUserProfilePicture().getItemUrl(), getApplicationContext(), 1, 2, true, false, true);
				} catch (Exception e) {
					log.error("Bad content recieved from the file picker");
				}
			}

			if (session != null) {
				session.onActivityResult(this, requestCode, resultCode, data);
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}

	void addButtonToRayMenu(int[] buttonInfo, final Activity activity) {
		int actionID = buttonInfo[1];
		int imageResID = buttonInfo[0];
		ImageView item = new ImageView(this);
		item.setImageResource(imageResID);
		OnClickListener listener = null;
		switch (actionID) {
		case COMMIT:
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Enums.Sex enumGender = Sex.Man;
					switch (userGenderView.getSelectedItemPosition()) {
					case 0:
						enumGender = Sex.Man;
						break;
					case 1:
						enumGender = Sex.Woman;
						break;
					}
					switch (CURRENT_MODE) {
					case SIGNUP_MODE:
						activeUser.setUserFirstName(userFirstNameView.getText().toString());
						activeUser.setUserLastName(userLastNameView.getText().toString());
						activeUser.setUserSex(enumGender);
						activeUser.setUserSexCode(enumGender.getValue());
						activeUser.setUserEmail(userEmailView.getText().toString());
						activeUser.setUserPassword(userPasswordView.getText().toString());
						activeUser.setUserJoinDate(System.currentTimeMillis() / 1000);
						String fullValdiation = activeUser.fullValidation(!IS_FACEBOOK);
						if (fullValdiation != null) {
							toasterObject.show(fullValdiation);
							break;
						}
						new ProcessUserRequest(activity, activeUser).execute();
						break;
					case LOGIN_MODE:
						activeUser.setUserEmail(userEmailView.getText().toString());
						activeUser.setUserPassword(userPasswordView.getText().toString());
						activeUser.setUserJoinDate(System.currentTimeMillis() / 1000);
						String partialValdiation = activeUser.partialValidation(!IS_FACEBOOK);
						if (partialValdiation != null) {
							toasterObject.show(partialValdiation);
							break;
						}
						new ProcessUserRequest(activity, activeUser).execute();
						break;
					}

				}
			};
			break;
		case CANCEL:
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					activeUser = new User();
					initializeAllViews();
				}
			};
			break;
		}
		rayMenu.addItem(item, listener);
	}

	class ProcessUserRequest extends AsyncTask<Void, Void, Void> {

		Activity activity;
		User returnedUser;
		User recievedUser;

		ProcessUserRequest(Activity activity, User recievedUser) {
			this.activity = activity;
			this.recievedUser = recievedUser;

		}

		@Override
		protected void onPreExecute() {
			progressDialog = Utils.getInstance().showProgressDialog(activity);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				switch (CURRENT_MODE) {
				case SIGNUP_MODE:
					returnedUser = new UserActions().signUp(recievedUser, WebReciever.getInstance().getConnection(), activity.getApplicationContext());
					int numTries = 5;
					Token token = null;
					while (numTries > 0 && token == null) {
						token = new ExecuteManagementMethods().getTokenAndUserId(getApplicationContext());
						Thread.sleep(1000);
						numTries--;
					}
					if (token == null && IS_FACEBOOK) {
						log.info("Failed to sign up with user imported from facebook, going to login.");
						returnedUser = new UserActions().logIn(recievedUser, WebReciever.getInstance().getConnection(), activity.getApplicationContext());
						new UserDbExecutors().put(activity, recievedUser);
					}
					break;
				case LOGIN_MODE:
					returnedUser = new UserActions().logIn(recievedUser, WebReciever.getInstance().getConnection(), activity.getApplicationContext());
					new UserDbExecutors().put(activity, returnedUser);
					break;
				}

			} catch (Exception e) {
				log.error("Problem occur while trying to create an account", e);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						toasterObject.show("There is an issue with our server. Please try again later.");
					}
				});
				returnedUser = new User(-1L);
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			if (returnedUser != null && returnedUser.getUserId().equals(-1L))
				return;
			if (returnedUser != null) {
				if (returnedUser.getUserId() == null) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							toasterObject.show("We can't find such user is our system. \nPlease sign up.");
						}
					});
					log.error("No User Id recivied");
					return;
				}
				toasterObject.show(returnedUser.welcome());
				Thread thread = new Thread() {
					public void run() {
						Thread.currentThread().setName("App Launcher Thread from sign up");
						try {
							Thread.sleep(SLEEP_TIME_BEFORE_LAUNCH);
							startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
							activity.finish();
							if (!IS_FACEBOOK) {
								new ItemActions().putFile(activity.getApplicationContext(), returnedUser.getUserId(), ContentTypes.PNG, activeUser.getUserProfilePicture().getItemUrl());
							}
							log.info("Got user object from server ! Lauching up !!");
							Intent myIntent = new Intent(getApplicationContext(), Splash.class);
							myIntent.putExtra(MainActivity.JUST_LOGGED_IN, true);
							startActivity(myIntent);
							new UserDbExecutors().put(getApplicationContext(), returnedUser);
						} catch (InterruptedException e) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									toasterObject.show("We have a problem process your request.\nPlease try again later.");
								}
							});
							returnedUser = new User(-1L);
							log.error("Error occured while sleeping", e);
						}
					}
				};
				thread.start();
			} else {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						toasterObject.show("We have a problem process your request.\nPlease try again later.");
					}
				});
				returnedUser.setUserId(-1L);
				log.error("Error occured while trying to get user from server. Returned object was null.");
			}
			super.onPostExecute(result);
		}
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
}
