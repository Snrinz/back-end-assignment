package th.co.scb.assignment.svcassignmentproject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.assignment.svcassignmentproject.assignment.controller.KafkaController;
import th.co.scb.assignment.svcassignmentproject.assignment.service.Producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaControllerTest {
    @Mock
    Producer producer;
    @InjectMocks
    KafkaController kafkaController;

    @Test
    void sendMessageToKafkaTopic() {
        doNothing().when(producer).sendMessage(any());

        kafkaController.sendMessageToKafkaTopic("Hello Test!");
        verify(producer).sendMessage(any());
    }
}