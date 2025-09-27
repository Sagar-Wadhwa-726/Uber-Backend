package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.entities.Ride;
import com.sagarw.project.uber.uberApp.entities.User;
import com.sagarw.project.uber.uberApp.entities.Wallet;
import com.sagarw.project.uber.uberApp.entities.WalletTransaction;
import com.sagarw.project.uber.uberApp.entities.enums.TransactionMethod;
import com.sagarw.project.uber.uberApp.entities.enums.TransactionType;
import com.sagarw.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.sagarw.project.uber.uberApp.repositories.WalletRepository;
import com.sagarw.project.uber.uberApp.services.WalletService;
import com.sagarw.project.uber.uberApp.services.WalletTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletTransactionService walletTransactionService;
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

//        walletTransactionService.createNewWalletTransaction(walletTransaction);

//        commented out the above line, because it will save wallet transaction then while saving the wallet we are again saving the transaction
//        with the same ID which will result in an error
        wallet.getTransactions().add(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found with ID " + walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with id : " + user.getId()));
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);
    }
}
