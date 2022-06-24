package th.co.scb.assignment.svcassignmentproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.assignment.svcassignmentproject.assignment.service.Consumer;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

public class ConsumerTest {

    @Mock
    Consumer consumer;

    @Test
    public void consume() throws IOException {
        consumer.consume("Hello Test!");
        verify(consumer).consume(any());
    }

}
