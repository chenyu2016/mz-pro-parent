#!/bin/sh
cd `pwd`;
echo "start proto...";
protoc --java_out=../../java *.proto;
echo "end proto...";
read -n1 -p "Press any key to continue...";
