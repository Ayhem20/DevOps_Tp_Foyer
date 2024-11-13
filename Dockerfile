# Utiliser une image de base avec Java 17
FROM openjdk:17-jdk-slim

# Définir les variables d'environnement pour l'application
ENV SPRING_PROFILES_ACTIVE=prod

# Créer un répertoire pour l'application
WORKDIR /app

# Copier le fichier JAR de la machine locale vers l'image Docker
COPY target/tp-foyer-5.0.0.jar /app/tp-foyer-5.0.0.jar

# Exposer le port de l'application
EXPOSE 8089

# Définir le point d'entrée pour l'application
ENTRYPOINT ["java", "-jar", "/app/tp-foyer-5.0.0.jar"]
