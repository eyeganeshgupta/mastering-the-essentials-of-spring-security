FROM eclipse-temurin:17

LABEL mentainer="eyeganeshgupta@gmail.com"

WORKDIR /app

COPY target/spring-security-with-jwt-0.0.1-SNAPSHOT.jar /app/spring-security-with-jwt.jar

ENTRYPOINT ["java", "-jar", "spring-security-with-jwt.jar"]