FROM openjdk:17
VOLUME /tmp
ADD build/libs/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar