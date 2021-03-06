// generated by Textmapper; DO NOT EDIT

package json

import (
	"fmt"
)

// Symbol represents a set of all terminal and non-terminal symbols of the json language.
type Symbol int

var symbolStr = [...]string{
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
}

func (n Symbol) String() string {
	if n < Symbol(NumTokens) {
		return Token(n).String()
	}
	i := int(n) - int(NumTokens)
	if i < len(symbolStr) {
		return symbolStr[i]
	}
	return fmt.Sprintf("nonterminal(%d)", n)
}

var tmAction = []int32{
	20, -3, -1, -21, 17, 18, 10, 11, 12, 13, 0, 15, 14, -1, 16, -1, 28, -41, -1,
	-1, 19, -47, 27, 22, -1, 25, -1, 29, -65, 21, -1, 8, 9, 1, 2, 3, 4, 24, 6, 5,
	7, 26, -2, -1, -2,
}

var tmLalr = []int32{
	4, -1, 10, -1, 11, -1, 13, -1, 14, -1, 15, -1, 16, -1, 2, 32, -1, -2, 4, -1,
	10, -1, 11, -1, 13, -1, 14, -1, 15, -1, 16, -1, 2, 32, 5, 31, -1, -2, 7, -1,
	5, 30, -1, -2, 4, -1, 10, -1, 11, -1, 13, -1, 14, -1, 15, -1, 16, -1, 2, 32,
	-1, -2, 4, -1, 10, -1, 11, -1, 13, -1, 14, -1, 15, -1, 17, -1, 2, 32, -1, -2,
}

var tmGoto = []int32{
	0, 2, 2, 6, 12, 20, 22, 24, 28, 28, 28, 40, 48, 48, 56, 64, 72, 78, 80, 80,
	82, 84, 90, 100, 110, 118, 126, 130, 132, 140, 142, 144,
}

var tmFromTo = []int8{
	43, 44, 2, 15, 13, 19, 15, 20, 19, 23, 26, 29, 1, 3, 3, 3, 21, 3, 28, 3, 18,
	22, 24, 28, 17, 21, 26, 30, 1, 4, 3, 4, 19, 24, 21, 4, 28, 31, 30, 24, 1, 5,
	3, 5, 21, 5, 28, 32, 1, 6, 3, 6, 21, 6, 28, 33, 1, 7, 3, 7, 21, 7, 28, 34, 1,
	8, 3, 8, 21, 8, 28, 35, 1, 9, 3, 9, 21, 9, 28, 36, 1, 43, 28, 37, 1, 10, 3,
	16, 21, 27, 0, 42, 1, 11, 3, 11, 21, 11, 28, 38, 0, 2, 1, 2, 3, 2, 21, 2, 28,
	2, 1, 12, 3, 12, 21, 12, 28, 39, 1, 13, 3, 13, 21, 13, 28, 13, 19, 25, 30,
	41, 19, 26, 1, 14, 3, 14, 21, 14, 28, 40, 3, 17, 3, 18,
}

var tmRuleLen = []int8{
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 4, 3, 0, 3, 1,
	3, 3, 1, 3, 1, 0, 0,
}

var tmRuleSymbol = []int32{
	19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21,
	22, 23, 24, 24, 25, 26, 27, 27, 28, 29, 29, 30, 30, 25,
}

// set(first JSONValue<+A>) = LBRACE
var Literals = []int32{
	2,
}

// set(follow error) = 
var afterErr = []int32{
}
