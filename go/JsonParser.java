package org.textmapper.json;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.textmapper.json.JsonLexer.ErrorReporter;
import org.textmapper.json.JsonLexer.Span;
import org.textmapper.json.JsonLexer.Tokens;
import org.textmapper.json.ast.AstEmptyObject;
import org.textmapper.json.ast.AstJSONArray;
import org.textmapper.json.ast.AstJSONObject;
import org.textmapper.json.ast.AstJSONValue;
import org.textmapper.json.ast.AstJSONValueA;
import org.textmapper.json.ast.AstLookaheadEmptyObject;
import org.textmapper.json.ast.AstLookaheadNotEmptyObject;

public class JsonParser {

	public static class ParseException extends Exception {
		private static final long serialVersionUID = 1L;

		public ParseException() {
		}
	}

	private final ErrorReporter reporter;

	public JsonParser(ErrorReporter reporter) {
		this.reporter = reporter;
	}

	private static final boolean DEBUG_SYNTAX = false;
	private static final int[] tmAction = JsonLexer.unpack_int(45,
		"\24\0\ufffd\uffff\uffff\uffff\uffeb\uffff\21\0\22\0\12\0\13\0\14\0\15\0\0\0\17\0" +
		"\16\0\uffff\uffff\20\0\uffff\uffff\34\0\uffd7\uffff\uffff\uffff\uffff\uffff\23\0" +
		"\uffd1\uffff\33\0\26\0\uffff\uffff\31\0\uffff\uffff\35\0\uffbf\uffff\25\0\uffff\uffff" +
		"\10\0\11\0\1\0\2\0\3\0\4\0\30\0\6\0\5\0\7\0\32\0\ufffe\uffff\uffff\uffff\ufffe\uffff");

	private static final int[] tmLalr = JsonLexer.unpack_int(80,
		"\3\0\uffff\uffff\11\0\uffff\uffff\12\0\uffff\uffff\14\0\uffff\uffff\15\0\uffff\uffff" +
		"\16\0\uffff\uffff\17\0\uffff\uffff\1\0\40\0\uffff\uffff\ufffe\uffff\3\0\uffff\uffff" +
		"\11\0\uffff\uffff\12\0\uffff\uffff\14\0\uffff\uffff\15\0\uffff\uffff\16\0\uffff\uffff" +
		"\17\0\uffff\uffff\1\0\40\0\4\0\37\0\uffff\uffff\ufffe\uffff\6\0\uffff\uffff\4\0\36" +
		"\0\uffff\uffff\ufffe\uffff\3\0\uffff\uffff\11\0\uffff\uffff\12\0\uffff\uffff\14\0" +
		"\uffff\uffff\15\0\uffff\uffff\16\0\uffff\uffff\17\0\uffff\uffff\1\0\40\0\uffff\uffff" +
		"\ufffe\uffff\3\0\uffff\uffff\11\0\uffff\uffff\12\0\uffff\uffff\14\0\uffff\uffff\15" +
		"\0\uffff\uffff\16\0\uffff\uffff\20\0\uffff\uffff\1\0\40\0\uffff\uffff\ufffe\uffff");

	private static final int[] tmGoto = JsonLexer.unpack_int(32,
		"\0\0\2\0\6\0\14\0\24\0\26\0\30\0\34\0\34\0\34\0\50\0\60\0\60\0\70\0\100\0\110\0\116" +
		"\0\120\0\120\0\120\0\122\0\124\0\132\0\144\0\156\0\166\0\176\0\202\0\204\0\214\0" +
		"\216\0\220\0");

	private static final int[] tmFromTo = JsonLexer.unpack_int(144,
		"\53\0\54\0\2\0\17\0\15\0\23\0\17\0\24\0\23\0\27\0\32\0\35\0\1\0\3\0\3\0\3\0\25\0" +
		"\3\0\34\0\3\0\22\0\26\0\30\0\34\0\21\0\25\0\32\0\36\0\1\0\4\0\3\0\4\0\23\0\30\0\25" +
		"\0\4\0\34\0\37\0\36\0\30\0\1\0\5\0\3\0\5\0\25\0\5\0\34\0\40\0\1\0\6\0\3\0\6\0\25" +
		"\0\6\0\34\0\41\0\1\0\7\0\3\0\7\0\25\0\7\0\34\0\42\0\1\0\10\0\3\0\10\0\25\0\10\0\34" +
		"\0\43\0\1\0\11\0\3\0\11\0\25\0\11\0\34\0\44\0\1\0\53\0\34\0\45\0\1\0\12\0\3\0\20" +
		"\0\25\0\33\0\0\0\52\0\1\0\13\0\3\0\13\0\25\0\13\0\34\0\46\0\0\0\2\0\1\0\2\0\3\0\2" +
		"\0\25\0\2\0\34\0\2\0\1\0\14\0\3\0\14\0\25\0\14\0\34\0\47\0\1\0\15\0\3\0\15\0\25\0" +
		"\15\0\34\0\15\0\23\0\31\0\36\0\51\0\23\0\32\0\1\0\16\0\3\0\16\0\25\0\16\0\34\0\50" +
		"\0\3\0\21\0\3\0\22\0");

