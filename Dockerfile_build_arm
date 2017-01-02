FROM resin/rpi-raspbian
RUN apt-get update && \
	apt-get install -y nodejs nodejs-legacy oracle-java8-jdk make curl npm python g++ && \
	apt-get clean && \
	rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* 

RUN addgroup octopoda-build && \
	adduser --disabled-password --ingroup octopoda-build --gecos "" octopoda-build

ADD https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein /usr/bin/lein
RUN chmod a+rx /usr/bin/lein 

USER octopoda-build
WORKDIR /home/octopoda-build/project
RUN lein
