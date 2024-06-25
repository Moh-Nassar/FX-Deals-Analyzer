FROM openjdk:17-jdk-slim as build

EXPOSE 8080

ARG JAR_FILE=target/fx-analyzer-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} fxdealanalyzer.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/fxdealanalyzer.jar"]
