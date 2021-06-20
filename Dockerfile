FROM openjdk:16

COPY ./*.jar Test-1.0-SNAPSHOT.jar
EXPOSE 8080

CMD java -jar /Test-1.0-SNAPSHOT.jar