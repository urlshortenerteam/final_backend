FROM openjdk:15-jdk-alpine
LABEL maintainer="liu--_--mianzhi@sjtu.edu.cn"
ENV IP2REGION backend/src/main/resources/ip2region.db
WORKDIR /app
COPY backend/target/*.jar /app
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
