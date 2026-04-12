all:
	javac -d . src/*.java

run: all
	java T1Stage3 config.txt move.txt

clean:
	del /Q *.class