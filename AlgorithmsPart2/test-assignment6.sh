#!/usr/bin/env bash
algs4="/Users/kdorman/Documents/algs4-2/algs4.jar"
junit411="/Applications/IntelliJ IDEA 15 CE.app/Contents/lib/junit-4.12.jar"
junit="/Applications/IntelliJ IDEA 15 CE.app/Contents/lib/junit.jar"
hamcrest="/Applications/IntelliJ IDEA 15 CE.app/Contents/lib/hamcrest-core-1.3.jar"

dependencies=".:$algs4:$junit411:$junit:$hamcrest"

srcLocation="src"
testMap="tests"
resources="../resources/wordnet";

zipFileName="wordnet-testing.zip"

cd ~/Documents/algs4-2/

echo "***Running Checkstyle:"
checkstyle-algs4 $srcLocation/*.java

echo "***Compiling Java(c):"
javac -cp "$dependencies" $srcLocation/*.java -Xlint:deprecation
#javac -cp "$dependencies" $testMap/*.java -Xlint:deprecation

echo "***Running findbugs:"
findbugs-algs4 $srcLocation/*.class

echo "***Running tests:"
#cd $testMap
cd ~/Documents/algs4-2/$srcLocation
echo "WordNetTest:"
#java -cp "$dependencies" WordNet #org.junit.runner.JUnitCore WordNetTest
#java -cp "$dependencies" Outcast $resources/synsets.txt $resources/hypernyms3InvalidTwoRoots.txt $resources/outcast5.txt

cd ~/Documents/algs4-2/$srcLocation

#echo "***Zipping files for submission:"
rm -f "$zipFileName" && zip -r "$zipFileName" "WordNet.java" "SAP.java" "Outcast.java"

cd ~/Documents/algs4-2/
