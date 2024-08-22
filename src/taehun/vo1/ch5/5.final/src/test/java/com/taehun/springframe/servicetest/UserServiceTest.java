package com.taehun.springframe.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.taehun.springframe.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.taehun.springframe.service.UserService.MIN_RECCOMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;
import com.taehun.springframe.service.UserService;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceFactory.class})
public class UserServiceTest {
	@Autowired UserService userService;	
	@Autowired UserDao userDao;
	@Autowired MailSender mailSender; 
	@Autowired PlatformTransactionManager transactionManager;
	
	static List<User> users;	// test fixture
	
	@BeforeEach
	public void setUp() {	
		
		users = Arrays.asList(
				new User("madnite1", "이상호", "p4", "lth1518@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("bumjin", "박범진", "p1", "lth1518@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", "lth1518@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", "lth1518@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),				
				new User("green", "오민규", "p5", "lth1518@gmail.com", Level.GOLD, 100, Integer.MAX_VALUE)
				);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), true);
		checkLevelUpgraded(users.get(1), false);
		checkLevelUpgraded(users.get(2), true);
		checkLevelUpgraded(users.get(3), false);
		checkLevelUpgraded(users.get(4), false);
	}
	
	// 
	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();	
		
		public List<String> getRequests() {
			return requests;
		}

		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);  
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {
		}
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		Optional<User> optionalUser = userDao.get(user.getId());
		if (!optionalUser.isEmpty()) {
			User userUpdate = optionalUser.get();
			if (upgraded) {
				assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
			}
			else {
				assertEquals(userUpdate.getLevel(), (user.getLevel()));
			}			
		}		
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	  // GOLD Level
		User userWithoutLevel = users.get(0);  
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);	  
		userService.add(userWithoutLevel);
		
		Optional<User> optionalUserWithLevelRead = userDao.get(userWithLevel.getId());
		if (!optionalUserWithLevelRead.isEmpty()) {
			User userWithLevelRead = optionalUserWithLevelRead.get();
			assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel()); 
		}		
		
		Optional<User> optionalUserWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		if (!optionalUserWithoutLevelRead.isEmpty()) {
			User userWithoutLevelRead = optionalUserWithoutLevelRead.get();
			assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
		}		
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = 
				new TestUserService(users.get(2).getId());  
		testUserService.setUserDao(this.userDao); 
		testUserService.setTransactionManager(this.transactionManager);
		testUserService.setMailSender(this.mailSender);
		 
		userDao.deleteAll(); 
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();			
			
		}
		catch(TestUserServiceException e) { 
			e.printStackTrace();
			
			fail("TestUserServiceException expected"); 
		}
		
		checkLevelUpgraded(users.get(1), false);
		//checkLevelUpgraded(users.get(1), true);
	}
	
	static class TestUserService extends UserService {
		private String id;
		
		private TestUserService(String id) {  
			this.id = id;
		}
		
		@Override
		public void upgradeLevels() {
			TransactionStatus status = this.getTransactionManager().
					getTransaction(new DefaultTransactionDefinition());
					
			try {
				for (User user : users) {
					if (canUpgradeLevel(user)) {
						upgradeLevel(user);
					}
				}
				this.getTransactionManager().commit(status);
			} catch (RuntimeException e) {
				this.getTransactionManager().rollback(status);
				throw e;
			}
//			TransactionSynchronizationManager.initSynchronization();  
//			Connection c = DataSourceUtils.getConnection(this.getDataSource()); 
//			c.setAutoCommit(false);
//			
//			try {								   
//				
//				for (User user : users) {
//					if (canUpgradeLevel(user)) {
//						upgradeLevel(user);
//					}
//				}
//				c.commit();  
//			} catch (Exception e) {    
//				c.rollback();
//				throw e;
//			} finally {
//				DataSourceUtils.releaseConnection(c, this.getDataSource());	
//				TransactionSynchronizationManager.unbindResource(this.getDataSource());  
//				TransactionSynchronizationManager.clearSynchronization();  
//			}
		}

		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) 
				throw new TestUserServiceException();  
			super.upgradeLevel(user);			
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
	}

}