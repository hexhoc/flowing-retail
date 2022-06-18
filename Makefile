#
# Makefile to manage the containers.
# Author: Vladislav Zhuravskiy <hexhoc@gmail.com>
#
.DEFAULT_GOAL:=help
IMAGE_PREFIX=frz
DOCKER_NETWORK=flowing-retail-network

# show some help
help:
	@echo ''
	@echo '  Usage:'
	@echo '    make <target>'
	@echo ''
	@echo '  Common Targets:'
	@echo '    create-network       Crea la red de Docker para los contenedores'
	@echo '    build                Construye todas las imágenes a partir de los Dockerfiles'
	@echo '    run                  Arranca todos los contenedores necesarios para ejecutar el sistema'
	@echo '    up                   build + run'
	@echo '    stop                 Para los contenedores'
	@echo '    restart              stop + run'
	@echo '    status               Recupera el estado de los contenedores'
	@echo ''
	@echo '  Clean Targets:'
	@echo '    clean-network        Borra la red Docker'
	@echo '    clean-images         Borra las imágenes creadas'
	@echo '    clean-orphan-images  Elimina las imágenes huérfanas'
	@echo ''

create-network:
ifeq ($(shell docker network ls | grep ${DOCKER_NETWORK} | wc -l),0)
	echo "Creating docker network ${DOCKER_NETWORK}"
	@docker network create ${DOCKER_NETWORK}
endif

build:
	cd microservices && mvn clean install
	cd microservices/checkout && docker build -t hexhoc/kbe-coffeehouse-coffee-service .

run:
	@docker-compose -p ${IMAGE_PREFIX} up

up: build run

stop:
	@docker-compose -p ${IMAGE_PREFIX} stop

restart: stop run

status:
	@docker-compose -p ${IMAGE_PREFIX} ps

clean-images:
	@docker-compose -p ${IMAGE_PREFIX} down --rmi local

clean-orphan-images:
	@docker rmi $(docker images --quiet --filter "dangling=true")

clean-network:
	@docker network rm ${DOCKER_NETWORK}