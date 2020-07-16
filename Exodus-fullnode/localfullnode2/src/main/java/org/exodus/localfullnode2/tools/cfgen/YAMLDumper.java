package org.exodus.localfullnode2.tools.cfgen;

import java.util.Map;

public class YAMLDumper {
	private final StringBuffer docBuffer;
	private final DumperOptions options;

	public YAMLDumper() {
		this.docBuffer = new StringBuffer();
		this.options = new DumperOptions();
	}

	public void multipleLines(String autoIndent, String key, String... values) {
		key(autoIndent, key);

		for (String value : values) {
			docBuffer.append(autoIndent).append(options.indent).append(value).append(options.newline);
		}
	}

	public void key(String autoIndent, String key) {
		docBuffer.append(autoIndent);
		docBuffer.append(key);
		docBuffer.append(options.colon);
		docBuffer.append(options.literalBlock);
		docBuffer.append(options.newline);
	}

	//@formatter:off
	/**
	 * 
	 *         - 192.168.207.130
	 *         - 172.17.2.118
	 */
	//@formatter:on
	public void list(String autoIndent, String... values) {
		for (String value : values) {
			docBuffer.append(autoIndent).append(options.indentation).append(" ").append(value).append(options.newline);
		}
	}

	public void list(String autoIndent, Map<String, String>... values) {

	}

	public class DumperOptions {
		private String newline = System.getProperty("line.separator");
		private String indent = "  ";// two spaces
		private String colon = ": ";// colon
		private String literalBlock = "|";
		private String indentation = "-";

		public DumperOptions() {
		}

	}
}
