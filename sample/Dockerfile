FROM openjdk:21

WORKDIR /usr/src/app
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run