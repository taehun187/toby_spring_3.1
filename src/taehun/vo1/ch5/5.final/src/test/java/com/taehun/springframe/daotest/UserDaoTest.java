package com.taehun.springframe.daotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestDaoFactory.class })
public class UserDaoTest {
	
	//@Autowired
	//private ApplicationContext context;	
	 
	@Autowired
	private UserDao dao;
	
	
	@Autowired
	private DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;
	
		
	@BeforeEach
	public void setUp() {
		new User("gyumee", "박성철", "springno1", "user1@ksug.org", Level.BASIC, 1, 0);
		new User("leegw700", "이길원", "springno2", "user2@ksug.org", Level.SILVER, 55, 10);
		new User("bumjin", "박범진", "springno3", "user3@ksug.org", Level.GOLD, 100, 40);
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
	public void count() throws SQLException, ClassNotFoundException {		
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		dao.add(user1);
		assertEquals(dao.getCount(), 1);
		
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		dao.add(user3);
		assertEquals(dao.getCount(), 3);		
	}
	
	@Test
	public void getUserFailure() throws SQLException, ClassNotFoundException {		
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);		
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, 
				() -> {dao.get("unknown_id");});	
	}	
	
	@Test
	public void getAll() throws SQLException  {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertEquals(users0.size(), 0);
		
		dao.add(user1); 
		List<User> users1 = dao.getAll();
		assertEquals(users1.size(), 1);
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2); 
		List<User> users2 = dao.getAll();
		assertEquals(users2.size(), 2);
		checkSameUser(user1, users2.get(0));  
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); 
		List<User> users3 = dao.getAll();
		assertEquals(users3.size(), 3);	
//		checkSameUser(user1, users3.get(0));  
//		checkSameUser(user2, users3.get(1));  
//		checkSameUser(user3, users3.get(2)); 
	
		checkSameUser(user3, users3.get(0));  // 고의로 문제를 발생 
		checkSameUser(user1, users3.get(1));  
		checkSameUser(user2, users3.get(2)); 
		
		
		return ;
	}
	
	private void checkSameUser(User user1, User user2) {
	    assertEquals(user1.getId(), user2.getId());
	    assertEquals(user1.getName(), user2.getName());
	    assertEquals(user1.getPassword(), user2.getPassword());
	    assertEquals(user1.getLevel(), user2.getLevel());
	    assertEquals(user1.getLogin(), user2.getLogin());
	    assertEquals(user1.getRecommend(), user2.getRecommend());
	}
	
	@Test
	public void duplciateKey() throws SQLException {
		dao.deleteAll();
		
		dao.add(user1);
		assertThrows(DuplicateKeyException.class, 
				() -> dao.add(user1));
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
				dao.add(user1);
				dao.add(user1); // MySQL 서버가 이 쿼리에 대한 결과로 MySQL Error Code를 전달함
		} //org.springframework.dao.DuplicateKeyException
//		catch(DuplicateKeyException e) {
//			e.printStackTrace();			
//		}
		catch(RuntimeException ex) {
			SQLException sqlEx = (SQLException)ex.getCause();
			SQLExceptionTranslator set = 
					new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			
			DataAccessException transEx = set.translate(null, null, sqlEx);
			//////////////////////////////////////////////////////////////
			assertEquals(DuplicateKeyException.class, transEx.getClass());
			//////////////////////////////////////////////////////////////
		}
	}
	
//	@Test
//	public void sqlExceptionTranslate2() throws ClassNotFoundException {
//		dao.deleteAll();
//		
//		try {
////			dao.add(user1);
////			dao.add(user1);
//			userDaoSql.add(user1);
//			userDaoSql.add(user1);
//		} //org.springframework.dao.DuplicateKeyException
//		catch(SQLException ex) {
//			System.out.println(ex);
//			System.out.println(ex.getErrorCode());
//			assertEquals(ex.getErrorCode(), MysqlErrorNumbers.ER_DUP_ENTRY);
////			if (ex.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
////				throw new DuplicateKeyException(ex.getMessage());				
////			}
////			else
////				throw new RuntimeException(ex);
//		}
//		finally {
//			
//		}
//	}		
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);  // 수정할 사용자
		dao.add(user2);  // 수정하지 않을 사용자
		
		// user1의 정보를 수정
		user1.setName("오민규");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
	//	Optional<User> Optuser1update = dao.get(user1.getId());
	//  if 
		// 수정된 사용자 정보 확인
		User user1update = dao.get(user1.getId()).orElse(null);
		checkSameUser(user1, user1update);
		
		// 수정되지 않은 사용자 정보 확인
		User user2same = dao.get(user2.getId()).orElse(null);
		checkSameUser(user2, user2same);
	}
	

}