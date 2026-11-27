package com.gms.customer_backend.booking.kafka.producer;

import com.gms.customer_backend.booking.kafka.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingKafkaProducer {

    private static final String TOPIC = "booking-requests";

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public void sendBookingRequest(BookingCreatedEvent event) {

        String bookingId = event.bookingId().toString();

        kafkaTemplate.send(TOPIC, bookingId, event)
                .whenComplete((result, exception) -> {

                    if (exception == null) {
                        log.info(
                                "Booking request sent to Kafka. Booking ID: {}",
                                bookingId
                        );
                    } else {
                        log.error(
                                "Failed to send booking request. Booking ID: {}",
                                bookingId,
                                exception
                        );
                    }
                });
    }
}