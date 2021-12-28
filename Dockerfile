FROM openjdk:11
WORKDIR /opt/server
COPY ./target/order-service-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "server.jar"]