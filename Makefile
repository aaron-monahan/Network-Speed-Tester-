all:	compile
	$(info Running the program)
	sudo java SpeedJesterMain

compile:
	$(info Compiling)
	sudo javac -d ./ ./SpeedJester/src/*.java
