#!/usr/bin/env bash
#algs4="/Users/kdorman/Documents/algs4-2/algs4.jar"
algs4="/Users/kyledorman/algs4-2/algs4.jar"
junit411="/Applications/IntelliJ IDEA 15 CE.app/Contents/lib/junit-4.12.jar"
junit="/Applications/IntelliJ IDEA 15 CE.app/Contents/lib/junit.jar"
hamcrest="/Applications/IntelliJ IDEA 15 CE.app/Contents/lib/hamcrest-core-1.3.jar"

dependencies=".:$algs4:$junit411:$junit:$hamcrest"

srcLocation="src"
resources="../resources/baseball";

zipFileName="baseball-testing.zip"

echo "***Running Checkstyle:"
checkstyle-algs4 $srcLocation/*.java

echo "***Compiling Java(c):"
javac -cp "$dependencies" $srcLocation/*.java -Xlint:deprecation

echo "***Running tests:"
cd $srcLocation
echo "BaseballElimination:"
java -cp "$dependencies" BaseballElimination $resources/teams7.txt

#echo "***Zipping files for submission:"
rm -f "$zipFileName" && zip -r "$zipFileName" "BaseballElimination.java"
