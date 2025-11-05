# the base image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ARG WAR_FILE=target/VoteEase-0.0.1-SNAPSHOT.war
COPY ${WAR_FILE} application.war
EXPOSE 8080

# Running the application (2GB MAX HEAP SIZE)
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app/application.war"]