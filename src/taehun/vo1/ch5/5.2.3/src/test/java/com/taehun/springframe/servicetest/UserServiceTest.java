package com.taehun.springframe.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static com.taehun.springframe.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.taehun.springframe.service.UserService.MIN_RECCOMEND_FOR_GOLD;


import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;
import com.taehun.springframe.service.UserService;

import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestServiceFactory.class })
public class UserServiceTest {
    @Autowired UserService userService;    
    @Autowired UserDao userDao;
    @Autowired DataSource dataSource;
    
    List<User> users;    // test fixture
    
    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
        		new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
        		new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),              
                 new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
                );
    }

    @Test
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for(User user : users) userDao.add(user);
        
        userService.upgradeLevels();
        
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
    	User userUpdate = userDao.get(user.getId()).orElseThrow(() 
    			-> new RuntimeException("User not found"));
        if (upgraded) {
            assertEquals(user.getLevel().nextLevel(), userUpdate.getLevel());
        }
        else {
            assertEquals(user.getLevel(), userUpdate.getLevel());
        }
    }
//    Optional<User> optional 
    
    @Test 
    public void add() {
        userDao.deleteAll();
        
        User userWithLevel = users.get(4);      // GOLD 레벨  
        User userWithoutLevel = users.get(0);  
        userWithoutLevel.setLevel(null);
        
        userService.add(userWithLevel);      
        userService.add(userWithoutLevel);
        
        User userWithLevelRead = userDao.get(userWithLevel.getId()).orElseThrow(() 
        		-> new RuntimeException("User not found"));
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId()).orElseThrow(() 
        		-> new RuntimeException("User not found"));
        
        assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel()); 
        assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());
    }
 
    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());  
        testUserService.setUserDao(this.userDao); 
        testUserService.setDataSource(this.dataSource);
         
        userDao.deleteAll();              
        for(User user : users) userDao.add(user);
        
        try {
            testUserService.upgradeLevels();   
            fail("TestUserServiceException expected"); // fail == 단위테스트 무조건 실패시키는
            
        }
        catch(TestUserServiceException e) { 
        }
        
        checkLevelUpgraded(users.get(1), false);
    }

    
    static class TestUserService extends UserService {
        private String id;
        
        private TestUserService(String id) {  
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();  
            super.upgradeLevel(user);  
        }
    }
    
    static class TestUserServiceException extends RuntimeException {
    }
}
