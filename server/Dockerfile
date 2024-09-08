# BUILD STAGE
FROM gradle:jdk21-alpine as build

WORKDIR /app

# Add build arguments
ARG DATASOURCE_URL
ARG DATASOURCE_USERNAME
ARG DATASOURCE_PASSWORD

# Copy the project files
COPY . .

# Build the project, skipping tests
RUN gradle init --type java-application --overwrite 
RUN ./gradlew build -DskipTests=true

# PRODUCTION STAGE
FROM alpine:3.19

# Install Java Runtime Environment
RUN apk add openjdk21-jre

WORKDIR /run

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar /run/server-0.0.1-SNAPSHOT.jar

# Expose port 8080 for the application
EXPOSE 8080

# Set environment variables for runtime (optional, can also be passed at runtime)
ENV DATASOURCE_URL=${DATASOURCE_URL}
ENV DATASOURCE_USERNAME=${DATASOURCE_USERNAME}
ENV DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/run/server-0.0.1-SNAPSHOT.jar"]
