package org.textmapper.json.ast;

import java.util.List;
import org.textmapper.json.JsonTree.TextSource;

public class AstJSONArray extends AstNode {

	private final List<AstJSONValueA> JSONElementList;

	public AstJSONArray(List<AstJSONValueA> JSONElementList, TextSource source, int line, int offset, int endoffset) {
		super(source, line, offset, endoffset);
		this.JSONElementList = JSONElementList;
	}

	public List<AstJSONValueA> getJSONElementList() {
		return JSONElementList;
	}

	@Override
	public void accept(AstVisitor v) {
		if (!v.visit(this)) {
			return;
		}
		if (JSONElementList != null) {
			for (AstJSONValueA it : JSONElementList) {
				it.accept(v);
			}
		}
	}
}
