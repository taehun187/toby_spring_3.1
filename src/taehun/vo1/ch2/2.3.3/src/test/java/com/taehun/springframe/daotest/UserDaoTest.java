package com.taehun.springframe.daotest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.taehun.springframe.dao.DaoFactory;
import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.User;
public class UserDaoTest {	
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {				
		ApplicationContext context =
				new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		// Assert that expected and actual are equal.
		// boolean condition = true;
		// ...
		// condition = false;
		// ...
		// assert condition -> 프로그램 중단되고 AssertError 발생  
		// ...
		// Assert that expected and actual are equal.  
		dao.deleteAll();	
		// Assertion 메소드들 중에 하나인,
		assertEquals(dao.getCount(), 0);
		
		User user = new User();
		user.setId("gyumee");
		user.setName("hello");
		user.setPassword("springno1");
		dao.add(user);
		assertEquals(dao.getCount(), 1);
		
		User user2 = dao.get(user.getId());		
		
		assertEquals(user2.getName(), user.getName());
		assertEquals(user2.getPassword(), user.getPassword());		
	}
}
