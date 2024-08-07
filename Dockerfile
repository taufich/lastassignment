# Use an official Tomcat image as a base
FROM tomcat:9.0.54-jdk11-openjdk-slim

# Maintainer info
LABEL maintainer="taufichabumugisha@gmail.com"

# Copy the WAR file to the webapps directory of Tomcat
COPY target/UserManagementSystem-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
