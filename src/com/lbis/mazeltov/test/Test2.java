package com.lbis.mazeltov.test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;

import com.lbis.server.actions.ItemActions;
import com.lbis.utils.Enums.ContentTypes;

public class Test2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_splash);
		try {
			final String TESTSTRING = new String("HONEY I'M HOMEEE, Yeah baby !!!!");
			FileOutputStream fOut = openFileOutput("smallFile1.txt", MODE_WORLD_WRITEABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			for (int j = 0; j < 600; j++)
				osw.write(TESTSTRING + TESTSTRING + TESTSTRING + TESTSTRING + TESTSTRING);
			osw.flush();
			osw.close();
			// FileInputStream fIn = openFileInput("smallFile.txt");
			// InputStreamReader isr = new InputStreamReader(fIn);
			// char[] inputBuffer = new char[TESTSTRING.length()];
			// isr.read(inputBuffer);
			// String readString = new String(inputBuffer);
			// boolean isTheSame = TESTSTRING.equals(readString);
			// Log.i("File Reading stuff", "success = " + isTheSame);
			// File file = new File(getFilesDir() +"/abcdefg1.txt");
			// File file = new
			// File("/mnt/sdcard/DCIM/Camera/VID_20131015_212650.mp4");
			// String response = Uploader.postFile(getFilesDir()
			// +"/VID_20131015_212650.mp4", "sdasda", "asdasda", "sdasda");
			// String response = Uploader.postFile(getFilesDir()
			// +"/smallFile.txt", "sdasda", "asdasda", "sdasda");
			// String response =
			// Uploader.postFile("/mnt/sdcard/DCIM/Camera/IMG_20131017_232849.jpg",
			// "sdasda", "asdasda", "sdasda");
			// response = response +"";
			// new
			// Upload1().uploadFile("/mnt/sdcard/DCIM/Camera/VID_20131015_212650.mp4","picture");
			// new Upload1().uploadFile(getFilesDir()+"/smallFile.txt",
			// "test1111");
			// ServerCommunication ss = new ServerCommunication();
			// response = response;
			// System.out.print(response);
			// ss.sendFile(file);
			// User user = new User
			// (null,"michael2","assraf",1320969600000L,Sex.Man, 1320969600000L,
			// "michael@michael.com",new Item(2222222L, "dsadsa",
			// Enums.ContentTypes.PNG, 2222222L),"Aa123456");
			// user =new UserActions().logIn(user,
			// WebReciever.getInstance().getConnection()
			// ,getApplicationContext());
			// user.getClassType();

			// class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
			// protected Void doInBackground(Void... urls) {
			// LinkedList<Event> ss;
			// try {
			// ss = new EventActions().getAllObjectsSince(315525600L,
			// WebReciever.getInstance().getConnection(),
			// getApplicationContext());
			//
			// Log.i("event", ss.get(1).getEventDescription());
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// return null;
			// }
			//
			// protected void onProgressUpdate(Void... progress) {
			// }
			//
			// protected void onPostExecute(Long result) {
			// }
			// }

			// new DownloadFilesTask().execute();
			// sLog.i("event", ss.get(1).getEventDescription());
			// new ItemActions().storeUpdatedServerMeta(getApplicationContext(),
			// new ServerMeta(new Token("2c834af7fce6623dddec016fbd90a962",
			// 13810055104469089L)));

			new Thread(new Runnable() {
				public void run() {
					try {
						new ItemActions().putFile(getApplicationContext(), 1122334455L, ContentTypes.AVI, "smallFile1.txt");
						// LinkedList<Event> events;
						// events = new EventActions().getAllObjectsSince(System
						// .currentTimeMillis(), WebReciever.getInstance()
						// .getConnection(), getApplicationContext());
						// Event ss = events.get(0);
						// Log.i("event", events.get(1).toString());
						// Log.i("event", events.get(2).toString());
						// Log.i("event", events.get(3).toString());
						// Log.i("event",events.get(4).toString());
						// ss.getEventId();
						// } catch (Exception e) {
						// String gg = e.getLocalizedMessage();
						// Log.i("JSON", gg);
						// }
					} catch (Exception e) {

					}
				}
			}).start();

			// new
			// HttpMultipartPost(this).execute("/mnt/sdcard/DCIM/Camera/VID_20131015_212650.mp4");
			// Intent intent = new Intent (getApplicationContext(),
			// VideoCaptureActivity.class);
			// startActivity(intent);
			// finish();
		} catch (Exception e) {
			String sss = e.getMessage();
			System.out.print(sss);
		}

	}

}
