#!/bin/bash

killbg() {
    for p in "${pids[@]}" ; do
        kill "$p";
    done
}

trap killbg EXIT

pids=()

echo "Uruchamianie Spring Boot..."
./gradlew bootRun > /dev/null 2>&1 & 
pids+=($!)
gradlew_pid=$!

sleep 5

echo "Uruchamianie aplikacji React..."
cd front/weather-app
npm install > /dev/null 2>&1
npm start > /dev/null 2>&1 &
pids+=($!)
npm_pid=$!

while true; do
	echo "Aby zakończyć, wprowadź CTRL + C"
   	read input
done

kill $npm_pid $gradlew_pid > /dev/null 2>&1

