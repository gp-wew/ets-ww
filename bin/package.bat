cd %~dp0
cd..

call mvn clean package -X -Dmaven.test.skip=true

cd bin
pause