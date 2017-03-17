package main.point.start.handling.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import main.point.start.SFSMain;
import main.point.start.handling.CatchHandling;

public class ListPopulate {
	public static int index = 0;

	public static void pop(String location) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File file = new File(location);
				File[] lof = file.listFiles();
				SFSMain.customName = new String[lof.length];
				SFSMain.origName = new String[lof.length];
				Arrays.fill(SFSMain.customName, "empty");
				Arrays.fill(SFSMain.origName, "empty");
				int x = 0;
				while (x < lof.length) {
					if (lof[x].exists() && lof[x].isFile()) {
						SFSMain.custEnab = true;
						SFSMain.btnCustom.setEnabled(true);
						ListPopulate.addTo(lof[x]);
					}
					++x;
				}
			}
		}).start();
		if (new File(String.valueOf(location) + "bk\\cfg.properties").exists()) {
			SFSMain.btnCusLoad.setEnabled(true);
		}
	}

	public static void update(String text, int x) {
		try {
			if (text != null && text.length() > 0) {
				SFSMain.custom = true;
				if (x == -1) {
					x = SFSMain.tree.getSelectedIndex();
				}
				String extname = SFSMain.origName[x];
				SFSMain.tree.remove(x);
				SFSMain.tree.add(String.valueOf(extname) + " -> " + text, x);
				SFSMain.customName[x] = text;
			}
		} catch (Exception extname) {
			// empty catch block
		}
	}

	public static void load() {
		block15: {
			String filename = String.valueOf(SFSMain.location) + "bk\\cfg.properties";
			File cfg = new File(filename);
			if (cfg.exists() && !cfg.isDirectory()) {
				Properties prop = new Properties();
				FileInputStream input = null;
				try {
					try {
						input = new FileInputStream(cfg);
						prop.load(input);
						int x = 0;
						while (x < index) {
							if (!SFSMain.origName[x].equals("empty") && SFSMain.origName[x].equals(SFSMain.tree.getItem(x))) {
								SFSMain.customName[x] = prop.getProperty(SFSMain.origName[x]);
								ListPopulate.update(SFSMain.customName[x], x);
							}
							++x;
						}
					} catch (Exception e) {
						CatchHandling.error(e);
						if (input == null)
							break block15;
						try {
							input.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static void addTo(File file) {
		String name = file.getName().toString();
		String[] splitName = name.split("\\.");
		if (splitName.length > 1) {
			String ext = splitName[splitName.length - 1].toUpperCase();
			boolean found = false;
			int x = 0;
			while (x < SFSMain.tree.getItemCount()) {
				if (SFSMain.tree.getItem(x).equals(ext)) {
					found = true;
				}
				++x;
			}
			if (!found) {
				SFSMain.customName[ListPopulate.index] = ext;
				SFSMain.origName[ListPopulate.index] = ext;
				SFSMain.tree.add(ext, index);
				++index;
			}
		}
	}

}
