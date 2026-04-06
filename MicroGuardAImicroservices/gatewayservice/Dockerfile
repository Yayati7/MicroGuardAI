FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/gatewayservice-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx384m", "-Xms128m", "-jar", "app.jar"]