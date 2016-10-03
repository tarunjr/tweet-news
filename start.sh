bin/zookeeper-server-start.sh -daemon config/zookeeper.properties 
bin/kafka-server-start.sh -daemon config/server.properties
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic TweetEvents
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic TweetEvents --partitions 3 --replication-factor 1
