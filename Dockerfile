FROM openjdk:26-ea-oracle
RUN groupadd spring && useradd -m -g spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=docker"]