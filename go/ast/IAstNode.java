package org.textmapper.json.ast;

import org.textmapper.json.JsonTree.TextSource;

public interface IAstNode {
	String getLocation();
	int getLine();
	int getOffset();
	int getEndoffset();
	TextSource getSource();
	String getResourceName();
	String getText();
	void accept(AstVisitor v);
}
