call C:\Apps\Tomcat\apache-tomcat-10.0.13\bin\shutdown.bat
call mvn clean install
xcopy /y .\target\SalAnbToyStore.war C:\Apps\Tomcat\apache-tomcat-10.0.13\webapps
call C:\Apps\Tomcat\apache-tomcat-10.0.13\bin\startup.bat