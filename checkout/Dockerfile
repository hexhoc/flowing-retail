FROM openjdk:17-jdk-alpine
VOLUME /tmp
ADD build/libs/checkout-1.0.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar