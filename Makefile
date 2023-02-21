#
# Makefile to manage the containers.
# Author: Vladislav Zhuravskiy <hexhoc@gmail.com>
#
.DEFAULT_GOAL:=help
IMAGE_PREFIX=flowing-retail
REPOSITORY=hexhoc
DOCKER_NETWORK=flowing

# show some help
help:
	@echo ''
	@echo '  Usage:'
	@echo '    make <target>'
	@echo ''
	@echo 'Common Targets:'
	@echo '	create-network		Create the Docker network for the containers'
	@echo '	build		Build gradle projects'
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
	cd microservices && sh gradlew clean build

up:
	cd docker-compose/flowing-retail && docker compose -p ${IMAGE_PREFIX} up -d --build

up-third-party:
	cd docker-compose/third-party && docker compose up -d --build

stop:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} stop

restart: stop run

status:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} ps

build-images:
	cd microservices/gateway-service && docker build -t ${REPOSITORY}/flowing-retail-gateway-service .
	cd microservices/customer-service && docker build -t ${REPOSITORY}/flowing-retail-customer-service .
	cd microservices/inventory-service && docker build -t ${REPOSITORY}/flowing-retail-inventory-service .
	cd microservices/monitor-service && docker build -t ${REPOSITORY}/flowing-retail-monitor-service .
	cd microservices/order-service && docker build -t ${REPOSITORY}/flowing-retail-order-service .
	cd microservices/payment-service && docker build -t ${REPOSITORY}/flowing-retail-payment-service .
	cd microservices/shipping-service && docker build -t ${REPOSITORY}/flowing-retail-shipping-service .
	cd microservices/frontend && docker build -t ${REPOSITORY}/flowing-retail-frontend .

clean-images:
	cd docker-compose && docker compose -p ${IMAGE_PREFIX} down --rmi local

clean-orphan-images:
	@docker rmi $(docker images --quiet --filter "dangling=true")

clean-network:
	@docker network rm ${DOCKER_NETWORK}