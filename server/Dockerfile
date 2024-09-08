# BUILD STAGE
FROM gradle:jdk21-alpine as build

WORKDIR /app

COPY . .

RUN gradle init --type java-application --overwrite 

RUN ./gradlew build -DskipTests=true

# PRODUCTION STAGE
FROM alpine:3.19

RUN apk add openjdk21-jre

WORKDIR /run

COPY --from=build /app/build/libs/*.jar /run/server-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/run/server-0.0.1-SNAPSHOT.jar"]
