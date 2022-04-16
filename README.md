[![Spring Boot Version](https://img.shields.io/badge/springboot-2.1.13-brightgreen.svg)](https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/)
[![Spring Boot Version](https://img.shields.io/badge/hibernate-5.4.12-brightgreen.svg)](https://hibernate.org/orm/releases/5.4/)
[![Spring Boot Version](https://img.shields.io/badge/hibernate_validator-6.0.18-brightgreen.svg)](https://hibernate.org/validator/releases/6.0/)
[![Spring Boot Version](https://img.shields.io/badge/javax_validattion-2.0.1-brightgreen.svg)](https://beanvalidation.org/)
[![Spring Boot Version](https://img.shields.io/badge/hsql-2.4.1-brightgreen.svg)](https://hsql.org)
[![Spring Boot Version](https://img.shields.io/badge/maven_surefire_plugin-3.0.0.M4-brightgreen.svg)](https://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html)
[![Spring Boot Version](https://img.shields.io/badge/maven_failsafe_plugin-3.0.0.M4-brightgreen.svg)](https://maven.apache.org/surefire/maven-failsafe-plugin/integration-test-mojo.html)
[![Spring Boot Version](https://img.shields.io/badge/dockerfile_maven_pluging-1.4.13-brightgreen.svg)](https://github.com/spotify/dockerfile-maven)
[![Spring Boot Version](https://img.shields.io/badge/commons_lang3-3.8.1-brightgreen.svg)](https://commons.apache.org/proper/commons-lang/)
[![Spring Boot Version](https://img.shields.io/badge/commons_lang-2.6-brightgreen.svg)](http://commons.apache.org/proper/commons-lang/)
[![Spring Boot Version](https://img.shields.io/badge/dozer-5.5.1-brightgreen.svg)](https://dozer.sourceforge.net/)
[![Spring Boot Version](https://img.shields.io/badge/joda_time-2.10.05-brightgreen.svg)](https://www.joda.org/joda-time/)
[![Spring Boot Version](https://img.shields.io/badge/jackson-2.10.0-brightgreen.svg)](https://github.com/FasterXML/jackson)
[![Spring Boot Version](https://img.shields.io/badge/guava-26.0_jre-brightgreen.svg)](https://github.com/google/guava/wiki/CollectionUtilitiesExplained)
[![Spring Boot Version](https://img.shields.io/badge/hamcrest-1.3-brightgreen.svg)](https://hamcrest.org/JavaHamcrest/)
[![Spring Boot Version](https://img.shields.io/badge/swagger-2.9.2-brightgreen.svg)](https://swagger.io)
[![Spring Boot Version](https://img.shields.io/badge/docker-18.06.1_ce-brightgreen.svg)](https://www.docker.com)


# Getting Started


- This application is an example of rest api with spring boot and docker. 
    - build on :
        - OS : [Linux Mint v19](https://www.linuxmint.com)
        - JDK : [open jdk 11.0.4](https://openjdk.java.net/projects/jdk/11/)

- features : 
    - rest API for an user : GET, POST, PUT, DELETE
    - unit test for the service layer
    - integration test run with maven failsafe plugin
    - integration test with a memory DB (hsql). The tests are for the layer :
        - repository
        - service
        - controller with spring test mvc
        - controller with rest assured
    - API available in swagger ui at : http://localhost:8080/trailerplan/swagger-ui.html
    - generate the service api for front end development
    - using dozer for mapping : dto <=> entity  
    - validation dto with javax validation
    - search entity with jpa criteria api
    - db schema from hsql-schema.sql and the data hsql-data.sql 
    - build docker images :
        - pull : openjdk:11.0.6-jre, first. We need just the jre
        - with docker-maven-plugin from spotify by profile docker-spotify
        - push to local registry
        - using docker file src/docker/Dockerfile
        - size : 358 mo in my environment 

- checkout the source
    - build with : 
        ```
        mvn clean install
        ```
    - run with :
        ``` 
        java -jar target/user-0.0.1-SNAPSHOT.jar
        ``` 
    - check with : 
        - ```curl http://localhost:8080/trailerplan/api/user/1``` the result is : 
            ```json
            {"id":1,"name":"Kilian","shortName":"Jornet","version":1,"creationDate":"2020-01-20@22:00:00","modificationDate":"2020-01-20@22:00:00","userName":"kjornet","password":"kjornet","mail":"kilian.jornet@themail.com","birthday":"1987-10-26@23:00:00","street":"1 placa Del Sol","zipCode":"17600","city":"Figueras","country":"Espagne"}
            ```
        - in a browser : http://localhost:8080/trailerplan/swagger-ui.html, the swagger api
        
    - run integration test : 
        ```
        mvn clean verify -P integration-test
        ```
        only the integration tests are running, the *IT.java in repository, service and controller layer
        
    - run the docker images with : 
      ```
      docker run --rm --name trailerplan-user-api -p 8080:8080 trailerplan-user-api
      ```
      then all the feature are available from the docker images

