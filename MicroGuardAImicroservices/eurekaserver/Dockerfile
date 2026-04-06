FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/eurekaserver-*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]