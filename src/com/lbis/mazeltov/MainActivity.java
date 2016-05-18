package com.lbis.mazeltov;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.view.Menu;
import com.capricorn.RayMenu;
import com.lbis.database.executors.model.EventDbExecutors;
import com.lbis.database.executors.model.NotificationToInformDbExecutors;
import com.lbis.database.executors.model.PostsDbExecutors;
import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.model.Event;
import com.lbis.model.Notification;
import com.lbis.model.Post;
import com.lbis.model.User;
import com.lbis.model.view.ProfilePopUpDialog;
import com.lbis.utils.RayMenuOnClickListener;
import com.lbis.utils.Utils;
import com.lbis.view.components.EventFragment;
import com.lbis.view.components.HomeFragment;

public class MainActivity extends BaseActivity {

	public static final int HOME_CLICK = 1;
	public static final int UPCOMING_CLICK = 2;
	public static final int MOMENT_CLICK = 3;
	public static final int NEWS_CLICK = 4;
	public static final int SETTINGS_CLICK = 5;

	public static final String JUST_LOGGED_IN = "just_logged_in";

	private int[] homeButton = new int[] { R.drawable.composer_camera, HOME_CLICK };
	private int[] upcomingButton = new int[] { R.drawable.composer_thought, UPCOMING_CLICK };
	private int[] momentClick = new int[] { R.drawable.composer_camera, MOMENT_CLICK };
	private int[] newsButton = new int[] { R.drawable.composer_with, NEWS_CLICK };
	private int[] settingsButton = new int[] { R.drawable.mazeltov_logo, SETTINGS_CLICK };

	private RayMenu rayMenu;

	private static boolean isFirstRun = true;
	private static boolean justLoggedIn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.getInstance().designActionBar(this, true);
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_to_top);
		setContentView(R.layout.activity_main);
		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.emptyview);
		rootView.setFocusableInTouchMode(true); // this line is important
		rootView.requestFocus();

		ImageView mazeltovRotatedImage = (ImageView) findViewById(R.id.mazeltov_rotated_logo);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.getInstance().getScreenSizes(getApplicationContext())[0] / 3, Utils.getInstance().getScreenSizes(getApplicationContext())[0] / 3);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		mazeltovRotatedImage.setLayoutParams(params);
		Animation firstAnimation = AnimationUtils.loadAnimation(this, R.anim.first_animation);
		mazeltovRotatedImage.startAnimation(firstAnimation);

		rayMenu = (RayMenu) findViewById(R.id.ray_menu);
		addButtonToRayMenu(homeButton);
		addButtonToRayMenu(upcomingButton);
		addButtonToRayMenu(momentClick);
		addButtonToRayMenu(newsButton);
		addButtonToRayMenu(settingsButton);

		if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(JUST_LOGGED_IN)) {
			justLoggedIn = getIntent().getExtras().getBoolean(JUST_LOGGED_IN);
		}

		if (isFirstRun() || justLoggedIn) {
			showMainScreen();
			setFirstRun(false);
			return;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Notification toBeDisplayedNotification = new NotificationToInformDbExecutors().getFirstObjectIfExists(getApplicationContext());
		if (toBeDisplayedNotification == null) {
			return;
		}
		new NotificationToInformDbExecutors().drop(getApplicationContext(), toBeDisplayedNotification);
		switch (toBeDisplayedNotification.getNotificationTypeEnum()) {
		case Follow:
			new UserDbExecutors().put(getApplicationContext(), (User) toBeDisplayedNotification.getNotificationData());
			new ProfilePopUpDialog(this, new User(((User) toBeDisplayedNotification.getNotificationData()).getId())).show();
			break;
		case Invitation:
			new EventDbExecutors().put(getApplicationContext(), (Event) toBeDisplayedNotification.getNotificationData());
			Bundle args = new Bundle();
			args.putLong(EventFragment.EVENT_ID_IDENTIFER, toBeDisplayedNotification.getClickListenerId());
			EventFragment newfrag = new EventFragment();
			newfrag.setArguments(args);
			Utils.getInstance().replaceFragment(getSupportFragmentManager(), newfrag);
			break;
		case Post:
			new PostsDbExecutors().put(getApplicationContext(), (Post) toBeDisplayedNotification.getNotificationData());
			showMainScreen();
			break;
		}

	}

	private void showMainScreen() {
		Fragment homeFragment = new HomeFragment();
		Bundle b = new Bundle();
		b.putBoolean(HomeFragment.IS_FIRST_RUN, true);
		homeFragment.setArguments(b);
		Utils.getInstance().replaceFragment(getSupportFragmentManager(), homeFragment);
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {

		return super.onCreateView(name, context, attrs);
	}

	public static void setFirstRun(boolean b) {
		isFirstRun = b;
	}

	private static boolean isFirstRun() {
		return isFirstRun;
	}

	private void addButtonToRayMenu(int[] buttonInfo) {
		int actionID = buttonInfo[1];
		int imageResID = buttonInfo[0];
		ImageView item = new ImageView(this);
		item.setImageResource(imageResID);
		RayMenuOnClickListener listener = new RayMenuOnClickListener(actionID, this);
		rayMenu.addItem(item, listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 1)
			getSupportFragmentManager().popBackStack();
		else
			moveTaskToBack(true);
	}
}