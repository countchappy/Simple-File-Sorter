package main.point.start.handling.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import main.point.start.SFSMain;
import main.point.start.files.XMLHandling;
import main.point.start.handling.Append;
import main.point.start.handling.CatchHandling;

public class OrganizeModeHandling {
	public static int success = 0;
	private static int amountCopied = 0;

	public static void CopyFile(File file) {
		if (file.isFile() && !file.isDirectory()) {
			String name = file.getName().toString();
			Append.out("File loaded and copying: " + name);
			String[] s = name.split("\\.");
			if (s.length > 1) {
				String orig = s[s.length - 1];
				String extname = orig.toUpperCase();
				if (SFSMain.simple) {
					String simpleName = name.replace("." + orig, "");
					name = String.valueOf(RenameHandler.renameSimple(simpleName)) + "." + orig;
				}
				if (SFSMain.custom) {
					int y = 0;
					while (y < ListPopulate.index) {
						if (!SFSMain.customName[y].equals("empty") && SFSMain.origName[y].equals(extname)) {
							extname = SFSMain.customName[y];
						}
						++y;
					}
				}
				amountCopied = 0;
				SFSMain.progressCurrent.setValue(0);
				int fileSize = (int) (file.length() / 1024);
				SFSMain.progressCurrent.setMaximum(fileSize);
				File newdir = new File(String.valueOf(SFSMain.location) + extname + "\\");
				if (!newdir.exists() && !newdir.isDirectory()) {
					Append.out("New directory made: " + extname);
					newdir.mkdir();
					XMLHandling.addXml("NULL", "NULL", newdir.getPath());
				}
				File newFile = new File(String.valueOf(SFSMain.location) + extname + "\\" + name);
				try {
					InputStream input = null;
					OutputStream output = null;
					try {
						int bytesRead;
						input = new FileInputStream(file);
						output = new FileOutputStream(newFile);
						byte[] buf = new byte[1024];
						while ((bytesRead = input.read(buf)) > 0) {
							output.write(buf, 0, bytesRead);
							SFSMain.progressCurrent.setValue(++amountCopied);
						}
					} finally {
						input.close();
						output.close();
					}
					XMLHandling.addXml(newFile.getAbsolutePath(), file.getAbsolutePath(), "NULL");
					file.delete();
				} catch (IOException e) {
					Append.out("File failed to copy!: " + name);
					CatchHandling.error(e);
				}
				if (newFile.length() == file.length()) {
					file.delete();
				}
			}
		}
		SFSMain.progressBar.setValue(++success);
		SFSMain.lblTotal.setText(String.valueOf(success) + " / " + CheckFiles.lofLength);
	}
}
