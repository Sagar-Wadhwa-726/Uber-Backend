package com.sagarw.project.uber.uberApp.strategies;

import com.sagarw.project.uber.uberApp.entities.enums.PaymentMethod;
import com.sagarw.project.uber.uberApp.strategies.Impl.PaymentStrategyPaymentThroughCash;
import com.sagarw.project.uber.uberApp.strategies.Impl.PaymentStrategyPaymentThroughWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {
    private final PaymentStrategyPaymentThroughWallet paymentStrategyPaymentThroughWallet;
    private final PaymentStrategyPaymentThroughCash paymentStrategyPaymentThroughCash;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case WALLET -> paymentStrategyPaymentThroughWallet;
            case CASH -> paymentStrategyPaymentThroughCash;
        };
    }
}
