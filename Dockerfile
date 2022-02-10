FROM adoptopenjdk/openjdk11
LABEL maintainer="mdevmann@yahoo.com"
VOLUME /rest-app
ADD build/libs/springrestfilelogger-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]
