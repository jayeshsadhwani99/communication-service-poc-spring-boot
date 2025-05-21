package com.example.communication.service.channel.whatsapp;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.service.CommunicationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
@Slf4j
public class TwilioWhatsappService implements CommunicationService {

    @Value("${twilio.account-sid}")
    private String sid;

    @Value("${twilio.auth-token}")
    private String token;

    @Value("${twilio.from-number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(sid, token);
        log.info("Initialized TwilioService with fromNumber={}", fromNumber);
    }

    @Override
    public boolean supports(MessageRequest request) {
        return true; // fallback for any channel
    }

    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            String to = request.getChannel() == MessageRequest.Channel.SMS
                    ? request.getTo()
                    : "whatsapp:" + request.getTo();
            String from = request.getChannel() == MessageRequest.Channel.SMS
                    ? fromNumber
                    : "whatsapp:" + fromNumber;

            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    request.getContent()).create();

            log.info("Twilio: sent message {} to {}", message.getSid(), to);
            return new MessageResponse(true, "TWILIO", message.getSid(), null);
        } catch (Exception e) {
            log.error("Twilio: send failed", e);
            return new MessageResponse(false, "TWILIO", null, e.getMessage());
        }
    }
}
