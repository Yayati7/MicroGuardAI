FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/configserver-*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]