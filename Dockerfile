FROM bellsoft/liberica-openjdk-alpine:17
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS=""
ENV JDBC_URL=""
ENV JDBC_USERNAME=""
ENV JDBC_PASSWORD=""
ENTRYPOINT exec java ${JAVA_OPTS} -jar /app.jar
