CREATE TABLE notifications (
                               notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               customer_id UUID NOT NULL,
                               booking_id UUID,

                               notification_type VARCHAR(50) NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               message TEXT NOT NULL,

                               is_read BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               read_at TIMESTAMPTZ,

                               CONSTRAINT chk_notification_type
                                   CHECK (notification_type IN (
                                                                'BOOKING_CREATED',
                                                                'BOOKING_CONFIRMED',
                                                                'BOOKING_REJECTED',
                                                                'BOOKING_CANCELLED',
                                                                'CANCELLATION_APPROVED',
                                                                'CANCELLATION_REJECTED',
                                                                'CHECKED_IN',
                                                                'SERVICE_STARTED',
                                                                'SERVICE_COMPLETED'
                                       ))
);

CREATE INDEX idx_notifications_customer_id
    ON notifications(customer_id);

CREATE INDEX idx_notifications_customer_created
    ON notifications(customer_id, created_at DESC);

CREATE INDEX idx_notifications_unread
    ON notifications(customer_id, created_at DESC)
    WHERE is_read = FALSE;