FROM openjdk:21 AS build

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

FROM openjdk:21

COPY --from=build /app/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "sleep 30 && java -jar /app/app.jar --spring.profiles.active=docker"]