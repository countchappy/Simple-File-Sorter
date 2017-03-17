package main.point.start.handling;

import java.text.SimpleDateFormat;
import java.util.Date;
import main.point.start.SFSMain;
import main.point.start.files.LogHandling;

public class Append {
	public static void out(String msg) {
		SFSMain.textArea.append(String.valueOf(msg) + "\n");
		if (SFSMain.log) {
			LogHandling.log(msg);
		}
	}

	public static void logend() {
		if (SFSMain.log) {
			String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
			String time = new SimpleDateFormat("kk:mm:ss").format(new Date());
			String milli = new SimpleDateFormat("SSS").format(new Date());
			LogHandling.log("-------------- Completed on: " + date + " at " + time + " and " + milli + " milliseconds --------------");
		}
	}

	public static void logbegin() {
		if (SFSMain.log) {
			String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
			String time = new SimpleDateFormat("kk:mm:ss").format(new Date());
			String milli = new SimpleDateFormat("SSS").format(new Date());
			LogHandling.log("-------------- Started on: " + date + " at " + time + " and " + milli + " milliseconds --------------");
		}
	}
}
