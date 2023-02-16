FROM openjdk:11

RUN mkdir /discovery

WORKDIR /discovery

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ./mvnw spring-boot:run -Dspring-boot.run.profiles=release