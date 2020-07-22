FROM openjdk:15-jdk-alpine
LABEL maintainer="liu--_--mianzhi@sjtu.edu.cn"
WORKDIR /app
COPY target/*.jar /app
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
EXPOSE 4000:8080
