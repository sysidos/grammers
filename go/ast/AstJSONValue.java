package org.textmapper.json.ast;

import org.textmapper.json.JsonTree.TextSource;

public class AstJSONValue extends AstNode {

	private final AstJSONObject JSONObject;
	private final AstEmptyObject EmptyObject;
	private final AstJSONArray JSONArray;
	private final string JSONString;

	public AstJSONValue(AstJSONObject JSONObject, AstEmptyObject EmptyObject, AstJSONArray JSONArray, string JSONString, TextSource source, int line, int offset, int endoffset) {
		super(source, line, offset, endoffset);
		this.JSONObject = JSONObject;
		this.EmptyObject = EmptyObject;
		this.JSONArray = JSONArray;
		this.JSONString = JSONString;
	}

	public AstJSONObject getJSONObject() {
		return JSONObject;
	}

	public AstEmptyObject getEmptyObject() {
		return EmptyObject;
	}

	public AstJSONArray getJSONArray() {
		return JSONArray;
	}

	public string getJSONString() {
		return JSONString;
	}

	@Override
	public void accept(AstVisitor v) {
		if (!v.visit(this)) {
			return;
		}
		if (JSONObject != null) {
			JSONObject.accept(v);
		}
		if (EmptyObject != null) {
			EmptyObject.accept(v);
		}
		if (JSONArray != null) {
			JSONArray.accept(v);
		}
	}
}
