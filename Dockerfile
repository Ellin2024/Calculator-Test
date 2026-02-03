FROM eclipse-temurin:21-jdk
WORKDIR /app
LABEL maintainer="javaguides.net"
ADD target/Calculator-v1.jar calculator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "calculator.jar"]
