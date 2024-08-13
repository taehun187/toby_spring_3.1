package com.taehun.SpringFrame.Trans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class TransferServiceImpl implements TransferService {

	private AccountRepository accountRepository;

    public TransferServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
    	System.out.println("TransferServiceImpl::init");
    }

    @Override
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        accountRepository.transfer(amount, fromAccountId, toAccountId);
    }

    @PreDestroy
    public void destroy() {
    	System.out.println("TransferServiceImpl::destroy");
    }
}
