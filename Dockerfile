​
​
FROM maven as build
WORKDIR /app
COPY . .
RUN mvn install

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/kaddem-1.1.2.jar /app/
EXPOSE 9055
CMD {"java","jar","kaddem-1.1.2.jar"}
