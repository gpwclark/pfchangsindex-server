FROM clojure:latest
MAINTAINER gpwclark@gmail.com

RUN apt-get update && apt-get install -y \
  vim

EXPOSE 8888

RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN mkdir -p /opt/pfchangsindex-server
ADD . /opt/pfchangsindex-server
WORKDIR /opt/pfchangsindex-server

RUN lein uberjar
ENTRYPOINT ["java", "-jar", "/opt/pfchangsindex-server/target/pfchangsindex-server-0.1.0-SNAPSHOT-standalone.jar"]
