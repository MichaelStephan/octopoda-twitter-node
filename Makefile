M2_REPOSITORY=~/.m2/repository/

all: build-img twitter-node 

twitter-node-internal:
	lein npm install && \
	lein cljsbuild once prod

build-img-arm:
	docker build -t octopoda/octopoda-twitter-node-build-arm -f Dockerfile_build_arm .

build-img:
	docker build -t octopoda/octopoda-twitter-node-build -f Dockerfile_build .

twitter-node-arm:
	docker run -v `pwd`:/home/octopoda-build/project -v ${M2_REPOSITORY}:/home/octopoda-build/.m2/repository/ octopoda/octopoda-twitter-node-build-arm make clean twitter-node-internal && \
	docker build -t octopoda/octopoda-twitter-node-arm -f Dockerfile_run_arm .

twitter-node:
	docker run -v `pwd`:/home/octopoda-build/project -v ${M2_REPOSITORY}:/home/octopoda-build/.m2/repository/ octopoda/octopoda-twitter-node-build make clean twitter-node-internal && \
	docker build -t octopoda/octopoda-twitter-node-x86 -f Dockerfile_run .

clean:
	lein clean

clean-npm:
	rm -rf node_modules
