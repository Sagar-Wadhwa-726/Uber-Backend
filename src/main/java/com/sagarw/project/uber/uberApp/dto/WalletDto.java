package com.sagarw.project.uber.uberApp.dto;

import com.sagarw.project.uber.uberApp.entities.WalletTransaction;
import lombok.Data;

import java.util.List;

@Data
public class WalletDto {
    private Long id;
    private UserDto user;
    private Double balance;
    private List<WalletTransaction> transactions;
}
