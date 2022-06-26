package th.co.scb.assignment.svcassignmentproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import th.co.scb.assignment.svcassignmentproject.assignment.service.Producer;

import javax.websocket.SendResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ProducerTest {

    @Mock
    KafkaTemplate kafkaTemplate;
    @InjectMocks
    Producer producer;

    @Test
    void sendMessage() {
        when(kafkaTemplate.send(any(), any())).thenReturn(mock(ListenableFuture.class));
        producer.sendMessage("Hello Test");
        verify(kafkaTemplate).send(any(), any());
    }
}
