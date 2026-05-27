FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/product-service-0.0.1-SNAPSHOT.jar product-service-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "product-service-0.0.1-SNAPSHOT.jar"]