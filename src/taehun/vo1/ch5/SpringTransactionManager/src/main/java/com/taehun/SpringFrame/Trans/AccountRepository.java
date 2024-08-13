package com.taehun.SpringFrame.Trans;

public interface AccountRepository {
    void transfer(double amount, String fromAccountId, String toAccountId);
}
