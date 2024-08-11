package com.intheeast.springframe.learningtest.factorybean;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig.class);
		
//		ConfigurableListableBeanFactory beanFactory =
//				context.getBeanFactory();
//		
//		beanFactory.destroyBean(context.getBean("myCustomObject", MyCustomObject.class));
//		
		
		// Bean Scope : singleton, prototype, request[Http Request]
		ConfigurableApplicationContext configAppContext = 
				(ConfigurableApplicationContext) context;
//		configAppContext.getBeanFactory().destroyBean(
//				"myCustomObject", 
//				MyCustomObject.class);

		
		// 빈으로 등록된 FactoryBean은 getBean으로 가져올 때 '&'를 붙여야 함.
		// 그렇지 않으면, FactoryBean이 생성하는 빈 객체를 리턴함!!!
		MyCustomObjectFactoryBean factoryBean =
				(MyCustomObjectFactoryBean) context.getBean("&myCustomObjectFactory");
		
//		beanFactory.registerSingleton("myCustomObject", 
//				factoryBean.getObject());
		configAppContext.getBeanFactory().registerSingleton("myCustomObject2", 
				factoryBean.getObject());
		
		MyCustomObject myObject = 
				(MyCustomObject) context.getBean("myCustomObject");
		
		MyCustomObject myObject2 = 
				(MyCustomObject) context.getBean("myCustomObject2");
		
		Assertions.assertNotNull(myObject2);
		
		
		System.out.println(myObject.getName());
	}

}
