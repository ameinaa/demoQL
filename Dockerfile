FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# rendre mvnw exécutable (OBLIGATOIRE)
RUN chmod +x mvnw

# build du projet
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# lancer le jar
CMD ["sh", "-c", "java -jar target/*.jar"]
