package com.github.cliffdurden.avro.statestore.exception.app;

import com.github.cliffdurden.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.function.BiFunction;

@SpringBootApplication
@Slf4j
public class AvroStateStoreExceptionApplication {

    private final ValueJoiner<Input1Avro, Input2Avro, OutputAvro> joiner = (input1, input2) ->
    {
        log.info("start join: input1={}, input2={}", input1, input2);
        return OutputAvro.newBuilder()
                .setSum1(input1.getSum())
                .setSum2(input2.getSum())
                .build();
    };

    public static void main(String[] args) {
        SpringApplication.run(AvroStateStoreExceptionApplication.class, args);
    }

    @Bean
    public BiFunction<KStream<String, Input1Avro>, KStream<String, Input2Avro>, KStream<String, OutputAvro>> processStream() {
        return (input1, input2) ->
                input1
                        .peek((k, v) -> log.info("start process record with key: {}, value: {}", k, v))
                        .join(input2, joiner, JoinWindows.of(Duration.ofSeconds(1)))
                        .toTable(Materialized.as("store"))
                        .toStream()
                        .peek((k, v) -> log.info("result: {}", v));
    }
}