package main.point.start.handling.files;

import java.io.File;
import main.point.start.SFSMain;
import main.point.start.files.XMLHandling;
import main.point.start.handling.Append;
import main.point.start.handling.CatchHandling;
import main.point.start.handling.ConfigHandle;
import main.point.start.handling.folder.CheckFolders;

public class CheckFiles {
	public static int lofLength = 0;

	public static void starts(boolean folderMode) {
		File xml = new File(String.valueOf(SFSMain.location) + "bk\\back.xml");
		if (xml.exists() && xml.isFile()) {
			XMLHandling.loadXml();
		} else {
			XMLHandling.createXml();
		}
		XMLHandling.createXml();
		Thread check = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (SFSMain.custom) {
						ConfigHandle.delOld();
						ConfigHandle.makeProp();
					}
					Append.logbegin();
					File[] lof = new File(SFSMain.location).listFiles();
					SFSMain.progressBar.setMaximum(lof.length);
					CheckFiles.lofLength = lof.length;
					SFSMain.lblTotal.setText("0 / " + CheckFiles.lofLength);
					int x = 0;
					while (x < lof.length) {
						OrganizeModeHandling.CopyFile(lof[x]);
						System.gc();
						x++;
					}
					Append.out("All files in " + SFSMain.location + " are now organized by extension");
					SFSMain.progressBar.setValue(SFSMain.progressBar.getMaximum());
					SFSMain.btnFix.setLabel("Close");
					SFSMain.close = true;
					SFSMain.btnFix.setEnabled(true);
					if (!folderMode) {
						Append.logend();
					}
					if (folderMode) {
						CheckFolders.starts(folderMode);
					} else if (SFSMain.xoc) {
						System.exit(0);
					}
				} catch (Exception e) {
					CatchHandling.error(e);
				}
			}
		});
		check.start();
	}

}
