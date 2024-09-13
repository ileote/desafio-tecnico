package com.github.ileote.service.impl;

import com.github.ileote.domain.entity.PagamentoEntity;
import com.github.ileote.service.SqsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


@Service
public class SqsServiceImpl implements SqsService {

    @Value("${aws.sqs.url.parcial}")
    private String QueueUrlparcial;

    @Value("${aws.sqs.url.total}")
    private String QueueUrltotal;

    @Value("${aws.sqs.url.excedente}")
    private String QueueUrlexcedente;

    private final SqsClient sqsClient;

    public SqsServiceImpl(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @Override
    public void enviarParaFilaParcial(PagamentoEntity pagamento) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(QueueUrlparcial)
                .messageBody(pagamento.toString())
                .build());
    }

    @Override
    public void enviarParaFilaTotal(PagamentoEntity pagamento) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(QueueUrltotal)
                .messageBody(pagamento.toString())
                .build());
    }

    @Override
    public void enviarParaFilaExcedente(PagamentoEntity pagamento) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(QueueUrlexcedente)
                .messageBody(pagamento.toString())
                .build());
    }
}
