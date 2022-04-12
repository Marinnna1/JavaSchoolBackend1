package ru.javaschool.JavaSchoolBackend2.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate template;

    public void publishMessage(@RequestBody CustomMessage message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(simpleDateFormat.format(new Date()));
        template.convertAndSend(MQConfig.EXCHANGE,
                MQConfig.ROUTING_KEY, message);
    }
}
