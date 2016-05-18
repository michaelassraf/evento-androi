package com.lbis.view.components;

import org.apache.log4j.Logger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.Item;
import com.lbis.model.User;
import com.lbis.server.WebReciever;
import com.lbis.server.actions.UserActions;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;

public class MyProfileFragement extends Fragment {

	public static final String USER_ID_IDENTFIRER = "user_id";

	TextView myProfileFirstNameTextView;
	TextView myProfileLastNameTextView;
	TextView myProfileEmailTextView;
	TextView myProfileGender;
	TextView myProfileBirthday;
	TextView myProfileAge;
	ImageView myProfileEmailImageView;

	Long userId;
	User user;

	Logger log = Logger.getLogger(getClass().getSimpleName());

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);
		myProfileFirstNameTextView = (TextView) rootView.findViewById(R.id.my_profile_first_name);
		myProfileLastNameTextView = (TextView) rootView.findViewById(R.id.my_profile_last_name);
		myProfileEmailTextView = (TextView) rootView.findViewById(R.id.my_profile_email);
		myProfileEmailImageView = (ImageView) rootView.findViewById(R.id.my_profile_picture);
		myProfileGender = (TextView) rootView.findViewById(R.id.my_profile_gender);
		myProfileBirthday = (TextView) rootView.findViewById(R.id.my_profile_birthday);
		myProfileAge = (TextView) rootView.findViewById(R.id.my_profile_age);

		if (getArguments() != null && getArguments().getLong(USER_ID_IDENTFIRER) > 0 && userId == null)
			userId = getArguments().getLong(USER_ID_IDENTFIRER);

		if (userId == null)
			userId = 0L;

		this.user = new UserDbExecutors().get(new User(userId), getActivity()) == null ? null : new UserDbExecutors().get(new User(userId), getActivity());

		if (this.user == null || !Utils.getInstance().checkIfIsValidUser(this.user)) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						new UserDbExecutors().put(getActivity(), new UserActions().getUserDetails(userId, WebReciever.getInstance().getConnection(), getActivity()));
					} catch (Throwable e) {
						log.fatal("Can't load user", e);
					}
					return null;
				}

				protected void onPostExecute(Void result) {

					user = new UserDbExecutors().get(userId, getActivity());
					displayUser(user);
				}

			}.execute();
		}

		if (Utils.getInstance().checkIfIsValidUser(this.user)) {
			displayUser(user);
		}

		return rootView;
	}

	@Override
	public void onResume() {
		if (Utils.getInstance().checkIfIsValidUser(this.user)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Item item = user.getUserProfilePicture();
					user.setUserProfilePicture(null);
					displayUser(user);
					user.setUserProfilePicture(item);
					displayUser(user);
				}
			}, 1000);
		}
		super.onResume();
	}

	void displayUser(final User user) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					myProfileFirstNameTextView.setText(user.getUserFirstName());
					myProfileLastNameTextView.setText(user.getUserLastName());
					myProfileEmailTextView.setText(user.getUserEmail());
					myProfileGender.setText(user.getUserSex().toString());
					myProfileBirthday.setText(Utils.getInstance().getDateAsString(getActivity(), user.getUserBirthday() * 1000));
					myProfileAge.setText(Utils.getInstance().calcAgeDummy(user.getUserBirthday() * 1000));
					Utils.getInstance().loadDesignedImageView(myProfileEmailImageView, user.getUserProfilePicture().getFormattedUrl(), getActivity(), 1, 2, true, false, false, PostType.Image, null, Utils.getInstance().getScreenSizes(getActivity())[0] / 10, Utils.getInstance().getScreenSizes(getActivity())[0] / 15);

				}
			});

		}

	}
}
