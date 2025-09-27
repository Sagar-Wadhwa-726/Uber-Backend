package com.sagarw.project.uber.uberApp.strategies.Impl;

import com.sagarw.project.uber.uberApp.entities.Driver;
import com.sagarw.project.uber.uberApp.entities.Payment;
import com.sagarw.project.uber.uberApp.entities.enums.PaymentStatus;
import com.sagarw.project.uber.uberApp.entities.enums.TransactionMethod;
import com.sagarw.project.uber.uberApp.repositories.PaymentRepository;
import com.sagarw.project.uber.uberApp.services.WalletService;
import com.sagarw.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*Rider was shown Rs. 100, driver will get Rs. 70, Rs. 30 commission for Uber*/

// In Cash payment all Rs. 100 goes to the driver, deduct Rs. 30 from Driver's wallet
// Rider's wallet is not needed in case of cash payment, all we need is drivers wallet

@Service
@RequiredArgsConstructor
public class PaymentStrategyPaymentThroughCash implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;
        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, payment.getRide(), TransactionMethod.RIDE);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
