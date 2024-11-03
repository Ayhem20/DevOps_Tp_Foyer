FROM openjdk:17-jdk-alpine
EXPOSE 8083
ADD target/*.jar tp-foyer.jar
ENTRYPOINT ["java", "-jar", "tp-foyer.jar"]
