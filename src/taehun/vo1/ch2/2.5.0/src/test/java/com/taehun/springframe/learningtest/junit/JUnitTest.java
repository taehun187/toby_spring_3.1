package com.taehun.springframe.learningtest.junit;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension; // SpringExtension 클래스


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJunit.class})
public class JUnitTest {
	@Autowired 
	ApplicationContext context; // 스프링 IoC 컨테이너가 자동주입
	
	// Set은 동일한 엘리먼트 중복을 허용하지 않는다. 
	// 엘리먼트 : 특정 JUnitTest 클래스 객체 참조(C언어에서 포인터:값으로 메모리주소)값
	static Set<JUnitTest> testObjects = new HashSet<>(); 
	static ApplicationContext contextObject = null;
	
	@Order(1)
	@Test public void test1() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Order(2)
	@Test public void test2() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	@DirtiesContext
	@Order(3)
	@Test public void test3() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Order(4)
	@Test public void test4() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
			
}