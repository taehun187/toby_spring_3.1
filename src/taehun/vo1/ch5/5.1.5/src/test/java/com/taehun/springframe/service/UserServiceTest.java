package com.taehun.springframe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.taehun.springframe.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.taehun.springframe.service.UserService.MIN_RECCOMEND_FOR_GOLD;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.daotest.TestDaoFactory;
import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	UserDao userDao;

	List<User> users; // test fixture

	@BeforeEach
	public void setUp() {
		users = Arrays.asList(new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1),
				new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE));
	}

	@Test
	public void upgradeLevels() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		userService.upgradeLevels();

		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId())
				.orElseThrow(() -> new RuntimeException("User not found: " + user.getId()));
		if (upgraded) {
			assertEquals(user.getLevel().nextLevel(), userUpdate.getLevel());
		} else {
			assertEquals(user.getLevel(), userUpdate.getLevel());
		}
	}

	@Test
	public void add() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();

		User userWithLevel = users.get(4); // GOLD 레벨
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId())
				.orElseThrow(() -> new RuntimeException("User not found: " + userWithLevel.getId()));
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId())
				.orElseThrow(() -> new RuntimeException("User not found: " + userWithoutLevel.getId()));

		assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel());
		assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());
	}
}