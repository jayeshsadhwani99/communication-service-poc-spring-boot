package com.example.communication.service.impl;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.service.CommunicationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class TwilioService implements CommunicationService {
    private final String fromNumber;

    public TwilioService(
        @Value("${twilio.account-sid}") String sid,
        @Value("${twilio.auth-token}") String token,
        @Value("${twilio.from-number}") String fromNumber
    ) {
        Twilio.init(sid, token);
        this.fromNumber = fromNumber;
    }

    @Override
    public boolean supports(MessageRequest request) {
        return true;  // fallback for any channel
    }

    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            Message message = Message.creator(
                new PhoneNumber(request.getChannel() == MessageRequest.Channel.SMS
                    ? request.getTo()
                    : "whatsapp:" + request.getTo()),
                new PhoneNumber(request.getChannel() == MessageRequest.Channel.SMS
                    ? fromNumber
                    : "whatsapp:" + fromNumber),
                request.getContent()
            ).create();
            return new MessageResponse(true, "TWILIO", message.getSid(), null);
        } catch (Exception e) {
            return new MessageResponse(false, "TWILIO", null, e.getMessage());
        }
    }
}