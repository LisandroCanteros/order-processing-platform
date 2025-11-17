#!/bin/bash

docker exec -it $(docker ps --filter "ancestor=confluentinc/cp-kafka:7.5.0" -q) \
  kafka-topics --create --topic order-created \
  --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

docker exec -it $(docker ps --filter "ancestor=confluentinc/cp-kafka:7.5.0" -q) \
  kafka-topics --create --topic payment-confirmed \
  --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
