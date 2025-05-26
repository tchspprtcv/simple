# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the POM file to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies as a separate layer
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application's source code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file 
ENTRYPOINT ["java", "-jar", "target/api-gateway-0.0.1-SNAPSHOT.jar"]