	private static final int[] tmRuleLen = JsonLexer.unpack_int(33,
		"\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\1\0\3\0" +
		"\0\0\4\0\3\0\0\0\3\0\1\0\3\0\3\0\1\0\3\0\1\0\0\0\0\0");

	private static final int[] tmRuleSymbol = JsonLexer.unpack_int(33,
		"\23\0\24\0\24\0\24\0\24\0\24\0\24\0\24\0\24\0\24\0\25\0\25\0\25\0\25\0\25\0\25\0" +
		"\25\0\25\0\25\0\26\0\27\0\30\0\30\0\31\0\32\0\33\0\33\0\34\0\35\0\35\0\36\0\36\0" +
		"\31\0");

	protected static final String[] tmSymbolNames = new String[] {
		"eoi",
		"'{'",
		"'}'",
		"'['",
		"']'",
		"':'",
		"','",
		"space",
		"MultiLineComment",
		"JSONString",
		"JSONNumber",
		"id",
		"'null'",
		"'true'",
		"'false'",
		"'A'",
		"'B'",
		"error",
		"invalid_token",
		"JSONText",
		"JSONValue",
		"JSONValue_A",
		"EmptyObject",
		"lookahead_EmptyObject",
		"JSONObject",
		"lookahead_notEmptyObject",
		"JSONMember",
		"JSONMemberList",
		"JSONArray",
		"JSONElementList",
		"JSONElementListopt",
	};

	public interface Nonterminals extends Tokens {
		// non-terminals
		int JSONText = 19;
		int JSONValue = 20;
		int JSONValue_A = 21;
		int EmptyObject = 22;
		int lookahead_EmptyObject = 23;
		int JSONObject = 24;
		int lookahead_notEmptyObject = 25;
		int JSONMember = 26;
		int JSONMemberList = 27;
		int JSONArray = 28;
		int JSONElementList = 29;
		int JSONElementListopt = 30;
	}

	// set(first JSONValue<+A>)
	private static int[] Literals = {
		1
	};

	// set(follow error)
	private static int[] afterErr = {
		
	};

	/**
	 * -3-n   Lookahead (state id)
	 * -2     Error
	 * -1     Shift
	 * 0..n   Reduce (rule index)
	 */
	protected static int tmAction(int state, int symbol) {
		int p;
		if (tmAction[state] < -2) {
			if (symbol == Tokens.Unavailable_) {
				return -3 - state;
			}
			for (p = -tmAction[state] - 3; tmLalr[p] >= 0; p += 2) {
				if (tmLalr[p] == symbol) {
					break;
				}
			}
			return tmLalr[p + 1];
		}
		return tmAction[state];
	}

	protected static int gotoState(int state, int symbol) {
		int min = tmGoto[symbol], max = tmGoto[symbol + 1];
		int i, e;

		while (min < max) {
			e = (min + max) >> 2 << 1;
			i = tmFromTo[e];
			if (i == state) {
				return tmFromTo[e+1];
			} else if (i < state) {
				min = e + 2;
			} else {
				max = e;
			}
		}
		return -1;
	}

	protected int tmHead;
	protected Span[] tmStack;
	protected Span tmNext;
	protected JsonLexer tmLexer;

