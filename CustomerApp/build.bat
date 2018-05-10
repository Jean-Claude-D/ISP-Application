@ECHO OFF

dir /s /b *.java > source.list

javac -Xlint:unchecked -Xdiags:verbose -classpath .;.\bin\ojdbc6.jar -d bin @source.list

PAUSE