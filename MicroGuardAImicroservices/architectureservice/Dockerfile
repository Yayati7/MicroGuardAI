FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/architectureservice-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Xmx384m", "-Xms128m", "-jar", "app.jar"]