	private Object parse(JsonLexer lexer, int initialState, int finalState, boolean noEoi) throws IOException, ParseException {

		tmLexer = lexer;
		tmStack = new Span[1024];
		tmHead = 0;
		int tmShiftsAfterError = 4;

		tmStack[0] = new Span();
		tmStack[0].state = initialState;
		tmNext = tmLexer.next();

		while (tmStack[tmHead].state != finalState) {
			int action = tmAction(tmStack[tmHead].state, tmNext == null ? Tokens.Unavailable_ : tmNext.symbol);
			if (action <= -3 && tmNext == null) {
				tmNext = tmLexer.next();
				action = tmAction(tmStack[tmHead].state, tmNext.symbol);
			}

			if (action >= 0) {
				reduce(action);
			} else if (action == -1) {
				shift(noEoi);
				tmShiftsAfterError++;
			}

			if (action == -2 || tmStack[tmHead].state == -1) {
				if (restore()) {
					if (tmShiftsAfterError >= 4) {
						reporter.error(MessageFormat.format("syntax error before line {0}", tmLexer.getTokenLine()), tmNext.line, tmNext.offset, tmNext.endoffset);
					}
					if (tmShiftsAfterError <= 1) {
						tmNext = tmLexer.next();
					}
					tmShiftsAfterError = 0;
					continue;
				}
				if (tmHead < 0) {
					tmHead = 0;
					tmStack[0] = new Span();
					tmStack[0].state = initialState;
				}
				break;
			}
		}

		if (tmStack[tmHead].state != finalState) {
			if (tmShiftsAfterError >= 4) {
				reporter.error(MessageFormat.format("syntax error before line {0}",
								tmLexer.getTokenLine()), tmNext == null ? tmLexer.getLine() : tmNext.line, tmNext == null ? tmLexer.getOffset() : tmNext.offset, tmNext == null ? tmLexer.getOffset() : tmNext.endoffset);
			}
			throw new ParseException();
		}
		return tmStack[noEoi ? tmHead : tmHead - 1].value;
	}

	protected boolean restore() throws IOException {
		if (tmNext == null) {
			tmNext = tmLexer.next();
		}
		if (tmNext.symbol == 0) {
			return false;
		}
		while (tmHead >= 0 && gotoState(tmStack[tmHead].state, 17) == -1) {
			dispose(tmStack[tmHead]);
			tmStack[tmHead] = null;
			tmHead--;
		}
		if (tmHead >= 0) {
			tmStack[++tmHead] = new Span();
			tmStack[tmHead].symbol = 17;
			tmStack[tmHead].value = null;
			tmStack[tmHead].state = gotoState(tmStack[tmHead - 1].state, 17);
			tmStack[tmHead].line = tmNext.line;
			tmStack[tmHead].offset = tmNext.offset;
			tmStack[tmHead].endoffset = tmNext.endoffset;
			return true;
		}
		return false;
	}

	protected void shift(boolean lazy) throws IOException {
		if (tmNext == null) {
			tmNext = tmLexer.next();
		}
		tmStack[++tmHead] = tmNext;
		tmStack[tmHead].state = gotoState(tmStack[tmHead - 1].state, tmNext.symbol);
		if (DEBUG_SYNTAX) {
			System.out.println(MessageFormat.format("shift: {0} ({1})", tmSymbolNames[tmNext.symbol], tmLexer.tokenText()));
		}
		if (tmStack[tmHead].state != -1 && tmNext.symbol != 0) {
			tmNext = lazy ? null : tmLexer.next();
		}
	}

	protected void reduce(int rule) {
		Span left = new Span();
		left.value = (tmRuleLen[rule] != 0) ? tmStack[tmHead + 1 - tmRuleLen[rule]].value : null;
		left.symbol = tmRuleSymbol[rule];
		left.state = 0;
		if (DEBUG_SYNTAX) {
			System.out.println("reduce to " + tmSymbolNames[tmRuleSymbol[rule]]);
		}
		Span startsym = (tmRuleLen[rule] != 0) ? tmStack[tmHead + 1 - tmRuleLen[rule]] : tmNext;
		left.line = startsym == null ? tmLexer.getLine() : startsym.line;
		left.offset = startsym == null ? tmLexer.getOffset() : startsym.offset;
		left.endoffset = (tmRuleLen[rule] != 0) ? tmStack[tmHead].endoffset : tmNext == null ? tmLexer.getOffset() : tmNext.offset;
		applyRule(left, rule, tmRuleLen[rule]);
		for (int e = tmRuleLen[rule]; e > 0; e--) {
			tmStack[tmHead--] = null;
		}
		tmStack[++tmHead] = left;
		tmStack[tmHead].state = gotoState(tmStack[tmHead - 1].state, left.symbol);
	}

