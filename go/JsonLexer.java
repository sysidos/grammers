package org.textmapper.json;

import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class JsonLexer {

	public static class Span {
		public Object value;
		public int symbol;
		public int state;
		public int line;
		public int offset;
		public int endoffset;
	}

	public interface Tokens {
		int Unavailable_ = -1;
		int eoi = 0;
		int Lbrace = 1;
		int Rbrace = 2;
		int Lbrack = 3;
		int Rbrack = 4;
		int Colon = 5;
		int Comma = 6;
		int space = 7;
		int MultiLineComment = 8;
		int JSONString = 9;
		int JSONNumber = 10;
		int id = 11;
		int _null = 12;
		int _true = 13;
		int _false = 14;
		int char_A = 15;
		int char_B = 16;
		int error = 17;
		int invalid_token = 18;
	}

	public interface ErrorReporter {
		void error(String message, int line, int offset, int endoffset);
	}

	public static final int TOKEN_SIZE = 2048;

	private Reader stream;
	final private ErrorReporter reporter;

	private CharSequence input;
	private int tokenOffset;
	private int l;
	private int charOffset;
	private int chr;

	private int state;

	private int tokenLine;
	private int currLine;
	private int currOffset;

	public JsonLexer(CharSequence input, ErrorReporter reporter) throws IOException {
		this.reporter = reporter;
		reset(input);
	}

	public void reset(CharSequence input) throws IOException {
		this.state = 0;
		tokenLine = currLine = 1;
		currOffset = 0;
		this.input = input;
		tokenOffset = l = 0;
		charOffset = l;
		chr = l < input.length() ? input.charAt(l++) : -1;
		if (chr >= Character.MIN_HIGH_SURROGATE && chr <= Character.MAX_HIGH_SURROGATE && l < input.length() &&
				Character.isLowSurrogate(input.charAt(l))) {
			chr = Character.toCodePoint((char) chr, input.charAt(l++));
		}
	}

	protected void advance() {
		if (chr == -1) return;
		currOffset += l - charOffset;
		if (chr == '\n') {
			currLine++;
		}
		charOffset = l;
		chr = l < input.length() ? input.charAt(l++) : -1;
		if (chr >= Character.MIN_HIGH_SURROGATE && chr <= Character.MAX_HIGH_SURROGATE && l < input.length() &&
				Character.isLowSurrogate(input.charAt(l))) {
			chr = Character.toCodePoint((char) chr, input.charAt(l++));
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTokenLine() {
		return tokenLine;
	}

	public int getLine() {
		return currLine;
	}

	public void setLine(int currLine) {
		this.currLine = currLine;
	}

	public int getOffset() {
		return currOffset;
	}

	public void setOffset(int currOffset) {
		this.currOffset = currOffset;
	}

	public String tokenText() {
		return input.subSequence(tokenOffset, charOffset).toString();
	}

	public int tokenSize() {
		return charOffset - tokenOffset;
	}

	private static final short tmCharClass[] = {
		1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		2, 1, 3, 1, 1, 1, 1, 1, 1, 1, 4, 5, 6, 7, 8, 9,
		10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 1, 1, 1, 1, 1,
		1, 13, 13, 13, 13, 14, 13, 15, 15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 16, 17, 18, 1, 1,
		1, 13, 19, 13, 13, 14, 19, 15, 15, 15, 15, 15, 15, 15, 20, 15,
		15, 15, 20, 15, 20, 21, 15, 15, 15, 15, 15, 22, 1, 23
	};

	private static final short tmBacktracking[] = {
		11, 11, 11, 8
	};

	private static final int tmFirstRule = -3;

	private static final int[] tmRuleSymbol = unpack_int(18,
		"\22\0\0\0\1\0\2\0\3\0\4\0\5\0\6\0\7\0\10\0\11\0\12\0\13\0\14\0\15\0\16\0\17\0\20" +
		"\0");

	private static final int tmClassesCount = 24;

	private static final short[] tmGoto = unpack_vc_short(672,
		"\1\ufffc\1\ufffd\1\33\1\24\2\ufffd\1\23\1\22\1\ufffd\1\16\1\15\1\7\1\6\3\5\1\4\1" +
		"\ufffd\1\3\3\5\1\2\1\1\30\ufffa\30\ufffb\30\ufff8\30\ufff9\12\ufff1\2\5\1\ufff1\3" +
		"\5\3\ufff1\3\5\2\ufff1\30\ufff7\10\ufff2\1\uffff\1\ufff2\2\7\2\ufff2\1\ufffe\11\ufff2" +
		"\5\ufffd\1\12\1\ufffd\1\12\2\ufffd\2\11\14\ufffd\12\ufff2\2\11\14\ufff2\12\ufffd" +
		"\2\11\26\ufffd\2\14\14\ufffd\12\ufff2\2\14\2\ufff2\1\ufffe\21\ufff2\1\uffff\5\ufff2" +
		"\1\ufffe\11\ufff2\4\ufffd\1\17\24\ufffd\3\17\1\20\23\17\1\ufffd\3\17\1\20\4\17\1" +
		"\21\16\17\30\ufff4\12\ufffd\1\15\1\7\14\ufffd\30\ufff6\1\ufffd\2\24\1\32\15\24\1" +
		"\25\6\24\3\ufffd\1\24\5\ufffd\1\24\7\ufffd\1\24\1\ufffd\2\24\1\26\14\ufffd\2\27\1" +
		"\ufffd\2\27\4\ufffd\1\27\16\ufffd\2\30\1\ufffd\2\30\4\ufffd\1\30\16\ufffd\2\31\1" +
		"\ufffd\2\31\4\ufffd\1\31\16\ufffd\2\24\1\ufffd\2\24\4\ufffd\1\24\4\ufffd\30\ufff3" +
		"\2\ufff5\1\33\25\ufff5");

	private static short[] unpack_vc_short(int size, String... st) {
		short[] res = new short[size];
		int t = 0;
		int count = 0;
		for (String s : st) {
			int slen = s.length();
			for (int i = 0; i < slen; ) {
				count = i > 0 || count == 0 ? s.charAt(i++) : count;
				if (i < slen) {
					short val = (short) s.charAt(i++);
					while (count-- > 0) res[t++] = val;
				}
			}
		}
		assert res.length == t;
		return res;
	}

	private static int mapCharacter(int chr) {
		if (chr >= 0 && chr < 126) return tmCharClass[chr];
		return chr == -1 ? 0 : 1;
	}

	public Span next() throws IOException {
		Span token = new Span();
		int state;

		tokenloop:
		do {
			token.offset = currOffset;
			tokenLine = token.line = currLine;
			tokenOffset = charOffset;

			// TODO use backupRule
			int backupRule = -1;
			for (state = this.state; state >= 0; ) {
				state = tmGoto[state * tmClassesCount + mapCharacter(chr)];
				if (state > tmFirstRule && state < 0) {
					token.endoffset = currOffset;
					state = (-1 - state) * 2;
					backupRule = tmBacktracking[state++];
					state = tmBacktracking[state];
				}
				if (state == tmFirstRule && chr == -1) {
					token.endoffset = currOffset;
					token.symbol = 0;
					token.value = null;
					reporter.error("Unexpected end of input reached", token.line, token.offset, token.endoffset);
					token.offset = currOffset;
					break tokenloop;
				}
				if (state >= tmFirstRule && chr != -1) {
					currOffset += l - charOffset;
					if (chr == '\n') {
						currLine++;
					}
					charOffset = l;
					chr = l < input.length() ? input.charAt(l++) : -1;
					if (chr >= Character.MIN_HIGH_SURROGATE && chr <= Character.MAX_HIGH_SURROGATE && l < input.length() &&
							Character.isLowSurrogate(input.charAt(l))) {
						chr = Character.toCodePoint((char) chr, input.charAt(l++));
					}
				}
			}
			token.endoffset = currOffset;

			token.symbol = tmRuleSymbol[tmFirstRule - state];
			token.value = null;

			if (token.symbol == -1) {
				reporter.error(MessageFormat.format("invalid token at line {0}: `{1}`, skipped", currLine, tokenText()), token.line, token.offset, token.endoffset);
			}

		} while (token.symbol == -1 || !createToken(token, tmFirstRule - state));
		return token;
	}

	protected int charAt(int i) {
		if (i == 0) return chr;
		i += l - 1;
		int res = i < input.length() ? input.charAt(i++) : -1;
		if (res >= Character.MIN_HIGH_SURROGATE && res <= Character.MAX_HIGH_SURROGATE && i < input.length() &&
				Character.isLowSurrogate(input.charAt(i))) {
			res = Character.toCodePoint((char) res, input.charAt(i++));
		}
		return res;
	}

	protected boolean createToken(Span token, int ruleIndex) throws IOException {
		boolean spaceToken = false;
		switch (ruleIndex) {
			case 8: // space: /[\t\r\n ]+/
				spaceToken = true;
				break;
			case 9: // MultiLineComment: /\/\*{commentChars}\*\//
				spaceToken = true;
				break;
			case 12:
				return createIdToken(token, ruleIndex);
		}
		return !(spaceToken);
	}

	private static Map<String,Integer> subTokensOfId = new HashMap<>();
	static {
		subTokensOfId.put("null", 13);
		subTokensOfId.put("true", 14);
		subTokensOfId.put("false", 15);
		subTokensOfId.put("A", 16);
		subTokensOfId.put("B", 17);
	}

	protected boolean createIdToken(Span token, int ruleIndex) {
		Integer replacement = subTokensOfId.get(tokenText());
		if (replacement != null) {
			ruleIndex = replacement;
			token.symbol = tmRuleSymbol[ruleIndex];
		}
		return true;
	}

	/* package */ static int[] unpack_int(int size, String... st) {
		int[] res = new int[size];
		boolean second = false;
		char first = 0;
		int t = 0;
		for (String s : st) {
			int slen = s.length();
			for (int i = 0; i < slen; i++) {
				if (second) {
					res[t++] = (s.charAt(i) << 16) + first;
				} else {
					first = s.charAt(i);
				}
				second = !second;
			}
		}
		assert !second;
		assert res.length == t;
		return res;
	}

}
