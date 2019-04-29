package com.interest.message.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface InterestSource {

    String MESSAGE_OUTPUT = "message_output";

    @Output(InterestSource.MESSAGE_OUTPUT)
    MessageChannel messageOutput();

}