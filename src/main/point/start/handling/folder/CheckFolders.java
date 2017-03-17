package main.point.start.handling.folder;

import java.io.File;
import main.point.start.SFSMain;
import main.point.start.handling.Append;

public class CheckFolders {
	public static void starts(boolean folderMode) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (folderMode) {
					Append.logbegin();
				}
				File dir = new File(SFSMain.location);
				File[] lof = dir.listFiles();
				int c = 0;
				while (c < lof.length) {
					FolderModeHandling.checkFolder(lof[c]);
					c++;
				}
				while (FolderModeHandling.success < lof.length) {
					SFSMain.progressBar.setValue(FolderModeHandling.success);
				}
				Append.out("All empty folders in " + SFSMain.location + " are now deleted");
				Append.logend();
				SFSMain.progressBar.setValue(SFSMain.progressBar.getMaximum());
				SFSMain.btnFix.setLabel("Close");
				SFSMain.close = true;
				SFSMain.btnFix.setEnabled(true);
				if (SFSMain.xoc) {
					System.exit(0);
				}
			}
		}).start();
	}

}
