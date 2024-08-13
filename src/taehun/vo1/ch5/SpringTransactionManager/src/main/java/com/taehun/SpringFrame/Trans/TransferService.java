package com.taehun.SpringFrame.Trans;

public interface TransferService {
    void transfer(double amount, String fromAccountId, String toAccountId);
}