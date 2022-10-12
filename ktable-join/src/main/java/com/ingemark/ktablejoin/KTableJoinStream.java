package com.ingemark.ktablejoin;

import com.ingemark.ktablejoin.config.AppConfig;
import com.ingemark.ktablejoin.joiner.EnrichedPaymentValueJoiner;
import com.ingemark.ktablejoin.pojo.CustomerInfo;
import com.ingemark.ktablejoin.pojo.EnrichedPaymentOrder;
import com.ingemark.ktablejoin.pojo.PaymentOrder;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
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
public class KTableJoinStream {

    public static final String CUSTOMER_INFO_STATE_STORE = "customerInfoStore";
    private static final Logger logger = LoggerFactory.getLogger(KTableJoinStream.class);
    private final AppConfig appConfig;

    public KTableJoinStream(AppConfig appConfig) {
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
    public KStream<String, EnrichedPaymentOrder> kStream(StreamsBuilder streamsBuilder,
                                                         Serde<String> stringSerde,
                                                         JsonSerde<PaymentOrder> paymentOrderJsonSerde,
                                                         JsonSerde<CustomerInfo> customerInfoJsonSerde,
                                                         JsonSerde<EnrichedPaymentOrder> enrichedPaymentOrderJsonSerde) {

        KTable<String, CustomerInfo> customerInfoKTable = streamsBuilder
                .table(appConfig.getCustomerInfoSourceTopic(),
                        Materialized.<String, CustomerInfo>as(Stores.persistentKeyValueStore(CUSTOMER_INFO_STATE_STORE))
                                .withKeySerde(stringSerde)
                                .withValueSerde(customerInfoJsonSerde));

        KStream<String, PaymentOrder> paymentOrderKStream = streamsBuilder
                .stream(appConfig.getPaymentOrderSourceTopic(), Consumed.with(stringSerde, paymentOrderJsonSerde))
                .peek((k, v) -> logger.info("Received payment order: {}", v))
                .selectKey((k, v) -> v.getCustomerId());

        var enrichedPaymentOrderKStream = paymentOrderKStream
                .join(customerInfoKTable, new EnrichedPaymentValueJoiner())
                .peek((key, value) -> logger.info("key[{}] value[{}]", key, value));

        enrichedPaymentOrderKStream.to(appConfig.getEnrichedPaymentOrderSinkTopic(), Produced.with(stringSerde, enrichedPaymentOrderJsonSerde));

        logger.info("topology: {}", streamsBuilder.build().describe());

        return enrichedPaymentOrderKStream;
    }

    @Bean
    public Serde<String> stringSerde() {
        return Serdes.String();
    }

    @Bean
    public JsonSerde<PaymentOrder> paymentOrderJsonSerde() {
        return new JsonSerde<>(PaymentOrder.class);
    }

    @Bean
    public JsonSerde<CustomerInfo> customerInfoJsonSerde() {
        return new JsonSerde<>(CustomerInfo.class);
    }

    @Bean
    public JsonSerde<EnrichedPaymentOrder> enrichedPaymentOrderJsonSerde() {
        return new JsonSerde<>(EnrichedPaymentOrder.class);
    }
}
