package com.ingemark.simpletransformer;

import com.ingemark.simpletransformer.config.AppConfig;
import com.ingemark.simpletransformer.pojo.Purchase;
import com.ingemark.simpletransformer.transformer.PurchaseLoyaltyTransformer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

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
public class SimpleTransformerStream {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTransformerStream.class);
    public static final String LOYALTY_STORE_NAME = "loyaltyPointsStore";

    private final AppConfig appConfig;

    public SimpleTransformerStream(AppConfig appConfig){
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
    public KStream<String, Purchase> kStream(StreamsBuilder streamsBuilder,
                                             Serde<String> stringSerde,
                                             JsonSerde<Purchase> purchaseJsonSerde,
                                             StoreBuilder<KeyValueStore<String, Integer>> loyaltyStoreBuilder){

        var purchaseKStream = streamsBuilder
                .stream(appConfig.getPurchaseSourceTopic(), Consumed.with(stringSerde, purchaseJsonSerde))
                .peek((k, v) -> logger.info("Received new record: {}", v));

        streamsBuilder.addStateStore(loyaltyStoreBuilder);

        var transformedStream = purchaseKStream
                .transformValues(() -> new PurchaseLoyaltyTransformer(LOYALTY_STORE_NAME, appConfig), LOYALTY_STORE_NAME);

        transformedStream.print(Printed.<String, Purchase>toSysOut().withLabel("Simple transform with state:"));
        transformedStream.to(appConfig.getEnrichedPurchaseSinkTopic(), Produced.with(stringSerde,purchaseJsonSerde));

        return transformedStream;
    }

    @Bean
    public StoreBuilder<KeyValueStore<String, Integer>> loyaltyStoreBuilder() {
        KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore(LOYALTY_STORE_NAME);
        return Stores.keyValueStoreBuilder(storeSupplier, Serdes.String(), Serdes.Integer());
    }

    @Bean
    public Serde<String> stringSerde() {
        return Serdes.String();
    }

    @Bean
    public JsonSerde<Purchase> purchaseJsonSerde() {
        return new JsonSerde<>(Purchase.class);
    }
}
