FROM eclipse-temurin:11-alpine

RUN apk update && apk add yt-dlp
RUN mkdir /opt/app /config
COPY /build/libs/app.jar /opt/app

CMD ["java", "-jar", "/opt/app/app.jar"]

