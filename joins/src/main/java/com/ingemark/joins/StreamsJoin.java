package com.ingemark.joins;


import com.ingemark.joins.config.AppConfig;
import com.ingemark.joins.joiner.PromotionJoiner;
import com.ingemark.joins.pojo.CoffeePurchase;
import com.ingemark.joins.pojo.Promotion;
import com.ingemark.joins.pojo.ShopPurchase;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Repartitioned;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.HashMap;

import static org.apache.kafka.clients.consumer.ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class StreamsJoin {

    private static final Logger logger = LoggerFactory.getLogger(StreamsJoin.class);

    private final AppConfig appConfig;

    public StreamsJoin(AppConfig appConfig){
        this.appConfig = appConfig;
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        var props = new HashMap<String, Object>();
        props.put(APPLICATION_ID_CONFIG, appConfig.getApplicationId());
        props.put(BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        props.put(ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, Promotion> kStream(StreamsBuilder streamsBuilder,
                                                 Serde<String> stringSerde,
                                                 JsonSerde<ShopPurchase> shopPurchaseJsonSerde,
                                                 JsonSerde<CoffeePurchase> coffeePurchaseJsonSerde,
                                                 JsonSerde<Promotion> promotionJsonSerde){
        var shopPurchaseStream = streamsBuilder
                .stream(appConfig.getPurchaseSourceTopic(), Consumed.with(stringSerde, shopPurchaseJsonSerde))
                .peek((k, v) -> logger.info("Received new purchase from the shop: {}", v));

        var coffeePurchaseStream = streamsBuilder
                .stream(appConfig.getCoffeePurchaseSourceTopic(),Consumed.with(stringSerde, coffeePurchaseJsonSerde))
                .peek((k,v) -> logger.info("Received new coffee purchase: {}", v));

/*        final Repartitioned<String, CoffeePurchase> repartitioned = Repartitioned
                .with(
                stringSerde,
                coffeePurchaseJsonSerde)
                .withName("coffee-throughput-repartition")
                .withNumberOfPartitions(3);

        var repartitionedCoffeePurchaseStream = coffeePurchaseStream
                .repartition(repartitioned);*/

        var promotionJoiner = new PromotionJoiner(appConfig);
        var oneMinuteJoin = JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(45));

        var joinedStream = shopPurchaseStream.join(
                coffeePurchaseStream,
                //repartitionedCoffeePurchaseStream,
                promotionJoiner,
                oneMinuteJoin,
                StreamJoined.with(stringSerde, shopPurchaseJsonSerde, coffeePurchaseJsonSerde)
                        .withName("promotion-join").withStoreName("promotion-join-store"));

        joinedStream.peek((key, value) -> logger.info("key[{}] value[{}]", key, value))
                .to(appConfig.getPromotionSinkTopic(), Produced.with(stringSerde, promotionJsonSerde));

        logger.info("topology: {}", streamsBuilder.build().describe());

        return joinedStream;

    }

    @Bean
    public Serde<String> stringSerde() {
        return Serdes.String();
    }

    @Bean
    public JsonSerde<ShopPurchase> shopPurchaseJsonSerde() {
        return new JsonSerde<>(ShopPurchase.class);
    }

    @Bean
    public JsonSerde<CoffeePurchase> coffeePurchaseJsonSerde() {
        return new JsonSerde<>(CoffeePurchase.class);
    }

    @Bean
    public JsonSerde<Promotion> promotionJsonSerde() {
        return new JsonSerde<>(Promotion.class);
    }
}
