FROM maven:3.6.3-openjdk-11 as build
WORKDIR /app
COPY . .
WORKDIR /app
COPY --from=build /app/target/kaddem-1.1.2.jar /app/
EXPOSE 9055
CMD {"java","jar","kaddem-1.1.2.jar"}
