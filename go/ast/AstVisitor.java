package org.textmapper.json.ast;

public abstract class AstVisitor {

	protected boolean visit(AstJSONValue n) {
		return true;
	}

	protected boolean visit(AstJSONValueA n) {
		return true;
	}

	protected boolean visit(AstEmptyObject n) {
		return true;
	}

	protected boolean visit(AstLookaheadEmptyObject n) {
		return true;
	}

	protected boolean visit(AstJSONObject n) {
		return true;
	}

	protected boolean visit(AstLookaheadNotEmptyObject n) {
		return true;
	}

	protected boolean visit(AstJSONArray n) {
		return true;
	}
}
