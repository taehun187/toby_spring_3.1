package com.taehun.springframe.daotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static 추가시 코드마다 Assertions. 안붙이고 그냥 assertEquals 할 수 있음 !
import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import com.taehun.springframe.dao.DaoFactory;
import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.User;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestMethodOrder(MethodOrderer.DisplayName.class)

public class UserDaoTest {
	@Autowired
	ApplicationContext context;
	
	private UserDao dao; 
	
	private User user1;
	private User user2;
	private User user3;
	
	
	@BeforeEach
	public void setUp() {
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(DaoFactory.class);
		this.dao = context.getBean("userDao", UserDao.class);
		
		
		this.user1 = new User("gyumee", "규미", "springno1");
		this.user2 = new User("leegw700", "기웅", "springno2");
		this.user3 = new User("bumjin", "범진", "springno3");
		
		System.setProperty("configFile", "test-config.properties");

	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {				
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
		
		dao.add(user1);
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		Optional<User> Optuserget1 = dao.get(user1.getId());
		if (!Optuserget1.isEmpty()) {
			User userget = Optuserget1.get();
			assertEquals(user1.getName(), userget.getName());
			assertEquals(user1.getPassword(), userget.getPassword());
		}		
		
		Optional<User> Optuserget2 = dao.get(user2.getId());	
		if (!Optuserget2.isEmpty()) {
			User userget = Optuserget2.get();
			assertEquals(user2.getName(), userget.getName());
			assertEquals(user2.getPassword(), userget.getPassword());		
		}		
	}
	
	@Test
//	@Order(2)
	public void getUserFailure() throws SQLException, ClassNotFoundException {		
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
		Assertions.assertThrows(EmptyResultDataAccessException.class, 
				// Excutable 인터페이스의 excute 메소드의 람다!!!
				() -> dao.get("unknown_id"));	
		
		
				
		// dao.get("unknown_id")가 EmptyResultDataAccessException.class 예외를 발생시키면,
		// 이 단위 테스트 성공...
		// 만약 그렇지 않으면-어떠한 예외도 발생하지 않거나, EmptyResultDataAccessException이 아닌 
		// 다른 예외가 발생하면, 이 단위 테스트는 실패!!!
	}
	@Test
	@DisplayName("count")
//	@Order(3)
	public void count() throws SQLException {
		
				
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
				
		dao.add(user1);
		assertEquals(dao.getCount(), 1);
		
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		dao.add(user3);
		assertEquals(dao.getCount(), 3);
	}
	
	@AfterEach
	public void afterEach() {
		System.clearProperty("configFile");
	}
	
	@AfterAll
	public static void goodBye() {
		System.out.println();
	}

}

