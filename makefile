all:
	javac *.java

run:
	java T1Stage1 config.txt move.txt

clean:
	rm -f *.class