package main.point.start.handling;

import java.io.File;
import java.util.concurrent.TimeUnit;
import main.point.start.SFSMain;
import main.point.start.files.XMLHandling;
import main.point.start.handling.files.CheckFiles;
import main.point.start.handling.files.RenameHandler;

public class ArgHandling {
	public static boolean printEr = false;
	private static boolean locked = true;

	public static void handle(String[] args) {
		if (args.length > 0) {
			int l = 0;
			while (l < args.length) {
				if (args[l].toUpperCase().equals("-P")) {
					printEr = true;
				}
				if (args[l].toUpperCase().equals("-L")) {
					if (locked) {
						ArgHandling.lock();
					}
					SFSMain.log = true;
					SFSMain.chkSetLog.setState(SFSMain.log);
				}
				if (args[l].toUpperCase().equals("-S")) {
					if (locked) {
						ArgHandling.lock();
					}
					SFSMain.simple = true;
					SFSMain.chkSetSimple.setState(SFSMain.simple);
					SFSMain.lblSimpleName.setText("'app_install2.175x.exe' will become '" + RenameHandler.renameSimple("app_install2.175x") + ".exe'");
				}
				if (args[l].toUpperCase().equals("-F")) {
					if (locked) {
						ArgHandling.lock();
					}
					SFSMain.folder = true;
					SFSMain.chkSetFolder.setState(SFSMain.folder);
				}
				if (args[l].toUpperCase().equals("-X")) {
					SFSMain.xoc = true;
				}
				if (args[l].toUpperCase().equals("-H")) {
					SFSMain.frmSorting.setVisible(false);
					System.out.println("SimpleFileSorter help message: ");
					System.out.println("Arguments  -  What they do");
					System.out.println("       -H  -  Displays the help messages");
					System.out.println("       -U  -  Unlocks settings locked by command line arguments after startup");
					System.out.println("       -X  -  Exits the program after sorting is complete");
					System.out.println("       -P  -  Print out errors through the console (Debugging)");
					System.out.println("       -D  -  Set the directory to sort on start");
					System.out.println("     The following settings will be locked after startup without -U");
					System.out.println("       -F  -  Sets the \"Delete Empty Folders Mode\" to True");
					System.out.println("       -S  -  Sets the \"Simple File Renaming Mode\" to True");
					System.out.println("       -L  -  Sets the \"Log Output to File\" to True");
					System.out.println("End of help message");
					System.exit(0);
				}
				if (args[l].toUpperCase().equals("-U")) {
					locked = false;
					SFSMain.label.setVisible(false);
					SFSMain.chkSetFolder.setEnabled(true);
					SFSMain.chkSetLog.setEnabled(true);
					SFSMain.chkSetSimple.setEnabled(true);
				}
				if (args[l].toUpperCase().equals("-D")) {
					SFSMain.location = args[l + 1];
					if (!SFSMain.location.endsWith("\\")) {
						SFSMain.location = String.valueOf(SFSMain.location) + "\\";
					}
					File file = new File(SFSMain.location);
					SFSMain.textLocation.setText(SFSMain.location);
					SFSMain.textArea.append("Location to organize set to: " + SFSMain.location + "\n");
					if (!file.exists() && !file.isDirectory()) {
						System.out.println("Invalid filepath: " + SFSMain.location);
						System.exit(0);
					}
					SFSMain.textLocation.setEnabled(false);
					SFSMain.btnFix.setEnabled(false);
					SFSMain.btnAdvSettings.setEnabled(false);
					SFSMain.btnCusLoad.setEnabled(false);
					SFSMain.btnCustom.setEnabled(false);
					XMLHandling.createXml();
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (Exception e1) {
						CatchHandling.error(e1);
					}
					CheckFiles.starts(SFSMain.folder);
				}
				l++;
			}
		}
	}

	private static void lock() {
		SFSMain.chkSetLog.setEnabled(false);
		SFSMain.chkSetSimple.setEnabled(false);
		SFSMain.chkSetFolder.setEnabled(false);
		SFSMain.label.setVisible(true);
	}
}
