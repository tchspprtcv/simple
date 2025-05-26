# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the POM file to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application's source code
COPY src ./src

# Package the application (skip tests to speed up build, tests should run in CI)
RUN ./mvnw package -DskipTests

# Make port 8084 available to the world outside this container
EXPOSE 8084

# Run the JAR file 
# The JAR name will be based on artifactId and version from pom.xml
ENTRYPOINT ["java", "-jar", "target/config-service-0.0.1-SNAPSHOT.jar"]
