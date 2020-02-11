package org.textmapper.json.ast;

import org.textmapper.json.JsonTree.TextSource;

public class AstLookaheadNotEmptyObject extends AstNode {

	public AstLookaheadNotEmptyObject(TextSource source, int line, int offset, int endoffset) {
		super(source, line, offset, endoffset);
	}

	@Override
	public void accept(AstVisitor v) {
		v.visit(this);
	}
}
