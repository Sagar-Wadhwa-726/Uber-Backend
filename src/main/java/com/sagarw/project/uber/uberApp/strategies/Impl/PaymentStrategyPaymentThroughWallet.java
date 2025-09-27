package com.sagarw.project.uber.uberApp.strategies.Impl;

import com.sagarw.project.uber.uberApp.entities.Driver;
import com.sagarw.project.uber.uberApp.entities.Payment;
import com.sagarw.project.uber.uberApp.entities.Rider;
import com.sagarw.project.uber.uberApp.entities.enums.PaymentStatus;
import com.sagarw.project.uber.uberApp.entities.enums.TransactionMethod;
import com.sagarw.project.uber.uberApp.repositories.PaymentRepository;
import com.sagarw.project.uber.uberApp.services.WalletService;
import com.sagarw.project.uber.uberApp.strategies.PaymentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// For Rider
/*Rs. 232, ride cost Rs. 100, final balance = Rs. 132*/

// For Driver
/*Rs. 500, add money 70 (70% of cost, as 30% commission), final balance = Rs. 570 = 500 + (100 - 30)*/

@Service
@RequiredArgsConstructor
public class PaymentStrategyPaymentThroughWallet implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final WalletService walletService;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(), driversCut, null, payment.getRide(), TransactionMethod.RIDE);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
