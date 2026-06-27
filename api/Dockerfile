FROM eclipse-temurin:17-jdk AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY core/pom.xml core/pom.xml
COPY cli/pom.xml cli/pom.xml
COPY api/pom.xml api/pom.xml
COPY core/src core/src
COPY api/src api/src

RUN ./mvnw -B -ntp -pl api -am package -DskipTests

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

RUN mkdir -p /data

COPY --from=build /workspace/api/target/*-api.jar /app/contacts-api.jar

ENV CONTACTS_API_PORT=7070
ENV CONTACTS_CSV_PATH=/data/contacts.csv

EXPOSE 7070

ENTRYPOINT ["java", "-jar", "/app/contacts-api.jar"]
