package com.taehun.springframe.servicetest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;
import com.taehun.springframe.service.Hello;
import com.taehun.springframe.service.HelloTarget;
import com.taehun.springframe.service.UpperCaseAdvice;
import com.taehun.springframe.service.UserService;
import com.taehun.springframe.service.UserServiceImpl;

import static com.taehun.springframe.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.taehun.springframe.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestServiceFactory.class})
public class UserServiceTest {
	@Autowired 
	UserService userService;
	
	@Autowired 
	UserServiceImpl userServiceImpl;
	
	@Autowired UserDao userDao;	
	
	@Autowired ApplicationContext context;

	
	@Autowired MailSender mailSender; 
	@Autowired PlatformTransactionManager transactionManager;
	
	static List<User> users;	// test fixture
	
	@BeforeEach
	public void setUp() {	
		
		users = Arrays.asList(
				new User("madnite1", "이상호", "p4", "intheeast1009@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("bumjin", "박범진", "p1", "intheeast0305@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", "kitec403@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", "intheeast0725@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),				
				new User("green", "오민규", "p5", "intheeast@gmail.com", Level.GOLD, 100, Integer.MAX_VALUE)
				);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userServiceImpl.upgradeLevels();
		
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
	public void upgradeLevelsProxy() throws Exception {		
		userDao.deleteAll();			  
		for(User user : users) 
			userDao.add(user);
				
		userService.upgradeLevels();	

	}
	
	@Test
	@DirtiesContext
	public void mockUpgradeLevels() throws Exception {
		//UserServiceImpl userServiceImpl = new UserServiceImpl();

		UserDao mockUserDao = mock(UserDao.class);	    
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		MailSender mockMailSender = mock(MailSender.class);  
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		verify(mockUserDao, times(2)).update(any(User.class));				  
		verify(mockUserDao).update(users.get(0));
		assertEquals(users.get(0).getLevel(), Level.GOLD);
		verify(mockUserDao).update(users.get(2));
		assertEquals(users.get(2).getLevel(), Level.SILVER);

		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);  
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertEquals(mailMessages.get(0).getTo()[0], users.get(0).getEmail());
		assertEquals(mailMessages.get(1).getTo()[0], users.get(2).getEmail());
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
	
	/*
	@Test
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = 
				new TestUserService(users.get(2).getId()); 		
		
		testUserService.setUserService(this.userServiceImpl);
		testUserService.setTransactionManager(this.transactionManager);
		testUserService.setUsImpl((UserServiceImpl) testUserService.getUserSerive());
		
		userDao.deleteAll(); 
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();			
			
		}
		catch(TestUserServiceException e) { 
			e.printStackTrace();			
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	static class TestUserService extends UserServiceTx {
		private String id;
		UserServiceImpl usImpl;
		
		private TestUserService(String id) {  
			this.id = id;
			
		}
		
		public void setUsImpl(UserServiceImpl usImpl) {
			this.usImpl = usImpl;
		}
		
		@Override
		public void upgradeLevels() {
			TransactionStatus status = this.getTransactionManager().
					getTransaction(new DefaultTransactionDefinition());					
			
			try {
				for (User user : users) {
					if (usImpl.canUpgradeLevel(user)) {
						upgradeLevel(user);
					}
				}
				this.getTransactionManager().commit(status);
			} catch (RuntimeException e) {
				this.getTransactionManager().rollback(status);
				throw e;
			}
		}

		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) 
				throw new TestUserServiceException();  
			this.usImpl.upgradeLevel(user);			
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
	}
	*/
	
	@Test
	public void ClassNamePointcutAdvisor() {
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			public ClassFilter getClassFilter() {
				return new ClassFilter() { // boolean matches(Class<?> clazz);
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		
		classMethodPointcut.setMappedName("sayH*");
		
		class HelloWorld extends HelloTarget {}; // local class 	
		checkAdviced(new HelloWorld(), classMethodPointcut, false);
		
//		System.out.println("***********************************************");
		class HelloToby extends HelloTarget {};  // local class 
		checkAdviced(new HelloToby(), classMethodPointcut, true);		
		
	}
	
	private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if (adviced) {
			assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
			assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
			assertEquals(proxiedHello.sayThanks("Toby"), "Thanks Toby");
		}
		else {
			assertEquals(proxiedHello.sayHello("Toby"), "Hello Toby");
			assertEquals(proxiedHello.sayHi("Toby"), "Hi Toby");
			assertEquals(proxiedHello.sayThanks("Toby"), "Thanks Toby");
		}
	}
	
	private void checkAdviced2(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();		
		
		System.out.println(proxiedHello.sayHello("Toby"));
		System.out.println(proxiedHello.sayHi("Toby"));
		System.out.println(proxiedHello.sayThanks("Toby"));
		
	}

}