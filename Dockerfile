# Use a base image with Java 17
FROM openjdk:17-jdk-slim

# Set environment variables for the application
ENV SPRING_PROFILES_ACTIVE=prod

# Create a directory for the application
WORKDIR /app

# Copy the JAR file from the local machine to the Docker image
COPY target/tp-foyer-6.0.0.jar /app/tp-foyer-6.0.0.jar

# Expose the application port
EXPOSE 8089

# Define the entry point for the application
ENTRYPOINT ["java", "-jar", "/app/tp-foyer-6.0.0.jar"]
