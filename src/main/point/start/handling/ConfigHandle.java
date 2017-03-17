package main.point.start.handling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import main.point.start.SFSMain;

public class ConfigHandle {
	public static void makeProp() {
		block15: {
			if (!new File(String.valueOf(SFSMain.location) + "bk\\cfg.properties").exists()) {
				Properties prop = new Properties();
				OutputStream output = null;
				try {
					try {
						output = new FileOutputStream(String.valueOf(SFSMain.location) + "bk\\cfg.properties");
						int y = 0;
						while (y < SFSMain.customName.length) {
							if (!SFSMain.customName[y].equals("empty")) {
								prop.setProperty(SFSMain.origName[y], SFSMain.customName[y]);
							}
							y++;
						}
						prop.store(output, null);
					} catch (IOException io) {
						io.printStackTrace();
						if (output == null)
							break block15;
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static void delOld() {
		File cfg = new File(String.valueOf(SFSMain.location) + "bk\\cfg.properties");
		if (cfg.exists() && !cfg.isDirectory()) {
			cfg.delete();
		}
	}
}
