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

### INSTALLING LOCAL PLANTUML 
# mvn install:install-file -Dfile=D:/PROJECTS/picto-web/org.eclipse.epsilon.picto.web/jars/plantuml-2018.8.jar -DgroupId=net.sourceforge.plantuml  -DartifactId=plantuml -Dversion=2018.8 -Dpackaging=jar -DgeneratePom=true

#FROM openjdk:18-ea-11-jdk-alpine3.15
FROM openjdk:11-jre-slim-bullseye
#WORKDIR /program/picto
#COPY picto .
WORKDIR /program
#COPY socialnetwork.ecore .
#COPY socialnetwork.emf .
#COPY socialnetwork.flexmi .
COPY picto.jar .
#WORKDIR /workspace
RUN apt-get update -y && \
  apt-get install -y graphviz nocache && \
  apt-get -y autoremove
ENTRYPOINT ["java", "-jar", "/program/picto.jar"]

