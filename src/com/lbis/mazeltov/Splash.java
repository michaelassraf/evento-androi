package com.lbis.mazeltov;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.concurrency.ConcurrencyUtils;
import com.lbis.database.executors.SQLiteClient;
import com.lbis.management.database.executors.ExecuteManagementMethods;
import com.lbis.model.User;
import com.lbis.user.connect.UserFacebookDialogFragment;
import com.lbis.user.connect.UserLogInDialogFragment;
import com.lbis.user.connect.UserSignUpDialogFragment;
import com.lbis.utils.Utils;

public class Splash extends BaseActivity {

	public static Long SLEEP_TIME_BEFORE_ACTIVITY_LAUNCH = 1000L;
	final Logger log = Logger.getLogger(getClass().getSimpleName());
	User activeUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.getInstance().removeActionBar(this);
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_to_bottom);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		MainActivity.setFirstRun(true);
		final Activity activity = this;
		Thread neatInitilazer = new Thread() {
			public void run() {
				log.info("Successully configured Log4J, logs will be at " + Utils.getInstance().configureLog4J());
				try {
					PackageInfo info = getPackageManager().getPackageInfo("com.lbis.mazeltov", PackageManager.GET_SIGNATURES);
					for (Signature signature : info.signatures) {
						MessageDigest md = MessageDigest.getInstance("SHA");
						md.update(signature.toByteArray());
						String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
						log.error("MY KEY HASH:" + sign);
					}
				} catch (NameNotFoundException e) {
				} catch (NoSuchAlgorithmException e) {
				}
				SQLiteClient.getConnetion(getApplicationContext());
				log.info("Successfully created DB connection");
				Intent openingMain;
				if (new ExecuteManagementMethods().getTokenAndUserId(getApplicationContext()) == null) {
					initializeUserScreen();
					// openingMain = new Intent(getApplicationContext(),
					// SignUpActivity.class);
					log.info("Token is null - this is rocky user !");
					// startActivity(openingMain);
					// if (activity != null)
					// activity.finish();
					return;
				} else {
					openingMain = new Intent(getApplicationContext(), MainActivity.class);
					if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(MainActivity.JUST_LOGGED_IN)) {
						openingMain.putExtra(MainActivity.JUST_LOGGED_IN, getIntent().getExtras().getBoolean(MainActivity.JUST_LOGGED_IN));
						log.info("Token is not null - this is user that just signed in/up, how cool is that!");
					} else
						log.info("Token is not null - this is awesome returned user !");

				}
				ConcurrencyUtils.getInstance().updateAllCache(0, getApplicationContext());
				log.info("Fired all cache update missles !");
				Utils.getInstance().registerGCM(activity);
				log.info("Taking a shot for crappy GCM registration");

				try {
					sleep(SLEEP_TIME_BEFORE_ACTIVITY_LAUNCH);
				} catch (InterruptedException e) {
				}

				startActivity(openingMain);
				if (activity != null)
					activity.finish();
			}

		};
		neatInitilazer.start();

		// try {
		// PackageInfo info =
		// getPackageManager().getPackageInfo(getPackageName(),
		// PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md;
		//
		// md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// String something = new String(Base64.encode(md.digest(), 0));
		// Log.d("Hash key", something);
		// }
		// } catch (Exception ex){
		// Log.e("Chibi", "chibi" ,ex);
		// }

	}

	private void initializeUserScreen() {
		final Button splashSignUpWithFB;
		final Button splashSignUp;
		final Button splashLogIn;

		splashSignUpWithFB = (Button) findViewById(R.id.splash_sign_up_with_facebook);
		splashSignUp = (Button) findViewById(R.id.splash_sign_up);
		splashLogIn = (Button) findViewById(R.id.splash_sign_in);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				splashSignUpWithFB.setEnabled(true);
				splashSignUp.setEnabled(true);
				splashLogIn.setEnabled(true);
				splashSignUpWithFB.setVisibility(View.VISIBLE);
				splashSignUp.setVisibility(View.VISIBLE);
				splashLogIn.setVisibility(View.VISIBLE);
			}
		});

		splashSignUpWithFB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchFacebookScreen();
			}
		});
		splashSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					public void run() {
						launchSignUpScreen();
					}
				});
			}
		});
		splashLogIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					public void run() {
						launchLogInScreen();
					}
				});

			}
		});

	}

	private void launchLogInScreen() {
		Utils.getInstance().showDialog((SherlockFragmentActivity) this, UserLogInDialogFragment.class.getName(), UserLogInDialogFragment.class.getName(), new UserLogInDialogFragment());

	}

	private void launchSignUpScreen() {
		Utils.getInstance().showDialog((SherlockFragmentActivity) this, UserSignUpDialogFragment.class.getName(), UserSignUpDialogFragment.class.getName(), new UserSignUpDialogFragment());
	}

	private void launchFacebookScreen() {
		Utils.getInstance().showDialog((SherlockFragmentActivity) this, UserFacebookDialogFragment.class.getName(), UserFacebookDialogFragment.class.getName(), new UserFacebookDialogFragment());
	}

}