#!/bin/sh
#Aufgabe 1d
SIZE=$((`wc -m < text.txt` / $2))
result=""
echo $SIZE
for i in `seq 1 $2`; do s=$(($i-1)) result+=$(mono Check.exe $1 $((s*$SIZE)) $(($i*$SIZE)))
done
echo $(node filter.js $result)