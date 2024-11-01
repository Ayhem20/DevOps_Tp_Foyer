FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD tp-foyer-5.0.4.jar tp-foyer-5.0.4.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer-5.0.4.jar"]
