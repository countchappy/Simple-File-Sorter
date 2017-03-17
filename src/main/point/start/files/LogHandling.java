package main.point.start.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import main.point.start.SFSMain;

public class LogHandling {
	public static void log(String msg) {
		try {
			File dir = new File(String.valueOf(SFSMain.location) + "bk\\");
			if (!dir.exists() || !dir.isDirectory()) {
				dir.mkdir();
			}
			File file = new File(String.valueOf(SFSMain.location) + "bk\\log.txt");
			PrintWriter bw = new PrintWriter(new FileWriter(file, true));
			System.out.println(msg);
			bw.println(msg);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			SFSMain.textArea.append("Cannot write to log file \n");
			SFSMain.log = false;
		}
	}
}
