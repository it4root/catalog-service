FROM openjdk:8-jdk-alpine
COPY target/catalog-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","/catalog-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8083