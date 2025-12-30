# =========================
# STAGE 1 : BUILD (MAVEN)
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# mvnw doit être exécutable
RUN chmod +x mvnw

# Copier le code source
COPY src src

# Build du JAR
RUN ./mvnw clean package -DskipTests


# =========================
# STAGE 2 : RUN (JAVA)
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copier le JAR depuis le stage build
COPY --from=build /app/target/*.jar app.jar

# Port Spring Boot
EXPOSE 8080

# Lancer l'application
CMD ["java", "-jar", "app.jar"]
