package com.taehun.springframe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.domain.Level;
import com.taehun.springframe.domain.User;

public class UserServiceImpl implements UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;

	private UserDao userDao;
	private MailSender mailSender;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}
	
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel(); 
		switch(currentLevel) {                                   
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER); 
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel); 
		}
	}

	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@lth.org");
		mailMessage.setSubject("Upgrade name");
		mailMessage.setText("Upgrade : " + user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}
	
	public void add(User user) {
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	public void deleteAll() { 	userDao.deleteAll(); }
	public Optional<User> get(String id) { 
		return userDao.get(id); 
	}
	public List<User> getAll() { return userDao.getAll(); }
	public void update(User user) { userDao.update(user); }
}