$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties

$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties

$KAFKA_HOME/bin/kafka-topics.sh --create --topic purchase --zookeeper localhost:2181 --partitions 3 --replication-factor 1

$KAFKA_HOME/bin/kafka-topics.sh --create --topic enriched-purchase --zookeeper localhost:2181 --partitions 3 --replication-factor 1
