SHELL=/bin/bash

build_code:
		mvn clean install -DskipTests -Dlicense.skip=true

up:
		@docker-compose -f docker-compose.yml up -d && docker-compose -f docker-compose.yml logs -f

stop:
		@docker-compose -f docker-compose.yml stop

down:
		@docker-compose  -f docker-compose.yml down --rmi local --volumes

mohkam:
		@docker-compose -f docker-compose.yml build --no-cache  && docker-compose -f docker-compose.yml up --build --force-recreate  -d && docker-compose -f docker-compose.yml logs -f

again:
		@docker-compose -f docker-compose.yml up --build --force-recreate  -d && docker-compose -f docker-compose.yml logs -f

restart:
		@make -k stop
		@make -k down
		@make -k up

clean:
		rm -rf app-logic-db/cloudeon/data/mysql/*

clean_dev:
		rm -rf cloudeon-assembly/target/*
		rm -rf cloudeon-server/target/*
		rm -rf cloudeon-ui/node_modules


bash_engine:
		@docker exec -it eon-engine bash -c "bash"

bash_db:
		@docker exec -it eon-engine bash -c "bash"
