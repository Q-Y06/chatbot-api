FROM eclipse-temurin:8-jdk
LABEL maintainer="crx"

WORKDIR /app
COPY ./chatbot-api-interfaces/target/chatbot-api.jar /app/app.jar

EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/app.jar"]
