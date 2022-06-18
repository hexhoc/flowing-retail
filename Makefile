#
# Makefile to manage the containers.
# Author: Vladislav Zhuravskiy <hexhoc@gmail.com>
#
.DEFAULT_GOAL:=help
IMAGE_PREFIX=flowing-retail
REPOSITORY=hexhoc
DOCKER_NETWORK=flowing-retail-network

# show some help
help:
	@echo ''
	@echo '  Usage:'
	@echo '    make <target>'
	@echo ''
	@echo 'Common Targets:'
	@echo '	create-network		Create the Docker network for the containers'
	@echo '	build-images		Build all images from Dockerfiles'
	@echo '	up			Start all containers needed to run the system'
	@echo '	stop			Stop all containers'
	@echo '	restart			Stop + run'
	@echo '	status			Retrieve the status of the containers'
	@echo ''
	@echo 'Clean Targets:'
	@echo '	clean-network		Delete the Docker network'
	@echo '	clean-images		Deletes the created containers and images'
	@echo '	clean-orphan-images	Removes orphan images'
	@echo ''

create-network:
ifeq ($(shell docker network ls | grep ${DOCKER_NETWORK} | wc -l),0)
	echo "Creating docker network ${DOCKER_NETWORK}"
	@docker network create ${DOCKER_NETWORK}
endif

build:
	cd microservices && mvn clean install

up:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} up -d --build

stop:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} stop

restart: stop run

status:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} ps

build-images:
	cd microservices/checkout && docker build -t ${REPOSITORY}/flowing-retail-kafka-checkout .
	cd microservices/inventory && docker build -t ${REPOSITORY}/flowing-retail-kafka-inventory .
	cd microservices/monitor && docker build -t ${REPOSITORY}/flowing-retail-kafka-monitor .
	cd microservices/order-zeebe && docker build -t ${REPOSITORY}/flowing-retail-kafka-order-zeebe .
	cd microservices/payment && docker build -t ${REPOSITORY}/flowing-retail-kafka-payment .
	cd microservices/shipping && docker build -t ${REPOSITORY}/flowing-retail-kafka-shipping .
	cd microservices/frontend && docker build -t ${REPOSITORY}/flowing-retail-kafka-frontend .

clean-images:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} down --rmi local

clean-orphan-images:
	@docker rmi $(docker images --quiet --filter "dangling=true")

clean-network:
	@docker network rm ${DOCKER_NETWORK}