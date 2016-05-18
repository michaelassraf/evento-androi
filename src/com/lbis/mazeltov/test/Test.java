package com.lbis.mazeltov.test;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;

import com.lbis.concurrency.ConcurrencyUtils;
import com.lbis.database.executors.model.PostsDbExecutors;
import com.lbis.database.executors.model.UserDbExecutors;
import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.model.Item;
import com.lbis.model.Notification;
import com.lbis.model.Post;
import com.lbis.model.User;
import com.lbis.utils.Enums.ContentTypes;
import com.lbis.utils.Enums.Sex;

public class Test extends Activity {

	Event ss;
	Notification kk;
	Post jj;
	Event ssss;
	LinkedList<Event> events1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// Event event = new Event(46546, Enums.EventType.BarMitvah, new User(),
		// "dsadas", "asdasdas", 45664645, 46466546);
		// Notification noti = new Notification(4546,
		// Enums.NotificationType.FollwoRequest, 45646);

		try {
			// new
			// SQLiteMethods(getApplicationContext()).basicExcution(BASIC_COMMANDS.BUILD,
			// SQLiteClient.getConnetion(getApplicationContext()));
			// // Post po = new Post(464, "dsadfa", 4646, "dasdas", new
			// // URL("http://www.sss.com"), Enums.PostType.Video, 5513213,
			// 4654);
			// // String dd = new EventActions().createNewRequest(event,
			// // WebReciever.getInstance().getConnection());
			// // ss = new EventActions().getObjectForObject(event,
			// // WebReciever.getInstance().getConnection());
			// // kk = new NotificationActions().getObjectForObject(noti,
			// // WebReciever.getInstance().getConnection());
			// // jj = new PostActions().getObjectForObject(po,
			// // WebReciever.getInstance().getConnection());
			//
			// LinkedList<Event> events = new LinkedList<Event>();
			//
			// int j = 1;
			//
			// for (int i = 0; i < 2000; i++) {
			// if (j == 5)
			// j = 1;
			// else
			// j = j + 1;
			// // Event elvt1 = new
			// Event((long)i,EventType.Wedding,(long)(i+10),""+Math.random()
			// *1000+"","http://lbis.dyndns.org/MyProjects/MichaelTest/" +
			// j+".jpg","Yeeahh Hatuna !! "
			// Event elvt = new
			// Event((long)i,EventType.Wedding,(long)(i+10),"name "+i,new
			// Item((long)i,"http://lbis.dyndns.org/MyProjects/MichaelTest/"+j,
			// ContentTypes.JPG, (long)i),"chibi",
			// Calendar.getInstance().getTimeInMillis()+ i*
			// 100000000,Calendar.getInstance().getTimeInMillis()+ i*
			// 100000000);
			// events.add(elvt);
			// }

			// new EventDbExecutors().putAll(getApplicationContext(), events);
			//
			// events1 = new EventDbExecutors().getAll(events.getFirst(),
			// getApplicationContext());
			//
			// int i = events.size();
			//
			// ssss = events1.get(5);
			//
			LinkedList<Post> posts = new LinkedList<Post>();

			// for (int i1 = 0; i1 < 2000; i1++) {
			// Post post1 = new Post(
			// i1,
			// i1,
			// "Michael",
			// "Zibi event",
			// 2,
			// "Amit's Bar Mitzvah",
			// new Item(
			// (long) i1,
			// "http://lbis.dyndns.org/MyProjects/MichaelTest/1",
			// ContentTypes.JPG, (long) i1),
			// Enums.PostType.Image, 1378425600, 1);
			// i1 = i1 + 1;
			// Post post2 = new Post(
			// i1,
			// i1,
			// "Yossi",
			// "Zibi event",
			// 1,
			// "Rosh Hashana Eve.",
			// new Item(
			// (long) i1,
			// "http://lbis.dyndns.org/MyProjects/MichaelTest/2",
			// ContentTypes.JPG, (long) i1),
			// Enums.PostType.Image, 1378339200, 1);
			// i1 = i1 + 1;
			// Post post3 = new Post(
			// i1,
			// i1,
			// "Roi",
			// "Zibi event",
			// 3,
			// "Marina's Bat Mitzvah",
			// new Item(
			// (long) i1,
			// "http://lbis.dyndns.org/MyProjects/MichaelTest/3",
			// ContentTypes.JPG, (long) i1),
			// Enums.PostType.Image, 1378252800, 1);
			// i1 = i1 + 1;
			// Post post4 = new Post(
			// i1,
			// i1,
			// "Yehuda",
			// "Zibi event",
			// 4,
			// "Yossi's Birthday",
			// new Item(
			// (long) i1,
			// "http://lbis.dyndns.org/MyProjects/MichaelTest/4",
			// ContentTypes.JPG, (long) i1),
			// Enums.PostType.Image, 1378166400, 1);
			// i1 = i1 + 1;
			// Post post5 = new Post(
			// i1,
			// i1,
			// "Michael",
			// "Zibi event",
			// 2,
			// "Roi's Wedding",
			// new Item(
			// (long) i1,
			// "http://lbis.dyndns.org/MyProjects/MichaelTest/5",
			// ContentTypes.JPG, (long) i1),
			// Enums.PostType.Image, 1378080000, 1);
			// i1 = i1 + 1;
			// posts.add(post1);
			// posts.add(post2);
			// posts.add(post3);
			// posts.add(post4);
			// posts.add(post5);
			// }

			new PostsDbExecutors().putAll(getApplicationContext(), posts);

			LinkedList<User> users = new LinkedList<User>();
			for (int i1 = 0; i1 < 2000; i1++) {
				users.add(new User((long) i1, "sss" + i1, "bbb" + i1, i1, Sex.Woman, i1, "sdasd@sss" + i1 + ".com", new Item(ContentTypes.AVI, i1), "sdasdasdad"));
			}

			new UserDbExecutors().putAll(getApplicationContext(), users);

			//
			// // LinkedList<Event> events12 = new
			// EventActions().getAllObjectsSince( new
			// // Events(), new Event(), new User(), 0, WebReciever
			// // .getInstance().getConnection());
			//
			// // LinkedList<Event> eventLL = events12.getObjects();
			// //
			// // Event eee = eventLL.get(4);
			//
			// User user = new User(null, "chibi", "chubi", 2313, Sex.Man,
			// 54646,
			// "ch6i@jl2kk.com", new Item(0, null, null, 0),
			// "sadAa111sdas");
			// //
			// // User ss = new UserActions().getObjectForObject(user,
			// WebReciever
			// // .getInstance().getConnection()).getData();
			// //
			// ServerSingleWrapper<User> sss = new
			// UserActions().getObjectForObject(new UserServerWrapper(user,new
			// ExecuteManagementMethods()
			// .getTokenAndUserId(getApplicationContext())), WebReciever
			// .getInstance().getConnection());

			//
			// new ExecuteManagementMethods()
			// .getTokenAndUserId(getApplicationContext());
			// Token token = new Token("2c834af7fce6623dddec016fbd90a962",
			// 13810055104469089L);
			// new ExecuteManagementMethods().setTokenAndUserId(
			// getApplicationContext(), new ServerMeta(token));
			// Token token2 = new ExecuteManagementMethods()
			// .getTokenAndUserId(getApplicationContext());
			//
			// Event ss = new Event(2, EventType.BarMitvah,
			// (long) new ExecuteManagementMethods().getTokenAndUserId(
			// getApplicationContext()).getUserId(), "chiuv",
			// new Item(2L, "sdasda", null, i), "sdasda", 238182, 2381822);
			//
			// Event mm = new EventActions()
			// .createNewRequest(
			// new EventServerWrapper(
			// new ServerMeta(
			// new ExecuteManagementMethods()
			// .getTokenAndUserId(getApplicationContext())),
			// ss),
			// WebReciever.getInstance().getConnection())
			// .getData();

			/*
			 * final String TESTSTRING = new String("HONEY I'M HOMEEE, Yeah baby !!!!"); FileOutputStream fOut = openFileOutput("samplefile4.txt",MODE_WORLD_WRITEABLE); OutputStreamWriter osw = new OutputStreamWriter(fOut); osw.write(TESTSTRING); osw.flush(); osw.close(); FileInputStream fIn = openFileInput("samplefile4.txt"); InputStreamReader isr = new InputStreamReader(fIn); char[] inputBuffer = new char[TESTSTRING.length()]; isr.read(inputBuffer); String readString = new String(inputBuffer); boolean isTheSame = TESTSTRING.equals(readString); Log.i("File Reading stuff", "success = " + isTheSame); File file = new File(getFilesDir() +"/samplefile4.txt");
			 * 
			 * new S3ExecuteMethods().put(file, "YEEAHH BABAY IT'S HIM !", this);
			 */

			ConcurrencyUtils.getInstance().updateAllCache(0, getApplicationContext());
			// ExecutorService es = Executors.newSingleThreadExecutor();
			// Future<?> result = es.submit(new Runnable() {
			// @Override
			// public void run() {
			//
			// }
			// });

			// try {
			// // Integer i = (Integer) result.get();
			// System.out.print(i);
			// i = i;
			// } catch (ExecutionException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}