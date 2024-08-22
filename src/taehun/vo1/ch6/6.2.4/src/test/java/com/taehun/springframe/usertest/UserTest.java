package com.taehun.springframe.usertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;

public class UserTest {
	User user;
	
	@BeforeEach
	public void setUp() {
		user = new User();
	}
	
	@Test()
	public void upgradeLevel() {
		Level[] levels = Level.values();
		for(Level level : levels) {
			if (level.nextLevel() == null) continue;
			user.setLevel(level);
			user.upgradeLevel();
			assertEquals(user.getLevel(), level.nextLevel());
		}
	}
	
	@Test
	void cannotUpgradeLevel() {
	    Level[] levels = Level.values();
	    for (Level level : levels) {
	        if (level.nextLevel() != null) continue;
	        user.setLevel(level);
	        assertThrows(IllegalStateException.class, () -> user.upgradeLevel());
	    }
	}
	/*@Test(expected=IllegalStateException.class)
	public void cannotUpgradeLevel() {
		Level[] levels = Level.values();
		for(Level level : levels) {
			if (level.nextLevel() != null) continue;
			user.setLevel(level);
			user.upgradeLevel();
		}
	}*/
}