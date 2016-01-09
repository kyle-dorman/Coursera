#!/usr/bin/env bash
algs4="/Users/kyledorman/algs4/algs4.jar"
junit411="/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/junit-4.11.jar"
junit="/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/junit.jar"
hamcrest="/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/hamcrest-core-1.3.jar"

dependencies=".:$algs4:$junit411:$junit:$hamcrest"

srcLocation="assignment5/src"
testMap="assignment5/src"

zipFileName="kdtree.zip"

cd ~/algs4/

echo "***Running Checkstyle:"
checkstyle-algs4 $srcLocation/*.java

echo "***Compiling Java(c):"
javac -cp "$dependencies" $srcLocation/*.java -Xlint:deprecation

echo "***Running findbugs:"
findbugs-algs4 $srcLocation/*.class

echo "***Running tests:"
cd $srcLocation
echo "***PointTest:"
#java -cp "$dependencies" org.junit.runner.JUnitCore BoardTest

echo "***TestingFile: puzzle11.txt"
#java -cp "$dependencies" -Xmx1600m Solver 8puzzle/puzzle11.txt

echo "***Zipping files for submission:"
rm -f "$zipFileName" && zip -r "$zipFileName" "KdTree.java" "PointSET.java"

cd ../..