FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/aianalysisservice-*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-Xmx384m", "-Xms128m", "-jar", "app.jar"]