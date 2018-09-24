FROM java:8
MAINTAINER gpwclark@gmail.com

# install ubuntu prereqs to install nfd
RUN apt-get update && apt-get install -y \
  vim

EXPOSE 6363

ENV PFCHANGS_APP_PATH
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN mkdir -p /opt/pfchangsindex-server
ADD . /opt/pfchangsindex-server

ENTRYPOINT ["/usr/bin/nfd"]
CMD ["-c", "/etc/ndn/nfd.conf"]
