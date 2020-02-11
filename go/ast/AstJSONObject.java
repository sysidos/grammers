package org.textmapper.json.ast;

import java.util.List;
import org.textmapper.json.JsonTree.TextSource;

public class AstJSONObject extends AstNode {

	private final AstLookaheadNotEmptyObject lookaheadNotEmptyObject;
	private final List<*Field> JSONMemberList;

	public AstJSONObject(AstLookaheadNotEmptyObject lookaheadNotEmptyObject, List<*Field> JSONMemberList, TextSource source, int line, int offset, int endoffset) {
		super(source, line, offset, endoffset);
		this.lookaheadNotEmptyObject = lookaheadNotEmptyObject;
		this.JSONMemberList = JSONMemberList;
	}

	public AstLookaheadNotEmptyObject getLookaheadNotEmptyObject() {
		return lookaheadNotEmptyObject;
	}

	public List<*Field> getJSONMemberList() {
		return JSONMemberList;
	}

	@Override
	public void accept(AstVisitor v) {
		if (!v.visit(this)) {
			return;
		}
		if (lookaheadNotEmptyObject != null) {
			lookaheadNotEmptyObject.accept(v);
		}
	}
}
