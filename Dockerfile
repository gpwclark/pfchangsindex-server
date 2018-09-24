FROM clojure:latest
MAINTAINER gpwclark@gmail.com

# install ubuntu prereqs to install nfd
RUN apt-get update && apt-get install -y \
  vim

#ENV APP_FOLDER /opt/pfchangsindex-server

EXPOSE 8888

RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN mkdir -p /opt/pfchangsindex-server
ADD . /opt/pfchangsindex-server
WORKDIR /opt/pfchangsindex-server

RUN lein uberjar
ENV APP $APP_FOLDER/target/pfchangsindex-server-0.1.0-SNAPSHOT-standalone.jar
RUN echo "$APP"
#ENTRYPOINT ["java", "-jar", "$APP"]
ENTRYPOINT ["java", "-jar", "/opt/pfchangsindex-server/target/pfchangsindex-server-0.1.0-SNAPSHOT-standalone.jar"]
#CMD ["-jar", "$APP"]
