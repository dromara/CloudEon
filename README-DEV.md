# Readme for Contribution


## Requirements

1. having `docker` and `docker-compose` installed on your system
2. `maven 3.x`


## Steps to take (Windows)

1. clone the repository.
2. `mvn clean install -DskipTests -Dlicense.skip=true`
3. `@docker-compose -f docker-compose.yml build --no-cache  && docker-compose -f docker-compose.yml up --build --force-recreate  -d && docker-compose -f docker-compose.yml logs -f`
4. head to the `http://localhost:7700`.



## Steps to take (UNIX)

1. clone the repository.
2. `make build_code`
3. `make mohkam`
4. head to the `http://localhost:7700`.