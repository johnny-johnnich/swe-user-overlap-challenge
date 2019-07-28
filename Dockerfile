FROM openjdk:11-jre-slim
VOLUME /tmp
COPY ./target/swe-user-overlap-challenge-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","swe-user-overlap-challenge-0.0.1-SNAPSHOT.jar"]