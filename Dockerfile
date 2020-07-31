FROM openjdk:15-jdk-alpine
LABEL maintainer="liu--_--mianzhi@sjtu.edu.cn"
WORKDIR /app
COPY backend/target/*.jar /app
COPY backend/src/main/resources/ip2region.db /app
ARG IP2REGION=/app/ip2region.db
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
