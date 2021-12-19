FROM openjdk:11-jdk
WORKDIR /app
COPY . /app
RUN ./gradlew --no-daemon build
EXPOSE 8080
CMD ["./gradlew", "--no-daemon", "bootRun"]


