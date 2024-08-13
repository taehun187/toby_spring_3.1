package com.taehun.SpringFrame.Trans;

import javax.sql.DataSource;

public class JdbcAccountRepository implements AccountRepository {

	private DataSource dataSource;

    public JdbcAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        // 실제로는 데이터베이스와 상호작용하여 이체 로직을 구현
        System.out.println("Transferring " + 
        		amount + " from " + fromAccountId + 
        		" to " + toAccountId);
    }
}
