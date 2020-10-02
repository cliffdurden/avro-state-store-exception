package com.github.cliffdurden.avro.statestore.exception.dummy.producer;

import com.github.cliffdurden.*;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.math.BigDecimal;
import java.util.*;

public class DummyProducerApp {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("acks", "0");
        properties.setProperty("retries", "10");
        properties.setProperty("client.id", "dummy");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        Input1Avro input1Avro = Input1Avro.newBuilder()
                .setSum(new BigDecimal("100"))
                .build();
        Input2Avro input2Avro = Input2Avro.newBuilder()
                .setSum(new BigDecimal("200"))
                .build();

        String key = UUID.randomUUID().toString();

        KafkaProducer<String, Input1Avro> kafkaProducer1 = new KafkaProducer<>(properties);
        ProducerRecord<String, Input1Avro> input1AvroProducerRecord = new ProducerRecord<>("input1-topic-name", key, input1Avro);
        kafkaProducer1.send(input1AvroProducerRecord, (recordMetadata, e) -> {
            if (e == null) {
                System.out.println("input1AvroProducerRecord sent. topic: " + recordMetadata.topic());
            } else {
                e.printStackTrace();
            }
        });

        KafkaProducer<String, Input2Avro> kafkaProducer2 = new KafkaProducer<>(properties);
        ProducerRecord<String, Input2Avro> input2AvroProducerRecord = new ProducerRecord<>("input2-topic-name", key, input2Avro);
        kafkaProducer2.send(input2AvroProducerRecord, (recordMetadata, e) -> {
            if (e == null) {
                System.out.println("input2AvroProducerRecord sent. topic: " + recordMetadata.topic());
            } else {
                e.printStackTrace();
            }
        });

        kafkaProducer1.flush();
        kafkaProducer2.flush();

        kafkaProducer1.close();
        kafkaProducer2.close();

    }
}
