package org.exodus.localfullnode2.tools.cfgen;

public class SeedParagraph implements IParagraph {
	private IParagraphWritable INST;

	@Override
	public IParagraphWritable buildWriter(ICheatSheet cheatSheet) {
		if (INST == null) {
			INST = new SeedParagraphWriter(cheatSheet);
		}

		return INST;

	}

}
