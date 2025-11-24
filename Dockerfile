#FROM openjdk:21-ea-oracle
#WORKDIR /app
#COPY . .
#RUN ./mvnw install -Pproduction -DskipTests
#RUN cp target/*.jar /app/app.jar
#ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]

# optimized :
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -Pproduction -DskipTests

FROM openjdk:21-ea-oracle
WORKDIR /app
RUN groupadd spring && useradd -m -g spring spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]