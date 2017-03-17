package main.point.start;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import main.point.start.files.XMLHandling;
import main.point.start.handling.ArgHandling;
import main.point.start.handling.CatchHandling;
import main.point.start.handling.files.CheckFiles;
import main.point.start.handling.files.ListPopulate;
import main.point.start.handling.files.RenameHandler;
import main.point.start.handling.folder.CheckFolders;

public class SFSMain {
	public static JFrame frmSorting;
	public static TextField textLocation;
	public static TextField textRename;
	public static Button btnAdvSettings;
	public static Button btnFix;
	public static Button btnUndo;
	public static Button btnCustom;
	public static Button btnCusLoad;
	public static Button btnRename;
	public static Button selectLocation;
	public static Checkbox chkSetLog;
	public static Checkbox chkSetSimple;
	public static Checkbox chkSetFolder;
	public static JProgressBar progressBar;
	public static JProgressBar progressCurrent;
	public static Label label;
	public static Label lblCustom;
	public static Label lblLocation;
	public static Label lblSimpleName;
	public static List tree;
	public static String location;
	public static TextArea textArea;
	public static boolean close;
	public static boolean folder;
	public static boolean log;
	public static boolean simple;
	public static boolean xoc;
	public static boolean custom;
	public static boolean custEnab;
	public static boolean folderOnly;
	public static String[] customName;
	public static String[] origName;
	private static boolean adv;
	private static Panel setMenu;
	private static Panel panel;
	private static Panel panelCustom;
	private Checkbox chkFolderOnly;
	public static JLabel lblTotal;

