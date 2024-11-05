FROM maven:3.6.3-openjdk-11 as build
WORKDIR /app
COPY . .
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/kaddem-1.1.3.jar /app/
EXPOSE 9055
CMD {"java","jar","kaddem-1.1.3.jar"}
