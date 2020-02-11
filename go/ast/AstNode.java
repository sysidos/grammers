package org.textmapper.json.ast;

import org.textmapper.json.JsonTree.TextSource;

public abstract class AstNode implements IAstNode {

	protected TextSource source;
	protected int line;
	protected int offset;
	protected int endoffset;

	public AstNode(TextSource source, int line, int offset, int endoffset) {
		this.source = source;
		this.line = line;
		this.offset = offset;
		this.endoffset = endoffset;
	}

	@Override
	public String getLocation() {
		return source.getLocation(offset);
	}

	@Override
	public int getLine() {
		return this.line;
	}

	@Override
	public int getOffset() {
		return this.offset;
	}

	@Override
	public int getEndoffset() {
		return this.endoffset;
	}

	@Override
	public TextSource getSource() {
		return source;
	}

	@Override
	public String getResourceName() {
		return source.getFile();
	}

	@Override
	public String getText() {
		return source.getText(offset, endoffset);
	}

	@Override
	public String toString() {
		return source == null ? "" : source.getText(offset, endoffset);
	}
}
