# define base docker image
FROM openjdk:latest

ADD target/advend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]

EXPOSE 8080