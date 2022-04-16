FROM openjdk:11.0.6-jre
MAINTAINER Buon SUI <boonsuli@gmail.com>
VOLUME /tmp
RUN mkdir -p /app/
WORKDIR /app
COPY ./target/trailerplan-user-api.jar /app/trailerplan-user-api.jar
CMD ["/usr/local/openjdk-11/bin/java", "-Dspring.profiles.active=dev-local-bd-memory-hsql", "-jar", "/app/trailerplan-user-api.jar"]
