FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/customer-service-0.0.1-SNAPSHOT.jar app.jar

ENV DB_HOST=db_host_placeholder
ENV DB_PORT=3306
ENV DB_NAME=db_name_placeholder
ENV DB_USERNAME=db_user_placeholder
ENV DB_PASSWORD=db_password_placeholder

ENTRYPOINT ["java", "-jar", "app.jar"]