package main.point.start.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import main.point.start.SFSMain;
import main.point.start.handling.Append;
import main.point.start.handling.CatchHandling;

public class XMLHandling {
	public XMLHandling() {
	}

	public static boolean isready = false;
	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;
	private static Document doc;
	private static String filepath = SFSMain.location + "bk\\back.xml";
	private static boolean called = false;
	private static int fileNum = 0;

	public static void createXml() {
		File dir = new File(SFSMain.location + "bk\\");
		dir.mkdir();
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("Organizer");
			doc.appendChild(rootElement);

			Element files = doc.createElement("folder");
			rootElement.appendChild(files);

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);

			isready = true;
		} catch (Exception e) {
			CatchHandling.error(e);
		}
	}

	public static void loadXml() {
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Document doc = docBuilder.parse(filepath);
			Node files = doc.getElementsByTagName("folder").item(0);
			NodeList fileList = files.getChildNodes();
			String nn = null;
			for (int x = 0; x < fileList.getLength(); x++) {
				nn = fileList.item(x).getNodeName();
				if (nn.startsWith("file")) {
					fileNum += 1;
				}
			}
		} catch (Exception e) {
			CatchHandling.error(e);
		}
	}

	public static void addXml(String newFile, String oldFile, String dirname) {
		try {
			Node files = doc.getElementsByTagName("folder").item(0);

			Element file = doc.createElement("file" + fileNum);
			fileNum += 1;
			file.setAttribute("old", oldFile);
			file.setAttribute("new", newFile);
			file.setAttribute("name", dirname);
			files.appendChild(file);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (Exception e) {
			if (!called) {
				CatchHandling.error(e);
				called = true;
			}
		}
	}

	private static int completed = 0;
	private static int pathPlace = 0;
	private static int amountCopied = 0;
	private static int progMax = 0;

	public static void readXml() {
		try {
			Append.logbegin();
			SFSMain.progressBar.setVisible(true);
			SFSMain.progressBar.setValue(0);
			File fXmlFile = new File(SFSMain.location + "bk\\back.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			Append.out("XML File loaded from backup");
			Node files = doc.getElementsByTagName("folder").item(0);

			NodeList fileList = files.getChildNodes();

			final String[] dirPaths = new String[fileList.getLength()];
			java.util.Arrays.fill(dirPaths, "NULL");
			pathPlace = 0;

			for (int x = 0; x < fileList.getLength(); x++) {
				String nodeName = fileList.item(x).getNodeName();
				if (nodeName.startsWith("file")) {
					progMax += 1;
				}
			}
			SFSMain.progressBar.setMaximum(progMax);
			SFSMain.lblTotal.setText(completed + " / " + progMax);

			new Thread(new Runnable() {
				public void run() {
					for (int re = 0; re < 1; re++) {
						for (int c = 1; c < fileList.getLength(); c++) {
							String nn = fileList.item(c).getNodeName();
							if (nn.startsWith("file")) {
								try {
									NamedNodeMap fileNodeMap = fileList.item(c).getAttributes();
									if (fileNodeMap != null) {
										Node oldf = fileNodeMap.getNamedItem("old");
										Node newf = fileNodeMap.getNamedItem("new");
										Node dirf = fileNodeMap.getNamedItem("name");
										String oldPath = oldf.getNodeValue();
										String newPath = newf.getNodeValue();
										String dirPath = dirf.getNodeValue();
										if ((oldPath.equals("NULL")) || (newPath.equals("NULL"))) {
											dirPaths[XMLHandling.pathPlace] = dirPath;
											XMLHandling.pathPlace += 1;
										} else {
											File oldFile = new File(oldPath);
											File newFile = new File(newPath);
											if (re == 0)
												Append.out("File loaded and copying: " + newFile);
											if (newFile.exists()) {
												XMLHandling.amountCopied = 0;
												SFSMain.progressCurrent.setValue(0);
												int fileSize = (int) (newFile.length() / 1024L);
												SFSMain.progressCurrent.setMaximum(fileSize);

												InputStream input = null;
												OutputStream output = null;
												try {
													int bytesRead;
													input = new FileInputStream(newFile);
													output = new FileOutputStream(oldFile);
													byte[] buf = new byte[1024];
													while ((bytesRead = input.read(buf)) > 0) {
														output.write(buf, 0, bytesRead);
														XMLHandling.amountCopied += 1;
														SFSMain.progressCurrent.setValue(XMLHandling.amountCopied);
													}
												} finally {
													input.close();
													output.close();
												}
												newFile.delete();
											}
										}
									}
								} catch (Exception e) {
									CatchHandling.error(e);
								}
								if (re == 0) {
									XMLHandling.completed += 1;
									SFSMain.progressBar.setValue(XMLHandling.completed);
									SFSMain.lblTotal.setText(XMLHandling.completed + " / " + XMLHandling.progMax);
								}
							}
							System.gc();
						}

						for (int x = 0; x < dirPaths.length; x++) {
							if (!dirPaths[x].equals("NULL")) {
								File dir = new File(dirPaths[x]);
								if (re == 0)
									Append.out("Folder deleted: " + dirPaths[x]);
								if ((dir.exists()) && (dir.isDirectory()))
									dir.delete();
								if (re == 0) {
									XMLHandling.completed += 1;
									SFSMain.progressBar.setValue(XMLHandling.completed);
								}
							}
						}
						if (re == 0) {
							Append.out("Rerunning restoration to make sure all files are restored");
						}
					}
					SFSMain.btnFix.setLabel("Close");
					SFSMain.btnFix.setEnabled(true);
					SFSMain.close = true;
					Append.out("All files back to original");
					File bk = new File(XMLHandling.filepath);
					if ((bk.exists()) && (!bk.isDirectory()))
						bk.delete();
					Append.logend();
					if (SFSMain.xoc) {
						System.exit(0);
					}
				}
			})

					.start();
		} catch (Exception e) {
			CatchHandling.error(e);
		}
	}

	public static void CheckDir() {
		File dir = new File(SFSMain.location + "bk\\");
		String filepath = SFSMain.location + "bk\\back.xml";
		if ((dir.exists()) && (dir.isDirectory())) {
			File xml = new File(filepath);
			if ((xml.exists()) && (!xml.isDirectory())) {
				SFSMain.btnUndo.setEnabled(true);
			}
		}
	}
}
