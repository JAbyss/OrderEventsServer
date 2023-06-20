
FROM openjdk:17.0.1-jdk-slim
RUN mkdir -p /app/

WORKDIR /app

COPY . /app/
ENV TZ=Asia/Yekaterinburg

CMD ["java", "-jar", "OrderServer-0.0.1-SNAPSHOT.jar"]