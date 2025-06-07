#!/bin/bash

echo "Starting SkyExplorer (Javalin)..."
echo "---------------------------------------------"
echo "Opening browser and navigating to:"
echo "http://localhost:7000/..."
echo "---------------------------------------------"

if which xdg-open > /dev/null; then
  xdg-open http://localhost:7000/
elif which open > /dev/null; then
  open http://localhost:7000/
else
  echo "Could not open your browser automatically. Go into a browser (Preferably: Microsoft Edge or Google Chrome and navigate to this address: http://localhost:7000/ "
fi

# Starta JAR-filen
java -jar target/SkyExplorer-1.1.0-jar-with-dependencies.jar
