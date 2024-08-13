package com.taehun.SpringFrame.Trans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
	
	public static void main(String[] args) {
        ApplicationContext ctx = 
        		new AnnotationConfigApplicationContext(SystemTestConfig.class);
        // 모든 설정이 구성 클래스 간에 정상적으로 연결됨
        TransferService transferService = ctx.getBean(TransferService.class);
        transferService.transfer(100.00, "A123", "C456");
    }

}
