### HOW TO BUILD AND PUSH ####
# docker image build . -t picto-web
# docker tag picto-web:latest alfayohannisyorkacuk/picto-web:latest
# docker push alfayohannisyorkacuk/picto-web:latest

### HOW TO RUN ON WINDOWS ###
# docker run --rm -i -t -v %cd%:/workspace --hostname=picto -p 8080:8080 --name=picto picto-web
# docker run --rm -i -t -v %cd%:/workspace --hostname=picto -p 8080:8080 --name=picto alfayohannisyorkacuk/picto-web

### HOW TO RUN ON LINUX ###
# docker run --rm -i -t -v $PWD:/workspace --hostname=picto -p 8080:8080 --name=picto picto-web
# docker run --rm -i -t -v $PWD:/workspace --hostname=picto -p 8080:8080 --name=picto alfayohannisyorkacuk/picto-web

FROM maven:3-openjdk-11 AS build

COPY . /sources
RUN cd /sources/org.eclipse.epsilon.picto.web && mvn -B clean install

FROM openjdk:11-jre-slim-bullseye AS dist

WORKDIR /program

RUN apt-get update && apt-get install -y \
  graphviz \
  nocache \
  && rm -rf /var/lib/apt/lists/*

COPY --from=build /sources/org.eclipse.epsilon.picto.web/picto.jar /program/picto.jar

ENTRYPOINT ["java", "-jar", "/program/picto.jar"]

