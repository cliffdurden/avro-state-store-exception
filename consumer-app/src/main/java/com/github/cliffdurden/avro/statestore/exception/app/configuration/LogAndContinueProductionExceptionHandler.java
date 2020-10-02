package com.github.cliffdurden.avro.statestore.exception.app.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;

import java.util.Map;

@Slf4j
public class LogAndContinueProductionExceptionHandler implements ProductionExceptionHandler {

    @Override
    public ProductionExceptionHandlerResponse handle(ProducerRecord<byte[], byte[]> record, Exception e) {
        log.error("error while serializing message. topic: {}, partition: {}, timestamp: {}",
                record.topic(),
                record.partition(),
                record.timestamp(), e);
        return ProductionExceptionHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.info("configure");
    }
}