	@SuppressWarnings("unchecked")
	protected void applyRule(Span tmLeft, int ruleIndex, int ruleLength) {
		switch (ruleIndex) {
			case 1:  // JSONValue : 'null'
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 2:  // JSONValue : 'true'
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 3:  // JSONValue : 'false'
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 4:  // JSONValue : 'B'
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 5:  // JSONValue : JSONObject
				tmLeft.value = new AstJSONValue(
						((AstJSONObject)tmStack[tmHead].value) /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 6:  // JSONValue : EmptyObject
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						((AstEmptyObject)tmStack[tmHead].value) /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 7:  // JSONValue : JSONArray
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						((AstJSONArray)tmStack[tmHead].value) /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 8:  // JSONValue : JSONString
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						((string)tmStack[tmHead].value) /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 9:  // JSONValue : JSONNumber
				tmLeft.value = new AstJSONValue(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 10:  // JSONValue_A : 'null'
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 11:  // JSONValue_A : 'true'
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 12:  // JSONValue_A : 'false'
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 13:  // JSONValue_A : 'A'
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 14:  // JSONValue_A : JSONObject
				tmLeft.value = new AstJSONValueA(
						((AstJSONObject)tmStack[tmHead].value) /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 15:  // JSONValue_A : EmptyObject
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						((AstEmptyObject)tmStack[tmHead].value) /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 16:  // JSONValue_A : JSONArray
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						((AstJSONArray)tmStack[tmHead].value) /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 17:  // JSONValue_A : JSONString
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						((string)tmStack[tmHead].value) /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 18:  // JSONValue_A : JSONNumber
				tmLeft.value = new AstJSONValueA(
						null /* JSONObject */,
						null /* EmptyObject */,
						null /* JSONArray */,
						null /* JSONString */,
						null /* input */, tmStack[tmHead].line, tmStack[tmHead].offset, tmStack[tmHead].endoffset);
				break;
			case 19:  // EmptyObject : lookahead_EmptyObject '{' '}'
				tmLeft.value = new AstEmptyObject(
						((AstLookaheadEmptyObject)tmStack[tmHead - 2].value) /* lookaheadEmptyObject */,
						null /* input */, tmStack[tmHead - 2].line, tmStack[tmHead - 2].offset, tmStack[tmHead].endoffset);
				break;
			case 21:  // JSONObject : lookahead_notEmptyObject '{' JSONMemberList '}'
				tmLeft.value = new AstJSONObject(
						((AstLookaheadNotEmptyObject)tmStack[tmHead - 3].value) /* lookaheadNotEmptyObject */,
						((List<*Field>)tmStack[tmHead - 1].value) /* JSONMemberList */,
						null /* input */, tmStack[tmHead - 3].line, tmStack[tmHead - 3].offset, tmStack[tmHead].endoffset);
				break;
			case 22:  // JSONObject : lookahead_notEmptyObject '{' '}'
				tmLeft.value = new AstJSONObject(
						((AstLookaheadNotEmptyObject)tmStack[tmHead - 2].value) /* lookaheadNotEmptyObject */,
						null /* JSONMemberList */,
						null /* input */, tmStack[tmHead - 2].line, tmStack[tmHead - 2].offset, tmStack[tmHead].endoffset);
				break;
			case 25:  // JSONMemberList : JSONMember
				tmLeft.value = new ArrayList();
				((List<*Field>)tmLeft.value).add(((*Field)tmStack[tmHead].value));
				break;
			case 26:  // JSONMemberList : JSONMemberList ',' JSONMember
				((List<*Field>)tmLeft.value).add(((*Field)tmStack[tmHead].value));
				break;
			case 27:  // JSONArray : '[' JSONElementListopt ']'
				tmLeft.value = new AstJSONArray(
						((List<AstJSONValueA>)tmStack[tmHead - 1].value) /* JSONElementList */,
						null /* input */, tmStack[tmHead - 2].line, tmStack[tmHead - 2].offset, tmStack[tmHead].endoffset);
				break;
			case 28:  // JSONElementList : JSONValue_A
				tmLeft.value = new ArrayList();
				((List<AstJSONValueA>)tmLeft.value).add(((AstJSONValueA)tmStack[tmHead].value));
				break;
			case 29:  // JSONElementList : JSONElementList ',' JSONValue_A
				((List<AstJSONValueA>)tmLeft.value).add(((AstJSONValueA)tmStack[tmHead].value));
				break;
		}
	}

	/**
	 * disposes symbol dropped by error recovery mechanism
	 */
	protected void dispose(Span value) {
	}

	public AstEmptyObject parseEmptyObject(JsonLexer lexer) throws IOException, ParseException {
		return (AstEmptyObject) parse(lexer, 0, 42, true);
	}

	public AstJSONValueA parseJSONText(JsonLexer lexer) throws IOException, ParseException {
		return (AstJSONValueA) parse(lexer, 1, 44, false);
	}
}
