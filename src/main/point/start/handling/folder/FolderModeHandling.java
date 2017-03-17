package main.point.start.handling.folder;

import java.io.File;

public class FolderModeHandling {
	public static int success = 0;

	public static void checkFolder(File file) {
		if ((file.exists()) && (file.isDirectory())) {
			File[] fileChildren = file.listFiles();
			if (fileChildren.length == 0) {
				main.point.start.handling.Append.out("Empty folder deleted: " + file.getName());
				file.delete();
			}
		}
		success += 1;
	}
}
