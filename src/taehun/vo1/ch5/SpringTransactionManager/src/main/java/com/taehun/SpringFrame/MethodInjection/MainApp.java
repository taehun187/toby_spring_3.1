package com.taehun.SpringFrame.MethodInjection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
	
	public static void main(String[] args) {
        // Spring 애플리케이션 컨텍스트 생성 및 설정 클래스 로드
        AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfig.class);

        // CommandManager 빈을 가져오기
        CommandManager commandManager = context.getBean(CommandManager.class);

        // process 메서드를 호출하여 Command를 실행
        Object result1 = commandManager.process("State1");
        System.out.println(result1);

        Object result2 = commandManager.process("State2");
        System.out.println(result2);

        // 애플리케이션 컨텍스트 종료
        context.close();
    }

}
