package com.taehun.SpringFrame.Composing_Configurations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program {
	
	public static void main(String[] args) {
	
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(ConfigB.class);
    
		// now both beans A and B will be available...
		Aaa aaa = context.getBean("aaa", Aaa.class);
		Bbb bbb = context.getBean("bbb", Bbb.class);
		
		System.out.println("Hello World");
	}
}