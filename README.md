# game-data Project

Game data service fetches store data from multiple online game stores (steam, gog)
and converts them to lists for further processing. 

It offers 2 REST endpoints:

Get games by search string:
GET
example url: http://localhost:8080/gog-parser/api/v1/gog/game?searchString=witcher

Get game prices:
POST
example url: http://localhost:8080/game-data/api/v1/gamedata/price?
+ object PriceRequest

Get games by search string:
GET
example url: http://localhost:8080/game-data/api/v1/gamedata/game?searchString=witcher

This project uses Quarkus, the Supersonic Subatomic Java Framework.

To run the project locally in docker run commands:

## Docker build
```shell script
docker build -f Dockerfile.jvm -t tjasad/rso-game-data .
```

## Docker run

```shell script
docker run -i --rm -p 8080:8080 tjasad/rso-game-data
```

## Docker push
```shell script
docker push tjasad/rso-game-data
```

## Dockerhub link

https://hub.docker.com/repository/docker/tjasad/rso-game-data

There is also a deployment file for k8s present which can be used for kubernetes deployment.