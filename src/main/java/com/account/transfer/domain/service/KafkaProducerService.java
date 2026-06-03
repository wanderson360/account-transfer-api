package com.account.transfer.domain.service;

import com.account.transfer.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value("${spring.kafka.topic.transactions}")
    private String transactionsTopic;

    public KafkaProducerService(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTransaction(Transaction transaction) {
        try {
            kafkaTemplate.send(transactionsTopic, transaction.getId(), transaction)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Transação publicada no Kafka com sucesso. ID: {}, Tópico: {}", 
                                    transaction.getId(), transactionsTopic);
                        } else {
                            log.error("Erro ao publicar transação no Kafka. ID: {}", 
                                    transaction.getId(), ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Erro ao enviar transação para Kafka. ID: {}", transaction.getId(), e);
        }
    }
}
