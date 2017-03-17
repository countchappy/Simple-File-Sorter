package main.point.start.handling.files;

import java.util.regex.Pattern;

public class RenameHandler {
	private static final Pattern bad = Pattern.compile("[\\Q][(){},.;!?_-<>%1234567890\\E]");

	public static String renameSimple(String name) {
		return bad.matcher(name).replaceAll("").trim().replace(" ", "");
	}
}
