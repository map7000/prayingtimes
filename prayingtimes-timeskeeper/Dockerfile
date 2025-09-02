FROM docker.io/library/amazoncorretto:21-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV PORT 8080
EXPOSE $PORT
CMD ["java", "-jar", "app.jar"]
