## CSC435 Web Services Assignment 2

Description of the project can be found in the `INSTRUCTIONS.md` file.

* This project is moreso for personal use because it persists a local MySQL database. If you clone this repo and already have MySQL and JDBC installed you can change the DB Connection information in `src/main/java/mainpackage/resources/Controller.java`

# Install Tomcat
* To run ths program Apache Tomcat must be installed
* To install go to the [Apache Tomcat download page](https://tomcat.apache.org/download-80.cgi) under the latest version (currenty 8.5.27) and select the file type under "Core Distributions" based on your OS
* Extract the file
* Add a PATH variable named TOMCAT_HOME that points to where you installed Tomcat

# Install Maven
* Maven must also be installed for packaging the .war (Web Application Resource) file
* To install Maven go to the [Maven download page](https://maven.apache.org/download.cgi), and select the file type under "Files" based on your OS
* Extract the file

# Maven commands, and stop/start Tomcat
* Once Maven is installed the commands in the `scripts` directory should work
* To install the Maven dependencies and plugins run `./scripts/install.sh`
* To clean the target folder run `./scripts/clean.sh`
* To package the .war file run `./scripts/package.sh`
* To deploy the .war file to Tomcat run `./scripts/deploy.sh`
* To start Tomcat run `./scripts/start.sh`
* To stop Tomcat run `./scripts/stop.sh`

# Important Notes
* For information on Java Servlets [here is a good article](http://www.ntu.edu.sg/home/ehchua/programming/java/JavaServlets.html) explaining the basics.
* Tomcat will be running on `localhost:8080/assignment-2` as the base directory of the Web Application