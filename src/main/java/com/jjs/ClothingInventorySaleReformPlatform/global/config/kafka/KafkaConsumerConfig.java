package com.jjs.ClothingInventorySaleReformPlatform.global.config.kafka;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.dto.ChatMessageDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.group-id}") // 동적 할당으로 변경 필요
    private String groupId;

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    /**
     * kafka에서 메시지를 소비하는 컨슈머를 생성하는 팩토리의 인터페이스
     */
    @Bean
    public ConsumerFactory<String, ChatMessageDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigurations()); // 설정된 프로퍼티를 사용하여 인스턴스를 생성하고 반환.
                                                        // 이 팩토리는 설정에 따른 kafka 컨슈터 인스턴스 생성하는데 사용됨.
    }

    private Map<String, Object> consumerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();// kafka 컨슈머 설정을 저장할 저장소.
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);// kafka 클러스터와 연결하기 위한 서버 목록 지정
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);// 컨슈머가 속할 컨슈머 그룹의 ID를 지정
        /**
         * kafka에서 메시지 키와 값은 바이트 배열로 전송됨.
         * 이 바이트 배열을 Java의 String 객체로 역직렬화(Deserialize) 진행
         * 즉, 메시지의 키와 값이 문자열 형태로 전송되었을 때 이를 올바르게 처리하기 위한 설정임.
         */
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JsonDeserializer.class);
        configurations.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
        configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest"); // earliest: 전체 , latest: 최신 메시지
        return configurations;
    }

    /**
     * kafka 메시지 리스너 생성 클래스
     * key, value의 타입이 모두 String = 메시지의 키와 값이 모두 문자열 형태
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory()); // kafka 컨슈머 팩토리 설정
        // consumerFactory() -> ConsumerFactory 객체 반환함. 이 객체는 kafka 컨슈머 인스턴스 생성에 필요한 설정 정보 가지고 있음.
        // 따라서 이 설정은 kafka 메시지를 어떻게 소비할 것인지 결정함.
        return factory;
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic(topic, 1, (short) 1);
    }
}
