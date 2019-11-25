FROM maven:3.5.4-jdk-10-slim
COPY . .
EXPOSE 8081
CMD mvn spring-boot:run
