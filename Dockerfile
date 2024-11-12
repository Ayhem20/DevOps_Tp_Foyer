# Utilisez une image de base avec OpenJDK 17 pour exécuter une application Spring Boot
FROM openjdk:17-jdk

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré dans l'image Docker
COPY target/tp-foyer-1.0.34-SNAPSHOT.jar app.jar

# Exposer le port sur lequel l'application s'exécute
EXPOSE 8089

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
