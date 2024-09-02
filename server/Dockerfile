FROM openjdk:21-jdk

WORKDIR /app

COPY build/libs/server-0.0.1-SNAPSHOT.jar ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
