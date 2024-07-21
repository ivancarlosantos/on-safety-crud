FROM maven:3.9.1-eclipse-temurin-17-alpine AS build

COPY /src /app/src

COPY /pom.xml /app

RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:17-alpine

LABEL key="app.crud-on-safety"

WORKDIR /usr/src/app

COPY --from=build /app/target/*.jar crud.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "crud.jar"]