	static {
		close = false;
		folder = false;
		log = false;
		simple = false;
		xoc = false;
		custom = false;
		custEnab = false;
		folderOnly = false;
		adv = false;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					SFSMain window = new SFSMain();
					window.initialize(args);
					SFSMain.frmSorting.setVisible(true);
					SFSMain.selectLocation.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void initialize(String[] args) {
		frmSorting = new JFrame();
		frmSorting.setResizable(false);
		frmSorting.setTitle("Simple File Sorter");
		frmSorting.setIconImage(Toolkit.getDefaultToolkit().getImage(SFSMain.class.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));
		frmSorting.setBounds(100, 100, 550, 425);
		frmSorting.setLocationRelativeTo(null);
		frmSorting.setDefaultCloseOperation(3);
		frmSorting.getContentPane().setLayout(null);
		setMenu = new Panel();
		setMenu.setVisible(false);
		panel = new Panel();
		panel.setLayout(null);
		panel.setBounds(10, 90, 524, 296);
		frmSorting.getContentPane().add(panel);
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(0, 0, 524, 244);
		panel.add(textArea);
		JLabel lblPrgTotal = new JLabel("Total:");
		lblPrgTotal.setHorizontalAlignment(4);
		lblPrgTotal.setBounds(10, 250, 76, 21);
		panel.add(lblPrgTotal);
		JLabel lblPrgThisFile = new JLabel("Current File:");
		lblPrgThisFile.setHorizontalAlignment(4);
		lblPrgThisFile.setBounds(10, 275, 76, 21);
		panel.add(lblPrgThisFile);
		lblTotal = new JLabel("");
		lblTotal.setHorizontalAlignment(0);
		lblTotal.setBounds(96, 250, 428, 21);
		panel.add(lblTotal);
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(244, 164, 96));
		progressBar.setBounds(96, 250, 428, 21);
		panel.add(progressBar);
		progressCurrent = new JProgressBar();
		progressCurrent.setBounds(96, 275, 428, 21);
		panel.add(progressCurrent);
		setMenu.setBounds(10, 90, 524, 296);
		frmSorting.getContentPane().add(setMenu);
		setMenu.setLayout(null);
		panelCustom = new Panel();
		panelCustom.setBounds(10, 90, 524, 296);
		panelCustom.setLayout(null);
		panelCustom.setVisible(false);
		frmSorting.getContentPane().add(panelCustom);
		btnFix = new Button("Start Sorting");
		btnFix.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (SFSMain.close) {
					System.exit(0);
				} else {
					SFSMain.btnAdvSettings.setEnabled(false);
					SFSMain.btnFix.setEnabled(false);
					SFSMain.btnCustom.setEnabled(false);
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (Exception e1) {
						CatchHandling.error(e1);
					}
					if (SFSMain.folderOnly) {
						CheckFolders.starts(false);
					} else {
						CheckFiles.starts(SFSMain.folder);
					}
				}
			}
		});
		btnFix.setBounds(10, 34, 524, 22);
		btnFix.setEnabled(false);
		frmSorting.getContentPane().add(btnFix);
		selectLocation = new Button("...");
		selectLocation.setBounds(503, 8, 31, 20);
		selectLocation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Select Folder to Organize");
				fc.setFileSelectionMode(1);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setApproveButtonText("Select");
				fc.showOpenDialog(SFSMain.frmSorting);
				if (fc.getSelectedFile() != null && (new File(SFSMain.location = String.valueOf(fc.getSelectedFile().toString()) + "\\").exists() || new File(SFSMain.location).isDirectory())) {
					XMLHandling.CheckDir();
					ListPopulate.pop(SFSMain.location);
					SFSMain.textLocation.setText(SFSMain.location);
					SFSMain.textArea.append("Location to organize set to: " + SFSMain.location + "\n");
					SFSMain.advanced(true);
					SFSMain.btnFix.setEnabled(adv);
				}
			}
		});
		frmSorting.getContentPane().add(selectLocation);
		btnAdvSettings = new Button("Show Advanced Settings");
		btnAdvSettings.setBounds(10, 62, 259, 22);
		btnAdvSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(setMenu.isVisible());
				SFSMain.btnFix.setEnabled(setMenu.isVisible());
				SFSMain.btnCustom.setEnabled(setMenu.isVisible() && SFSMain.custEnab);
				setMenu.setVisible(!setMenu.isVisible());
				if (setMenu.isVisible()) {
					SFSMain.btnAdvSettings.setLabel("Hide Advanced Settings");
				} else {
					SFSMain.btnAdvSettings.setLabel("Show Advanced Settings");
				}
			}
		});
		frmSorting.getContentPane().add(btnAdvSettings);
		btnCustom = new Button("Custom Sorting Options");
		btnCustom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(panelCustom.isVisible());
				SFSMain.btnFix.setEnabled(panelCustom.isVisible());
				SFSMain.btnAdvSettings.setEnabled(panelCustom.isVisible());
				panelCustom.setVisible(!panelCustom.isVisible());
			}
		});
		btnCustom.setEnabled(false);
		btnCustom.setBounds(275, 62, 259, 22);
		frmSorting.getContentPane().add(btnCustom);
		btnRename = new Button("Rename Selected Item To ...");
		btnRename.setBounds(350, 0, 174, 22);
		btnRename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = SFSMain.textRename.getText().trim();
				ListPopulate.update(text, -1);
			}
		});
		panelCustom.add(btnRename);
		btnCusLoad = new Button("Load the Saved Custom Settings");
		btnCusLoad.setBounds(0, 274, 524, 22);
		btnCusLoad.setEnabled(false);
		btnCusLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ListPopulate.load();
			}
		});
		panelCustom.add(btnCusLoad);
		btnUndo = new Button("Undo Organization");
		btnUndo.setEnabled(false);
		btnUndo.setBounds(10, 10, 504, 22);
		btnUndo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SFSMain.btnAdvSettings.setEnabled(false);
				SFSMain.btnCustom.setEnabled(false);
				setMenu.setVisible(false);
				panel.setVisible(true);
				SFSMain.btnAdvSettings.setLabel("Show Advanced Settings");
				XMLHandling.readXml();
			}
		});
		setMenu.add(btnUndo);
		chkSetLog = new Checkbox("Log Messages and Errors to Text File");
		chkSetLog.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				SFSMain.log = SFSMain.chkSetLog.getState();
			}
		});
		chkSetLog.setBounds(10, 95, 504, 22);
		setMenu.add(chkSetLog);
		chkSetSimple = new Checkbox("Rename Files to a more Simple Name");
		chkSetSimple.setBounds(10, 123, 504, 22);
		chkSetSimple.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				SFSMain.simple = SFSMain.chkSetSimple.getState();
				if (SFSMain.simple) {
					SFSMain.lblSimpleName.setText("'app_install2.175x.exe' will become '" + RenameHandler.renameSimple("app_install2.175x") + ".exe'");
				} else {
					SFSMain.lblSimpleName.setText("'app_install2.175x.exe' will become 'app_install2.175x.exe'");
				}
			}
		});
		setMenu.add(chkSetSimple);
		chkSetFolder = new Checkbox("Delete Empty Folders");
		chkSetFolder.setBounds(10, 38, 504, 22);
		chkSetFolder.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				SFSMain.folder = SFSMain.chkSetFolder.getState();
				SFSMain.this.chkFolderOnly.setEnabled(SFSMain.folder);
				SFSMain.this.chkFolderOnly.setState(false);
			}
		});
		setMenu.add(chkSetFolder);
		textLocation = new TextField();
		textLocation.setEditable(false);
		textLocation.setBounds(141, 8, 356, 20);
		frmSorting.getContentPane().add(textLocation);
		textLocation.setColumns(10);
		textRename = new TextField();
		textRename.setBounds(0, 0, 342, 22);
		panelCustom.add(textRename);
		lblCustom = new Label("Change the name of the directory where the files with corresponding extensions will be sorted");
		lblCustom.setAlignment(1);
		lblCustom.setBounds(0, 25, 524, 22);
		panelCustom.add(lblCustom);
		lblLocation = new Label("Location to Organize:");
		lblLocation.setBounds(10, 11, 121, 14);
		frmSorting.getContentPane().add(lblLocation);
		tree = new List();
		tree.setBounds(0, 56, 524, 209);
		panelCustom.add(tree);
		label = new Label("Settings Have Been Disabled due to Some Launch Arguments");
		label.setAlignment(1);
		label.setBounds(10, 264, 504, 22);
		label.setVisible(false);
		setMenu.add(label);
		lblSimpleName = new Label("'app_install2.175x.exe' will become 'app_install2.175x.exe'");
		lblSimpleName.setForeground(new Color(153, 153, 153));
		lblSimpleName.setAlignment(1);
		lblSimpleName.setBounds(10, 151, 504, 22);
		setMenu.add(lblSimpleName);
		this.chkFolderOnly = new Checkbox("Only Delete Empty Folders and Do Not Organize");
		this.chkFolderOnly.setEnabled(false);
		this.chkFolderOnly.setBounds(56, 66, 458, 22);
		this.chkFolderOnly.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				SFSMain.folderOnly = SFSMain.this.chkFolderOnly.getState();
			}
		});
		setMenu.add(this.chkFolderOnly);
		ArgHandling.handle(args);
	}

	static void advanced(boolean bl) {
		adv = bl;
	}

}
