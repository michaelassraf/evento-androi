package com.lbis.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Window;
import com.capricorn.RayMenu;
import com.google.android.gcm.GCMRegistrar;
import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.concurrency.SwitchUsersConnectionState;
import com.lbis.database.executors.model.FollowDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.mazeltov.event.EventBasicDetailsDialogFragment;
import com.lbis.media.camrecorder.PictureDisplayActivity;
import com.lbis.media.camrecorder.SelectEventDialogFragment;
import com.lbis.media.camrecorder.VideoPlaybackActivity;
import com.lbis.model.User;
import com.lbis.notifications.RegisterGCMOnServer;
import com.lbis.utils.Enums.DBTables;
import com.lbis.utils.Enums.PostType;
import com.squareup.picasso.Picasso;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Utils {

	private static Utils utils;
	private static BitmapDrawable placeHolder;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public static final int DISCARD = 4;
	public static final int SHARE = 5;

	public static Utils getInstance() {
		if (utils == null)
			utils = new Utils();

		return utils;
	}

	public List<String> getTablesNames() {
		ArrayList<String> tables = new ArrayList<String>();
		for (DBTables table : DBTables.values())
			tables.add(table.toString());
		return tables;
	}

	public String calcAge(Long birthday) {
		Long now = new Date().getTime();
		if (birthday.toString().length() > 11)
			birthday = birthday / 1000;
		return "" + ((now - birthday) / (31556926000L));
	}

	public String calcAgeDummy(Long birthday) {
		Long now = new Date().getTime();
		return "" + ((now - birthday) / (31556926000L));
	}

	public String getUserProfile(Long profileId) {
		if (profileId == null || profileId.equals(0L))
			return Enums.URLs.DefalutNoLoadedPicture.getValue();
		StringBuilder sb = new StringBuilder();
		sb = sb.append(Enums.URLs.ProfilePictureBase.getValue());
		sb.append(profileId);
		sb.append(".jpg");
		return sb.toString();
	}

	public int[] getScreenSizes(Context context) {
		int screen[] = new int[2];
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2) {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			screen[0] = display.getWidth();
			screen[1] = display.getHeight();
		}

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screen[0] = size.x;
			screen[1] = size.y;
		}
		return screen;
	}

	public ProgressDialog showProgressDialog(Context context) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		return progressDialog;
	}

	public void designActionBar(final SherlockFragmentActivity activity, boolean isOverlayed) {
		if (isOverlayed)
			activity.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		actionBar.setCustomView(R.layout.app_title);
		activity.findViewById(R.id.secondry_action_bar).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(activity, EventBasicDetailsDialogFragment.class.getName(), EventBasicDetailsDialogFragment.class.getName(), EventBasicDetailsDialogFragment.getDialogInstance());
			}
		});
	}

	public void removeActionBar(final SherlockFragmentActivity activity) {
		// activity.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ActionBar actionBar = activity.getSupportActionBar();
		// actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// actionBar.setStackedBackgroundDrawable(new ColorDrawable(
		// Color.TRANSPARENT));
		// actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	public void showDialog(SherlockFragmentActivity activity, String previousFragmentTag, String prospetctedFragmentTag, DialogFragment fragment) {
		FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
		Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(previousFragmentTag);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(previousFragmentTag);
		fragment.show(ft, prospetctedFragmentTag);
	}

	public void removeDialogs(SherlockFragmentActivity activity, String... dialogs) {
		for (String dialog : dialogs) {
			Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(dialog);
			if (prev != null) {
				FragmentManager manager = activity.getSupportFragmentManager();
				FragmentTransaction trans = manager.beginTransaction();
				trans.remove(prev);
				trans.commit();
				manager.popBackStack();
			}
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void customizeWebView(WebView view, Activity act, String data, boolean zoomToMiddle) {
		if (act != null)
			view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getInstance().getScreenSizes(act)[1]));
		view.getSettings().setBuiltInZoomControls(false);
		view.getSettings().setSupportZoom(false);
		view.getSettings().setLoadsImagesAutomatically(true);
		view.setBackgroundColor(Color.TRANSPARENT);
		view.getSettings().setJavaScriptEnabled(true);
		view.setSaveEnabled(true);
		view.addJavascriptInterface(new WebViewInterface(view), "JSInterface");
		if (data != null)
			view.loadData(data, "text/html; charset=UTF-8", null);
		// check page size after the page loads
		if (zoomToMiddle) {
			view.getSettings().setDefaultZoom(ZoomDensity.FAR);
			view.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					view.loadUrl("javascript:window.JSInterface.getContentSizes(document.getElementsByTagName('html')[0].scrollWidth, document.getElementsByTagName('html')[0].scrollHeight);");
				}
			});
		}
	}

	public String checkPasswordStrength(String passwordText) {
		if (!(passwordText.toLowerCase(Locale.getDefault()) != passwordText)) {
			return "Password must contain lower and upper case letters";
		} // lower and upper case
		if (!(passwordText.length() > 6)) {
			return "Password should be at least 7 charcters";
		} // good pw length of 9+
		int[] blanksDigits = checkBlanksAndDigits(passwordText);
		int numDigits = blanksDigits[0];
		int blanks = blanksDigits[1];
		if (numDigits < 1) {
			return "Password must contain digits";
		}// contain digits
		if (numDigits == passwordText.length()) {
			return "Password must contain letters and digits";
		}// contain other things except digits
		if (blanks > 0) {
			return "Password cannot contain blanks and spaces";
		}// contain blanks
		return null;
	}

	private int[] checkBlanksAndDigits(String inString) {
		int numDigits = 0;
		int blanks = 0;
		int length = inString.length();
		for (int i = 0; i < length; i++) {
			if (Character.isDigit(inString.charAt(i))) {
				numDigits++;
			}
			if (inString.charAt(i) == ' ') {
				blanks++;
			}
		}
		return new int[] { numDigits, blanks };
	}

	public String getDateAsString(Context ctx, long date) {
		return DateFormat.getDateFormat(ctx).format(date);
	}

	public String getHourAsString(Context ctx, long date) {
		return DateFormat.getTimeFormat(ctx).format(date);
	}

	public String getDateAndHourAsString(Context ctx, long date) {
		return getDateAsString(ctx, date) + " @" + getHourAsString(ctx, date);
	}

	// -1 for failure
	public long getDateAsLong(String date) {
		try {
			SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			f.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date d = f.parse(date);
			return d.getTime();
		} catch (ParseException e) {
			return -1;
		}
	}

	public String getInitialDateAsString(Context ctx) {
		Calendar initialCalendar = Calendar.getInstance();
		initialCalendar = Calendar.getInstance();
		initialCalendar.set(Calendar.DAY_OF_MONTH, 01);
		initialCalendar.set(Calendar.MONTH, 01);
		initialCalendar.set(Calendar.YEAR, 1980);
		initialCalendar.set(Calendar.HOUR, 00);
		initialCalendar.set(Calendar.MINUTE, 00);
		initialCalendar.set(Calendar.SECOND, 1980);
		return DateFormat.getDateFormat(ctx).format(initialCalendar.getTimeInMillis());
	}

	public void scorllToBottom(final ScrollView view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ObjectAnimator animY = ObjectAnimator.ofInt(view, "scrollY", view.getScrollY() - 10, view.getHeight());
				animY.setDuration(600);
				animY.start();
			}
		}, 0);
	}

	public void scorllToTop(final ScrollView view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ObjectAnimator animY = ObjectAnimator.ofInt(view, "scrollY", view.getScrollY() - 10, 0);
				animY.setDuration(100);
				animY.start();
			}
		}, 600);
	}

	public Drawable createPlaceholderDrawable(Context ctx, int widthScale, int heightScale) {

		if (placeHolder != null)
			return placeHolder;

		int width = getScreenSizes(ctx)[0] / widthScale;
		int height = getScreenSizes(ctx)[1] / heightScale;
		Bitmap dummyBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(dummyBitmap);
		Paint p = new Paint();
		p.setColor(ctx.getResources().getColor(R.color.white));
		c.drawColor(Color.TRANSPARENT);
		Bitmap logo = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.mazeltov_logo);
		if (logo.getHeight() > height || logo.getWidth() > width) {
			logo = Bitmap.createScaledBitmap(logo, logo.getHeight() / 3, logo.getWidth() / 3, false);
		}
		c.drawBitmap(logo, (width / 2) - logo.getWidth() / 2, (height / 2) - logo.getHeight() / 2, p);

		BitmapDrawable placeHolder = new BitmapDrawable(ctx.getResources(), dummyBitmap);
		placeHolder.setAlpha(120);
		return placeHolder;
	}

	public Drawable createBackgroundDrawable(Context ctx, int widthScale, int heightScale) {

		int width = getScreenSizes(ctx)[0];
		int height = getScreenSizes(ctx)[1];
		Bitmap dummyBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(dummyBitmap);
		Paint p = new Paint();
		p.setColor(ctx.getResources().getColor(R.color.white));
		c.drawColor(Color.WHITE);
		Bitmap logo = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.background);
		c.drawBitmap(logo, width, height, p);

		BitmapDrawable placeHolder = new BitmapDrawable(ctx.getResources(), dummyBitmap);
		placeHolder.setAlpha(80);
		return placeHolder;
	}

	public void loadDesignedImageView(ImageView imageView, String imagePath, final Context ctx, int widthScale, int heightScale, boolean addOnClickListener, boolean matchParentLayout, boolean isFile, PostType mediaType, String videoLocation, Integer radius, Integer margin) {
		int width = Utils.getInstance().getScreenSizes(ctx)[0];
		int height = Utils.getInstance().getScreenSizes(ctx)[1];
		if (matchParentLayout) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / widthScale, height / heightScale);
			imageView.setLayoutParams(params);
		}

		if (GenericValidator.isBlankOrNull(imagePath)) {
			imagePath = Enums.URLs.DefalutNoLoadedPicture.getValue();
			isFile = false;
		}

		if (radius == null || margin == null) {
			radius = 0;
			margin = 0;
		}

		if (isFile)
			Picasso.with(ctx).load(new File(imagePath)).resize(width / widthScale, height / heightScale).centerCrop().placeholder(Utils.getInstance().createPlaceholderDrawable(ctx, widthScale, heightScale)).transform(new RoundedTransformation(radius, margin)).into(imageView);
		else
			Picasso.with(ctx).load(imagePath).resize(width / widthScale, height / heightScale).centerCrop().placeholder(Utils.getInstance().createPlaceholderDrawable(ctx, widthScale, heightScale)).transform(new RoundedTransformation(radius, margin)).into(imageView);
		if (addOnClickListener) {
			final String finalImagePath = imagePath;
			switch (mediaType) {
			case Video:
				final String finalVideoPath = videoLocation;
				OnClickListener videoClickListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ctx, VideoPlaybackActivity.class);
						intent.putExtra(VideoPlaybackActivity.IS_SHARE, false);
						intent.putExtra(VideoPlaybackActivity.THUMBNAIL_LOCATION, finalImagePath);
						intent.putExtra(VideoPlaybackActivity.VIDEO_LOCATION, finalVideoPath);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ctx.startActivity(intent);
					}
				};
				imageView.setOnClickListener(videoClickListener);
				break;
			case Image:
				final boolean finalIsFile = isFile;
				OnClickListener pictureClickListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ctx, PictureDisplayActivity.class);
						if (finalIsFile)
							intent.putExtra("file", finalImagePath);
						if (!finalIsFile)
							intent.putExtra("url", finalImagePath);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ctx.startActivity(intent);
					}
				};
				imageView.setOnClickListener(pictureClickListener);
				break;
			}
		}

	}

	public void loadDesignedImageView(ImageView imageView, final String imageUrl, final Context ctx, int widthScale, int heightScale) {
		this.loadDesignedImageView(imageView, imageUrl, ctx, widthScale, heightScale, true, true, false);
	}

	public String configureLog4J() {
		try {
			LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "mazeltov.log");
			logConfigurator.setRootLevel(Level.INFO);
			logConfigurator.configure();
			return logConfigurator.getFileName();
		} catch (Exception e) {
			log.error("Can't configure Log4J", e);
			e.printStackTrace();
			return "Can't configure Log4J";
		}
	}

	public Long setChosenDate(Context ctx, TextView dateView, int year, int monthOfYear, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.YEAR, year);

		dateView.setText(Utils.getInstance().getDateAsString(ctx, cal.getTimeInMillis()));
		return cal.getTimeInMillis();
	}

	public void replaceFragment(FragmentManager fragmentManager, Fragment fragment) {
		this.replaceFragment(fragmentManager, fragment, R.id.emptyview);
	}

	public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int destViewId) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(destViewId, fragment, fragment.getClass().getName());
		ft.addToBackStack(fragment.toString());
		ft.commitAllowingStateLoss();
	}

	public String formatValidatationResponse(String oldPhrase, String newResult) {
		if (oldPhrase == null)
			return newResult;
		return oldPhrase + " and " + newResult.toLowerCase(Locale.US);
	}

	public void launchImageChoser(Activity activity, String title, int callbackNum) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		}
		activity.startActivityForResult(Intent.createChooser(intent, title), callbackNum);
	}

	public String getPath(Activity activity, Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		if (cursor == null)
			return "";
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	public void registerGCM(Activity activity) {
		GCMRegistrar.checkDevice(activity);
		GCMRegistrar.checkManifest(activity);
		int numTries = 5;
		String regId = GCMRegistrar.getRegistrationId(activity);
		if (GenericValidator.isBlankOrNull(regId)) {
			while (true) {
				try {
					GCMRegistrar.register(activity, Enums.GCM.GCMSenderId.getValue());
					regId = GCMRegistrar.getRegistrationId(activity);
					if (GenericValidator.isBlankOrNull(regId) && numTries > 0) {
						Thread.sleep(5000);
						throw new Exception();
					}
					Looper.prepare();
					new RegisterGCMOnServer(activity.getApplicationContext()).execute(regId);
					break;
				} catch (Exception e) {
					if (--numTries == 0) {
						log.error("Error while trying to register GCM - ", e);
					}
				}
			}
		} else
			new RegisterGCMOnServer(activity.getApplicationContext()).execute(regId);
	}

	public void addMediaButtonToRayMenu(int[] buttonInfo, final SherlockFragmentActivity activity, RayMenu rayMenu, final String localPicturePath, final String localVideoPath) {
		int actionID = buttonInfo[1];
		int imageResID = buttonInfo[0];
		ImageView item = new ImageView(activity);
		item.setImageResource(imageResID);
		OnClickListener listener = null;
		switch (actionID) {
		case SHARE:
			listener = new OnClickListener() {
				public void onClick(View v) {
					showDialog(activity, SelectEventDialogFragment.class.getName(), SelectEventDialogFragment.class.getName(), SelectEventDialogFragment.getDialogInstance(localPicturePath, localVideoPath));
				}
			};
			break;
		case DISCARD:
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.finish();
				}
			};
			break;
		}
		rayMenu.addItem(item, listener);
	}

	public void loadDesignedImageView(ImageView userProfilePictureView, String itemUrl, Context applicationContext, int i, int j, boolean b, boolean c, boolean d) {
		this.loadDesignedImageView(userProfilePictureView, itemUrl, applicationContext, i, j, b, c, d, PostType.Image, null, null, null);

	}

	public boolean checkIfIsValidUser(User user) {
		if (user == null || user.getUserProfilePicture() == null || GenericValidator.isBlankOrNull(user.getUserProfilePicture().getFormattedUrl()) || user.getUserFirstName() == null)
			return false;
		return true;

	}

	public String checkIfIsFollowingString(Long UserId, Context context) {
		return new FollowDbExecutors().isFollowing(UserId, context) ? "Following" : "Follow";
	}

	public boolean checkIfIsFollowing(Long userId, Context context) {

		return new FollowDbExecutors().isFollowing(userId, context);
	}

	public void displayUser(final User user, Boolean isFollowing, Boolean isLoggedInUser, TextView userNameView, TextView userDetailsView, ImageView userProfileView, final Button userFollownButton, final Activity activity) {
		userNameView.setText(user.getUserFirstName() + " " + user.getUserLastName());
		userDetailsView.setText(userDetailsGenerator(user, activity));
		loadDesignedImageView(userProfileView, user.getUserProfilePicture().getFormattedUrl(), activity, 3, 3, true, true, false);
		// handling follow button
		// if is already don't display
		if (isLoggedInUser) {
			userFollownButton.setText(activity.getResources().getString(R.string.its_you));
			userFollownButton.setOnClickListener(null);
			return;
		}

		if (isFollowing == null)
			return;
		userFollownButton.setText(Utils.getInstance().checkIfIsFollowingString(user.getId(), activity));

		userFollownButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AsyncTaskMap.getAsyncHashMap().cancelAll();
				SwitchUsersConnectionState task = new SwitchUsersConnectionState(userFollownButton, activity, user);
				AsyncTaskMap.getAsyncHashMap().add(task);
				task.execute();
			}
		});
	}

	private CharSequence userDetailsGenerator(User user, Context context) {
		return new StringBuilder().append(user.getUserSexCode() == 1 ? "Male" : "Female").append(" ").append(Utils.getInstance().calcAgeDummy(user.getUserBirthday() * 1000)).append(" years old").append("\nMember since ").append(Utils.getInstance().getDateAsString(context, user.getUserJoinDate() * 1000)).toString();
	}

}
