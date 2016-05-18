package com.lbis.utils;

public class Enums {

	public enum EventType {
		BarMitvah("Bar Mitvah"), Wedding("Wedding"), Hanukkah("Hanukkah"), Passover("Passover"), Purim("Purim"), Christmas("Christmas"), Easter("Easter"), MardiGras("Mardi Gras"), SaintPatricksDay("Saint Patricks Day"), EidalAdha("Eidal Adha"), EidulFitr("Eidul Fitr");
		private String eventTypeValue;

		private EventType(String eventTypeValue) {
			this.eventTypeValue = eventTypeValue;
		}

		public String getEventTypeValue() {
			return eventTypeValue;
		}
	}

	public enum PostType {
		Video, Image
	}

	public enum NotificationType {
		Post(1), Follow(2), Invitation(3);

		private final int id;

		NotificationType(int id) {
			this.id = id;
		}

		public int getValue() {
			return id;
		}
	}

	public enum DBTables {
		Events, Users, Notifications, Posts, Follows, NotificationToInform;
	}

	public enum UsersConnection {
		Follow(1), Following(2), GetFollowers(3);

		private final int id;

		UsersConnection(int id) {
			this.id = id;
		}

		public int getValue() {
			return id;
		}
	}

	public enum Sex {
		Man(1), Woman(2);

		private final int id;

		Sex(int id) {
			this.id = id;
		}

		public int getValue() {
			return id;
		}
	}

	public enum BASIC_COMMANDS {
		BUILD("CREATE TABLE IF NOT EXISTS ", " (k INTEGER PRIMARY KEY, v BLOB)", "Create"), DESTROY("DROP TABLE IF EXISTS ", "", "Destroy");

		private String basicCommandValue;
		private String bottomCommandValue;
		private String commandValueDescription;

		BASIC_COMMANDS(String basicCommandValue, String bottomCommandValue, String commandValueDescription) {
			this.basicCommandValue = basicCommandValue;
			this.bottomCommandValue = bottomCommandValue;
			this.commandValueDescription = commandValueDescription;
		}

		public String getBasicCommandValue() {
			return basicCommandValue;
		}

		public String getBottomCommandValue() {
			return bottomCommandValue;
		}

		public String getCommandValueDescription() {
			return commandValueDescription;
		}
	}

	public enum MANAGEMENT_BASIC_COMMANDS {

		BUILD("CREATE TABLE IF NOT EXISTS Management (userId INTEGER PRIMARY KEY, tokenValue BLOB)", "Create"), DESTROY("DROP TABLE IF EXISTS Management (userId INTEGER PRIMARY KEY, tokenValue BLOB)", "Destroy");

		private String command;
		private String description;

		MANAGEMENT_BASIC_COMMANDS(String command, String description) {
			this.command = command;
			this.description = description;
		}

		public String getCommandValue() {
			return command;
		}

		public String getCommandDescription() {
			return description;
		}
	}

	public enum URLs {
		ProfilePictureBase("http://lbis.dyndns.org/MyProjects/MichaelTest/profiles_numbers/"), SignUp("http://legacy.lbisgroup.com/MazelTov/WebDev/mtreg.php"), LogIn("http://legacy.lbisgroup.com/MazelTov/WebDev/mtlogin.php"), Test("http://legacy.lbisgroup.com/MazelTov/WebDev/postWS.php"), CreateNewEvent("http://legacy.lbisgroup.com/MazelTov/WebDev/mtcrevt.php"), UploadUrl("http://legacy.lbisgroup.com/MazelTov/WebDev/mtupload.php"), EventSinceUrl("http://legacy.lbisgroup.com/MazelTov/WebDev/mtupcoming.php"), UserSearchUrl("http://legacy.lbisgroup.com/MazelTov/WebDev/mtusearch.php"), UserObjectForObjectUrl("http://legacy.lbisgroup.com/MazelTov/WebDev/mtgtusr.php"), MediaPrefixURL("http://legacy.lbisgroup.com/MazelTov/storage/"), GetEvent("http://legacy.lbisgroup.com/MazelTov/WebDev/mtgtevt.php"), GetCoolFeed("http://legacy.lbisgroup.com/MazelTov/WebDev/mtfeed.php"), RegisterPush("http://legacy.lbisgroup.com/MazelTov/WebDev/mtpush.php"), ShareNewPost(
				"http://legacy.lbisgroup.com/MazelTov/WebDev/mtcrpost.php"), FuckIWantTheNotifications("http://legacy.lbisgroup.com/MazelTov/WebDev/mtnotifications.php"), FollowURL("http://legacy.lbisgroup.com/MazelTov/WebDev/mtfollow.php"), GetAllFollows("http://legacy.lbisgroup.com/MazelTov/WebDev/mtgtfollowers.php"), DefalutNoLoadedPicture("http://www.mazeltov.com/hp/images/hp-icon-celebrate.png");
		private final String url;

		URLs(String url) {
			this.url = url;
		}

		public String getValue() {
			return url;
		}
	}

	public enum GCM {
		GCMSenderId("57223510654");
		private final String value;

		GCM(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum RequestEndStatus {
		SUCCESS, FAILURE
	}

	public enum AsyncResult {
		SUCCESS, FAILURE
	}

	public enum ContentTypes {
		PNG, JPG, AVI, MP4
	}

	public enum GoogleMapsAPI {
		APIURL("https://maps.googleapis.com/maps/api/place/queryautocomplete/json?sensor=false&key=AIzaSyA25COcV0MTrDxOgsmVkHA_8-qeRbOVjQc&input=");
		String googleMapsAPI;

		private GoogleMapsAPI(String googleMapsAPI) {
			this.googleMapsAPI = googleMapsAPI;
		}

		public String getGoogleMapsAPI() {
			return googleMapsAPI;
		}

	}
}
