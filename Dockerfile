FROM maven:3.5.4-jdk-10-slim
COPY . .
EXPOSE 8081
ENTRYPOINT mvn spring-boot:run

