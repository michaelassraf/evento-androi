package com.lbis.model;

import com.lbis.database.model.KeyObjectIfc;
import com.lbis.database.model.SimpleIfc;
import com.lbis.database.model.ValueObjectAbs;
import com.lbis.model.view.ListItemAbs;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.Sex;
import com.lbis.utils.GenericValidator;
import com.lbis.utils.Utils;

public class User extends ValueObjectAbs<User> implements KeyObjectIfc, SimpleIfc<User>, ListItemAbs {

	private Long userId;
	private String userFirstName;
	private String userLastName;
	private long userBirthday;
	private Enums.Sex userSex;
	private long userJoinDate;
	private String userEmail;
	private Item userProfilePicture;
	private String userPassword;
	private int userSexCode;

	public User() {
		this.userId = null;
	}

	public User(Long userId, String userFirstName, String userLastName, long userBirthday, Sex userSex, long userJoinDate, String userEmail, Item userProfilePicture, String userPassword) {
		this.userId = userId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userBirthday = userBirthday;
		this.userSex = userSex;
		this.userJoinDate = userJoinDate;
		this.userEmail = userEmail;
		this.userProfilePicture = userProfilePicture;
		this.userPassword = userPassword;
		this.userSexCode = userSex.getValue();
	}

	public User(User user) {
		userId = user.userId;
		userFirstName = user.userFirstName;
		userLastName = user.userLastName;
		userBirthday = user.userBirthday;
		userSex = user.userSex;
		userJoinDate = user.userJoinDate;
		userEmail = user.userEmail;
		userProfilePicture = user.userProfilePicture;
		userPassword = user.userPassword;
		userSexCode = user.userSexCode;
	}

	public User(long userId) {
		this.userId = userId;
	}

	public User(String userEmail, String userPassword, String userProfilePicture) {
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	public String fullValidation(boolean isNative) {
		String validatationResult = null;

		if (!GenericValidator.isEmail(userEmail)) {
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "email is not valid");
		}

		if (GenericValidator.isBlankOrNull(userFirstName))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "First name cannot be empty");

		if (GenericValidator.isBlankOrNull(userLastName))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Last name cannot be empty");

		if (!isNative)
			return validatationResult;

		if (userProfilePicture == null) {
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Please select a picture");
		}

		if (userBirthday < 1) {
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Please enter your birthday");
		}

		if (userProfilePicture != null && GenericValidator.isBlankOrNull(userProfilePicture.getItemUrl()))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "Threre's a problem with the picutre,\nplease select another one");

		String passwordCheck = Utils.getInstance().checkPasswordStrength(userPassword);

		if (!GenericValidator.isBlankOrNull(passwordCheck))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, passwordCheck);

		return validatationResult;

	}

	public String partialValidation(boolean isNative) {
		String validatationResult = null;

		if (!GenericValidator.isEmail(userEmail))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, "email is not valid");

		if (!isNative)
			return validatationResult;

		String passwordCheck = Utils.getInstance().checkPasswordStrength(userPassword);

		if (!GenericValidator.isBlankOrNull(passwordCheck))
			validatationResult = Utils.getInstance().formatValidatationResponse(validatationResult, passwordCheck);

		return validatationResult;
	}

	public int getUserSexCode() {
		return userSexCode;
	}

	public void setUserSexCode(int userSexCode) {
		this.userSexCode = userSexCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public long getUserBirthday() {
		return userBirthday;
	}

	public void setUserBirthday(long userBirthday) {
		this.userBirthday = userBirthday;
	}

	public Enums.Sex getUserSex() {
		return userSex;
	}

	public void setUserSex(Enums.Sex userSex) {
		this.userSex = userSex;
	}

	public long getUserJoinDate() {
		return userJoinDate;
	}

	public void setUserJoinDate(long userJoinDate) {
		this.userJoinDate = userJoinDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String welcome() {
		return new StringBuilder().append("Hi, ").append(userFirstName).append("! welcome and mazeltov !").toString();
	}

	@Override
	public Class<User> getClassType() {
		return User.class;
	}

	@Override
	public long getObjectKey() {
		return getUserId();
	}

	@Override
	public Long getId() {
		return this.userId;
	}

	public Item getUserProfilePicture() {
		return this.userProfilePicture == null ? new Item() : this.userProfilePicture;
	}

	public void setUserProfilePicture(Item userProfilePicture) {
		this.userProfilePicture = userProfilePicture;
	}

	@Override
	public String getMainText() {
		return new StringBuilder().append(this.getUserFirstName() != null ? this.getUserFirstName() : "").append(" ").append(this.getUserLastName() != null ? this.getUserLastName() : "").toString();
	}

	@Override
	public String getSubText() {
		return getUserFirstName() + " " + getUserLastName() + Utils.getInstance().calcAge(getUserBirthday()) + " years old " + this.getUserSex() != null ? this.getUserSex().toString() : "";
	}

	@Override
	public String getPicturePath() {
		return this.getUserProfilePicture() != null ? this.getUserProfilePicture().getFormattedUrl() : "http://upload.wikimedia.org/wikipedia/commons/e/ef/Yair_lapid_2010.JPG";
	}

	@Override
	public String toString() {
		return new StringBuilder().append("First Name is " + this.userFirstName == null ? "nothing" : this.userFirstName).append("Last Name is " + this.userLastName == null ? "nothing" : this.userLastName).append("Email is " + this.userEmail == null ? "nothing" : this.userEmail).append("Sex is " + this.userSex == null ? "nothing" : this.userSex).append("User id is " + this.userId == null ? "nothing" : this.userId).toString();
	}

	@Override
	public String getCoolText() {
		return new StringBuilder().append(getMainText()).append(" started following you !").toString();
	}

	@Override
	public Long getClickListenerId() {
		return getUserId();
	}

	@Override
	public Item getItem() {
		return getUserProfilePicture();
	}

	@Override
	public String getSearchText() {
		return "Add " + getUserFirstName() + " to your event !! ";
	}

	@Override
	public String getAddedText() {
		return getUserFirstName() + " will be added !! ";
	}

}