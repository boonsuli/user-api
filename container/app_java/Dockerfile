FROM openjdk:11.0-jre
MAINTAINER Buon SUI <boonsuli@gmail.com>
VOLUME /tmp
RUN mkdir -p /app/
WORKDIR /app
COPY ./target/user-0.0.1-SNAPSHOT.jar /app/trailerplan-user-api.jar
CMD ["/usr/local/openjdk-11/bin/java", "-Dspring.profiles.active=dev-local-bd-memory-hsql", "-jar", "/app/trailerplan-user-api.jar"]
