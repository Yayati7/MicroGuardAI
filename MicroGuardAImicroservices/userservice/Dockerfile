FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/userservice-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Duser.timezone=Asia/Kolkata", "-Xmx384m", "-Xms128m", "-jar", "app.jar"]