all:
	javac -d . src/*.java

run: all
	java SimulatorTest config.txt move.txt

clean:
	del /Q *.class