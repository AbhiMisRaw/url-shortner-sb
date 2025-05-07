# maven image with java 23 to build Spring boot app
FROM eclipse-temurin:17-jdk AS build

# setting the working directory

WORKDIR /app

COPY mvnw ./
COPY .mvn/ .mvn/

RUN chmod +x mvnw

# copy all requiered files like pom.xml and install dependencies
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# copy source code
COPY src ./src
RUN ./mvnw clean package -DskipTests

# use java 23 runtime to run the app
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Exposing the port
EXPOSE 8080

# specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
