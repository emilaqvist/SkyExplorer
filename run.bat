@echo off
echo Starting SkyExplorer (Javalin)...

echo ---------------------------------------------
echo Opening browser and navigating to:
echo http://localhost:7000/...
echo ---------------------------------------------
start http://localhost:7000/
java -jar target/SkyExplorer-1.1.0-jar-with-dependencies.jar

pause
