
default: classes

classes: ./src/*.java
	javac -g -d ./bin ./src/*.java

run: classes
	java -cp ./bin MainClassName

clean:
	rm -f ./bin/*.class