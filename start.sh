#mvn clean install -DskipTests
#docker build -f docker-files/Dockerfile-api -t diceapi .
docker build -f docker-files/Dockerfile-db -t dicemongo .
docker-compose --file docker-files/docker-compose.yaml up
