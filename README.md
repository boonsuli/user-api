# Getting Started

- This application is an example of rest api with spring boot and docker. 
    - version of the components : 
        - [spring boot 2.1.13.Final](https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/)
        - [hibernate : 5.4.12.Final](https://hibernate.org/orm/releases/5.4/)
        - [hibernate-validator : 6.0.18.Final](https://hibernate.org/validator/releases/6.0/)
        - [javax-validation : 2.0.1.Final](https://beanvalidation.org/)
        - [hsql : 2.4.1](http://hsqldb.org/)
        - [maven-surefire-plugin : 3.0.0.M4](https://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html)
        - [maven-failsafe-plugin : 3.0.0-M4](https://maven.apache.org/surefire/maven-failsafe-plugin/integration-test-mojo.html)
        - [dockerfile-maven-plugin : 1.4.13](https://github.com/spotify/dockerfile-maven)
        - [commons-lang3 : 3.8.1](http://commons.apache.org/proper/commons-lang/)
        - [commons-lang : 2.6](http://commons.apache.org/proper/commons-lang/)
        - [dozer : 5.5.1](http://dozer.sourceforge.net/)
        - [joda-time : 2.10.5](https://www.joda.org/joda-time/)
        - [jackson : 2.10.0](https://github.com/FasterXML/jackson)
        - [guava : 26.0-jre](https://github.com/google/guava/wiki/CollectionUtilitiesExplained)
        - [hamcrest : 1.3](http://hamcrest.org/JavaHamcrest/)
        - [swagger : 2.9.2](https://swagger.io/)
        - [docker 18.06.1-ce](https://www.docker.com/)

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
      then all the feature are available from the docker images ;-)

    - enjoy ;-)
