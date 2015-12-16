all:	compile
	$(info Running the program)
	java SpeedJesterMain

compile:
	$(info Compiling)
	javac -d ./ ./SpeedJester/src/*.java