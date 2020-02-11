package org.textmapper.json.ast;

import org.textmapper.json.JsonTree.TextSource;

public class AstEmptyObject extends AstNode {

	private final AstLookaheadEmptyObject lookaheadEmptyObject;

	public AstEmptyObject(AstLookaheadEmptyObject lookaheadEmptyObject, TextSource source, int line, int offset, int endoffset) {
		super(source, line, offset, endoffset);
		this.lookaheadEmptyObject = lookaheadEmptyObject;
	}

	public AstLookaheadEmptyObject getLookaheadEmptyObject() {
		return lookaheadEmptyObject;
	}

	@Override
	public void accept(AstVisitor v) {
		if (!v.visit(this)) {
			return;
		}
		if (lookaheadEmptyObject != null) {
			lookaheadEmptyObject.accept(v);
		}
	}
}
