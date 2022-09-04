FROM maven:3.6.0-jdk-11
WORKDIR /app
COPY . .

RUN mvn clean package

CMD mvn spring-boot:run