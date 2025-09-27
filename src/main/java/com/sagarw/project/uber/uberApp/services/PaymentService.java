package com.sagarw.project.uber.uberApp.services;

import com.sagarw.project.uber.uberApp.entities.Payment;
import com.sagarw.project.uber.uberApp.entities.Ride;
import com.sagarw.project.uber.uberApp.entities.enums.PaymentStatus;

public interface PaymentService {
    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
