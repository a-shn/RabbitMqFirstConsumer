package ru.itis.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import ru.itis.models.UserData;
import ru.itis.services.DischargeLetterGenerator;
import ru.itis.services.LetterGenerator;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String EXCHANGE_NAME = "letters_exchange";

    public static void main(String[] args) {
        LetterGenerator letterGenerator = new DischargeLetterGenerator();
        ObjectMapper objectMapper = new ObjectMapper();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String queue = channel.queueDeclare().getQueue();

            channel.queueBind(queue, EXCHANGE_NAME, "");
            channel.basicQos(2);
            channel.basicConsume(queue, false, (consumerTag, message) -> {
                try {
                    UserData userData = objectMapper.readValue(new String(message.getBody()), UserData.class);
                    System.out.println("generating PDF");
                    String filename = UUID.randomUUID().toString() + ".pdf";
                    letterGenerator.generateLetter(userData, filename);
                    System.out.println(filename + " created");
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                } catch (IOException e) {
                    System.err.println("FAILED");
                    channel.basicPublish("", "fail_logs", null, message.getBody());
                    channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                }
            }, consumerTag -> {});
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
