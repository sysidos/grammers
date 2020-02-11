TM_DIR=/Users/p21_0044/Desktop/ll/tools/textmapper

all: gen

gen: json.tm
	@${TM_DIR}/tm-tool/libs/textmapper.sh $<
	# @go fmt ./... > /dev/null
	# @go install ./... > /dev/null

clean:
	$(RM) -v listener.go lexer.go lexer_tables.go parser.go parser_tables.go token.go
	$(RM) -rf -v ast/
	$(RM) -rf -v selector/

.PHONY: all gen clean
